package com.ms.WebPlatform.services;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ms.WebplatformApplication;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MsrtcDeviceLog;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.Json_Activity;
import com.ms.WebPlatform.utility.QueryClass;
import com.ms.WebPlatform.utility.Query_uptime_down_time;
import com.sun.jndi.cosnaming.CNNameParser;

@Service
public class UptimeReportImpl implements UptimeReport {

	@Autowired
	RawDataInterface rawDatainterface;
	@Autowired
	private Importer imp;
	private static DecimalFormat df = new DecimalFormat("0.00");
	private Connection conn_uat = null;
	private Connection conn_prod = null;
	
	  private static final Logger logger = LoggerFactory.getLogger(WebplatformApplication.class);
	  
	  
	  
	  
	  
	  @Override
		public GlobalResponse getImeiLIst(Integer page,Integer rawCount) throws SQLException {
		 
		  List<MsrtcDeviceLog> msrtcObjlist = new ArrayList<>();
			GlobalResponse resp = new GlobalResponse();
			String query = " select rvl.device_imei,rvl.vehicle_reg_no,rvl.event_location,rvl.event_date from report_vehicle_location rvl limit "+page+","+rawCount;
			Connection conn = QueryClass.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				 MsrtcDeviceLog msrtcObj = new MsrtcDeviceLog();
				 msrtcObj.setDeviceImei(rs.getString("device_imei"));
				 msrtcObj.setReceivedTime(rs.getDate("event_date"));
				msrtcObjlist.add(msrtcObj);
			}
			boolean bool = msrtcObjlist.size() > 0 ? true:false;
			resp.setData(msrtcObjlist);
			resp.setCode(bool ? 1:0);
			resp.setStatus( bool ? "true" : "false");
			resp.setMessage(bool ? "success" : "data not available");
			return resp;
		}

	  
	  private GlobalResponse getTripSchedulFRomDb(Temp_Response request, String type){
		  int marker = 0;
		  String query = "";
		  if(type.equals("cancel")) {
			  query = QueryClass.getcalcelTripQuery(request.getStartDate(), request.getEndDate(), 4);
		  }else if(type.equals("extra")) {
			  query = QueryClass.getExtraTripQuery(request.getStartDate(), request.getEndDate(), 1);		 
			  
		  }else {
			  query = QueryClass.getTripSchedule(request);
			  marker = 1;
		  }
		  
		  logger.info("welcome  -----");
			GlobalResponse resp = new GlobalResponse();
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapObj = null;
			
			
			try {
				if(conn_prod == null || conn_prod.isClosed()) {
					conn_prod = rawDatainterface.getConnection();
				}
				
				
				Statement stmt = conn_prod.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					mapObj = new HashMap<>();
					//System.out.println("result data --- "+rs.getString("depot_code") +"  "+rs.getString("route_code"));
					mapObj.put("tripDate", rs.getString("trip_date"));
					//mapObj.put("division", rs.getString("division_name"));
					mapObj.put("depotCode", rs.getString("depot_code"));  
					mapObj.put("depotName", rs.getString("depot_name"));
					mapObj.put("tripNo", rs.getString("trip_no"));
					mapObj.put("vehicleNo", rs.getString("vehicle_reg_no"));
					mapObj.put("divisionName", rs.getString("division_name"));
					mapObj.put("routeName", rs.getString("route_long_name"));				
					mapObj.put("schedule", marker == 1 ? rs.getString("schedule_no") : "");
				
				  mapList.add(mapObj);
				}
				marker = 0;
				List<Map<String, Object>> mapListNew = filterByDepotCode(mapList, request.getDepotCodes());
				if(mapListNew.size() > 0) {
				  if(request.getIs_export() == 1) {
					  String pdfExcel = type.equals("cancel") ? imp.getPdfExcel().dailyReportCanceltripExport(mapListNew) : 
						  type.equals("extra") ? imp.getPdfExcel().getExtraTripReport(mapListNew) : 
							  type.equals("all") ? imp.getPdfExcel().getAllTripReport(mapListNew) : "";
					     resp.setData(pdfExcel);
						 resp.setStatus("success");
						 resp.setMessage("success");
						 resp.setCode(1);
						return resp;
				  }
					
				  resp.setCode(1);	
				  resp.setCount(mapListNew.size());
				  mapListNew =  modifyList(mapListNew, request.getPageNo(), request.getRowCount());
					 resp.setData(mapListNew);
					 resp.setStatus("success");
					 resp.setMessage("success");
				}else {
					resp.setCode(0);
					 resp.setStatus("fail");
					 resp.setMessage("data not available");
				}			

				
			} catch (SQLException e) {
				resp.setCode(-1);
				resp.setMessage(e.getMessage());
				resp.setStatus("fail");
				e.printStackTrace();
			}
			return resp;
		  
		  
	  }

		@Override
		public GlobalResponse getTripSchedule(Temp_Response request) {			
			return getTripSchedulFRomDb(request, "all");
		}
	  
	  @Override
		public GlobalResponse getBreakDownReport(Temp_Response request) {
		  logger.info("welcome getCancelTrip -----");
			GlobalResponse resp = new GlobalResponse();
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapObj = null;
			
			System.out.println("vehicleCount --- >>> "+QueryClass.getVehicleCountOfDepot(request.getDepotCodes()));
			
			
			try {
				
				if(conn_prod == null || conn_prod.isClosed()) {
					conn_prod = rawDatainterface.getConnection();
				}
				
				Statement stmt = conn_prod.createStatement();
				ResultSet rs = stmt.executeQuery(QueryClass.getBREAKdOWNQuery(request));
				while(rs.next()) {
					mapObj = new HashMap<String, Object>();
					//dataDetails.put("lat", rs.getString("lat"));
					//dataDetails.put("lon", rs.getString("lon"));
					mapObj.put("depot", rs.getString("depot_name"));
					mapObj.put("devision", rs.getString("division_name"));
					mapObj.put("division", rs.getString("division_name"));
					mapObj.put("vehicle",rs.getString("vehicle_reg_no"));
					 
				/*
				 * String timeStr = rs.getString("event_date"); timeStr = timeStr.substring(0,
				 * timeStr.length() - 2);
				 */
					mapObj.put("time","");
					mapObj.put("status", rs.getString("vehicle_status"));
					mapObj.put("depotCode", rs.getString("depot_code"));
					
					mapList.add(mapObj);
				}
				
				List<Map<String, Object>> mapListNew = filterByDepotCode(mapList, request.getDepotCodes());
				if(mapListNew.size() > 0) {
				  if(request.getIs_export() == 1) {
					  String resultUrl = imp.getPdfExcel().dailyReportBreakDown(mapListNew);
					  resp.setData(resultUrl);
						 resp.setStatus("success");
						 resp.setMessage("success");
						 resp.setCode(1);
						 return resp;
					  
				  }					
				  resp.setCode(1);	
				  resp.setCount(mapListNew.size());
				  mapListNew =  modifyList(mapListNew, request.getPageNo(), request.getRowCount());
					 resp.setData(mapListNew);
					 resp.setStatus("success");
					 resp.setMessage("success");
				}else {
					resp.setCode(0);
					 resp.setStatus("fail");
					 resp.setMessage("data not available");
				}			

				
			} catch (SQLException e) {
				resp.setCode(-1);
				resp.setMessage(e.getMessage());
				resp.setStatus("fail");
				e.printStackTrace();
			}
			return resp;
		}


		@Override
		public GlobalResponse getExtraTrip(Temp_Response request) {

			return getTripSchedulFRomDb(request, "extra");
			
		}
	  
	@Override
	public GlobalResponse getCancelTrip(Temp_Response request) {
		
		if(request.getIs_export() == 1) {
			request.setRowCount(0);
			request.setPageNo(0);
		}
		
		return getTripSchedulFRomDb(request, "cancel");
	}

	@Override
	public String downLoadUptimeReport() {
		
		System.out.println("called method >>>>>>>>>");
		
		LocalDateTime ldt = new LocalDateTime();
		String month = ldt.getMonthOfYear() < 10 ? "0"+ldt.getMonthOfYear() : String.valueOf(ldt.getMonthOfYear());
		String day = ldt.getDayOfMonth() < 10 ? "0"+ldt.getDayOfMonth() : String.valueOf(ldt.getDayOfMonth());
		String fileName = "uptime_"+ldt.getYear()+"-"+month+"-"+day;
        String dtFormat = ldt.getYear()+"-"+month+"-"+day;
        
		Thread t = new Thread() { 
			public void run()
			{
				try {
					List<Map<String, String>> jsonStringList = makeQuery();
					String jsonString = new Gson().toJson(jsonStringList);
					Json_Activity.createJsonFile(jsonString, fileName);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};t.start();
		
		
		return "success";
	}
	
	@Override
	public String StoreUpDownCount() {
		try {
			LocalDateTime lDT = new LocalDateTime(); //2020-03-19
			String days = lDT.getDayOfMonth() > 10 ? String.valueOf(lDT.getDayOfMonth()) : "0"+lDT.getDayOfMonth();
			String month = lDT.getMonthOfYear() > 10 ? String.valueOf(lDT.getMonthOfYear()) : "0"+lDT.getMonthOfYear();
			String dtFormat = lDT.getYear()+"-"+month+"-"+days;
			Map<String,Integer> countMap = new HashMap<>();
			List<Map<String,Integer>> countMapList = new ArrayList<Map<String,Integer>>();
 			
			System.out.println("welcome ---- >>> ");
			Integer upCnt=0,dwnCnt=0,actualDown=0,OFFRoadTRANSFERREDSCRAPPED=0,BreakdownAtDWSMaintancevehicl=0,parkedVehicle = 0,totalehicle=0;
			upCnt = Query_uptime_down_time.totlVehicleUp();
			totalehicle =  Query_uptime_down_time.totalVehicle();
			dwnCnt = totalehicle - upCnt ;
			parkedVehicle = Query_uptime_down_time.parckedCount();
			OFFRoadTRANSFERREDSCRAPPED = Query_uptime_down_time.OFFRoadTRANSFERREDSCRAPPED();
			BreakdownAtDWSMaintancevehicl = Query_uptime_down_time.BreakdownAtDWSMaintancevehicle();
			actualDown = dwnCnt - (OFFRoadTRANSFERREDSCRAPPED + BreakdownAtDWSMaintancevehicl + BreakdownAtDWSMaintancevehicl);
			
			countMap.put("upcount", upCnt);
			countMap.put("downcount", dwnCnt);
			countMap.put("parkedVehicle", parkedVehicle);
			countMap.put("offRd-trnf-scrped",OFFRoadTRANSFERREDSCRAPPED );
			countMap.put("DWS-Maint-BD", BreakdownAtDWSMaintancevehicl);
			countMap.put("totalVehicle", totalehicle);
			countMap.put("actualDown",actualDown);			
			countMap.put("totalVehicle", totalehicle);
			countMapList.add(countMap);
			
		
			String result = new Gson().toJson(countMapList);		     	
			Thread t = new Thread() { 
				public void run()
				{
					try {
						System.out.println("this is result -- >> "+result);
						String fileName = "count_data_"+dtFormat;
						Json_Activity.createJsonFile(result, fileName);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			};t.start();
			
			
			
			
			System.out.println("result ---- >>> "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	
	
	@Override
	public String createDowntime() {
		try {
			LocalDateTime lDT = new LocalDateTime(); //2020-03-19
			String days = lDT.getDayOfMonth() > 10 ? String.valueOf(lDT.getDayOfMonth()) : "0"+lDT.getDayOfMonth();
			String month = lDT.getMonthOfYear() > 10 ? String.valueOf(lDT.getMonthOfYear()) : "0"+lDT.getMonthOfYear();
			String dtFormat = lDT.getYear()+"-"+month+"-"+days;
			
			System.out.println("welcome ---- >>> ");
			
			
					     	
			Thread t = new Thread() { 
				public void run()
				{
					try {
						List<Map<String, String>> resultList = getDownTimeJsonString(dtFormat);
						String result = new Gson().toJson(resultList);
						String fileName = "downtime_"+dtFormat;
						Json_Activity.createJsonFile(result, fileName);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			};t.start();
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	private void makeExcel() {

	}
	
	private  List<Map<String, String>> getDownTimeJsonString(String dtFormat) throws SQLException {
		
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Map<String, String> mapObj = null;
		
		String selectFeild = "rvl.main_battery_status,rvl.gps_status,rvl.speed,rvl.ignition_status,rvl.division_id,rvl.event_date, rvl.division_name, rvl.depot_name,rvl.depot_id,rvl.vehicle_reg_no,rvl.device_imei, rvl.vehicle_status" + 
        ",route_code,trip_no,(select d.depot_code from depot d where d.depot_id = rvl.depot_id) depot_code";
		
		String query = "select "+selectFeild+"  from report_vehicle_location rvl where rvl.device_imei in (select device_imei from device where is_valid = 'Y' and device_imei not in "
				+ " (SELECT rvl.device_imei from report_vehicle_location rvl where rvl.event_date like '"+dtFormat+"%')" + 
				")";
		
		logger.info("Scheduler Query --->>> "+query);
//

		if(conn_prod == null || conn_prod.isClosed()) {
			conn_prod = rawDatainterface.getConnection();
		}
		
		Statement stmt = conn_prod.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			mapObj = new HashMap<>();
			//System.out.println("result data --- "+rs.getString("depot_code") +"  "+rs.getString("route_code"));
			mapObj.put("eventDt", rs.getString("event_date"));
			mapObj.put("division", rs.getString("division_name"));
			mapObj.put("depot", rs.getString("depot_name"));
			mapObj.put("regNo", rs.getString("vehicle_reg_no"));
			mapObj.put("deviceImei", rs.getString("device_imei"));
			mapObj.put("status", rs.getString("vehicle_status"));
			
			mapObj.put("depotCode", rs.getString("depot_code"));
			mapObj.put("routeCode", rs.getString("route_code") == null ? "" : rs.getString("route_code"));
			mapObj.put("tripNo", rs.getString("trip_no") == null ? "":rs.getString("trip_no"));
			
			mapObj.put("divisionId", rs.getString("division_id"));
			mapObj.put("ignition", rs.getString("ignition_status") == null ? "":rs.getString("ignition_status"));
			mapObj.put("speed", rs.getString("speed") == null ? "":rs.getString("speed"));
			mapObj.put("gps", rs.getString("gps_status") == null ? "":rs.getString("gps_status"));
			mapObj.put("mainPower", rs.getString("main_battery_status") == null ? "":rs.getString("main_battery_status"));
		
			mapList.add(mapObj);
		}
		
		conn_prod.close();
		return mapList;
	}

	private List<Map<String, String>> makeQuery() {
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Map<String, String> mapObj = null;
		Connection conn = rawDatainterface.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT rvl.main_battery_status,rvl.gps_status,rvl.speed,rvl.division_id,rvl.ignition_status,rvl.event_date, rvl.division_name, rvl.depot_name,rvl.depot_id,rvl.vehicle_reg_no,rvl.device_imei, rvl.vehicle_status\r\n" + 
				",route_code,trip_no,(select d.depot_code from depot d where d.depot_id = rvl.depot_id) depot_code FROM msrtc.report_vehicle_location as rvl\r\n" + 
				" WHERE rvl.event_date > CURDATE()\r\n" + 
				
                " AND (rvl.vehicle_status = 'BREAK DOWN' "+
				" OR rvl.vehicle_status = 'SCRAPPED' "+
				" OR rvl.vehicle_status = 'OFF ROAD' "+
				" OR rvl.vehicle_status = 'TRANSFERRED' "+
				" OR rvl.vehicle_status = 'MAINTENANCE' "+
				" OR rvl.vehicle_status = 'AT DWS' OR rvl.vehicle_status = 'ON ROAD') "+				
				" ORDER BY rvl.division_name ASC; ";

		try {
			logger.info("Scheduler Query --->>> "+query);
			 stmt = conn.createStatement();
			 rs = stmt.executeQuery(query);
			while (rs.next()) {
		//speed,route_code,gps_status,ignition_status,main_battery_status,running_status,trip_no,trip_date

				mapObj = new HashMap<>();
				//System.out.println("result data --- "+rs.getString("depot_code") +"  "+rs.getString("route_code"));
				mapObj.put("eventDt", rs.getString("event_date"));
				mapObj.put("division", rs.getString("division_name"));
				mapObj.put("divisionId", rs.getString("division_id"));
				mapObj.put("depot", rs.getString("depot_name"));
				mapObj.put("regNo", rs.getString("vehicle_reg_no"));
				mapObj.put("deviceImei", rs.getString("device_imei"));
				mapObj.put("status", rs.getString("vehicle_status"));  //ignition_status
				mapObj.put("depotCode", rs.getString("depot_code"));
				mapObj.put("routeCode", rs.getString("route_code") == null ? "" : rs.getString("route_code"));
				mapObj.put("tripNo", rs.getString("trip_no") == null ? "":rs.getString("trip_no"));
				mapObj.put("ignition", rs.getString("ignition_status") == null ? "":rs.getString("ignition_status"));
				mapObj.put("speed", rs.getString("speed") == null ? "":rs.getString("speed"));
				mapObj.put("gps", rs.getString("gps_status") == null ? "":rs.getString("gps_status"));
				mapObj.put("mainPower", rs.getString("main_battery_status") == null ? "":rs.getString("main_battery_status"));
			
			  mapList.add(mapObj);
			}
			
			
			return mapList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}

		return null;
	}
	
	
	
	@Override
	public GlobalResponse getUptimeReport(Temp_Response request) {
		
		//logger.info("getUptimeReport -- >>  "+request.toString());
		List<Map<String, Object>> listMap = new ArrayList<>();
		Map<String, Object> statusCount= new HashMap<String, Object>();
		int onroad=0,atdws=0,trnf=0,maintain=0,offrd=0,scrp=0,breakdwn=0;
		int upDown =  request.getSearch() == null ? 3 : request.getSearch().equalsIgnoreCase("down") ? 1 : request.getSearch().equalsIgnoreCase("up")? 2 : 0;

		//logger.info("UptimeDownTime Request type  --- >>>> "+upDown);
		if(upDown == 2) {						
		  listMap =  Json_Activity.readFromAJsonFile(request.getStartDate(),1);  // getting uptime data
		}else if(upDown == 1) {			
			listMap =  Json_Activity.readFromAJsonFile(request.getStartDate(),0); // getting downtime data
		}else {			
			listMap =  Json_Activity.readFromAJsonFile(request.getStartDate(),1);
			List<Map<String, Object>> listMapDown = Json_Activity.readFromAJsonFile(request.getStartDate(),0);		
			
			//logger.info("\n\nresult --- >>>  uptime - "+listMap.size() +" <----> downTime - "+listMapDown.size());			
			
			listMap.addAll(listMapDown);		
			
		}
		
		GlobalResponse resp = new GlobalResponse();
		
		if(listMap == null) {
			resp.setCode(0);
			resp.setMessage("data not available on given date");
			resp.setStatus("fail");
			return resp;
		}
		//System.out.println("size before--->> "+listMap.size());
		List<Map<String, Object>> listMapCountsOnly =  Json_Activity.readFromAJsonFile(request.getStartDate(),3);
			if(request.getDepotCodes().size() < 200) {
			   listMap = filterByDepotCode(listMap, request.getDepotCodes());
			   Integer divisionId = QueryClass.getDivisionIdFromDepot(request.getDepotCodes().get(0));
			   listMapCountsOnly =  Json_Activity.readFromAJsonFile_division_count(request.getStartDate(),divisionId);
			 
			}else {
				//System.out.println("All depots Data");
			}
		//System.out.println("size after--->> "+listMap.size());
		
		
		Map<String, Object> dataCountUptime = listMapCountsOnly.get(0);
		
		//-----equal size---
		String countData = dataCountUptime.get("totalVehicle").toString();
		String countData_Down = dataCountUptime.get("downcount").toString();
		String countData_Up = dataCountUptime.get("upcount").toString();
		
		if(listMap.size() > Integer.parseInt(countData))
		     listMap = listMap.subList(0, Integer.parseInt(countData));	
		else if(upDown == 1 && listMap.size() > Integer.parseInt(countData_Down)) {
			 listMap = listMap.subList(0, Integer.parseInt(countData_Down));	
		}else if(upDown == 2 && listMap.size() > Integer.parseInt(countData_Up)) {
			listMap = listMap.subList(0, Integer.parseInt(countData_Up));
		}
		
	//	System.out.println("size now--->> "+listMap.size()+"  "+Integer.parseInt(countData));
		
			
		
		
		if(request.getIs_export() == 1) { //listMapDownTime
			
			String urlPath = "";
			if(upDown == 3 || upDown == 0) {
				//List<Map<String, Object>> listMapDownTime =  Json_Activity.readFromAJsonFile(request.getStartDate(),0);
				//listMapDownTime.addAll(listMap);				
				urlPath = imp.getPdfExcel().uptimeDowntime(listMap,"updowntime");
			}else if(upDown == 1) {
				urlPath = imp.getPdfExcel().uptimeDowntime(listMap,"downtime");
			}else if(upDown == 2) {
				urlPath = imp.getPdfExcel().uptimeDowntime(listMap,"uptime");
			}
			resp.setCode(1);
			resp.setMessage("success");
			resp.setStatus("success");
			resp.setData(urlPath);
			return resp;
			
		}		
	
				
		resp.setCode(1);
		
		if(request.getDepotCodes().size() == 1) {
			 sortList(listMap);
			Map<String, Object> mapObj = getCountsforDepot(listMap,request);
			statusCount.put("finalCount", mapObj);
			listMap = modifyList(listMap, request.getPageNo(), request.getRowCount());
			resp.setData(listMap);
			resp.setCountData(statusCount);
			resp.setMessage("success");
			return resp;
		}
		
		if (request.getDepotCodes().size() > 100) {
			 sortList(listMap);
			Map<String, Object> mapObj = listMapCountsOnly.get(0);
			Integer down = Integer.parseInt(mapObj.get("downcount").toString());
			Integer offRdtrnfscrped = Integer.parseInt(mapObj.get("offRd-trnf-scrped").toString());
			Integer parked = Integer.parseInt(mapObj.get("parkedVehicle").toString());
			Integer DWSMaintBD = Integer.parseInt(mapObj.get("DWS-Maint-BD").toString());
			Integer actualDwn = down-(offRdtrnfscrped + parked + DWSMaintBD); 
			mapObj.put("actualDown", actualDwn);
		  statusCount.put("finalCount", mapObj);
		  
		  listMap = modifyList(listMap, request.getPageNo(), request.getRowCount());
			resp.setData(listMap);
			resp.setCountData(statusCount);
			resp.setMessage("success");
			return resp;
		} else {	
			Map<String, Object> mapObj = listMapCountsOnly.get(0);
			Integer down = Integer.parseInt(mapObj.get("downcount").toString());
			Integer offRdtrnfscrped = Integer.parseInt(mapObj.get("offRd-trnf-scrped").toString());
			Integer parked = Integer.parseInt(mapObj.get("parkedVehicle").toString());
			Integer DWSMaintBD = Integer.parseInt(mapObj.get("DWS-Maint-BD").toString());
			Integer actualDwn = down-(offRdtrnfscrped + parked + DWSMaintBD); 
			mapObj.put("actualDown", actualDwn);
		  statusCount.put("finalCount", mapObj);	
		  
		  listMap = modifyList(listMap, request.getPageNo(), request.getRowCount());
			resp.setData(listMap);
			resp.setCountData(statusCount);
			resp.setMessage("success");
			return resp;
		}
		
		
	}
	
	private static void sortList(List<Map<String, Object>> listMap) {
		// TODO Auto-generated method stub
		listMap.sort((o2, o1) -> o1.get("eventDt").toString().compareTo(o2.get("eventDt").toString()));

	}


	private Map<String, Object> getCountsforDepot(List<Map<String, Object>> listMap,Temp_Response temoObj) {
		Integer downCount=0,upcount=0,offRdTrnfScrped=0,DWSMaintBD=0,parked=0;
		Map<String, Object> count = new HashMap<>();
		
		

		for(Map<String, Object> mapObj : listMap) {
			//System.out.println("debaging --- >> "+new Gson().toJson(mapObj));
			if(mapObj.get("eventDt").toString().split(" ")[0].equals(temoObj.getStartDate())) {upcount++;}
			else {
				//System.out.println("debaging elsse part  --- >> "+new Gson().toJson(mapObj));
				downCount++;
				if(mapObj.get("status").toString().equals("AT DWS") || mapObj.get("status").toString().equals("MAINTENANCE") || mapObj.get("status").toString().equals("BREAK DOWN")) {
					DWSMaintBD++;
				}else if(mapObj.get("status").toString().equals("OFF ROAD") || mapObj.get("status").toString().equals("TRANSFERRED") || mapObj.get("status").toString().equals("SCRAPPED")) {
					offRdTrnfScrped++;
				}else if(mapObj.get("status").toString().equals("ON ROAD") && mapObj.get("mainPower").toString().equals("0") || mapObj.get("ignition").toString().equals("0")) {
					parked++;
				}else {
					System.out.println("unidentified data"+mapObj.get("status").toString());
				}
			}
			
				
		}
		
		count.put("offRd-trnf-scrped", offRdTrnfScrped);
		count.put("DWS-Maint-BD", DWSMaintBD);
		count.put("upcount", upcount);
		count.put("downcount", downCount);
		count.put("totalVehicle", (upcount + downCount) );
		count.put("parkedVehicle", parked);
		Integer actualDwn = downCount-(offRdTrnfScrped + parked + DWSMaintBD); 
		count.put("actualDown", actualDwn);
		
		
		return count;
	}

	private static List<Map<String, Object>> filterByDepotCode(List<Map<String, Object>> listData,List<String> depotCodeList){
		System.out.println("filterMethod -- >>> "+listData.size()+" "+depotCodeList.toString());
		List<Map<String, Object>> newList = new ArrayList<>();
		
		if (depotCodeList.size() < 200 && depotCodeList.size() > 1) {
			Integer divisionId = QueryClass.getDivisionIdFromDepot(depotCodeList.get(0));
			logger.info("single division ::::::::  ");
			if (divisionId > 0) {
				try {
					Predicate<Map<String, Object>> condition = exp -> !exp.get("divisionId").toString().equals(divisionId.toString()) ;					
					listData.removeIf(condition);
					return listData;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		for (Map<String, Object> mapObj : listData) {
			if (depotCodeList.contains(mapObj.get("depotCode"))) {
				newList.add(mapObj);
			}
		}

		return newList;
	}

	private static List<Map<String, Object>> modifyList(List<Map<String, Object>> listMap,int pageNo,int rowCount){
		
		System.out.println("Size :: "+listMap.size()+" "+pageNo +" "+rowCount);
		List<Map<String, Object>> list = new ArrayList<>();
		int startCount = (pageNo * rowCount);
		int endCount = (pageNo * rowCount)+rowCount;
		try {
			if(listMap.size() == 0) {
				return listMap;
			}
			if(pageNo == 0) {
				System.out.println("size -- >>> f"+listMap.size());
				list = listMap.subList(0, listMap.size() > rowCount ? rowCount : listMap.size());
				return list;
			}
			if(listMap.size() > (pageNo * rowCount)) {
			    if(listMap.size() < endCount) {
			    	list = listMap.subList(startCount,listMap.size() );
			    }else {
			    	list = listMap.subList(startCount,endCount );
			    }
				
				return list;
			}else {
				
				int countDat = list.size()%rowCount;
				list = listMap.subList(Math.max(listMap.size() - 10, 0), listMap.size());
				return list;		
			}
		}catch (IndexOutOfBoundsException e) { 
            System.out.println("Exception thrown : index out of Bond" + e); 
        }catch (IllegalArgumentException e) { 
        	e.printStackTrace();
            System.out.println("Exception thrown : IllegalArgumentException" + e); 
        }catch(Exception e) {
        	System.out.println("Exception catch--- >> "+e.getMessage());
        }
		
		return null;
		
	}
	
	private static List<Map<String, Object>> getdownTime(Temp_Response request){
		
		List<Map<String, Object>> listMap =  Json_Activity.readFromAJsonFile(request.getStartDate(),0);
		return  filterByDepotCode(listMap, request.getDepotCodes());
		
		
	}
	
	public static void main(String[] args) {
		/*
		 * LocalDateTime lDT = new LocalDateTime(); //2020-03-19 String days =
		 * lDT.getDayOfMonth() > 10 ? String.valueOf(lDT.getDayOfMonth()) :
		 * "0"+lDT.getDayOfMonth(); String month = lDT.getMonthOfYear() > 10 ?
		 * String.valueOf(lDT.getMonthOfYear()) : "0"+lDT.getMonthOfYear(); String
		 * dtFormat = lDT.getYear()+"-"+month+"-"+days;
		 * 
		 * System.out.println(dtFormat);
		 */
		
		
	}
	@Override
	public String StoreData_depot(){
		Map<String, Integer> countMap = new HashMap<>();
		List<Map<String, Integer>> countMapList = new ArrayList<Map<String, Integer>>();
		
		LocalDateTime lDT = new LocalDateTime(); //2020-03-19
		String days = lDT.getDayOfMonth() > 10 ? String.valueOf(lDT.getDayOfMonth()) : "0"+lDT.getDayOfMonth();
		String month = lDT.getMonthOfYear() > 10 ? String.valueOf(lDT.getMonthOfYear()) : "0"+lDT.getMonthOfYear();
		String dtFormat = lDT.getYear()+"-"+month+"-"+days;
		
		try {
			
			Connection conn = QueryClass.getConnection();
			Statement stmt = conn.createStatement();
			for (int i = 1; i <= 56; i++) {
		     	ResultSet rs = stmt.executeQuery(QueryClass.getQueryForDepotwise(i));
		     	 List<Map<String, Object>> mapList = new ArrayList<>();
		     	while(rs.next()) {
		     		Map<String, Object> mapObj = new HashMap<>();
		     		mapObj.put("fixed_status", rs.getString("fixed_status") == null ? "" : rs.getString("fixed_status"));
		     		mapObj.put("divisionId", rs.getString("division_id"));
		     		mapObj.put("division", rs.getString("division_name"));
		     		mapObj.put("depotCode", rs.getString("depot_code"));
		     		mapObj.put("regNo", rs.getString("vehicle_reg_no"));
		     		mapObj.put("eventDt", rs.getString("event_date"));
		     		mapObj.put("status", rs.getString("vehicle_status"));
		     		mapObj.put("ignition", rs.getString("ignition_status"));
		     		mapObj.put("deviceImei", rs.getString("device_imei"));
		     		mapObj.put("depot", rs.getString("depot_name"));
		     		mapObj.put("mainPower", rs.getString("main_battery_status") == null ? "":rs.getString("main_battery_status"));
		     		
		     		mapList.add(mapObj);
		     		
		     	}
		     	String result = new Gson().toJson(mapList);
		     	
		      	
				
				//-------save to json file -----------//
		     	//saveTodatabase(i,mapList,dtFormat);
		     	String fileName = "division/count_data_"+String.valueOf(i)+"_"+ dtFormat;
		     	
		     	Json_Activity.createJsonFile(result, fileName);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	synchronized private void  saveTodatabase(Integer data_id,String type, String jsonStr,Integer size, String dtFormat) {
		
	
				try {
					logger.info("scheduler saving to table --- >>>type,stringlength,dateformate "+type+"  "+jsonStr.length()+" "+dtFormat);
					String sql = " insert into temp_storage_report_data (data_id,report_type,total_count,json_string,created_dt) values("+data_id+",'"+type+"',"+size+",'"+jsonStr+"','"+dtFormat+"')";
					if(conn_uat == null || conn_uat.isClosed()) {
						conn_uat = QueryClass.getConnection_uat();
					}
					//Statement stmt = conn_uat.createStatement();
					//stmt.executeUpdate(sql);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		

	}

	@Override
	public String StoreUpDownCount_divisionwise() {
		try {
			
				
			
			LocalDateTime lDT = new LocalDateTime(); //2020-03-19
			String days = lDT.getDayOfMonth() > 10 ? String.valueOf(lDT.getDayOfMonth()) : "0"+lDT.getDayOfMonth();
			String month = lDT.getMonthOfYear() > 10 ? String.valueOf(lDT.getMonthOfYear()) : "0"+lDT.getMonthOfYear();
			String dtFormat = lDT.getYear()+"-"+month+"-"+days;
			
			for (int i = 1; i <= 56; i++) {
				ConstantData.temp_division_id = i;
				Map<String, Integer> countMap = new HashMap<>();
				List<Map<String, Integer>> countMapList = new ArrayList<Map<String, Integer>>();

				System.out.println("welcome ---- >>> ");
				Integer upCnt = 0, dwnCnt = 0, actualDown = 0, OFFRoadTRANSFERREDSCRAPPED = 0,
						BreakdownAtDWSMaintancevehicl = 0, parkedVehicle = 0, totalehicle = 0;
				upCnt = Query_uptime_down_time.totlVehicleUp2();
				totalehicle = Query_uptime_down_time.totalVehicle2();
				dwnCnt = totalehicle - upCnt;
				parkedVehicle = Query_uptime_down_time.parckedCount2();
				OFFRoadTRANSFERREDSCRAPPED = Query_uptime_down_time.OFFRoadTRANSFERREDSCRAPPED2();
				BreakdownAtDWSMaintancevehicl = Query_uptime_down_time.BreakdownAtDWSMaintancevehicle2();
				actualDown = dwnCnt - (OFFRoadTRANSFERREDSCRAPPED + BreakdownAtDWSMaintancevehicl + BreakdownAtDWSMaintancevehicl);

				countMap.put("upcount", upCnt);
				countMap.put("downcount", dwnCnt);
				countMap.put("parkedVehicle", parkedVehicle);
				countMap.put("offRd-trnf-scrped", OFFRoadTRANSFERREDSCRAPPED);
				countMap.put("DWS-Maint-BD", BreakdownAtDWSMaintancevehicl);
				countMap.put("totalVehicle", totalehicle);
				countMap.put("actualDown", actualDown);
				countMap.put("totalVehicle", totalehicle);
				countMapList.add(countMap);

				String result = new Gson().toJson(countMapList);
				
				int count = i;		     	
				Thread t = new Thread() { 
					public void run()
					{
						try {
							saveTodatabase(count,"division_count",result,countMapList.size(), dtFormat);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				};t.start();
				
				
				
				System.out.println("this is result -- >> " + result);
				String fileName = "count_data_"+ConstantData.temp_division_id +"_"+ dtFormat;
				Json_Activity.createJsonFile(result, fileName);
				System.out.println("result ---- >>> " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	
	
	

	
}
