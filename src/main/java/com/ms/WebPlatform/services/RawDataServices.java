package com.ms.WebPlatform.services;


import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ms.WebPlatform.model.MsrtcDeviceLog;
import com.ms.WebPlatform.model.ReportVehicleLocation;
import com.ms.WebPlatform.model.Stop;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.utility.CallApis;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.QueryClass;
import com.opencsv.CSVReader;

@Service
public class RawDataServices implements RawDataInterface {
	private static final Logger logger = LoggerFactory.getLogger(RawDataServices.class);
	
	@Autowired
	private EntityManager em;
	@Autowired
	private Importer imp;
	
	
	
	private Connection conn = null;
	/*
	 * @Autowired private DivisionRepository divisionRepo;
	 * 
	 * @Autowired private DepotRepository depoRepo;
	 * 
	 * @Autowired private GraphInterface graph;
	 * 
	 * @Autowired private ServiceInterface service;
	 * 
	 */
	
	@Override
	public GlobalResponse rawdataCsv() {
		GlobalResponse resp = new GlobalResponse();
		List<Map<String, Object>> list =  getRawDataFromCsv("123456789012345", "2020-04-20");
		if(list != null) {
			resp.setCode(1);
			resp.setCount(list.size());
			resp.setData(list);
			resp.setStatus("true");
			resp.setMessage("success");
		}else {
			resp.setCode(0);
			resp.setCount(0);
			//resp.setData(list);
			resp.setStatus("false");
			resp.setMessage("data not available");
		}
		
		return resp;
	}
	
