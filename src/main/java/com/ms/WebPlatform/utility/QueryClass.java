package com.ms.WebPlatform.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.microsoft.schemas.office.x2006.encryption.STSaltSize;
import com.ms.WebplatformApplication;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.services.RawDataInterface;
import com.opencsv.CSVWriter;

import ch.qos.logback.classic.pattern.Util;

@Component
public class QueryClass {
	@Autowired private RawDataInterface rawdata;
	
	 private static final Logger logger = LoggerFactory.getLogger(QueryClass.class);
	 private static Connection con = null;
	
	
	public static List<Map<String, Object>> uptimeData(String type, String dateStr) {
		List<Map<String, Object>> listMap = new ArrayList<>();
		dateStr +=" 00:00:00"; 
		String query = "select json_string,total_count from temp_storage_report_data where created_dt ='"+dateStr+"' and report_type ='"+type+"'";
		
		try {
			Connection conn = getConnection_uat();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
			
				String jsonString  = rs.getString("json_string");
				Integer listCount = rs.getInt("total_count");
				logger.info("result uptime --- >> "+listCount+"  "+jsonString.length());
				
				 JSONArray jsonArr = new JSONArray(jsonString);

			        for (int i = 0; i < jsonArr.length(); i++)
			        {
			            JSONObject jsonObj = jsonArr.getJSONObject(i);			            		
			            HashMap<String, Object> mapObj = new Gson().fromJson(jsonObj.toString(), HashMap.class);
			            listMap.add(mapObj);          
			         
			        }
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return listMap;
	}
	 
	 
	
	public static String[] getDailyReportQuery(String dateStr,int page,int rowCount ,ArrayList<String> stringArr, ArrayList<String> vehicleNos) {
		StringBuilder sb = new StringBuilder();
		//select ld.device_id,ld.event_lat lat,ld.event_long lon,ld.depot_code,(select vehicle_reg_no from vehicle where vehicle_id = ld.vehicle_id) regNo from location_data ld where event_date > '2020-02-25 00:00:01' and event_date < '2020-02-25 23:59:00' and depot_code in ('ARG','nasik') limit 10
        String []queryContainer = {"",""};
		String stringIN = "";
		String vehicleNo =vehicleNos.size()>0 ? vehicleNos.get(0).trim() : null;
		for(String ss : stringArr) {
			stringIN+="'"+ss+"',";
      }
		stringIN +="'test'";
		if(page == 0) {}
		else {
			if(page > 0) {
				page = page * 10;
			}
		}
		String dtfrom = dateStr+" "+"00:00:01";
		String dtTo = dateStr+" "+"23:59:00";
		sb.append("select ld.device_id,ld.event_lat lat,ld.event_long lon,(select depot_name from depot where depot_code = ld.depot_code) depotName,event_date evntDt, ");
		sb.append("(select vehicle_reg_no from vehicle where vehicle_id = ld.vehicle_id) regNo ");
		sb.append("from location_data ld where ld.event_date > '"+dtfrom+"' and ld.event_date < '"+dtTo+"' ");
		if(vehicleNo != null) {sb.append(" and ld.vehicle_id = (select vcl.vehicle_id from vehicle vcl where vcl.vehicle_reg_no ='"+vehicleNo+"')");}
		sb.append(" and ld.depot_code in  (");
		sb.append(stringIN+") ");
		sb.append(" order by event_date desc limit "+page+","+rowCount);
		
		System.out.println("BIG QUERY --- >> "+sb.toString());
		queryContainer[0] = sb.toString();
		
		
		
		//select count(DISTINCT(vehicle_id)) from location_data where event_date > '2020-02-26' and depot_code in ('NAG','SLD','BSL','MRBD','UNA','RJRA','URN','TKPR','IGT','YLA','KRT','NBR','OMG','LTR','MHD','SNNR','PPR','THNE','PGNB','SRGD','MCT','NRK','BGL','PTRI','GGP','MWR','KNDR','SSWD','SKR','AKT','MNGD','MADK','JGT','KRG','BNG','KOP','HBL','WRR','PLUS','RDHN','MLKBLD','DVD','CHL','RJP','KLMNR','STR','GDL','TSGN','HNV','KND','LGN','SWR','SHRL','JNTR','ALB','KRM2','MGPIR','KGL','WSM','ISLP','BVD','SHVG','JLN','IDR','CPD','ARG','GDC','INDO','TKRE','PRTR','KTL','AKKT','VJP','ERDL','HDGN','AKJ','TLGWRD','NLG','MNG','ASTI','SGN','PSD','HMT','DHL','SNDK','PRL','DRR','BVINC')

		sb = new StringBuilder();
		sb.append("select count(DISTINCT(vehicle_id)) countData from location_data where ");
		sb.append(" event_date > '"+dtfrom+"' and event_date < '"+dtTo+"' ");
		if(vehicleNo != null) {sb.append(" and ld.vehicle_id = (select vcl.vehicle_id from vehicle vcl where vcl.vehicle_reg_no ='"+vehicleNo+"')");}

		sb.append("and depot_code in  (");
		sb.append(stringIN+") ");
		
		queryContainer[1] = sb.toString();
		
		System.out.println("complete Query -- >>> "+queryContainer[1]+"\n"+queryContainer[0]);
		return queryContainer;
		
	}

	
	public static String[] query_dailyReport(ArrayList<String> depots,int page,int rowCount) {
		StringBuilder sb = new StringBuilder();
		String[] stringArr = {"",""};
		
		String stringIN = "";
		new QueryClass().storeValidVehicleCount();
		
		if(depots.size() > 0 && depots.size() < 50) {
			new QueryClass().storeValidVehicleCount_DEpot(depots);
		}
		
		for(String ss : depots) {
			stringIN+="'"+ss+"',";
      }
		stringIN +="'test'";
		stringIN = "("+stringIN+") ";
		
		sb.append("SELECT rvl.event_date, rvl.division_name, rvl.depot_name, rvl.vehicle_reg_no, rvl.vehicle_status ");
		sb.append(" FROM msrtc.report_vehicle_location as rvl INNER JOIN depot d on rvl.depot_id = d.depot_id ");
		sb.append(" WHERE rvl.event_date > CURDATE() ");
		if(depots != null && depots.size() > 0) {sb.append(" AND d.depot_code in "+ stringIN );}
		sb.append(" AND (rvl.vehicle_status = 'BREAK DOWN' ");
		sb.append(" OR rvl.vehicle_status = 'SCRAPPED' ");
		sb.append(" OR rvl.vehicle_status = 'OFF ROAD' ");
		sb.append(" OR rvl.vehicle_status = 'TRANSFERRED' ");
		sb.append(" OR rvl.vehicle_status = 'MAINTENANCE' ");
		sb.append(" OR rvl.vehicle_status = 'AT DWS' OR rvl.vehicle_status = 'ON ROAD') ");
		sb.append(" ORDER BY rvl.event_date DESC ");
		if(page ==0 && rowCount == 0 ) {
			logger.info("All data  use for PDF & EXCEL");
		}else {
			if(page > 0) {
				page = page * rowCount;
			}
		  sb.append("limit "+page+","+rowCount);
		}
		
		stringArr[0] = sb.toString();
		
		sb = new StringBuilder();
		sb.append("SELECT COUNT(rvl.vehicle_reg_no) countData FROM msrtc.report_vehicle_location as rvl INNER JOIN depot d on rvl.depot_id = d.depot_id ");
		sb.append(" WHERE rvl.event_date > CURDATE() ");
		if(depots != null && depots.size() > 0) {sb.append(" AND d.depot_code in "+ stringIN );}
		sb.append(" AND (rvl.vehicle_status = 'BREAK DOWN' ");
		sb.append(" OR rvl.vehicle_status = 'SCRAPPED' ");
		sb.append(" OR rvl.vehicle_status = 'OFF ROAD' ");
		sb.append(" OR rvl.vehicle_status = 'TRANSFERRED' ");
		sb.append(" OR rvl.vehicle_status = 'MAINTENANCE' ");
		sb.append(" OR rvl.vehicle_status = 'AT DWS' OR rvl.vehicle_status = 'ON ROAD') ");
		sb.append(" ORDER BY rvl.vehicle_status ASC ");
		stringArr[1] = sb.toString();			
		
		System.out.println("queryList --> > > "+stringArr[0]+" \n"+stringArr[1]);
		return stringArr;
		
		
	}
	
	
	public static Map<String, Integer> countStatus_Vehicle(Connection conn,ArrayList<String> depots) throws SQLException {
		//String query = "select rvs.vehicle_status status,count(*) dataCount from report_vehicle_location rvs where event_date > CURDATE() GROUP by rvs.vehicle_status";
       String stringIN = "";
		
		for(String ss : depots) {
			stringIN+="'"+ss+"',";
      }
		stringIN +="'test'";
		stringIN = "("+stringIN+") ";
		
		String query = "SELECT rvl.vehicle_status status,COUNT(*) dataCount  FROM msrtc.report_vehicle_location as rvl INNER JOIN depot d on rvl.depot_id = d.depot_id  WHERE rvl.event_date > CURDATE()  AND d.depot_code in "
		+stringIN +" GROUP by rvl.vehicle_status ;";
		System.out.println("query --->> "+query);
		
		Statement stmt2=conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(query);	
		Map<String, Integer> mapCount = new HashMap<String, Integer>();
		Integer totalCount = 0;
					
		
		  mapCount.put("MAINTENANCE", 0);
		  mapCount.put("SCRAPPED", 0);
		  mapCount.put("TRANSFERRED", 0);
		  mapCount.put("AT DWS", 0);
		  mapCount.put("BREAK DOWN", 0);
		 
		while(rs2.next()) {
			mapCount.put(rs2.getString("status"), rs2.getInt("dataCount"));
			//totalCount +=rs2.getInt("dataCount");
		}
		//mapCount.put("AT DWS", getTotalBdscpdwsmain("AT DWS"));
		//mapCount.put("BREAK DOWN", getTotalBdscpdwsmain("BREAK DOWN"));
		//mapCount.put("MAINTENANCE", getTotalBdscpdwsmain("MAINTENANCE"));
		//mapCount.put("SCRAPPED", getTotalBdscpdwsmain("SCRAPPED"));
		//mapCount.put("TRANSFERRED", getTotalBdscpdwsmain("TRANSFERRED"));
		
		
		
		System.out.println("countMap -- >>>"+new Gson().toJson(mapCount));
		//mapCount.put("Total", totalCount);
		return mapCount;
	}
	
	
	
	public void countData(String query,Connection conn) throws InterruptedException {
		
	
		Thread t = new Thread() { // Creating an object of Anonymous class which extends Thread class and passing
									// this object to the reference of Thread class.
			public void run() // Anonymous class overriding run() method of Thread class
			{
				setName("Anonymous Thread");

				
				
				try {
					System.out.println("before query >>>>");
					Statement stmt2=conn.createStatement();
					ResultSet rs2 = stmt2.executeQuery(query);
					System.out.println("After  query >>>>");
					while(rs2.next()) {
						System.out.println("inSide Loop>>>> ");
						ConstantData.totalDataCount = rs2.getInt("countData");
					}
				} catch (SQLException e) {
				
					e.printStackTrace();
				}


			}
		};
		t.start();
		
		Thread.sleep(3000);
	}
	
	
	//---------canceltripBY date ------------//
	public static String getcalcelTripQuery(String dateFrom,String dateTo , Integer type) {
		StringBuilder sb = new StringBuilder();
		sb.append("select mted.trip_seq, mted.trip_date, msm.trip_no,d.depot_code,d.depot_name,r.route_long_name,v.vehicle_reg_no,");
		sb.append(" (select mdm.division_name FROM msrtc_division_master mdm where mdm.division_id = d.division_id) division_name");
		sb.append(" from msrtc_trip_entry_details mted INNER JOIN ");
		sb.append(" msrtc_schedule_master msm on mted.schedule_id = msm.schedule_id INNER JOIN ");
		sb.append(" depot d on d.depot_id = msm.depot_id ");
		sb.append(" INNER JOIN vehicle v on mted.vehicle_id = v.vehicle_id ");
		sb.append(" INNER JOIN route r on r.route_id = msm.route_id ");
		if(dateTo == null ) {
		  sb.append(" where mted.trip_date ='"+dateFrom+"'  and mted.status_id ="+type);
		}else {
			sb.append(" where mted.trip_date >='"+dateFrom+"'  and mted.trip_date <='"+dateTo+"' and  mted.status_id ="+type);
		}
		
		System.out.println("cencelTrip Query--- >>> "+sb.toString());
		return sb.toString();
			
	}
	
	
	
	public static String getExtraTripQuery(String dateFrom,String dateTo , Integer type) {
		StringBuilder sb = new StringBuilder();
		sb.append("select mted.trip_seq, mted.trip_date,msm.schedule_id, msm.trip_no, d.depot_code,d.depot_name,r.route_long_name,v.vehicle_reg_no,");
		sb.append("(select mdm.division_name FROM msrtc_division_master mdm where mdm.division_id = d.division_id) division_name ");
		sb.append(" from msrtc_trip_entry_details mted INNER JOIN ");
		sb.append(" msrtc_schedule_master msm on mted.schedule_id = msm.schedule_id ");
		sb.append(" INNER JOIN depot d on d.depot_id = msm.depot_id ");
		sb.append(" INNER JOIN vehicle v on mted.vehicle_id = v.vehicle_id ");
		sb.append(" INNER JOIN route r on r.route_id = msm.route_id ");
		if(dateTo != null && dateTo.length() > 0) {
			sb.append(" where mted.trip_date >='"+dateFrom+"' and mted.trip_date <='"+dateTo+"' and mted.is_extra = 1");
		}else {
			sb.append(" where mted.trip_date ='"+dateFrom+"'  and mted.is_extra = 1");
		}
			
		
		System.out.println("queryTest -->>> "+sb.toString());
		return sb.toString();
		
	}
	
	
	public static String getBREAKdOWNQuery(Temp_Response data) {	

		StringBuilder sb = new StringBuilder();
				
		sb.append("select DISTINCT(md.division_name),d.depot_name,v.vehicle_reg_no, v.vehicle_status,d.depot_code from vehicle v  ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join vehicle_depot vd1 on vd1.vehicle_id = v.vehicle_id ");
		sb.append(" inner join depot d on d.depot_id = vd1.depot_id ");		
		sb.append(" inner join msrtc_division_master md on md.division_id =d.division_id ");		
		sb.append(" where v.vehicle_status ='Break down' and vd.is_valid = 'Y' and vd1.is_valid ='Y' ");		
		
		System.out.println("Query --- >>>> "+sb.toString());
		return sb.toString();
		
		
	}
	
	
	public static Integer getTotalBdscpdwsmain(String type) {
		String strStr = "select count(v.vehicle_reg_no) dataCount from vehicle v inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id where v.vehicle_status ='"+type+"'  and vd.is_valid = 'Y' and v.is_valid = 'y';";
	    Integer count = 0;
		try {
		
			Connection conn = getConnection();
			Statement stmt2=conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(strStr);	
			
			while(rs2.next()) {
				count = rs2.getInt("dataCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("count --- query "+strStr);
		return count;
		
	}
	
	public static String getTripSchedule(Temp_Response request) {
		StringBuilder sb = new StringBuilder();	
		sb.append(" select mts.schedule_no,mted.trip_seq, mted.trip_date, msm.trip_no,d.depot_code,d.depot_name,r.route_long_name,v.vehicle_reg_no,mdm.division_name,mted.is_extra,mted.status_id");
		sb.append(" from msrtc_trip_entry_details mted INNER JOIN msrtc_schedule_master msm ");
		sb.append(" on msm.schedule_id = mted.schedule_id INNER JOIN depot d  ");
		sb.append(" on d.depot_id = msm.depot_id INNER JOIN msrtc_division_master mdm on mdm.division_id = d.division_id INNER JOIN vehicle v "); 
		sb.append(" on v.vehicle_id = mted.vehicle_id INNER JOIN route r on r.route_id = msm.route_id ");
		sb.append(" INNER JOIN msrtc_trip_entry mte on mte.trip_entry_id = mted.trip_entry_id ");
		sb.append(" INNER JOIN msrtc_trip_schedule mts on mte.trip_id = mts.trip_schedule_id ");
				
			
		   if(request.getEndDate() != null && request.getEndDate().length() > 0) {
			   sb.append(" where mted.trip_date >= '"+request.getStartDate()+"' and mted.trip_date <='"+request.getEndDate()+"' ");
			}else {
				sb.append(" where mted.trip_date = '"+request.getStartDate()+"' ");
			}
		if(request.getSearch() != null && request.getSearch().equalsIgnoreCase("actual")) {
			sb.append(" and mted.is_extra=0 and (mted.status_id =2 or mted.status_id =3) ");
		}else {
		  
		}
		System.out.println("query -- >>> "+sb.toString());
		
		return sb.toString();
		
		
		
	}
	
	//--------------store valid vehivle---------//
	private void storeValidVehicleCount_DEpot(List<String> depots) {

     String stringIN = "";
		
		for(String ss : depots) {
			stringIN+="'"+ss+"',";
      }
		stringIN +="'test'";
		stringIN = "("+stringIN+") ";
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(v.vehicle_reg_no) valid_vehicle from vehicle v inner join vehicle_device vd  ");
		sb.append(" on vd.vehicle_id = v.vehicle_id INNER join vehicle_depot vdep ");
		sb.append(" on vdep.vehicle_id = v.vehicle_id inner join depot d  ");
		sb.append(" on d.depot_id = vdep.depot_id  where vd.is_valid = 'Y'  and d.depot_code in "+stringIN);
		
		
		 String totalVehicleCount =sb.toString();
		 logger.info("depowise vehicle count query -- >> "+totalVehicleCount);
		 
		 try {
			Connection conn = getConnection();
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(totalVehicleCount);

			while (rs2.next()) {
				ConstantData.validVehicle_depotwise = rs2.getInt("valid_vehicle");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("the total no. of valid mapped vehicle is ---- >> " + ConstantData.validVehicle + "  ");

	}
	
	
	private void   storeValidVehicleCount() {
		
		
		StringBuilder sb = new StringBuilder();
		/*sb.append("select count(*) valid_vehicle  from vehicle v INNER JOIN vehicle_device vd ");
		sb.append(" on vd.vehicle_id = v.vehicle_id INNER JOIN device d "); 
		sb.append(" on vd.device_id = d.device_id ");
		sb.append(" where vd.is_valid = 'y' and d.is_valid = 'y' and v.is_valid = 'y'; ");*/        // --- changed this as per Devendra's instructions on 10-04-2020 
		
		String totalVehicleCount = "select count(v.vehicle_reg_no) valid_vehicle from vehicle v inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id where vd.is_valid = 'Y'";
		try {
			Connection conn = getConnection();
			Statement stmt2=conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(totalVehicleCount);	
			
			while(rs2.next()) {
				ConstantData.validVehicle = rs2.getInt("valid_vehicle");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	   logger.info("the total no. of valid mapped vehicle is ---- >> "+ConstantData.validVehicle+"  ");	
		
	}
	
	public static Integer getVehicleCountOfDepot(List<String> depotList) {
		Integer totalCount = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(DISTINCT(v.vehicle_reg_no)) vehicleCount ,d.depot_code  from vehicle v ");
		sb.append(" INNER join vehicle_depot vd on v.vehicle_id = vd.vehicle_id ");
		sb.append(" INNER join depot d on d.depot_id = vd.depot_id  ");
		sb.append(" inner join vehicle_device vdev on vdev.vehicle_id = v.vehicle_id  ");
		sb.append(" inner join device devc on devc.device_id = vdev.device_id ");
		sb.append(" where vdev.is_valid = 'y' and v.is_valid = 'y' and devc.is_valid = 'y' GROUP by d.depot_code ");

		if (ConstantData.vehicleCountInDepot == null) {
			ConstantData.vehicleCountInDepot = new HashMap<String, Integer>();

			try {
				Connection conn = getConnection();
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sb.toString());

				while (rs2.next()) {
					
					// vehicle_reg_no
					String depotCode = rs2.getString("depot_code");
					Integer vehicleCount = rs2.getInt("vehicleCount");
					ConstantData.vehicleCountInDepot.put(depotCode, vehicleCount);
					

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			for (String ss : depotList) {
				System.out.println("depotList --- >>>> "+ss.toUpperCase());
				int count = ConstantData.vehicleCountInDepot.get(ss)==null ? 0 : ConstantData.vehicleCountInDepot.get(ss);
				totalCount += count;
			}
		}

		return totalCount;
	}
	
	public static String getQueryForDepotwise(Integer divsionId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select vcl.vehicle_status fixed_status , v.division_id,v.division_name,v.depot_name,d.depot_code,v.vehicle_reg_no,v.device_imei,v.event_date,v.vehicle_status,v.ignition_status,v.main_battery_status ");
		sb.append(" from report_vehicle_location v inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id inner join vehicle vcl on vcl.vehicle_id = v.vehicle_id");
		sb.append(" inner join depot d on d.depot_id = v.depot_id where vd.is_valid = 'Y' and v.division_id ="+divsionId+" order by v.event_date desc "); 
		return sb.toString();		
	}
	
	
	
	public static String getTrnfVehicle(Temp_Response tempObj) {
		StringBuilder sb = new StringBuilder();	
		Integer  pageNo = tempObj.getPageNo() == 0 ? 0 : tempObj.getPageNo() * 10   ;
		Integer  rowCount = tempObj.getRowCount();
		String whereClose = "";
		if(tempObj.getStartDate() != null && tempObj.getEndDate() != null) {
			logger.info("start date end date  --- >>> "+tempObj.getStartDate() +"  "+tempObj.getEndDate() );
			String endDt = tempObj.getEndDate()+" 23:59:59";
		   whereClose = "where tva.created_dt >= '"+tempObj.getStartDate()+"' and tva.created_dt <='"+endDt+"' ";
		}
		
		if(tempObj.getFromDepotCodes() != null && tempObj.getFromDepotCodes().size() > 0) {
			String stringIN = "";
			for(String ss : tempObj.getFromDepotCodes()) {
				stringIN+="'"+ss+"',";
	      }
			stringIN +="'test'";
			stringIN = "("+stringIN+")";
			
			whereClose += whereClose.length() > 1 ? " and tva.from_depot_code in "+stringIN : " where tva.from_depot_code in "+stringIN;
		}
		
		if(tempObj.getToDepotCodes() != null && tempObj.getToDepotCodes().size() > 0) {
			String stringIN = "";
			for(String ss : tempObj.getToDepotCodes()) {
				stringIN+="'"+ss+"',";
	      }
			stringIN +="'test'";
			stringIN = "("+stringIN+")";
			
			whereClose += whereClose.length() > 1 ? " and tva.to_depot_code in "+stringIN : " where tva.to_depot_code in "+stringIN;
		}
		
		logger.info("whereclose- >> "+whereClose);
		//where tva.created_dt >= '2020-04-16' and tva.created_dt <= '2020-04-16 23:59:59'
		sb.append(" select tva.created_dt,tva.vehicle_status,tva.vehicle_reg_no,dfrm.depot_name from_depot,d2.depot_name to_depot,mdmFrom.division_name from_division,mdmTo.division_name to_division ");
		sb.append(" from temp_vehicle_activity tva left join depot dfrm ");
		sb.append(" on dfrm.depot_code = tva.from_depot_code left join depot d2 ");
		sb.append(" on d2.depot_code = tva.to_depot_code left join msrtc_division_master mdmFrom  ");
		sb.append(" on mdmFrom.division_id = dfrm.division_id left join msrtc_division_master mdmTo ");
		sb.append(" on mdmTo.division_id = d2.division_id "+whereClose );
        sb.append(" order by tva.created_dt desc ");
	        if(tempObj.getIs_export() == null || tempObj.getIs_export() == 0) {
	        sb.append(" limit "+pageNo+","+rowCount);
	        }
		
        
        logger.info("query -->> "+sb.toString());
       ConstantData.transFerredVehicleCount =  countTrnfVehicle(whereClose);  // storing counts;
		return sb.toString();	
		
	}
	
	
	public static Integer countTrnfVehicle(String whereClouse) { 
		String countQuerty = " select count(*) vehicleCount from temp_vehicle_activity tva "+whereClouse;
		Integer countVehicle =  0;
		try {
			Connection conn = getConnection_uat();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(countQuerty);
			while(rs.next()) {
				countVehicle = rs.getInt("vehicleCount");
				return countVehicle;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return countVehicle;
	}
	
	public static Integer getDivisionIdFromDepot(String depotCode) {
		try {
						//depotCodeVsDivisionId
			
			if(ConstantData.depotCodeVsDivisionId.get(depotCode) != null && ConstantData.depotCodeVsDivisionId.get(depotCode) > 0) {
				return ConstantData.depotCodeVsDivisionId.get(depotCode);
			}
			Connection conn = getConnection();
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery("select division_id,depot_code from depot");
			Integer divisionId = 0;
			while (rs2.next()) {	
				 ConstantData.depotCodeVsDivisionId.put(rs2.getString("depot_code"), rs2.getInt("division_id"));
			}
			
			conn.close();
			stmt2.close();
			rs2.close();
			return ConstantData.depotCodeVsDivisionId.get(depotCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	//-------------mapp  unmapp--------------------//
	public static String mappDevices(Temp_Response tempObj) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select DISTINCT (ods.id), md.division_name,d1.depot_name,v.vehicle_reg_no, d.device_imei,d.device_imei_alias,d.manufacturer, ");
		sb.append(" ods.update_dt,om.user_login,(case when ods.status = '5' then 'Mapped' END )MappingStatus from ops_device_state ods ");
	    sb.append(" inner join ops_membership_user om on om.user_id = ods.updated_by ");
	    sb.append(" inner join vehicle_device vd on vd.device_id = ods.device_id ");
	    sb.append(" inner join vehicle v on v.vehicle_id = vd.vehicle_id ");
	    sb.append(" inner join device d on d.device_id = vd.device_id ");
	    sb.append(" inner join vehicle_depot vd1 on v.vehicle_id = vd1.vehicle_id ");
	    sb.append(" inner join depot d1 on vd1.depot_id = d1.depot_id ");
	    sb.append(" inner join msrtc_division_master md on d1.division_id = md.division_id ");
	    sb.append(" where status = '5' and vd.is_valid ='Y' ");// update_dt <= CURRENT_DATE  ");	
		
	    if(tempObj.getStartDate() != null && tempObj.getEndDate() != null) {
	    	String endDt = tempObj.getEndDate()+" 23:59:59";
	    	sb.append(" and update_dt >='"+tempObj.getStartDate()+"' and update_dt<='"+endDt+"'");
	    }else {
	    	sb.append(" and update_dt >= CURRENT_DATE  ");
	    }
	    
	    if(tempObj.getDepotCodes().size() > 0 && tempObj.getDepotCodes().size() < 100) {
	    	String stringIN = "";
	    	for(String ss : tempObj.getDepotCodes()) {
				stringIN+="'"+ss+"',";
	      }
			stringIN +="'test'";
			sb.append(" and d1.depot_code in ("+stringIN+")");
	    }
		System.out.println("complete query --- >>> "+sb.toString());
		
		return sb.toString();
	}
	
	public static String unmappDevices(Temp_Response tempObj) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select DISTINCT (ods.id) , md.division_name,d1.depot_name,v.vehicle_reg_no, d.device_imei,d.device_imei_alias,d.manufacturer, ");
		sb.append(" ods.update_dt,om.user_login,(case when ods.status = '6' then 'Unmapped' END )MappingStatus from ops_device_state ods ");
	    sb.append(" inner join ops_membership_user om on om.user_id = ods.updated_by ");
	    sb.append(" inner join vehicle_device vd on vd.device_id = ods.device_id ");
	    sb.append(" inner join vehicle v on v.vehicle_id = vd.vehicle_id ");
	    sb.append(" inner join device d on d.device_id = vd.device_id ");
	    sb.append(" inner join vehicle_depot vd1 on v.vehicle_id = vd1.vehicle_id ");
	    sb.append(" inner join depot d1 on vd1.depot_id = d1.depot_id ");
	    sb.append(" inner join msrtc_division_master md on d1.division_id = md.division_id ");
	    sb.append(" where status = '6' and vd.is_valid ='Y' ");// update_dt <= CURRENT_DATE  ");	
		
	    if(tempObj.getStartDate() != null && tempObj.getEndDate() != null) {
	    	String endDt = tempObj.getEndDate()+" 23:59:59";
	    	sb.append(" and update_dt >='"+tempObj.getStartDate()+"' and update_dt<='"+endDt+"'");
	    }else {
	    	sb.append(" and update_dt <= CURRENT_DATE  ");
	    }
	    
	    if(tempObj.getDepotCodes().size() > 0 && tempObj.getDepotCodes().size() < 100) {
	    	String stringIN = "";
	    	for(String ss : tempObj.getDepotCodes()) {
				stringIN+="'"+ss+"',";
	      }
			stringIN +="'test'";
			sb.append(" and d1.depot_code in ("+stringIN+")");
	    }
		System.out.println("complete query --- >>> "+sb.toString());
		
		return sb.toString();
	}
	
	
	
	
	
	public static Integer devicePing_on_a_day(String date,String status,String imei) {
		Integer count = 0;
		String query = "";
		if(status != null && status.length() > 0) {
			query = "select count(*) dataCount from msrtc_rawdata.msrtc_device_log where message_status='"+status+"' AND device_imei ='"+imei+"' and message_time like '"+date+"%'";
		}else {
			query = "select count(*) dataCount from msrtc_rawdata.msrtc_device_log where  device_imei ='"+imei+"' and message_time like '"+date+"%'";

		}
		
		logger.info("count query -->> "+query);
		Connection conn = getConnection_dbwise("msrtc_rawdata");
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
			  return rs.getInt("dataCount");	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	
	public static List<Map<String, Object>> devicePing_on_a_day_data(Temp_Response tempObj) {
		Integer count = 0;
		List<Map<String, Object>> mapList = new ArrayList<>();
		String query = "";
		int pageCount =  tempObj.getPageNo() * tempObj.getRowCount();
		if(tempObj.getSearch() != null && tempObj.getSearch().length() > 0) {
			query = " select device_imei ,message_status,message_time " + 
					" from msrtc_device_log mdl where message_time like '"+tempObj.getStartDate()+"%' AND message_status ='"+tempObj.getSearch()+"' AND  device_imei ='"+tempObj.getImei()+"'  order by log_id desc limit "+pageCount+","+tempObj.getRowCount();
		}else {
			query = " select device_imei ,message_status,message_time " + 
					" from msrtc_device_log mdl where   message_time like '"+tempObj.getStartDate()+"%'  AND device_imei ='"+tempObj.getImei()+"'  order by log_id desc limit "+pageCount+","+tempObj.getRowCount();

		}
		logger.info("query -->> "+query);
		
		Connection conn = getConnection_dbwise("msrtc_rawdata");
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
			  Map<String , Object> mapObj = new HashMap<>();
			  mapObj.put("imei", rs.getString("device_imei"));
			  mapObj.put("message", rs.getString("message_status").equalsIgnoreCase("L")?"Live":"History");
			  mapObj.put("time", rs.getString("message_time"));
			  mapList.add(mapObj);
 			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return mapList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//-------------------connections------------------------//
	
	
	public static Connection getConnection() {		 
		//String hostIp = "192.191.190.10:3306/";		  
		 
			try {
				if(ConstantData.is_server == 1 && con == null) {
					String prodDbIp = _gpty().getProperty("database.url.prod").trim();
				 con=DriverManager.getConnection("jdbc:mysql://"+prodDbIp+"/msrtc","msrtc_read_only","G@nG@P0in7");
					return con;
				}else if(con == null) {
				  con=DriverManager.getConnection("jdbc:mysql://103.197.121.84:3306/msrtc","msrtc_read_only","G@nG@P0in7");
				}
				return con;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			return null;
	}
	
	public static Connection getConnection_dbwise(String dbnm) {		 
		
		
			try {
				if(ConstantData.is_server == 1) {
					String prodDbIp = _gpty().getProperty("database.url.prod").trim();
					Connection con=DriverManager.getConnection("jdbc:mysql://"+prodDbIp+"/"+dbnm,"msrtc_read_only","G@nG@P0in7");
					return con;
				}
				Connection con=DriverManager.getConnection("jdbc:mysql://103.197.121.84:3306/"+dbnm,"msrtc_read_only","G@nG@P0in7");
				return con;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			return null;
	}
	
	public static Connection getConnection_uat() {		 
		  
		
			try {
				
				if(ConstantData.is_server == 1) {
					String uatDbIp = _gpty().getProperty("database.url.uat").trim();
					Connection con=DriverManager.getConnection("jdbc:mysql://"+uatDbIp+"/msrtc","msrtc","MsrTc@12345");
					return con;
				}
				Connection con=DriverManager.getConnection("jdbc:mysql://103.197.121.83:3306/msrtc","msrtc","MsrTc@12345");
				return con;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			return null;
	}
	
	
	 
    public static Connection getConnection_rawData() {		 
		//String hostIp = "192.191.190.10:3306/";		  
		 
			try {
				if(ConstantData.is_server == 1) {
					String prodDbIp = _gpty().getProperty("database.url.prod").trim();
				  Connection con=DriverManager.getConnection("jdbc:mysql://"+prodDbIp+"/msrtc_rawdata","msrtc_read_only","G@nG@P0in7");
				  return con;
				}else {
					Connection con=DriverManager.getConnection("jdbc:mysql://103.197.121.84:3306/msrtc_rawdata","msrtc_read_only","G@nG@P0in7");
					return con;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			return null;
	}
	
    
    private static  Properties _gpty() {
    	InputStream _inputApplicationFile;
		try {
			_inputApplicationFile = new FileInputStream("msrtc_graph_application.properties");
			Properties _prop = new Properties();
		    _prop.load(_inputApplicationFile);
		    return _prop;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
    }
	
    
public static void main(String[] args) {
	String data = "rvl.division_name,rvl.depot_name,rvl.route_code,rvl.device_imei,rvl.event_location,rvl.event_date,rvl.speed,rvl.ignition_status,rvl.gps_status,rvl.main_battery_status,rvl.vehicle_status,rvl.running_status,rvl.vehicle_reg_no";
   data = data.replace("rvl.", "");
 String str = "";
 List<String> strList = new ArrayList<>();
	for(String ss : data.split(",")) {
		strList.add(ss);
   }
	
	System.out.println("res-->"+strList.size()+"  "+strList.toString());
	

}

    
	
}
