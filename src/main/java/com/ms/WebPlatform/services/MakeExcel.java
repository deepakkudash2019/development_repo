package com.ms.WebPlatform.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gson.Gson;
import com.ms.WebPlatform.utility.ConstantData;

public class MakeExcel {

	
	
	 
	   //--Make excel file -----//
	   public static String makeExcel(List<Map<String, Object>> mapList) {
		   try {
			letExportToExcel(mapList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   //http://209.190.15.106:8080/msrtc_storage/
		   return "dailyReport.xlsx";
		   
	   }
	   
	   
	   
	   
	   
	   public static String makeExcel_canceltrip(List<Map<String, Object>> mapList) {
			try {
				letExportToExcel_cancelTrip(mapList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "cancel_trip.xlsx";
		}
	   
	   
	   public static String breakDown(List<Map<String, Object>> mapList) {
		   try {
			letExportToExcel_breakDown(mapList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return "breakdown.xlsx";
		   
	   }
	   
	  
	   public static String extraTrip(List<Map<String, Object>> mapListNew) {
		   try {
			   getextraTrip(mapListNew);
			} catch (Exception e) {
				e.printStackTrace();
			}
			   return "extratrip.xlsx";
		}


	   public static String alltrip(List<Map<String, Object>> mapListNew) {
		   try {
			   getAlltrip(mapListNew);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "alltrip.xlsx";
		}


	   public static String uptimeDowntimeReport(List<Map<String, Object>> mapListNew, String fileNm) {
		   try {
			   upDownReport(mapListNew,fileNm);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileNm+".xlsx";
		}

	


		public static String transferVehicle(List<Map<String, Object>> mapList, String type) {
			 try {
				 transferVehicle_excel(mapList,type);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return type+".xlsx";
		}

		
		public static String mappunmapp_excel(List<Map<String, Object>> mapList, String type) {
			try {
				mappunmapp_excel_file(mapList,type);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return type+".xlsx";
		}


	private static void mappunmapp_excel_file(List<Map<String, Object>> mapList, String type)throws Exception 
	   {
		  
		   
		   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
	      
	       Map<String, Object[]> data = new TreeMap<String, Object[]>();
	       data.put("1", new Object[] { "Division","Depot", "Vehicle", "Imei", "status" ,"date"});

	       Integer count = 1;
		for (Map<String, Object> mapObj : mapList) {
			count ++;			
			data.put(count.toString(), new Object[] {mapObj.get("division").toString(),mapObj.get("depot").toString(),
					mapObj.get("vehicle").toString(), mapObj.get("deviceImei").toString(),mapObj.get("status").toString(),
					mapObj.get("date").toString() });
			
		}
	       //Iterate over data and write to sheet
	       Set<String> keyset = data.keySet();
	       int rownum = 7;
	       int slnOcount = 1;
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
	           //Write the workbook in file system
	           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/"+type+".xlsx"));
	           workbook.write(out);
	           out.close();
	           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	       }	
		}





	private static void transferVehicle_excel(List<Map<String, Object>> mapList, String type)throws Exception 
	   {
		  
		   
		   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
	      
	       Map<String, Object[]> data = new TreeMap<String, Object[]>();
	       data.put("1", new Object[] {"Vehicle","Status", "From", "To", "Date"});

	       Integer count = 1;
		for (Map<String, Object> mapObj : mapList) {
			count ++;			
			data.put(count.toString(), new Object[] {mapObj.get("vehicle"),mapObj.get("status"),mapObj.get("from"), mapObj.get("to"),mapObj.get("date") });
			
		}
	       //Iterate over data and write to sheet
	       Set<String> keyset = data.keySet();
	       int rownum = 7;
	       int slnOcount = 1;
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
	           //Write the workbook in file system
	           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/"+type+".xlsx"));
	           workbook.write(out);
	           out.close();
	           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	       }
	       
	   }





	private static void letExportToExcel(List<Map<String, Object>> mapList) throws Exception 
	   {
		  
		   
		   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
           Workbook workbook = WorkbookFactory.create(inputStream);
           Sheet sheet = workbook.getSheetAt(0);
	      
	       Map<String, Object[]> data = new TreeMap<String, Object[]>();
	       data.put("1", new Object[] {"Depot","Vehicle No","Trip Time","Status"});

	       Integer count = 1;
		for (Map<String, Object> mapObj : mapList) {
			count ++;			
			data.put(count.toString(), new Object[] {mapObj.get("depot"),mapObj.get("vehicle"),mapObj.get("time"), mapObj.get("status") });
			
		}
	       //Iterate over data and write to sheet
	       Set<String> keyset = data.keySet();
	       int rownum = 7;
	       int slnOcount = 1;
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
	           //Write the workbook in file system
	           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/dailyReport.xlsx"));
	           workbook.write(out);
	           out.close();
	           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	       }
	   }


	private static void upDownReport(List<Map<String, Object>> mapList, String fileNm) throws Exception {

		FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath + "/sample_file.xlsx"));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"Division","Depot", "Vehicle No", "DeviceImei", "LastUpdtedTime"});

		Integer count = 1;
		for (Map<String, Object> mapObj : mapList) {
			count++;
			data.put(count.toString(), new Object[] { mapObj.get("division"), mapObj.get("depot"),
					mapObj.get("regNo"), mapObj.get("deviceImei"), mapObj.get("eventDt") });

		}
		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 7;
		int slnOcount = 1;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath + "/"+fileNm+".xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	   
	 private static void letExportToExcel_cancelTrip(List<Map<String, Object>> mapList) throws Exception 
	   {
		  
		   
		   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
         Workbook workbook = WorkbookFactory.create(inputStream);
         Sheet sheet = workbook.getSheetAt(0);
	     
         
	       Map<String, Object[]> data = new TreeMap<String, Object[]>();
	       data.put("1", new Object[] {"Division","Depot","Vehicle","TripNo","Route"});

	       Integer count = 1;
		for (Map<String, Object> mapObj : mapList) {
			count ++;			
			data.put(count.toString(), new Object[] {mapObj.get("division"),mapObj.get("depotName"),mapObj.get("vehicleNo"),mapObj.get("tripNo"), mapObj.get("routeName") });
			
		}
	       //Iterate over data and write to sheet
	       Set<String> keyset = data.keySet();
	       int rownum = 7;
	       int slnOcount = 1;
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
	           //Write the workbook in file system
	           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/cancel_trip.xlsx"));
	           workbook.write(out);
	           out.close();
	           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	       }
	   }
	
	 

		private static void getextraTrip(List<Map<String, Object>> mapListNew)throws Exception 
		   {
			  
			   
			   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
	         Workbook workbook = WorkbookFactory.create(inputStream);
	         Sheet sheet = workbook.getSheetAt(0);
		    
	         
		       Map<String, Object[]> data = new TreeMap<String, Object[]>();
		       data.put("1", new Object[] {"Division","Depot","Vehicle","TripNo","Route"});

		       Integer count = 1;
			for (Map<String, Object> mapObj : mapListNew) {
				count ++;	
				System.out.println("extra Trip -- >>  "+new Gson().toJson(mapObj));
				data.put(count.toString(), new Object[] {mapObj.get("divisionName"),mapObj.get("depotName"),mapObj.get("vehicleNo"),mapObj.get("tripNo"), mapObj.get("routeName") });
				
			}
		       //Iterate over data and write to sheet
		       Set<String> keyset = data.keySet();
		       int rownum = 7;
		       int slnOcount = 1;
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
		           //Write the workbook in file system
		           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/extratrip.xlsx"));
		           workbook.write(out);
		           out.close();
		           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		       } 
		       catch (Exception e) 
		       {
		           e.printStackTrace();
		       }
		       
		   }

	 
	
	 private static void letExportToExcel_breakDown(List<Map<String, Object>> mapList) throws Exception {
		 
		   
		   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
       Workbook workbook = WorkbookFactory.create(inputStream);
       Sheet sheet = workbook.getSheetAt(0);
       
       
	       Map<String, Object[]> data = new TreeMap<String, Object[]>();
	       data.put("1", new Object[] {"Division","Depot","Vehicle","Status"});

	       Integer count = 1;
		for (Map<String, Object>  mapObj : mapList) {
			count ++;			
			data.put(count.toString(),new Object[] {mapObj.get("division"),mapObj.get("depot"),mapObj.get("vehicle"),mapObj.get("status") });
			
		}
	
	       //Iterate over data and write to sheet
	       Set<String> keyset = data.keySet();
	       int rownum = 7;
	       int slnOcount = 1;
	       for (String key : keyset)
	       {
	           Row row = sheet.createRow(rownum++);
	           Object [] objArr = data.get(key);
	           int cellnum = 0;
	          
	           for (Object obj : objArr)
	           {
	        	   System.out.println("data size2 -- > "+obj);
	              Cell cell = row.createCell(cellnum++);
	              if(obj instanceof String)
	                   cell.setCellValue((String)obj);
	               else if(obj instanceof Integer)
	                   cell.setCellValue((Integer)obj);
	           }
	       }
	       try
	       {
	           //Write the workbook in file system
	           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/breakdown.xlsx"));
	           workbook.write(out);
	           out.close();
	           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	       }
			
		}





		private static void getAlltrip(List<Map<String, Object>> mapListNew)throws Exception 
		   {
			  
			   
			   FileInputStream inputStream = new FileInputStream(new File(ConstantData.fileStoragePath+"/sample_file.xlsx"));
	         Workbook workbook = WorkbookFactory.create(inputStream);
	         Sheet sheet = workbook.getSheetAt(0);
		    
	         
		       Map<String, Object[]> data = new TreeMap<String, Object[]>();
		       data.put("1", new Object[] {"Division","Depot","Vehicle","TripNo","Route"});

		       Integer count = 1;
			for (Map<String, Object> mapObj : mapListNew) {
				count ++;	
				System.out.println("extra Trip -- >>  "+new Gson().toJson(mapObj));
				data.put(count.toString(), new Object[] {mapObj.get("divisionName"),mapObj.get("depotName"),mapObj.get("vehicleNo"),mapObj.get("tripNo"), mapObj.get("routeName") });
				
			}
		       //Iterate over data and write to sheet
		       Set<String> keyset = data.keySet();
		       int rownum = 7;
		       int slnOcount = 1;
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
		           //Write the workbook in file system
		           FileOutputStream out = new FileOutputStream(new File(ConstantData.fileStoragePath+"/alltrip.xlsx"));
		           workbook.write(out);
		           out.close();
		           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		       } 
		       catch (Exception e) 
		       {
		           e.printStackTrace();
		       }
		}





		









		




	





	

	 
	 
	 
}
