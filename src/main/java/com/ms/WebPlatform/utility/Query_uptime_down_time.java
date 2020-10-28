package com.ms.WebPlatform.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ms.WebPlatform.repository.DivisionRepository;

public class Query_uptime_down_time {

	
	public static Integer actualDown() {
		//  Down - DWS/Maintance/BD - Off road/Transferred/Scraped - Parked
		
		Integer actualDown = totlVehicleDown() - (OFFRoadTRANSFERREDSCRAPPED() + BreakdownAtDWSMaintancevehicle() + parckedCount());
		
		return actualDown;
	}
	
	public static Integer OFFRoadTRANSFERREDSCRAPPED() {
		StringBuffer sb = new StringBuffer();	
		
		sb.append("select Sum(D.Total) count_data from ( ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status = 'OFF ROAD' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id  inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='TRANSFERRED' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='SCRAPPED' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE) D");
		
		ResultSet rs = queryExecuter(sb.toString());
		return getCountOnly(rs, "count_data");
		
	}
	
	public static Integer BreakdownAtDWSMaintancevehicle() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select Sum(D.Total) count_data from (");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status = 'AT DWS' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE  UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='MAINTENANCE' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='BREAK DOWN' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE) D");
		
		ResultSet rs = queryExecuter(sb.toString());
		return getCountOnly(rs, "count_data");
		
		
	}
	
	public static Integer totlVehicleDown() {
		Integer totalVehicle = totalVehicle();
	    Integer totalUp  = totlVehicleUp();
	    Integer totalDown = (totalVehicle - totalUp) ;
	    return totalDown;
	}
	
	public static Integer totlVehicleUp() {
		String queryStr = " select count(*) count_data from report_vehicle_location where event_date >= CURRENT_DATE ";
		ResultSet rs = queryExecuter(queryStr);
		return getCountOnly(rs, "count_data");
		
	}
	
	public static Integer totalVehicle() {
		String queryStr = "select count(vehicle_reg_no) count_data from report_vehicle_location v inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id where vd.is_valid = 'Y'";
	
		ResultSet rs = queryExecuter(queryStr);
		return getCountOnly(rs, "count_data");
	}
	
	public static Integer parckedCount() {		
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(*) count_data from msrtc.report_vehicle_location rv ");
		sb.append(" inner join vehicle v on v.vehicle_id = rv.vehicle_id ");		
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" where rv.ignition_status = '0' and rv.main_battery_status ='0' and v.vehicle_status = 'ON ROAD' ");
		sb.append(" and v.is_valid = 'Y' and vd.is_valid ='Y' ");
		sb.append(" and event_date <CURRENT_DATE ");	
			
		ResultSet rs = queryExecuter(sb.toString());
		return getCountOnly(rs, "count_data");
	
	}
	
	
	private static ResultSet queryExecuter(String query) {		
		
		try {
			Connection conn = QueryClass.getConnection();
			Statement stmt2=conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(query);	
			
			return rs2;
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static Integer getCountOnly(ResultSet rs,String lebelStr) {
		
		//ResultSet rs = queryExecuter(sb.toString());
		try {
			while(rs.next()) {
				return rs.getInt(lebelStr);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	    return 0;
		
	}
	
	
	//---------------depotWise---------------//
	

	
	public static Integer actualDown2(Integer divId) {
		//  Down - DWS/Maintance/BD - Off road/Transferred/Scraped - Parked
		
		Integer actualDown = totlVehicleDown2() - (OFFRoadTRANSFERREDSCRAPPED2() + BreakdownAtDWSMaintancevehicle2() + parckedCount2());
		
		return actualDown;
	}
	
	public static Integer OFFRoadTRANSFERREDSCRAPPED2() {
		StringBuffer sb = new StringBuffer();	
		
		sb.append("select Sum(D.Total) count_data from ( ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status = 'OFF ROAD' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE and rl.division_id="+ConstantData.temp_division_id+" UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id  inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='TRANSFERRED' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE and rl.division_id="+ConstantData.temp_division_id+" UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='SCRAPPED' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE and rl.division_id="+ConstantData.temp_division_id+") D");
		
		ResultSet rs = queryExecuter2(sb.toString());
		return getCountOnly2(rs, "count_data");
		
	}
	
	public static Integer BreakdownAtDWSMaintancevehicle2() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select Sum(D.Total) count_data from (");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status = 'AT DWS' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE and rl.division_id="+ConstantData.temp_division_id+" UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='MAINTENANCE' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE and rl.division_id="+ConstantData.temp_division_id+" UNION ALL ");
		sb.append(" select count(Distinct(v.vehicle_reg_no)) 'Total' from vehicle v ");
		sb.append(" inner join report_vehicle_location rl on rl.vehicle_id = v.vehicle_id ");
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" where v.vehicle_status ='BREAK DOWN' and vd.is_valid = 'Y' and rl.event_date <= CURRENT_DATE and rl.division_id="+ConstantData.temp_division_id+")  D");
		
		ResultSet rs = queryExecuter2(sb.toString());
		return getCountOnly2(rs, "count_data");
		
		
	}
	
	public static Integer totlVehicleDown2() {
		Integer totalVehicle = totalVehicle2();
	    Integer totalUp  = totlVehicleUp2();
	    Integer totalDown = (totalVehicle - totalUp) ;
	    return totalDown;
	}
	
	public static Integer totlVehicleUp2() {
		String queryStr = " select count(*) count_data from report_vehicle_location where event_date >= CURRENT_DATE  and division_id="+ConstantData.temp_division_id;
		ResultSet rs = queryExecuter2(queryStr);
		return getCountOnly2(rs, "count_data");
		
	}
	
	public static Integer totalVehicle2() {
		String queryStr = "select count(vehicle_reg_no) count_data from report_vehicle_location v inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id where vd.is_valid = 'Y'  and v.division_id="+ConstantData.temp_division_id;
	
		ResultSet rs = queryExecuter2(queryStr);
		return getCountOnly2(rs, "count_data");
	}
	
	public static Integer parckedCount2() {		
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(*) count_data from msrtc.report_vehicle_location rv ");
		sb.append(" inner join vehicle v on v.vehicle_id = rv.vehicle_id ");		
		sb.append(" inner join vehicle_device vd on vd.vehicle_id = v.vehicle_id ");
		sb.append(" where rv.ignition_status = '0' and rv.main_battery_status ='0' and v.vehicle_status = 'ON ROAD' ");
		sb.append(" and v.is_valid = 'Y' and vd.is_valid ='Y' ");
		sb.append(" and event_date <CURRENT_DATE  and rv.division_id="+ConstantData.temp_division_id);	
			
		ResultSet rs = queryExecuter2(sb.toString());
		return getCountOnly2(rs, "count_data");
	
	}
	
	
	private static ResultSet queryExecuter2(String query) {		
		
		try {
			Connection conn = QueryClass.getConnection();
			Statement stmt2=conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(query);	
			
			return rs2;
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static Integer getCountOnly2(ResultSet rs,String lebelStr) {
		
		//ResultSet rs = queryExecuter(sb.toString());
		try {
			while(rs.next()) {
				return rs.getInt(lebelStr);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	    return 0;
		
	}
	
}
