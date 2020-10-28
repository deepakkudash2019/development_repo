package com.ms.WebPlatform.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ms.WebPlaatform.exceldata.DepotExcel;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MsrtcDivisionMaster;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.QueryClass;

@Service
public class QueryImpl implements QueryInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(QueryImpl.class);

	@Autowired
	private Importer imp;
	
	@Override
	public InputStream getTotalScheduleOfDepots(String dt){
		
	
		return processing(dt);
		
		
	}
	
	
	
	private InputStream processing(String dt) {
		List<MsrtcDivisionMaster> allDiv = divisionFill();
		List<Map<String, Object>> result = GlobalSlectFromtbl(getCrewDeviceQuery(dt));
		
		
		Map<Integer, Integer> depotCount=new HashMap<Integer, Integer>();
		for(Map<String, Object> mapObj : result) {
			
			Integer key = Integer.parseInt(mapObj.get("depot_id").toString());
			Integer value = Integer.parseInt(mapObj.get("cnt").toString());
			
			depotCount.put(key, value);
		}
		System.out.println("processing -- "+depotCount.toString());
		for(MsrtcDivisionMaster divObj : allDiv) {
			for(DepotExcel obj : divObj.getDepotExcel()) {				
				List<Map<String, Object>> vehicleCount = GlobalSlectFromtbl(getTotalVehicle(obj.getDepotId(), dt));				
				Integer totalVehicle = Integer.parseInt(vehicleCount.get(0).get("totalvehicle").toString());
				
				obj.setTotalvehicleAssignment(totalVehicle);
				obj.setTotalsch(depotCount.get(obj.getDepotId())==null?0:depotCount.get(obj.getDepotId()));
			}
		}
		System.out.println("division size -- "+allDiv.size());
		
		
		//--making Excel----//
		try {
			return makeExcel(allDiv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void migrate(List<Depot> depots,List<DepotExcel> depotExcel) {
		for(Depot dep : depots) {
			DepotExcel depoExcel = new DepotExcel();
			depoExcel.setDepotId(dep.getDepot_id());
			depoExcel.setDepotName(dep.getDepot_name());
			depotExcel.add(depoExcel);
		}
	}
	
	private List<MsrtcDivisionMaster> divisionFill() {
		if (ConstantData.divListStaticMap == null ) {
			List<MsrtcDivisionMaster> divList = Lists.newArrayList(imp.getDivisionRepo().findAll());
			ConstantData.divListStaticMap = new HashMap<Integer, MsrtcDivisionMaster>();
			for (MsrtcDivisionMaster obj : divList) {
				List<Depot> depotList = imp.getDepoRepo().findBydivisionId(obj.getDivisionId());
				List<DepotExcel>depoExcelLIst = new ArrayList<DepotExcel>();
				migrate(depotList, depoExcelLIst);
				obj.setDepots(depotList);
				obj.setDepotExcel(depoExcelLIst);
				ConstantData.divListStaticMap.put(obj.getDivisionId(), obj);
				
			}
			
			System.out.println(" DIVISION SET SUCCESS :::::::::: ");
		}else {
			
		}
		List<MsrtcDivisionMaster> divisionList = new ArrayList<>(ConstantData.divListStaticMap.values());
		System.out.println("division Fill -- >> ::  "+divisionList.size());
		return divisionList;
	}
	
    
public static String getCrewDeviceQuery(String dt) {
	StringBuffer sb = new StringBuffer();
	sb.append("select  mts.depot_id,count(*) cnt ");
	sb.append("from msrtc_trip_duty mtd ");
	sb.append("inner JOIN  msrtc_duty_master mdm on mtd.duty_id = mdm.duty_id "); 
	sb.append("inner join msrtc_duty_master_details mdmd on mdm.duty_id = mdmd.duty_id ");
	sb.append("inner join msrtc_schedule_master msm on mdmd.trip_id = msm.schedule_id ");
	sb.append("inner join depot d on msm.depot_id = d.depot_id ");
	sb.append("inner join msrtc_division_master mdiv on d.division_id = mdiv.division_id ");
	sb.append("inner join msrtc_trip_schedule mts on mtd.trip_schedule_id = mts.trip_schedule_id ");
	sb.append("inner join msrtc_trip_entry mte on mte.trip_id = mts.trip_schedule_id ");
	sb.append("where mts.is_valid=1 and mte.trip_date = '"+dt+"' group by mts.depot_id ");
	
	
	return sb.toString();
	
	
}

public static String getTotalVehicle(Integer depotid,String dt) {
	StringBuffer sb = new StringBuffer();
	sb.append("select count(distinct(mte.vehicle_id)) totalvehicle");
	sb.append(" from msrtc_trip_duty mtd inner JOIN  msrtc_duty_master mdm on mtd.duty_id = mdm.duty_id ");
	sb.append("inner join msrtc_duty_master_details mdmd on mdm.duty_id = mdmd.duty_id ");
	sb.append("inner join msrtc_schedule_master msm on mdmd.trip_id = msm.schedule_id ");
	sb.append("inner join depot d on msm.depot_id = d.depot_id ");
	sb.append("inner join msrtc_division_master mdiv on d.division_id = mdiv.division_id ");
	sb.append("inner join msrtc_trip_schedule mts on mtd.trip_schedule_id = mts.trip_schedule_id ");
	sb.append("inner join msrtc_trip_entry mte on mte.trip_id = mts.trip_schedule_id ");
	sb.append("where mts.is_valid=1 and mte.trip_date ='"+dt+ "' and  mts.depot_id ="+depotid);
	
	return sb.toString();
}
	



	 private static InputStream makeExcel(List<MsrtcDivisionMaster> divisionList) throws Exception 
	   {
		  
		   
		   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
       Workbook workbook = WorkbookFactory.create(inputStream);
       Sheet sheet = workbook.getSheetAt(0);
	     
       
	       Map<String, Object[]> data = new LinkedHashMap<String, Object[]>(); //new TreeMap<String, Object[]>();
	       data.put("1", new Object[] {"Division","Depot","totalSch","schTimeFromTo","vehicleAssign","totalCrew","CrewAssignment"});

	       Integer count = 2;
		for (MsrtcDivisionMaster division : divisionList) {
			for(DepotExcel depoExl : division.getDepotExcel()) {
			 		
			 data.put(count.toString(), new Object[] {division.getDivisionName(),depoExl.getDepotName(),depoExl.getTotalsch(),
					 depoExl.getTimeSch(),depoExl.getTotalvehicleAssignment(),depoExl.getTotalCrew(),depoExl.getCrewAssignment()});
			 count ++;
			}
			
			data.put(count.toString() , new Object[] {" "," "," "," "," "," "," "});
			count++;
		}
	       //Iterate over data and write to sheet
	       Set<String> keyset = data.keySet();
	       int rownum = 7;
	       int slnOcount = 1;
	       System.out.println("key -- ::: "+keyset.toString());
	       for (String key : keyset)
	       {
	           Row row = sheet.createRow(rownum++);
	           Object [] objArr = data.get(key);
	           int cellnum = 0;
	           for (Object obj : objArr)
	           {
	              Cell cell = row.createCell(cellnum++);
	              if(obj instanceof String)
	                   cell.setCellValue((String)obj);
	               else if(obj instanceof Integer)
	                   cell.setCellValue((Integer)obj);
	           }
	       }
	       try
	       {
	          
	           
	           ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				byte[] barray = bos.toByteArray();
				InputStream is = new ByteArrayInputStream(barray);
				System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
				return is;
	           
	          
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	       }
	       
	       return null;
	   }
	

	
	
	//---------------------------DB-InterFace------//
	@SuppressWarnings("resource")
	public static List<Map<String, Object>> GlobalSlectFromtbl(String query) {
		Connection connection = QueryClass.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	    Map<String, Object> row = null;
		
		try {
			
			statement  = connection.createStatement();
			
			resultSet  = statement.executeQuery(query);	
			 ResultSetMetaData metaData = resultSet.getMetaData();
			 Integer columnCount = metaData.getColumnCount();
			 
			while(resultSet.next()) {
				row = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
		            row.put(metaData.getColumnName(i), resultSet.getObject(i));
		        }
				
				
				resultList.add(row);
				
			}
			
			return resultList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	
	
}