	private static  List<Map<String, Object>>   getRawDataFromCsv(String deviceImei,String date) {
		String fileNm1 = deviceImei+"/device-data_"+date+"-04-00-00.csv";
		String fileNm2 = deviceImei+"/device-data_"+date+"-08-00-00.csv";
		String fileNm3 = deviceImei+"/device-data_"+date+"-12-00-00.csv";
		String fileNm4 = deviceImei+"/device-data_"+date+"-16-00-00.csv";
		String fileNm5 = deviceImei+"/device-data_"+date+"-20-00-00.csv";
		String fileNm6 = deviceImei+"/device-data_"+date+"-24-00-00.csv";
		
		//String csvFile = System.getProperty("user.dir")+"\\route_not_exist2.csv";
		
		String csvFile = ConstantData.raw_data_root_path+"/"+fileNm1;
		try {
            System.out.println(csvFile);
			
			FileReader filereader = new FileReader(csvFile);
			CSVReader csvReader = new CSVReader(filereader);
			String dataLstTemp[] ;
			int count = 0;
			
	
			String notIn=new String();
			List<Map<String, Object>> mapLIst = new ArrayList<>();
			
			while ((dataLstTemp = csvReader.readNext()) != null) {				
				if(count == 0) {
					System.out.println("length-- >> "+dataLstTemp[0]+"  "+dataLstTemp[1]+" "+dataLstTemp[2]);
				}else {
					Map<String, Object> mapObj = new HashMap<>();
					mapObj.put("receivedTime",dataLstTemp[0]);
					mapObj.put("pingTime",dataLstTemp[1]);
					mapObj.put("rawData",dataLstTemp[2]);
					mapLIst.add(mapObj);
				}
				count++;
				
			}
			System.out.println(count);
			return mapLIst;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public Object getLastPing(MsrtcDeviceLog deviceObj) {
		logger.info("welcome >>>> services.getLastPing");
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			
		
			Map<String, String> mapObj = null;
			List<Map<String, String>> mapList = new ArrayList<Map<String,String>>();
			/*Statement stmt=conn.createStatement();
			//imeiNo = "867322030864474";
			if(checkDeviceImei(deviceObj,stmt)) {
				
				return null;
			}*/
			
		connection = QueryClass.getConnection_rawData();
	    statement =connection.createStatement();
			
			String selectString = "SELECT message_time,date_add(received_time,interval 330 minute) as received_time,device_imei,message_status,log_id,message";
			String queryStr = selectString+" FROM msrtc_rawdata.msrtc_device_log where device_imei='"+deviceObj.getDeviceImei()+"'  order by log_id desc LIMIT "+deviceObj.getCount();
			resultSet=statement.executeQuery(queryStr);  
			 System.out.println("this is query --->>> "+queryStr);
			 while(resultSet.next())  {
				 System.out.println("h... k......--- >> ");
				 mapObj = new HashMap<String, String>();
					 mapObj.put("messageTime",resultSet.getString("message_time"));
					 mapObj.put("receivedTime",resultSet.getString("received_time"));
					 mapObj.put("imei",resultSet.getString("device_imei"));
					 mapObj.put("msgStatus",resultSet.getString("message_status"));
					 mapObj.put("logId",resultSet.getString("log_id"));  
					 mapObj.put("rawData",resultSet.getString("message"));
				 mapList.add(mapObj);
			   
			 }

			logger.info("result data ---- >>> "+mapList.size());
			 return mapList;
		} catch (Exception e) {
		   e.printStackTrace();	
		}finally {
			 try {
				connection.close();
				statement.close();
				resultSet.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			try {
				if(connection.isClosed()) {
					logger.info("connection closed in final block executeed ");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		return null;
	}
	
	
	private static boolean checkDeviceImei(MsrtcDeviceLog deviceObj, Statement stmt) throws SQLException {
		String queryStr = "select is_valid,device_id from device where device_imei ='"+deviceObj.getDeviceImei()+"'";
		System.out.println("this is the query --->> "+queryStr);
		 ResultSet rs=stmt.executeQuery(queryStr);  
		 while(rs.next()) {
			 if(rs.getInt("device_id") > 0  && rs.getString("is_valid") != null && rs.getString("is_valid").equalsIgnoreCase("y")) {
				 return false;
			 }
			 
		 }
		 return true;
	}
	
	
	@Override
	public GlobalResponse dailyReport(Temp_Response temp_resObj) {
		ConstantData.totalDataCount = 0;
		Map<String, Object> dataDetails = null;
		List<Map<String, Object>> mapList = new ArrayList<>();
		GlobalResponse data = new GlobalResponse();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		
		try {
			if(conn == null || conn.isClosed()) {
		     	conn = getConnection();
		    }
			if(temp_resObj.getIs_export() == 1) {
				temp_resObj.setPageNo(0);
				temp_resObj.setRowCount(0);
			}
			String[] queryArr = QueryClass.query_dailyReport(temp_resObj.getDepotCodes(), temp_resObj.getPageNo(), temp_resObj.getRowCount());//QueryClass.getDailyReportQuery(temp_resObj.getStartDate(), temp_resObj.getPageNo(), temp_resObj.getRowCount(), temp_resObj.getDepotCodes(),temp_resObj.getVehicleNos());
			//String query2 = QueryClass.query_dailyReport(temp_resObj.getDepotCodes(), temp_resObj.getPageNo(), temp_resObj.getRowCount());
			
			logger.info("dailyReport query -- >> "+queryArr[0]+"  \n"+queryArr[1]+"\n\n");
			new QueryClass().countData(queryArr[1],conn);
			Statement stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery(queryArr[0]);
	
			while(rs.next()) {
				dataDetails = new HashMap<String, Object>();
				//dataDetails.put("lat", rs.getString("lat"));
				//dataDetails.put("lon", rs.getString("lon"));
				dataDetails.put("depot", rs.getString("depot_name"));
				dataDetails.put("vehicle",rs.getString("vehicle_reg_no"));
				 
				String timeStr = rs.getString("event_date");
				timeStr = timeStr.substring(0, timeStr.length() - 2);
				dataDetails.put("time",timeStr);
				dataDetails.put("status", rs.getString("vehicle_status"));
			
				mapList.add(dataDetails);
			}
			System.out.println("");
			
			if(ConstantData.totalDataCount == 0) {ConstantData.totalDataCount = 6050;}
			
			Integer totalVehicle = temp_resObj.getDepotCodes().size() > 0 && temp_resObj.getDepotCodes().size() < 50 ? ConstantData.validVehicle_depotwise : ConstantData.validVehicle ;
			
			counts = QueryClass.countStatus_Vehicle(conn,temp_resObj.getDepotCodes());			
			counts.put("totalUnpinged",  totalVehicle - ConstantData.totalDataCount);
			counts.put("totalCount", totalVehicle);
			counts.put("totalDataCount", ConstantData.totalDataCount);
			
			//System.out.println("size -- >> "+countCount+mapList.size()+"  "+ConstantData.totalDataCount+"  vehicle -- >>> "+ConstantData.validVehicle);
			
			if(mapList == null || mapList.size() == 0) {
				data.setCode(0);
				data.setData(mapList);
				data.setMessage("data not available");
				System.out.println("device not found ---->>> ");
			}else {
			     
				data.setCode(1);
				data.setData(temp_resObj.getIs_export() == 1 ? imp.getPdfExcel().dailyReportExport(mapList) : mapList);
				data.setCountData(counts);
			}
			
			
			rs.close();
			stmt.close();
			conn.close();
			System.out.println("END --- >>> Resultant  ---- >>>>>> "+mapList.size());
			} catch (Exception e) {
			e.printStackTrace();
			data.setCode(-1);
			data.setMessage(e.getMessage());
			
		}		
		
		
		
		return data;
	}
	
	//------------------------------------query-------------------------------//
	
	private static String getQueryUsingLatitudeLongitude(String lat,String lan) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT (select v.vehicle_reg_no from vehicle v where v.vehicle_id = a.vehicle_id ) regNo,a.event_lat,a.event_lon,");
		sb.append("(111.111 *DEGREES(ACOS(LEAST(1.0, COS(RADIANS("+lat+")) ");
		sb.append("  * COS(RADIANS(a.event_lat)) ");
		sb.append("  * COS(RADIANS("+lan+" - a.event_lon))");
		sb.append("  + SIN(RADIANS("+lat+"))* SIN(RADIANS(a.event_lat))))))  km ");
		sb.append("   FROM report_vehicle_location  a order by km ");
		sb.append("   LIMIT 100");
		
		System.out.println("query print--->>> "+sb.toString());
		
		return sb.toString();
	}
	
	
	private static String getQueryForStopInRange(String lat,String lan) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT a.stop_code,a.stop_name,a.stop_desc,a.stop_lat,a.stop_lon,");
		sb.append(" ");
		sb.append("(111.111 *DEGREES(ACOS(LEAST(1.0, COS(RADIANS("+lat+"))");
		sb.append(" ");
		sb.append("* COS(RADIANS(a.stop_lat))   * COS(RADIANS("+lan+" - a.stop_lon))");
		sb.append(" ");
		sb.append("+ SIN(RADIANS("+lat+"))* SIN(RADIANS(a.stop_lat))))))  km  FROM stop  a");
		sb.append(" ");
		sb.append(" where a.stop_lat > 0 order by km limit 300");
		
		
		System.out.println("result is --->>> "+sb.toString());
		return sb.toString();
				
	}
	
	
	//-------------------------End query------------------------------//
	
	@Override
	public Object getVehicleInRange(String lat, String lan, Integer km) {
		ReportVehicleLocation rvlObj = null;
		Stop stpObj = null;
		List<ReportVehicleLocation> rvlObjList = new ArrayList<ReportVehicleLocation>();
		List<Stop> StopList = new ArrayList<Stop>();
		Map<String, Object> vehicle_stopData = new HashMap<String, Object>();
		try {
			if (conn == null || conn.isClosed()) {
				conn = getConnection();
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getQueryUsingLatitudeLongitude(lat, lan));

			while (rs.next()) {
				rvlObj = new ReportVehicleLocation();
				if (rs.getDouble("km") > km) {
					break;
				}
				if (rs.getDouble("km") > 0) {
					rvlObj.setKilometer(rs.getDouble("km"));
					rvlObj.setVehicleRegNo(rs.getString("regNo"));
					rvlObj.setEventLat(rs.getString("event_lat"));
					rvlObj.setEventLon(rs.getString("event_lon"));
					rvlObjList.add(rvlObj);
				}
				
			}
			
			//-----------------fetching Stops--------------//
			
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(getQueryForStopInRange(lat, lan));
			while(rs2.next()) {
				stpObj = new Stop();
				if(rs2.getDouble("km") > km) {break;}
				stpObj.setStopCode(rs2.getString("stop_code"));
				stpObj.setStopName(rs2.getString("stop_name"));
				stpObj.setStopLat(rs2.getString("stop_lat"));
				stpObj.setStopLon(rs2.getString("stop_lon"));
				stpObj.setKilometer(rs2.getDouble("km"));
				StopList.add(stpObj);
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}

		vehicle_stopData.put("vehicle_data", rvlObjList);
		vehicle_stopData.put("stops_data",StopList);
		return vehicle_stopData;

	}
	
	@Override
	public Object getRoutData(Integer stopId) {
		String data = "{\"stopId\":"+ stopId+"}";
		try {
			System.out.println("welcome services --- >>");
			String result  = new CallApis().callApiWithHeader(data);
			
			System.out.println("result --- >>>> "+result);
			ObjectMapper mapper = new ObjectMapper();
			 Map<String, Object> map2 = new HashMap<String, Object>();		
				map2 = mapper.readValue(result, Map.class);
			
			System.out.println("mapData --- >>>> "+map2.get("stop"));
			System.out.println("mapDataObj ---- >>> "+map2.get("data"));
			
			List<Map<String, Object>> mapList = (List<Map<String, Object>>) map2.get("data");
			System.out.println("res3222 0---- >>> "+mapList);
			
			for(Map<String, Object> mapObj : mapList) {
				
				boolean bool = mapObj.get("routeCode") == null ? false : true;
				String routeName = bool ? getRouteNme(bool ? mapObj.get("routeCode").toString() : null) : null;
				mapObj.put("routeName", routeName);
				
			}
			
			map2.put("data", mapList);
			return map2;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	private  String getRouteNme(String routeCode) {
		
		try {
			String query = "select route_long_name routeName from route where route_short_name = '"+routeCode+"'";
			if(conn == null || conn.isClosed()) {
		     	conn = getConnection();
		    }
			Statement stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				
				return rs.getString("routeName");
			}
			
			
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return null;
		
	}
	
	
	//-----------Production_Db connection-----//	
	
	@Override
	public Connection getConnection() {		 
			  
		return QueryClass.getConnection();
		/*	try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			try {
				JpaConfig jpaObj = new JpaConfig();
				Connection con=DriverManager.getConnection("jdbc:mysql://103.197.121.84:3306/msrtc","msrtc_read_only","G@nG@P0in7");
				return con;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			return null;*/
	}
	
	
	
	

	@Override
	public List<Map<String, Object>> secureQuery(String query){
		
		
		
        EntityManager session = imp.getEm();//entityManagerFactory.createEntityManager();
        try {
            Object resLIst = session.createNativeQuery(query);//session.createNativeQuery(query).getHints();

            System.out.println("result --- >>> "+new Gson().toJson(resLIst));
            return null;
        }
        catch (NoResultException e){
            return null;
        }
        finally {
            if(session.isOpen()) session.close();
        }
	
	}
	
	
	
	
}
