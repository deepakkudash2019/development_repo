package com.ms.WebPlatform.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MembershipGroup;
import com.ms.WebPlatform.model.MsrtcDeviceLog;
import com.ms.WebPlatform.model.MsrtcDivisionMaster;
import com.ms.WebPlatform.model.ReportVehicleLocation;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.services.Importer;
import com.ms.WebPlatform.services.RawDataServices;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.DataContainer;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.Utility;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/rosmerta-dev")
public class DataController {
	@Autowired
	private Importer imp;

	List<Map<String, Object>> mapList = null;
	private static final Logger logger = LoggerFactory.getLogger(RawDataServices.class);
	
	//-------------------MobileRequirement--------------------//
	
	public static void main(String[] args) {
		try {
			//Utility.excelGenerate(null,imp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	  
	@RequestMapping(value = "/api/data/getUptimeReport_test/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getUptimeReport2(@RequestParam("datevalue")String datevalue) {
		 
		 System.out.println("welcome --- >> "+datevalue);
		  Temp_Response request = new Temp_Response();		  
		  request.setDepotCodes(ConstantData.depotCodesList);
		  request.setRowCount(20000);
		  request.setIs_export(0);
		  request.setPageNo(0);
		  request.setSearch("");
		  request.setStartDate(datevalue);
		  
		  
		  GlobalResponse res =   imp.getUptimeReport().getUptimeReport(request);
		  if(res.getMessage() != null) {
			  System.out.println("data exist");
		  }else {
			  
			  return null;
		  }	 
		  
		try {
			InputStream  is = Utility.excelGenerate(res,request,imp);
			
			
			 HttpHeaders headers = new HttpHeaders();
		        headers.add("Content-Disposition", "attachment; filename="+datevalue+"uptimeExce.xlsx");
		    
		     return ResponseEntity
		                  .ok()
		                  .headers(headers)
		                  .body(new InputStreamResource(is));
			
			
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return null;
	  }
	  
	  //----------End-----------------------//
	
	@RequestMapping(value = "/api/data/readCsv", method = RequestMethod.GET)
	 public GlobalResponse readCsvFile(@RequestParam("path")String path) {
			
		GlobalResponse resp = new GlobalResponse();
		
			
			logger.info("complete file path --- >>> ");
			try {
				logger.info("csvFile-- >>> "+path);
				
				FileReader filereader = new FileReader(path.trim());
				CSVReader csvReader = new CSVReader(filereader);
				String dataLstTemp[] ;
				String titleArr[] = new String[13];
				int count = 0;			
				Map<String, String> mapObj = null ;
				List<Map<String, String>> mapList = new ArrayList<>();
				while ((dataLstTemp = csvReader.readNext()) != null) {
					
					if(count == 0) {
						System.out.println("-- >> "+dataLstTemp.length);
						for(int i =0;i<dataLstTemp.length;i++) {
							titleArr[i] = dataLstTemp[i];
						}
						
					}	
					
					
					if(count > 0) {
						mapObj = new HashMap<>();
						for(int i =0;i<dataLstTemp.length;i++) {							
							mapObj.put(titleArr[i], dataLstTemp[i]);							
						}	
						mapList.add(mapObj);
						
					}
					count++;
					
					
				}
				logger.info("data count -- >>> "+count);
				boolean bool = mapList.size() > 0 ;
				resp.setCode(bool ? 1 : 0);
				resp.setStatus(bool ? "true":"false");
				resp.setMessage(bool ? "success":"data not available");
				resp.setData(mapList);
				
			} catch (Exception e) {
				logger.info("catch block -- >> "+e.getMessage());
				e.printStackTrace();
				resp.setCode(-1);
				resp.setStatus("fail");
				resp.setMessage(e.getMessage());
			}
			
			return resp;
		}
	 
	
	
	
	@RequestMapping(value = "/api/data/reportVehicleLoc/", method = RequestMethod.GET)
	public String reportVehicleLoc() {
		GlobalResponse data = new GlobalResponse();		
		data.setCode(1);
		data.setData(null);
		return new Gson().toJson(data);
	}
	
	@RequestMapping(value = "/api/data/getStopInfo/{token}/{stopCode}/", method = RequestMethod.GET)
	public String getStopInfo() {
		return null;
	}
	
	@RequestMapping(value = "/api/data/getNearByVehicle/", method = RequestMethod.POST)
	public String getNearByVehicle(@RequestBody ReportVehicleLocation rvlObj) {		
		
		return null;
	}
	
	//----------------End--------------------------------//
	
	
	@RequestMapping(value = "/api/data/getDepotGraphForManager/", method = RequestMethod.POST)
	public String getDepotGraphForManager(@RequestBody Depot object) throws Exception {
		GlobalResponse data = new GlobalResponse();
		logger.info("welcome >>>> Controller.getDepotGraphForManager");
		try {
			object = imp.getDepoRepo().findOne(object.getDepot_id());
			data.setCode(1);
			data.setMessage("success");
			data.setData(imp.getService().getDepotGraphForManager(object));

			Map<String, String> mapObj = new HashMap<String, String>();
			mapObj.put("Division", object.getDepot_name());
			List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
			mapList.add(mapObj);
			data.setDepot(mapList);
			data.setCountData(ConstantData.countMapList_forAll_depot_manager);

		} catch (Exception e) {
			e.printStackTrace();
			data.setCode(-1);
			data.setMessage(e.getMessage());
		}
		return new Gson().toJson(data);
	}

	@RequestMapping(value = "/api/data/getDepotGraph/", method = RequestMethod.POST)
	public String getDepotGraph(@RequestBody DataContainer object) throws Exception {
		GlobalResponse data = new GlobalResponse();
		logger.info("welcome >>>> Controller.getDepotGraph");
		// System.out.println("welcome getUserList >>>> "+new Gson().toJson(object));
		try {
			data.setStatus("success");
			data.setCode(1);
			data.setData(imp.getService().getGraphDataForDepo(object.getMessage()));
			data.setDepot(ConstantData.depotList);
			data.setCountData(ConstantData.countMapList_forAll_depot);

		} catch (Exception e) {
			e.printStackTrace();
			data.setCode(-1);
			data.setMessage(e.getMessage());
		}

		return new Gson().toJson(data);
	}
	
	

	
	

	@RequestMapping(value = "/api/data/init", method = RequestMethod.GET)
	public String init(@RequestParam("value") String value) {
        GlobalResponse globaldat = new GlobalResponse();
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

			globaldat.setData(imp.getGraph().getGraphData(value));
			globaldat.setDepot(ConstantData.countMapList_forAll);
			String result = new Gson().toJson(globaldat).toString();
			// result = result.replace("0-1", "aa");
			return result;

		} catch (Exception e) {
			e.printStackTrace(); 
			logger.info("welcome >>>> Controller.init Catch block"+e.getMessage());
			return e.getMessage();
		}

	}

	
	//----------------------20-02-2020---------------//
	

	@RequestMapping(value = "/api/data/getrouteName/{stopId}/", method=RequestMethod.GET)
	public Object getVehicleBylatlan (@PathVariable("stopId") Integer stopId){
		
		return imp.getRawDataInterface().getRoutData(stopId);
		
		
	}
	
	
	@RequestMapping(value = "/api/data/getBusType/{token}/", method = RequestMethod.GET)
	public Object getBusType(@PathVariable("token")String token) {
		GlobalResponse respObj = new GlobalResponse();	
		
		return respObj;
		
	}
	@RequestMapping(value = "/api/data/getuptime/{token}/")
	public Object getUptimeReport(@PathVariable("token")String token) {
		GlobalResponse respObj = new GlobalResponse();
		
		
		return respObj;
		
	}

	
	@RequestMapping(value = "api/data/DailyReport/", method = RequestMethod.POST)
	public GlobalResponse dailyReport(@RequestBody Temp_Response temp_resObj) {
		System.out.println("request ---- >>> "+new Gson().toJson(temp_resObj));	
			
			return  imp.getRawDataInterface().dailyReport(temp_resObj);			
	}
	
	
	@RequestMapping(value="/api/data/updateMembershipGroup/", method = RequestMethod.POST)
	public GlobalResponse updtaeMembershipGroup(@RequestBody MembershipGroup mgrpObj) {
		System.out.println("welcome --- >> "+new Gson().toJson(mgrpObj));
		return imp.getService().updtaeMembershipGroup(mgrpObj);
	}
	
	
	//-----------downLoad any file -----------------//
	@GetMapping(value = "/downloadany_excel/")
    public ResponseEntity<InputStreamResource> excelCustomersReport(@RequestParam("fileNm")String fileNm) throws IOException {
	 File file =  null;   
	 logger.info("data controller download any excel -- >> "+fileNm);
    if(ConstantData.is_server == 1) {
    	String filePath =fileNm;
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
	
	
	
	
}
