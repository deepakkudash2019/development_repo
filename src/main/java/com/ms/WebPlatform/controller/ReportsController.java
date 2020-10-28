package com.ms.WebPlatform.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayInputStream;
import java.io.File;
import com.google.gson.Gson;
import com.ms.WebplatformApplication;
import com.ms.WebPlatform.model.Advertisement;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.model.Vehicle;
import com.ms.WebPlatform.services.Importer;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.QueryClass;
import com.ms.WebPlatform.utility.Sceduler_Class;
import com.ms.WebPlatform.utility.Utility;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/rosmerta-dev")
public class ReportsController {


	
	@Autowired
	private Importer imp;  
	
	
	 
	//------------------Advertisement---------//
	@RequestMapping(value = "/api/report/advertisement/", method = RequestMethod.POST)
	public String SaveAdvertisement(@RequestBody Advertisement adv) {
	  GlobalResponse resp = new GlobalResponse();
		try {
			//logger.info("welcome :::::  "+adv.toString());
			
			
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
		
		return new Gson().toJson(resp);
	}
	
	
	
	
	@RequestMapping(value = "/rosmerta-dev/api/report/urlTest/", method = RequestMethod.GET)
	public String UrlTEst() {
		Map<String, String> mapObj = new HashMap<String, String>();	
		mapObj.put("key1", "true");
		return new Gson().toJson(mapObj);
	}
	
	@RequestMapping(value = "/rosmerta-dev/api/report/urlTestPost/", method = RequestMethod.POST)
	public String UrlTEst(@RequestBody Object object) {
		System.out.println("UrlTEst postmethoid "+new Gson().toJson(object));
		Map<String, String> mapObj = new HashMap<String, String>();	
		mapObj.put("key1", "true");
		return new Gson().toJson(mapObj);
	}
 
	@RequestMapping(value = "/api/report/makeZip/{token}/", method = RequestMethod.GET)
	public String makeZip(@PathVariable("token") String tkn) {
		GlobalResponse resp = new GlobalResponse();
		try {
			String zipFIlenm = "json_activity_"+new Date().getTime()+".zip";
			Utility.pack("json_activity", zipFIlenm);
			resp.setCode(1);
			resp.setMessage("packed to zip successfully");
			resp.setData(zipFIlenm);
			
		} catch (Exception e) {
			resp.setCode(-1);
			resp.setMessage(e.getMessage());
		}
		
		return new Gson().toJson(resp);
	}
	
	
	@RequestMapping(value = "/api/report/getImeilist/{page}/{rawCount}/{token}/", method = RequestMethod.GET)
	public GlobalResponse getimeilist(@PathVariable("page") Integer page,@PathVariable("rawCount")Integer rawCount,@PathVariable("token") String tkn) {
		System.out.println("");
		try {
			return imp.getUptimeReport().getImeiLIst(page,rawCount);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalResponse resp = new GlobalResponse();
			resp.setCode(-1);
			resp.setMessage(e.getMessage());
			resp.setStatus("fail");
			return resp;
		}		
	}
	
	
	@RequestMapping(value = "/api/report/getUptimeReport/", method = RequestMethod.POST)
	public GlobalResponse getUptimeReport(@RequestBody Temp_Response request) {
		System.out.println("");
		try {
			if(request.getDepotCodes() == null || request.getDepotCodes().size() == 0) {
				ArrayList<String> depotCodes = new ArrayList<>();
				for(Depot depot : ConstantData.AlldepotList) {
					depotCodes.add(depot.getDepotCode());
				}
				request.setDepotCodes(depotCodes);
			}
			return imp.getUptimeReport().getUptimeReport(request);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalResponse resp = new GlobalResponse();
			resp.setCode(-1);
			resp.setMessage(e.getMessage());
			resp.setStatus("fail");
			return resp;
		}		
	}
	
	
	@RequestMapping(value = "/api/report/getCancelTrip/", method = RequestMethod.POST)
	public GlobalResponse getCancelTrip(@RequestBody Temp_Response request){	
		System.out.println("");
		return imp.getUptimeReport().getCancelTrip(request);
	}
	
	
	@RequestMapping(value = "/api/report/getExtraTrip/", method = RequestMethod.POST)
	public GlobalResponse getExtraTrip(@RequestBody Temp_Response request){	
		System.out.println("");
		return imp.getUptimeReport().getExtraTrip(request);
	}
	
	@RequestMapping(value = "/api/report/getBreakDown/", method = RequestMethod.POST)
	public GlobalResponse getBreakDown(@RequestBody Temp_Response request){	
		System.out.println("");
		return imp.getUptimeReport().getBreakDownReport(request);
	}
	@RequestMapping(value = "/api/report/getTripSchedule/", method = RequestMethod.POST)
	public GlobalResponse getTripSchedule(@RequestBody Temp_Response request){	
		System.out.println("");
		return imp.getUptimeReport().getTripSchedule(request);
	}
	
	//--transfered vehicle----------
	
	@RequestMapping(value = "/api/report/saveVehicle/{token}/", method = RequestMethod.POST)
	public GlobalResponse saveVehicleActivity(@PathVariable("token") String token ,@RequestBody Vehicle rvlObj) {	
		System.out.println("vehicle activity -- >>> "+token);
		return imp.getService().saveVehicleActivity(rvlObj);
	}
	
	@RequestMapping(value = "/api/report/getVehicle/{token}/", method = RequestMethod.POST)
	public GlobalResponse getVehicleActivity(@PathVariable("token") String token ,@RequestBody Temp_Response tempObj) {	
		System.out.println("vehicle activity -- >>> "+token);
		return imp.getService().getVehicleActivity(tempObj);
	}

	
	@RequestMapping(value = "/api/report/mappunmapp/{token}/", method = RequestMethod.POST)
	public GlobalResponse getmappUnmapped(@PathVariable("token") String token ,@RequestBody Temp_Response tempObj) {	
		System.out.println("vehicle activity -- >>> "+token);
		return imp.getService().getmappUnmapped(tempObj);
	}
	
	@GetMapping(value = "/test")
	public String getImage() throws IOException {
		imp.getUptimeReport().StoreData_depot();
		return "success";
		
	}
	
	@RequestMapping(value = "/api/report/getDevicePing/{token}/", method = RequestMethod.POST)
	public GlobalResponse getDevicePing(@PathVariable("token") String token ,@RequestBody Temp_Response tempObj) {	
		System.out.println("vehicle activity -- >>> "+token);
		return imp.getService().getDevicePing(tempObj);
	}
	
	
	@RequestMapping(value = "/api/report/cleanData/{token}/", method = RequestMethod.GET)
	public String CleanDATA(@PathVariable("token") String token ) {	
		System.out.println("vehicle activity -- >>> "+token);
		
		String pathStr = "E:/Deepak/file_storage_test/json_activity/";
		try {
			
			 String extension = "txt";
	            String startWith = "test";
	            LocalDate beforeDate = LocalDate.parse("2018-06-05");

	            String path = "E:/Deepak/file_storage_test/json_activity/";
	            List<Path> paths = Files.list(Paths.get(path)).collect(Collectors.toList());
	            List<File> fileList = new ArrayList<File>();

	            for (Path entry : paths) {
	                System.out.println("Entry: " + entry);
	                if(entry.toString().contains("2020-04-11")) {
	                	System.out.println("found -- >>> "+entry);
	                	File fff = entry.toFile();
	                	fileList.add(fff);
	                }
	                BasicFileAttributes attributes = Files.readAttributes(entry, BasicFileAttributes.class);
	                System.out.println("\tCreation Time/UTC Time: " + attributes.creationTime());
	                Instant instant = Instant.parse(attributes.lastModifiedTime().toString());
	                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
	                System.out.println("\tLocal Date: " + ldt.toLocalDate());
	                System.out.println("\tBefore Date: " + beforeDate);
	                if (entry.getFileName().toString().startsWith(startWith) && entry.getFileName().toString().endsWith(extension) && ldt.toLocalDate().isBefore(beforeDate)) {
	                    System.out.println("\tMarked for deletion: Yes");                      
	                }
	                else {
	                    System.out.println("\tMarked for deletion: No");
	                }

	                System.out.println("\n");
	            }
			
	            
	            for(File ff : fileList) {
	            	if(ff.isFile()) {
	            		System.out.println("yes  ----");
	            		ff.deleteOnExit();
	            		
	            	}
	            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "Success";
	}
	
	
	 @GetMapping(value = "/download_excel/{fileNm}/")
	    public ResponseEntity<InputStreamResource> excelCustomersReport(@PathVariable("fileNm")String fileNm) throws IOException {
		 File file =  null;   
	    if(ConstantData.is_server == 1) {
	    	String filePath = "msrtc_storage/"+fileNm;
	    	file = new File(filePath);
	    }else {
		 file = new File("E:/exported_file/"+fileNm);
	    }
		 ByteArrayInputStream bis = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
	    // return IOUtils.toByteArray(in);
	    
	    HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "attachment; filename="+fileNm);
	    
	     return ResponseEntity
	                  .ok()
	                  .headers(headers)
	                  .body(new InputStreamResource(bis));
	    }
	
	
	
	@RequestMapping(value = "/download_pdf/{fileNm}/", method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport(@PathVariable("fileNm")String fileNm) throws IOException {

		 File file =  null;   
		    if(ConstantData.is_server == 1) {
		    	String filePath = "msrtc_storage/"+fileNm;
		    	file = new File(filePath);
		    }else {
		    	file = new File("E:/exported_file/"+fileNm);
		    }

        ByteArrayInputStream bis = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));

       
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename="+fileNm);


        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
	
	
	@RequestMapping(value = "/api/report/test2/", method = RequestMethod.GET)
	public String test2(){		
		
		try {
			System.out.println("welcome --- >>> ");
			java.net.URI location = new java.net.URI("https://www.google.com");
			
			return location.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	@RequestMapping(value = "/api/report/startBkup/", method = RequestMethod.GET)
	public String startBkup(){
		System.out.println("welcome ----- >>> ");		
		new Thread()
		{
		    public void run() {
		        System.out.println("thread call ");
		        Sceduler_Class.db_backup();
		    }
		}.start();
		
		
		return "method called successfully ";
	}
	
	//   getTotalBdscpdwsmain("MAINTENANCE");
	
	@RequestMapping(value = "/api/report/test/{type}/", method = RequestMethod.GET)
	public Object testCount(@PathVariable("type") String type){		
		return QueryClass.getTotalBdscpdwsmain(type);
	}
	
	@RequestMapping(value = "/api/report/test/", method = RequestMethod.GET)
	public String testVehicle() {
		imp.getService().csvenerator();
		
		return "success";
	}
	
	
		
	@RequestMapping(value = "/api/report/getCrewMappingExcel/{dt}/", method = RequestMethod.GET)
	public  ResponseEntity<InputStreamResource>  getExcelOFcrewSchedulermapping(@PathVariable("dt")String dtStr) {
		
		InputStream is =  imp.getQurInterface().getTotalScheduleOfDepots(dtStr);
		 
		 HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "attachment; filename=schedule_Crew_mapping.xlsx");
	    
	     return ResponseEntity
	                  .ok()
	                  .headers(headers)
	                  .body(new InputStreamResource(is));
		
		
		
	}
	
	
	
	
	
}
