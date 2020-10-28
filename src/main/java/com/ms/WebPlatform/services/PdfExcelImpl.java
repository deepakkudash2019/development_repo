package com.ms.WebPlatform.services;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.ms.WebPlatform.utility.ConstantData;

@Service
public class PdfExcelImpl implements PdfExcelInterface {

	@Override
	public String dailyReportExport(List<Map<String, Object>> mapList) {
		
		try {
			String urls = "";
			String excel = MakeExcel.makeExcel(mapList);
			String pdf =  letExportToPdf(mapList);
			
	
			String fileUrl = excel +","+pdf;
		return fileUrl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}
	
	@Override
	public String dailyReportCanceltripExport(List<Map<String, Object>> mapList) {
		//http://13.233.153.29/images/logo/msrtcLogo.png
		Map<String, String> mapStr = new HashMap<String, String>();
		try {
			String urls = "";
			String excel = MakeExcel.makeExcel_canceltrip(mapList);
			String pdf =  letExportToPdf_cancelTrip(mapList);
				mapStr.put("excel", excel); 
				mapStr.put("pdf", pdf);
		
				String fileUrl = excel +","+pdf;
			return fileUrl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}
	
	
	@Override
	public String dailyReportBreakDown(List<Map<String, Object>> mapListNew) {
		//http://13.233.153.29/images/logo/msrtcLogo.png
		Map<String, String> mapStr = new HashMap<String, String>();
				try {
					
					String urls = "";
					String excel = MakeExcel.breakDown(mapListNew);
					String pdf =  letExportToPdf_breakDown(mapListNew);
					String fileUrl = excel +","+pdf;
			
				return fileUrl;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "error";
	}
	

		@Override
		public String getExtraTripReport(List<Map<String, Object>> mapListNew) {
			
			try {
				
				String urls = "";
				String excel = MakeExcel.extraTrip(mapListNew);
				String pdf =  letExportToPdf_extratrip(mapListNew);
				String fileUrl = excel +","+pdf;
		
			return fileUrl;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}
		    
		@Override
		public String getAllTripReport(List<Map<String, Object>> mapListNew) {
			
			try {
				
				String urls = "";
				String excel = MakeExcel.alltrip(mapListNew);
				String pdf =  letExportToPdf_alltrip(mapListNew);
				String fileUrl = excel +","+pdf;
		
			return fileUrl;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}


		

		@Override
		public String uptimeDowntime(List<Map<String, Object>> mapListNew,String fileNm) {
			try {
				
			System.out.println("report type  --- >>> "+fileNm);
				String excel = MakeExcel.uptimeDowntimeReport(mapListNew,fileNm);
				String pdf =  letExportToPdf_uptimeDowntime(mapListNew,fileNm);
				String fileUrl = excel +","+pdf;
		
			return fileUrl;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}
		
		@Override
		public String tranferVehicleReport(List<Map<String, Object>> mapList, String type) {
			try {
				
				System.out.println("report type  --- >>> "+type);
					String excel = MakeExcel.transferVehicle(mapList,type);
					String pdf =  transferVehicle(mapList,type);
					String fileUrl = excel +","+pdf;
			
				return fileUrl;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "error";
		}

		
	@Override
	public String mappunmapp(List<Map<String, Object>> mapList,String type) {
		try {

			System.out.println("report type  --- >>> " + type);
			String excel = MakeExcel.mappunmapp_excel(mapList, type);
			String pdf = mappunmapp_pdf(mapList, type);
			String fileUrl = excel + "," + pdf;

			return fileUrl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
		
	}

	private String mappunmapp_pdf(List<Map<String, Object>> mapList, String type)throws Exception {
		// Creating a PdfWriter
		ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
		String dest = ConstantData.fileStoragePath + "/"+type+".pdf";
		PdfWriter writer = new PdfWriter(dest);
		PdfDocument pdfDoc = new PdfDocument(writer);
		pdfDoc.addNewPage();

		float[] pointColumnWidths = { 1,2, 2, 2, 2, 2,2 };
		Table table = new Table(pointColumnWidths);

		String[] titleArr = { "slno", "Division","Depot", "Vehicle", "Imei", "status" ,"date"};
		addContentTotable(table, titleArr,1);
		Integer count = 0;

		
		
		for (Map<String, Object> mapObj : mapList) {
			count++;
			String[] bodyArr = { count.toString(), mapObj.get("division").toString(),mapObj.get("depot").toString(),
					mapObj.get("vehicle").toString(), mapObj.get("deviceImei").toString(),mapObj.get("status").toString(),
					mapObj.get("date").toString()};
			addContentTotable(table, bodyArr,0);
		}

		Image img = new Image(data);
		img.setWidth(100);
		Document document = new Document(pdfDoc);
		document.add(img);
		Paragraph pg = new Paragraph("\n\n");
		document.add(pg);
		document.add(table);
		document.close();
		System.out.println("PDF Created");

		// http://209.190.15.106:8080/msrtc_storage/
		return type + ".pdf";
	}

	private String transferVehicle(List<Map<String, Object>> mapList, String type)throws Exception {
		// Creating a PdfWriter
		ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
		String dest = ConstantData.fileStoragePath + "/"+type+".pdf";
		PdfWriter writer = new PdfWriter(dest);
		PdfDocument pdfDoc = new PdfDocument(writer);
		pdfDoc.addNewPage();

		float[] pointColumnWidths = { 1,2, 2, 2, 2, 2 };
		Table table = new Table(pointColumnWidths);

		String[] titleArr = { "slno", "Vehicle","Status", "From", "To", "Date" };
		addContentTotable(table, titleArr,1);
		Integer count = 0;

		
		
		for (Map<String, Object> mapObj : mapList) {
			count++;
			String[] bodyArr = { count.toString(), mapObj.get("vehicle").toString(),mapObj.get("status").toString(),
					mapObj.get("from").toString(), mapObj.get("to").toString(),
					mapObj.get("date").toString()};
			addContentTotable(table, bodyArr,0);
		}

		Image img = new Image(data);
		img.setWidth(100);
		Document document = new Document(pdfDoc);
		document.add(img);
		Paragraph pg = new Paragraph("\n\n");
		document.add(pg);
		document.add(table);
		document.close();
		System.out.println("PDF Created");

		// http://209.190.15.106:8080/msrtc_storage/
		return type + ".pdf";
		}

	private String letExportToPdf_uptimeDowntime(List<Map<String, Object>> mapList, String fileNm) throws Exception {
		// Creating a PdfWriter
		ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
		String dest = ConstantData.fileStoragePath + "/"+fileNm+".pdf";
		PdfWriter writer = new PdfWriter(dest);
		PdfDocument pdfDoc = new PdfDocument(writer);
		pdfDoc.addNewPage();

		float[] pointColumnWidths = { 1,1, 1, 1, 2, 2 };
		Table table = new Table(pointColumnWidths);

		String[] titleArr = { "slno", "Division","Depot", "Vehicle No", "DeviceImei", "LastUpdtedTime" };
		addContentTotable(table, titleArr,1);
		Integer count = 0;

		
		
		for (Map<String, Object> mapObj : mapList) {
			count++;
			String[] bodyArr = { count.toString(), mapObj.get("division").toString(),mapObj.get("depot").toString(),
					mapObj.get("regNo").toString(), mapObj.get("deviceImei").toString(),
					mapObj.get("eventDt").toString()};
			addContentTotable(table, bodyArr,0);
		}

		Image img = new Image(data);
		img.setWidth(100);
		Document document = new Document(pdfDoc);
		document.add(img);
		Paragraph pg = new Paragraph("\n\n");
		document.add(pg);
		document.add(table);
		document.close();
		System.out.println("PDF Created");

		// http://209.190.15.106:8080/msrtc_storage/
		return fileNm + ".pdf";
	}

public static String letExportToPdf(List<Map<String, Object>> mapList) throws Exception {              
      // Creating a PdfWriter 
	   ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
      String dest = ConstantData.fileStoragePath+"/dailyReport.pdf";       
      PdfWriter writer = new PdfWriter(dest);             
      PdfDocument pdfDoc = new PdfDocument(writer);    
      pdfDoc.addNewPage();               
     
      float [] pointColumnWidths = {1,3,3,3,3}; 
      Table table = new Table(pointColumnWidths);
      
      String [] titleArr = {"slno","Depot","Vehicle No","Trip Time","Status"};
      addContentTotable(table, titleArr,1);      
      Integer count=0;
      
      for(Map<String, Object> mapObj : mapList) {
    	  count++;    	 
    	  String []bodyArr = {count.toString(),mapObj.get("depot").toString(),mapObj.get("vehicle").toString(),mapObj.get("time").toString(),mapObj.get("status").toString()};
          addContentTotable(table, bodyArr,0);    	 
      }
      
      
      Image img = new Image(data);
      img.setWidth(100);      
           Document document = new Document(pdfDoc);            
           document.add(img);
           Paragraph pg = new Paragraph("\n\n");
           document.add(pg);
      document.add(table); 
      document.close();              
      System.out.println("PDF Created");    
      
      //http://209.190.15.106:8080/msrtc_storage/
      return "dailyReport.pdf";
   } 

   
  //--for cancelTrip
   public static String letExportToPdf_cancelTrip(List<Map<String, Object>> mapList) throws Exception {              
	      // Creating a PdfWriter 
		   ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
	      String dest = ConstantData.fileStoragePath+"/cancel_trip.pdf";       
	      PdfWriter writer = new PdfWriter(dest);             
	      PdfDocument pdfDoc = new PdfDocument(writer);    
	      pdfDoc.addNewPage();               
	     
	      float [] pointColumnWidths = {1,3,3,3,3,3,3}; 
	      Table table = new Table(pointColumnWidths);
	      
	      String [] titleArr = {"slno","Division","Depot","Vehicle No","TripNo","Schedule","route"};
	      addContentTotable(table, titleArr,1);      
	      Integer count=0;
	      
	      for(Map<String, Object> mapObj : mapList) {
	    	  count++;  
	    	  
	    	  String []bodyArr = {count.toString(),mapObj.get("divisionName").toString(),mapObj.get("depotName").toString(),mapObj.get("vehicleNo").toString(),mapObj.get("tripNo").toString(),mapObj.get("schedule").toString(),mapObj.get("routeName").toString()};
	          addContentTotable(table, bodyArr,0);    	 
	      }
	      
	      
	      Image img = new Image(data);
	      img.setWidth(100);      
	           Document document = new Document(pdfDoc);            
	           document.add(img);
	           Paragraph pg = new Paragraph("\n\n");
	           document.add(pg);
	      document.add(table); 
	      document.close();              
	      System.out.println("PDF Created");    
	      
	      return "cancel_trip.pdf";
	   } 
   
   
   private String letExportToPdf_breakDown(List<Map<String, Object>> mapListNew)throws Exception {
       
    // Creating a PdfWriter 
	   ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
    String dest = ConstantData.fileStoragePath+"/breakdown.pdf";       
    PdfWriter writer = new PdfWriter(dest);             
    PdfDocument pdfDoc = new PdfDocument(writer);    
    pdfDoc.addNewPage();               
   
    float [] pointColumnWidths = {2,5,5,5,5}; 
    Table table = new Table(pointColumnWidths);
    
    String [] titleArr = {"slno","Division","Depot","Vehicle No","Status"};
    addContentTotable(table, titleArr,1);      
    Integer count=0;
    
   
    for(Map<String, Object> mapObj : mapListNew) {
  	  count++;  
  	  
  	  String []bodyArr = {count.toString(),mapObj.get("devision").toString(),mapObj.get("depot").toString(),mapObj.get("vehicle").toString(),mapObj.get("status").toString()};
        addContentTotable(table, bodyArr,0);    	 
    }
    
    
    Image img = new Image(data);
    img.setWidth(100);      
         Document document = new Document(pdfDoc);            
         document.add(img);
         Paragraph pg = new Paragraph("\n\n");
         document.add(pg);
    document.add(table); 
    document.close();              
    System.out.println("PDF Created");    
    
    return "breakdown.pdf";
	}
   
   //---extratrip---
   private String letExportToPdf_extratrip(List<Map<String, Object>> mapList) throws Exception {              
	      // Creating a PdfWriter 
		   ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
	      String dest = ConstantData.fileStoragePath+"/extratrip.pdf";       
	      PdfWriter writer = new PdfWriter(dest);             
	      PdfDocument pdfDoc = new PdfDocument(writer);    
	      pdfDoc.addNewPage();               
	     
	      float [] pointColumnWidths = {1,3,3,3,3,3,3}; 
	      Table table = new Table(pointColumnWidths);
	      
	      String [] titleArr = {"slno","Division","Depot","Vehicle No","TripNo","Schedule","route"};
	      addContentTotable(table, titleArr,1);      
	      Integer count=0;
	      
	      for(Map<String, Object> mapObj : mapList) {
	    	  count++;  
	    	  
	    	  String []bodyArr = {count.toString(),mapObj.get("divisionName").toString(),mapObj.get("depotName").toString(),mapObj.get("vehicleNo").toString(),mapObj.get("tripNo").toString(),mapObj.get("schedule").toString(),mapObj.get("routeName").toString()};
	          addContentTotable(table, bodyArr,0);    	 
	      }
	      
	      
	      Image img = new Image(data);
	      img.setWidth(100);      
	           Document document = new Document(pdfDoc);            
	           document.add(img);
	           Paragraph pg = new Paragraph("\n\n");
	           document.add(pg);
	      document.add(table); 
	      document.close();              
	      System.out.println("PDF Created");    
	      
	      return "extratrip.pdf";
	}
   
		
		private String letExportToPdf_alltrip(List<Map<String, Object>> mapList)throws Exception {              
		    // Creating a PdfWriter 
			   ImageData data = ImageDataFactory.create("http://13.233.153.29/images/logo/msrtcLogo.png");
		    String dest = ConstantData.fileStoragePath+"/alltrip.pdf";       
		    PdfWriter writer = new PdfWriter(dest);             
		    PdfDocument pdfDoc = new PdfDocument(writer);    
		    pdfDoc.addNewPage();               
		   
		    float [] pointColumnWidths = {1,3,3,3,3,3,3}; 
		    Table table = new Table(pointColumnWidths);
		    
		    String [] titleArr = {"slno","Division","Depot","Vehicle No","TripNo","Schedule","route"};
		    addContentTotable(table, titleArr,1);      
		    Integer count=0;
		    
		    for(Map<String, Object> mapObj : mapList) {
		  	  count++;  
		  	  
		  	  String []bodyArr = {count.toString(),mapObj.get("divisionName").toString(),mapObj.get("depotName").toString(),mapObj.get("vehicleNo").toString(),mapObj.get("tripNo").toString(),mapObj.get("schedule").toString(),mapObj.get("routeName").toString()};
		        addContentTotable(table, bodyArr,0);    	 
		    }
		    
		    
		    Image img = new Image(data);
		    img.setWidth(100);      
		         Document document = new Document(pdfDoc);            
		         document.add(img);
		         Paragraph pg = new Paragraph("\n\n");
		         document.add(pg);
		    document.add(table); 
		    document.close();              
		    System.out.println("PDF Created");    
		    
		    return "alltrip.pdf";
	 }
   
   
   private static void addContentTotable(Table table,String[] tableCells, int i) {
	   Cell cell1; 	
	   
	   for(String ss : tableCells) {
		   cell1 = new Cell();
		   cell1.add(ss);
		   if(i==0)cell1.setFontSize(8f);
		   else cell1.setFontSize(10f);
		   table.addCell(cell1);
	   }
	   
	 
   }










}
