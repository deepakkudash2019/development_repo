package com.ms.WebPlatform.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ms.WebplatformApplication;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MembershipGroup;
import com.ms.WebPlatform.model.MsrtcDivisionMaster;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.model.UserAccessPrevillage;
import com.ms.WebPlatform.model.Vehicle;
import com.ms.WebPlatform.repository.DepotRepository;
import com.ms.WebPlatform.repository.DivisionRepository;
import com.ms.WebPlatform.repository.UserAccessRepository;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.QueryClass;
import com.ms.WebPlatform.utility.Utility;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ServiceImpl implements ServiceInterface {
 
	@Autowired
	private Importer imp;
	
	/*  @Autowired
	private DivisionRepository divisionRepo;
	@Autowired
	private DepotRepository depoRepo;
	@Autowired
	private EntityManager em;
	@Autowired
	private GraphInterface graphinterface;
	@Autowired
	private UserAccessRepository userAccessRepo;  */

    private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);
    private static Connection connProd = null;
    
    
    @Override
	public GlobalResponse getDevicePing(Temp_Response tempObj) {
    	GlobalResponse resp = new GlobalResponse();
		
		Integer countLive = QueryClass.devicePing_on_a_day(tempObj.getStartDate(), "L", tempObj.getImei());
		Integer countHistory = QueryClass.devicePing_on_a_day(tempObj.getStartDate(), "H", tempObj.getImei());
		Integer countTotal = tempObj.getSearch()==null || tempObj.getSearch().length()==0 ? (countLive + countHistory):tempObj.getSearch().equals("L") ? countLive :countHistory ;//QueryClass.devicePing_on_a_day(tempObj.getStartDate(), tempObj.getSearch(), tempObj.getImei());
		List<Map<String, Object>> mapList = QueryClass.devicePing_on_a_day_data(tempObj);
		Boolean bool = mapList.size() > 0 ? true:false;
		Map<String, Integer> finalCount=new HashMap<>();
		finalCount.put("live", countLive);
		finalCount.put("history", countHistory);
		resp.setCode(bool ? 1 : 0);
		resp.setStatus(bool ? "true" : "false");
		resp.setData(mapList);
		resp.setCount(countTotal);
		resp.setMessage(bool ? "success" : "data not available");
		resp.setCountData(finalCount);
		return resp;
	}

    
    private static  List<Map<String, Object>> getmappUnmapped_data(Temp_Response tempObj) throws SQLException{
    	List<Map<String, Object>> mapList = new ArrayList<>();
    	String query = "";
    	if(tempObj.getSearch().equals("mapp")) {
    		query = QueryClass.mappDevices(tempObj);
    	}else {
    		query = QueryClass.unmappDevices(tempObj);
    	}
    	if(connProd == null || connProd.isClosed()) {connProd = QueryClass.getConnection(); }
    	  
    	Statement stmt = connProd.createStatement();
    	ResultSet rs = stmt.executeQuery(query);
    	//,,,,update_dt,user_login,MappingStatus
    	while(rs.next()) {
    		Map<String, Object> mapObj = new HashMap<>();
    		mapObj.put("division", rs.getString("division_name"));
    		mapObj.put("depot", rs.getString("depot_name"));
    		mapObj.put("vehicle", rs.getString("vehicle_reg_no"));
    		mapObj.put("deviceImei", rs.getString("device_imei"));
    		mapObj.put("deviceimeiAlias", rs.getString("device_imei_alias"));
    		mapObj.put("manufacturer", rs.getString("manufacturer"));
    		mapObj.put("date", rs.getString("update_dt"));
    		mapObj.put("user", rs.getString("user_login"));
    		mapObj.put("status", rs.getString("MappingStatus") == null ? "":rs.getString("MappingStatus"));
    		
    		
    		mapList.add(mapObj);
    	}
    	
    	return mapList;
    }


	@Override
	public GlobalResponse getmappUnmapped(Temp_Response tempObj) {

		GlobalResponse resp = new GlobalResponse();
		List<Map<String, Object>> mapList = new ArrayList<>();
		int mappCount = 0, unmappCount = 0, total = 0;
		Map<String, Integer> countMap = new HashMap<>();

		try {
			if (tempObj.getSearch() == null || tempObj.getSearch().length() < 1) {
				tempObj.setSearch("mapp");
				List<Map<String, Object>> listMapp = getmappUnmapped_data(tempObj);
				mappCount = listMapp.size();
				tempObj.setSearch("unmapp");
				List<Map<String, Object>> listUnmapp = getmappUnmapped_data(tempObj);
				
				mapList.addAll(listMapp);
				mapList.addAll(listUnmapp);
				
				unmappCount = listUnmapp.size();
				total = (mappCount + unmappCount);
			}else if(tempObj.getSearch().equalsIgnoreCase("mapp")) {
				System.out.println("calling mapped ");
				tempObj.setSearch("mapp");
				mapList = new ArrayList<>();
				mapList = getmappUnmapped_data(tempObj);
				mappCount = mapList.size();
				total = (mappCount + unmappCount);
			}else {
				tempObj.setSearch("unmapp");
				mapList = new ArrayList<>();
				mapList = getmappUnmapped_data(tempObj);
				unmappCount = mapList.size();
				total = (mappCount + unmappCount);
			}

			if(tempObj.getIs_export() != null && tempObj.getIs_export() == 1) {
				resp.setCode(1);
				String type = tempObj.getSearch() == null || tempObj.getSearch().length() < 1 ? "mappunmapp" : tempObj.getSearch();
				System.out.println("-->> "+mapList.size());
				resp.setData(imp.getPdfExcel().mappunmapp(mapList,type));
				resp.setMessage("success");
				return resp;
			}
			
			
			boolean bool = mapList.size() > 0 ? true:false;
				if(bool) {
					countMap.put("total", total);
					countMap.put("totalMapped", mappCount);
					countMap.put("totalUnmapped", unmappCount);
				}
				
			resp.setCode(bool ? 1 : 0);
			resp.setData(modifyList(mapList, tempObj.getPageNo(), tempObj.getRowCount()));
			resp.setCountData(bool ? countMap : "");
			resp.setMessage(bool ? "success" : "datanot available");
			resp.setCount(total);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMessage(e.getMessage());
			resp.setStatus("fail");
			
			return resp;
		}finally {
			try {
				connProd.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
    @Override
	public GlobalResponse getVehicleActivity(Temp_Response tempObj) {
		GlobalResponse resp = new GlobalResponse();
		Connection conn = null;
		Statement stmt = null;
		 ResultSet rs  = null;
    	try {
    		System.out.println("payload check -- >> "+new Gson().toJson(tempObj));
    		 String query = "";    		 
		   query = QueryClass.getTrnfVehicle(tempObj);
		  
		  System.out.println("result -->>> "+query);
		  conn = QueryClass.getConnection_uat();
		   stmt = conn.createStatement();
		   rs = stmt.executeQuery(query);	
		  List<Map<String, Object>> mapList = new ArrayList<>();
		  //vehicle_reg_no,from_depot,to_depot,from_division,to_division
		  while(rs.next()) {
			  String fromdepot = rs.getString("from_depot");
			  String todepot = rs.getString("to_depot") ; 
			  String fromDivision = rs.getString("from_division");
			  String toDivision = rs.getString("to_division");
			  
			  fromDivision +="  ( "+fromdepot+" )";
			  toDivision += "   ( "+todepot+" )";
			  
			  Map<String, Object> mapObj = new HashMap<>();
			  mapObj.put("vehicle",rs.getString("vehicle_reg_no"));
			  mapObj.put("status",rs.getString("vehicle_status"));
			  mapObj.put("fromDepot",rs.getString("from_depot"));
			  mapObj.put("toDepot",rs.getString("to_depot"));
			  mapObj.put("fromDivision",rs.getString("from_division"));
			  mapObj.put("toDivision",rs.getString("to_division"));
			  mapObj.put("from",fromDivision);
			  mapObj.put("to",toDivision);
			  mapObj.put("date",rs.getString("created_dt"));
			  
			  mapList.add(mapObj);
			  
		  }
		  //mapList = mapList.size() > 0 ? filterDEviceData(mapList,tempObj) : mapList;
		  resp.setCode(mapList.size() > 0 ?1:0);
		  resp.setMessage(mapList.size() > 0 ? "success" : "data not available");
		  if(tempObj.getIs_export() == 1) {
			  resp.setData(imp.getPdfExcel().tranferVehicleReport(mapList,"transfered"));
		  }else {
		   resp.setData(mapList);
		   resp.setCount(ConstantData.transFerredVehicleCount);
		  }
		  return resp;
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMessage(e.getMessage());
			resp.setStatus("fail");
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
	public GlobalResponse saveVehicleActivity(Vehicle rvlObj) {
		System.out.println("welcome vehicle Activity :::: "+new Gson().toJson(rvlObj));
		GlobalResponse resp = new GlobalResponse();
		try {
			rvlObj.setCreatedDt(new Date());
			String dateStr = Utility.fromDateToString(new Date());
			String query = "insert into temp_vehicle_activity (vehicle_reg_no,vehicle_status,from_depot_code,to_depot_code,created_Dt) " + 
					"values('"+rvlObj.getVehicleRegNo()+"','"+rvlObj.getVehicleStatus()+"','"+rvlObj.getFromDepotCode()+"','"+rvlObj.getToDepotCode()+"','"+dateStr+"')";
			
			System.out.println("query -- >> "+query);
			Connection conn = QueryClass.getConnection_uat();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
			resp.setData(rvlObj);
			resp.setCode(1);
			resp.setMessage("saved successfully");
			resp.setStatus("success");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMessage(e.getMessage());
			resp.setStatus("fail");
		}
		return resp;
	}
    
	@Override
	public Object getGraphDataForDepo(String strVal) {
		MsrtcDivisionMaster divObj = imp.getDivisionRepo().findBydivisionName(strVal);
		System.out.println("the data is >>> "+divObj+"  "+strVal);
		ConstantData.depotList = imp.getDepoRepo().findBydivisionId(divObj.getDivisionId());	
		
		
		return makeMapGraph(ConstantData.depotList,divObj.getDivisionId());
	}
	
	private Object makeMapGraph(List<Depot> depotList,Integer divisionId) {
		
		Map<String, String> mapObj = null;
		List<Map<String, String>> mapObjList = new ArrayList<Map<String,String>>();
        int count_i = 0;
		
		for (int i = 0 ; i<ConstantData.strArr2.length ; i++) {
			mapObj = new LinkedHashMap<String, String>();
			mapObj.put("timing", ConstantData.strArr2[i]);
			for (Depot dpt : depotList) {
				String depotWithOutSpace = dpt.getDepot_name().replace(" ", ""); // -------------this is tempory code , As shubham Request me to remove space -- 27-11-2019
				
				Integer totalCOunt = Utility.getDepotScheduleCount(ConstantData.strArr[i], dpt.getDepot_id());				
				
				
				mapObj.put(depotWithOutSpace+"_unAssigned",totalCOunt.toString());
				mapObj.put(depotWithOutSpace+"_assigned",String.valueOf(0));
				mapObj.put(depotWithOutSpace+"_total",totalCOunt.toString());
				mapObj.put("Depot_"+depotWithOutSpace,depotWithOutSpace);
				mapObj.put("depotId", dpt.getDepot_id().toString());
				
			}
			
			mapObjList.add(mapObj);
			count_i++;
		}
		setCount(mapObjList, divisionId);
		return modifyDataOnTimeFor_AllDepotOf_Division(mapObjList);
		
	}
	
	

	private  void setCount(List<Map<String, String>> mapObjList,Integer divisionId) {
		List<Object> authors =imp.getGraph().ExecuteQuery();
		
		System.out.println("checking route data >>>> "+new Gson().toJson(authors));
		for(Object obj : authors) {
			String res = new Gson().toJson(obj);
			res = res.replace("[", "");
			res = res.replace("]", "");
			res = res.replace("\"", "");
			// total, Assigned_trip, depot_id, RoundTOHour, depot_code, depot_name, division_id		
			
			
			System.out.println(" Result >>> "+res);
			String []StrArr = res.split(",");
			if(StrArr[6] != null  && StrArr[6].length() > 0  && StrArr[6].equalsIgnoreCase(divisionId.toString())) {
				System.out.println("Data Matched >>>>>>>>  ");
				Integer hour = Integer.parseInt(StrArr[3]);
				String timeSlot = ConstantData.strArr2[hour];//hour < 23 ? hour+"-"+(hour+1) : "23-0";
				for(Map<String, String> mapObj : mapObjList) {
					if(mapObj.get("timing") != null && mapObj.get("timing").equals(timeSlot)) {
						String depotNameWithOutSpace = StrArr[5].trim().replace(" ", "");
						
						Integer totalDepot = mapObj.get(depotNameWithOutSpace+"_total") != null ? Integer.parseInt(mapObj.get(depotNameWithOutSpace+"_total")) : 0;
						Integer unassigned = totalDepot - Integer.parseInt(StrArr[1]);
						mapObj.put(depotNameWithOutSpace+"_unAssigned",String.valueOf(unassigned));
						mapObj.put(depotNameWithOutSpace+"_assigned",StrArr[1]);
						
						mapObj.put("Depot_"+depotNameWithOutSpace,depotNameWithOutSpace);
						break;
					}
				}
				
				
				
			}
			
			
		}
		
		System.out.println("data check >>> "+new Gson().toJson(mapObjList));
		
	}

	private static Depot globalDepot =null;
	@Override
	public Object getDepotGraphForManager(Depot depot) {
		globalDepot = depot;
		List<Object> authors =imp.getGraph().ExecuteQueryForDEpot(depot.getDepot_id());
		
		
		Map<String, String> mapStr = null;
		List<Map<String, String>> mapList = new ArrayList<Map<String,String>>();
		for(int i=0;i< ConstantData.strArr2.length; i++) {
			
			String totalSchedule = Utility.getDepotScheduleCount(ConstantData.strArr[i], depot.getDepot_id()).toString();
			mapStr = new LinkedHashMap<String, String>();
			mapStr.put("timing", ConstantData.strArr2[i]);
			mapStr.put("DepotName", depot.getDepot_name());
			mapStr.put("assigned", "0");
			mapStr.put("unAssigned", totalSchedule);
			mapStr.put("Total", Utility.getDepotScheduleCount(ConstantData.strArr[i], depot.getDepot_id()).toString());
			
			mapList.add(mapStr);
		}
		
		//System.out.println("\t\tthis is REsult >>>>>>>>>  "+new Gson().toJson(mapList));
		
		for(Object obj : authors) {
			String res = new Gson().toJson(obj);
			res = res.replace("[", "");
			res = res.replace("]", "");
			res = res.replace("\"", "");
			// total, Assigned_trip, depot_id, RoundTOHour, depot_code, depot_name, division_id		
			
			
			System.out.println(" Result >>> "+res);
			String []StrArr = res.split(",");
			if(StrArr[2] != null && StrArr[2].length() > 0 && Integer.parseInt(StrArr[2].trim()) == depot.getDepot_id()) {
				
				System.out.println("this is 1st condition >>>>>  "+StrArr[2]);
				Integer hour = Integer.parseInt(StrArr[3]);
				String timeSlot =  ConstantData.strArr2[hour];
				 for(Map<String, String> mapStr2:mapList) {
					 if(mapStr2.get("timing").equals(timeSlot)) {
						    //mapStr.put("timimg", ss);
						 System.out.println("this is Timing >>>>>  "+mapStr2.get("timing")+"  "+timeSlot);
						 Integer depotTotalCount = Utility.getDepotScheduleCount(ConstantData.strArr[hour], depot.getDepot_id());
						 Integer unassigned = depotTotalCount - Integer.parseInt(StrArr[1]);
						// mapStr2 = new LinkedHashMap<String, String>();
						 mapStr2.put("timing", timeSlot);
						 mapStr2.put("DepotName", depot.getDepot_name());
						 mapStr2.put("assigned", StrArr[1]);
						 mapStr2.put("unAssigned", unassigned.toString());
						 mapStr2.put("Total", depotTotalCount.toString());	
							
						break;
					 }
					 
					 
				 }
				
			}
			
		}
		System.out.println("\n\nFinal Res>>>> "+new Gson().toJson(mapList));
		
		return modifyDataOnCurrentHour_forManagerGrapoh(mapList,depot);
		
	}
	
	
	//-------------------MODIFY ACCORDING TO TIME----------------------//
	
	
	
	private static List<Map<String, String>> modifyDataOnTimeFor_AllDepotOf_Division(
			List<Map<String, String>> mapObjList) {
		List<Map<String, String>> mapObjList2 = null;
		ConstantData.countMapList_forAll_depot = new ArrayList<Map<String,String>>();
		try {
			mapObjList2 = new ArrayList<Map<String, String>>();
			DateTime dt = new DateTime();
			int hours = dt.getHourOfDay();

			for (Map<String, String> mapObj : mapObjList) {
				String timeHour = mapObj.get("timing").split("-")[0].trim().split(":")[0].trim();
				countTotal_depot(mapObj);
				if (Integer.parseInt(timeHour) == hours) {
					mapObjList2.add(mapObj);
					break;

				} else {
					mapObjList2.add(mapObj);
				}

			}

			return mapObjList2;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapObjList;
	}

	private static List<Map<String, String>> modifyDataOnCurrentHour_forManagerGrapoh(List<Map<String, String>> mapList, Depot depot) {
  
		ConstantData.countMapList_forAll_depot_manager = new ArrayList<Map<String,String>>();
		try {
			List<Map<String, String>> mapList2 = new ArrayList<Map<String, String>>();
			DateTime dt = new DateTime();
			int hours = dt.getHourOfDay();
			for (Map<String, String> mapObj : mapList) {
				countTotal_depot_manager(mapObj,depot);
				String timeHour = mapObj.get("timing").split("-")[0].trim().split(":")[0].trim();
				if (Integer.parseInt(timeHour) == hours) {
					mapList2.add(mapObj);
					break;
				} else {
					mapList2.add(mapObj);
				}

			}

			System.out.println("Testing DATA --- >>> "+new Gson().toJson(ConstantData.countMapList_forAll_depot_manager));
			
			return mapList2;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapList;
	}
	
	
	// ----------------Counting--------------------------//

		private static void countTotal_depot_manager(Map<String, String> mapObj, Depot depot) {
			// Set<String> mapSet = mapObj.keySet();

			System.out.println("\n\ncountTotal_depot_manager this is Total COunt ----->>"+new Gson().toJson(mapObj));
		
				String depotName = depot.getDepot_name();		

				countEngine_depot_manager(depotName, mapObj.get("assigned"), mapObj.get("unAssigned"),mapObj.get("Total"));

			

		}

		private static void countEngine_depot_manager(String depotNm, String assigned, String unAssigned, String total) {

			int countLoop = 0;
			int checker = 0;

			System.out.println("countEngine_depot_manager ----->>> "+depotNm+" "+assigned+" "+unAssigned+" "+total);
			for (Map<String, String> mapObj : ConstantData.countMapList_forAll_depot_manager) {
				if (mapObj.get("depotNm").equalsIgnoreCase(depotNm)) {
					Integer assignedVal = Integer.parseInt(mapObj.get("assigned")) + Integer.parseInt(assigned);
					Integer unAssignedVal = Integer.parseInt(mapObj.get("unAssigned")) + Integer.parseInt(unAssigned);
					Integer totalVal = Integer.parseInt(mapObj.get("total")) + Integer.parseInt(total);

					mapObj.put("assigned", assignedVal.toString());
					mapObj.put("unAssigned", unAssignedVal.toString());
					mapObj.put("total", totalVal.toString());
					checker = 1;
					break;
				}
				

				countLoop++;
			}

			if (checker == 0) {

				Map<String, String> newMapObj = new HashMap<String, String>();

				newMapObj.put("assigned", "0");
				newMapObj.put("unAssigned", "0");
				newMapObj.put("total", "0");
				newMapObj.put("depotNm", depotNm);

				ConstantData.countMapList_forAll_depot_manager.add(newMapObj);

				countEngine_depot_manager(depotNm, assigned, unAssigned, total);  // recursiv calling ----

			}

		}
		
		//----------Count2-------------//
		private static void countTotal_depot(Map<String, String> mapObj) {
			// Set<String> mapSet = mapObj.keySet();

			System.out.println("\t\tTHis is total Count >>>>>>  "+ ConstantData.AlldepotList.size());
			for (Depot depoObj : ConstantData.AlldepotList) {
				System.out.println("this is count Total ---------- >>>>>> "+depoObj.getDepot_name());
				String depotName = depoObj.getDepot_name().replace(" ", "");
				
				String key4Total = depotName + "_total";
				String key4Assigned = depotName + "_assigned";
				String key4UnAssigned = depotName + "_unAssigned";

				if(mapObj.get(key4Assigned) != null)
				  countEngine_depot(depotName, mapObj.get(key4Assigned), mapObj.get(key4UnAssigned),mapObj.get(key4Total));

			}

		}
		
		
		private static void countEngine_depot(String depotNm, String assigned, String unAssigned, String total) {

			int countLoop = 0;
			int checker = 0;

			for (Map<String, String> mapObj : ConstantData.countMapList_forAll_depot) {
				if (mapObj.get("depotNm").equalsIgnoreCase(depotNm)) {
					Integer assignedVal = Integer.parseInt(mapObj.get(depotNm+"_assigned")) + Integer.parseInt(assigned);
					Integer unAssignedVal = Integer.parseInt(mapObj.get(depotNm+"_unAssigned")) + Integer.parseInt(unAssigned);
					Integer totalVal = Integer.parseInt(mapObj.get(depotNm+"_total")) + Integer.parseInt(total);

					mapObj.put(depotNm+"_assigned", assignedVal.toString());
					mapObj.put(depotNm+"_unAssigned", unAssignedVal.toString());
					mapObj.put(depotNm+"_total", totalVal.toString());
					checker = 1;
					break;
				}

				countLoop++;
			}

			if (checker == 0) {

				Map<String, String> newMapObj = new HashMap<String, String>();

				newMapObj.put(depotNm+"_assigned", "0");
				newMapObj.put(depotNm+"_unAssigned", "0");
				newMapObj.put(depotNm+"_total", "0");
				newMapObj.put("depotNm", depotNm);

				ConstantData.countMapList_forAll_depot.add(newMapObj);

				countEngine_depot(depotNm, assigned, unAssigned, total);  // recursiv calling ----

			}

		}

	@Override
	public String getUserAccessjson(String userAccessJson) {
		logger.info("welcome >>>>>><<<<<"+userAccessJson);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(userAccessJson, Map.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (map != null) {

			logger.info("converted to map --->>> " + map.toString());
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*
		 * 
		 * String str = userAccessJson; String userId =
		 * str.substring(str.indexOf("<userId>")+8, str.indexOf("</userId>"));
		 * System.out.println("userId -->> "+userId); UserAccessPrevillage uapObj =
		 * userAccessRepo.findByuserId(Integer.parseInt(userId.trim())); String
		 * groupAccessJson =uapObj != null ? uapObj.getPrivilegeJson() : "";
		 */
		logger.info("converted to map --->>> " + map.toString());
		return new Gson().toJson(map);
	}
		
	
		private static String ChangeUserAccess(String completeStr,String groupAccessJson) {
			if(groupAccessJson.isEmpty()) {return completeStr;}
			 System.out.println("result -in change userAccess-->>>> "+completeStr);
			 String requiredString = completeStr.substring(completeStr.indexOf("<groupPrivilege>"), completeStr.indexOf("</groupPrivilege>"));
			// String newString = "<groupPrivilege>{\"accessMenus\":[\"dashboardMenu\",\"monMainMenu\",\"mapViewMen]}";
			 groupAccessJson = "<groupPrivilege>"+groupAccessJson+"</groupPrivilege>";
			 System.out.println("requiredString ::::123456  "+requiredString);
			 completeStr = completeStr.replace(requiredString, groupAccessJson);
			 System.out.println("result --- >>> "+completeStr);
			return completeStr;
		}

		@Override
		public GlobalResponse updtaeMembershipGroup(MembershipGroup mgrpObj) {
			GlobalResponse response = new GlobalResponse();
			try {
				MembershipGroup mObj = imp.getMembershipGrpRepo().findOne(mgrpObj.getGroupId());
				
				if(mObj != null) {	
					System.out.println("updating ....");
					mObj.setGroupPrivilege(new Gson().toJson(mgrpObj.getGroupPrivilegeObj()));
					imp.getMembershipGrpRepo().save(mObj);
					response.setCode(1);
					response.setMessage("update");
				}else {
					response.setCode(1);
					response.setMessage("else part");
					System.out.println("else part .......  ");
					if(mgrpObj != null && mgrpObj.getGroupPrivilegeObj() != null) {
						System.out.println("saving ....");
						mgrpObj.setGroupPrivilege(new Gson().toJson(mgrpObj.getGroupPrivilegeObj()));
						imp.getMembershipGrpRepo().save(mgrpObj);
						response.setCode(1);
						response.setMessage("saved");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				response.setCode(-1);
				response.setMessage(e.getMessage());
			}
			return response;
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
					
					int countDat = listMap.size()%rowCount;
					
					System.out.println("modulus Check -- >> "+countDat+" "+listMap.size() +" "+rowCount);
					list = listMap.subList(listMap.size() - countDat, listMap.size());
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


	@Override
  public void csvenerator() {
		LocalDateTime lDT = new LocalDateTime();
		String dayOfMonth = lDT.getDayOfMonth() < 10 ? "0"+lDT.getDayOfMonth() : lDT.getDayOfMonth()+"";
		String monthOfYear = lDT.getMonthOfYear() < 10 ? "0"+lDT.getMonthOfYear() : lDT.getMonthOfYear()+"";
		String namre= "csv_"+dayOfMonth+"-"+monthOfYear+"-"+lDT.getYear()+".csv";
		generateCsv(namre);
  }
		
	private static void generateCsv(String fileNm) {

		Thread t = new Thread() {
			public void run() {
				// ---------------fetching data-----------------------//
				String query = " select rvl.division_name,rvl.depot_name,rvl.division_id,rvl.depot_id,rvl.route_code,rvl.device_imei,rvl.event_location,rvl.event_date, "
						+ " rvl.speed,rvl.ignition_status,rvl.gps_status,rvl.main_battery_status,rvl.vehicle_status,rvl.running_status,rvl.vehicle_reg_no from report_vehicle_location rvl INNER JOIN vehicle_device vd  "
						+ "on rvl.vehicle_id = vd.vehicle_id where vd.is_valid = 'y' order by rvl.event_date desc";

				String dataArrTitle[] = new String[15];
				Connection connection = null;
				Statement stmStatement = null;
				ResultSet resultSet = null;
				List<String[]> dataListList = new ArrayList<>();
				
				

				try {
					connection = QueryClass.getConnection();
					stmStatement = connection.createStatement();
					resultSet = stmStatement.executeQuery(query);
					List<String> colimnList = columnName();
					int count = 0;
					
					while (resultSet.next()) {
						String dataArr[] = {"","","","","","","","","","","","","","",""};
						int miniCount = 0;
						for (String columnName : colimnList) {
							String valueColumn = resultSet.getString(columnName) != null ? resultSet.getString(columnName) : " ";
							dataArr[miniCount] = valueColumn;
							
							if(count ==0) {dataArrTitle[miniCount] = columnName;}
							
							miniCount++;
						}
						dataListList.add(dataArr);
						count++;
					}

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					if (connection != null) {
						try {
							connection.close();
							stmStatement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}

				// --------------creating CSV file--------------//

				File file = new File(fileNm);

				try {

					FileWriter outputfile = new FileWriter(file);
					CSVWriter writer = new CSVWriter(outputfile, ',',
							CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

					logger.info("CSV  created -- >> dataSize  " + dataListList.size() + "  file name -- >> " + fileNm);

					dataListList.add(0,dataArrTitle);
					writer.writeAll(dataListList);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		};
		t.start();

	}


		public static  List<String>  columnName() {
			String data = "rvl.division_name,rvl.depot_name,rvl.division_id,rvl.depot_id,rvl.route_code,rvl.device_imei,rvl.event_location,rvl.event_date,rvl.speed,rvl.ignition_status,rvl.gps_status,rvl.main_battery_status,rvl.vehicle_status,rvl.running_status,rvl.vehicle_reg_no";
		   data = data.replace("rvl.", "");
		 String str = "";
		 List<String> strList = new ArrayList<>();
			for(String ss : data.split(",")) {
				strList.add(ss);
		   }
			
			System.out.println("res-->"+strList.size()+"  "+strList.toString());
			

			return strList;
		}
		
		
		@Override
		public MsrtcDivisionMaster getDivisionById(Integer id) {
	      
			try {

				System.out.println("welcome >>>>>>>>>>  ");
				logger.info("welcome >>>> Controller.init");

				if (ConstantData.divListStaticMap == null) {
					List<MsrtcDivisionMaster> divList = Lists.newArrayList(imp.getDivisionRepo().findAll());
					ConstantData.divListStaticMap = new HashMap<Integer, MsrtcDivisionMaster>();
					for (MsrtcDivisionMaster obj : divList) {
						ConstantData.divListStaticMap.put(obj.getDivisionId(), obj);
					}
				}

				return ConstantData.divListStaticMap.get(id);

			} catch (Exception e) {
				e.printStackTrace(); 
				logger.info("welcome >>>> Controller.init Catch block"+e.getMessage());
				return null;
			}

		}
		
		

	
}
