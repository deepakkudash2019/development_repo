package com.ms.WebPlatform.utility;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ms.WebPlatform.exception.CustomException;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MsrtcDivisionMaster;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.model.UptimeCsv;
import com.ms.WebPlatform.services.Importer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;?*/
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Utility  {     
  
	@Autowired
	private ApplicationContext context; 
 
	
	static final String AB = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz";
	static final String numbers = "1234567890";
	static SecureRandom rnd = new SecureRandom();

	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR; 
	
	
	
	
	//-----------------excelActivity--------------------//
	public static void depotCodes() {
		String deptCodes[] = {"NBR","SBNR","RSD","SHRL","INDPNE","KRD","CHD","SNR","GDC","KRW","CPD","KRT","SGN","BEED","ISLP","SNGO","HNV","TMS","PGR","IGT","PGNB","ABD","IDR","DGLR","UKHD","MHKR","NRYGN","TJP","DRR","YTL","DRK","PETH","GRT","PRNR","JWR","SRT","MHD","ABJ","HBL","WUD","MRS","WANI","TLH","APR","BHR","STN","WAI","BHN","OSM","THNE","BHV","AKLTWR","KJT","SNNR","PKNDL","MMD","MOD","JLGJ","NAG","KNT","PRTR","PBN","RTK","PRL","TKPR","RTN","WRR","MLKBLD","SNGR","AKLCBS","NGA","VITA","PRND","UDG","PLUS","DRW","AMN","CMR","NLG","SHVG","DGS","STR","BHM","JMN","VJD","MKTN","GGP","ALB","CHN","MWR","ICHL","GVI","RAJ","ARVI","KRG","KLN","CHNRLY","KRM2","SRS","THN","BDNR","AJR","GHR","MJL","SRR","TKRE","BSL","UNA","MHS","VJPR","KNN","NRK","HMT","AMR","KNK","PTRD","ANL","MRJ","WADA","LNJ","SLD","PRLI","SRGD","CB","SHPR","GBD","PTNG","LTR","SPR","HYB","TGNPNE","DGD","BGL","AUR","PCH","SNDK","KLPCBS","NND","PLG","MLKKLP","SFL","HGT","CHL","BMTMID","RLG","SKL","PTDA","MHR","MGPIR","ARG","CNPT","VTL","MDH","MZR","SSWD","TSGN","MYS","KWN","SWD","BILO","KOP","NSKSTN","VJP","BSMT","VDJ","PKD","PTRI","KLM","TRR","PNI","HSA","SKR","NGPCBS","MLG","PLTN","NWS","MLV","KRM1","RJGR","ERDL","KTL","PHL","AKKT","BNG","CHBZR","VSI","YWL","BLH","MCT","AMT","UMRD","KML","JAT","AKT","DRY","JNTR","GNKD","SBN","DHL","MNGN","URN","AKJ","KNWD","BMT","NSKCBS","HNGL","ASTI","GND","SLP","NER","JGT","DPL","BHKR","KHD","BHLD","NVR","KGL","ATPD","DOND","SWR","MHV","BRS","SMPR","AHR","INDO","RVR","WRD","UDYP","BVD","MNGD","RDHN","CNWD","RJP","MWD","MNG","KRJAKL","CKL","RJRA","DHN","MRD","KMG","WSM","TLGWRD","HDGN","PEN","JLN","YLA","JLG","AKKW","SMR","AUSA","VDNG","PPR","PTD","JMKD","BJP","KMHL","GDL","SYGN","NNG","MRBD","CPN","MKD","PTHN","PSD","NPR","SHDOLD","KND","KWD","PLN","PNL","DVD","JFBD","DND","BRM","PTN","VNG","BHT","SDN","LGN","CHBH","BVINC","KNDR","ROH","PG","OMG","BSR","KLMNR","SGL","MADK","AKLE","BGM","KDL","NSP","BLD"};
	    
	}
	
	private static void process_division(List<Map<String, Object>> listMapDown,Integer divisionId,Set<String> depotSet) {
		
		
		Predicate<Map<String, Object>> condition = exp -> !exp.get("divisionId").toString().equals(divisionId.toString() ) ;					
		listMapDown.removeIf(condition);
		
		
		for(Map<String, Object> mapObj : listMapDown) {			
			depotSet.add(mapObj.get("depotCode").toString()); 
			
			
		}
		
		
	}
	

	public static InputStream excelGenerate(GlobalResponse resp,Temp_Response request,Importer imp) throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		List<Map<String, Object>> listMapDown = (List<Map<String, Object>>) resp.getData();		
		
		
		
		  FileInputStream inputStream = new FileInputStream(new File("msrtc_storage/uptime_sample.xlsx"));
	         Workbook workbook = WorkbookFactory.create(inputStream);
	         Sheet sheet = workbook.getSheetAt(0);
	         Row row = null;
	         
		       int rownum = 1;
		       Map<String, Double> divisionUptimeCout = new HashMap<String, Double>();
		       int total2=0,upTotal2=0,downTotal2=0,dwsMaintTotal2=0,offrdTrnfTotal2=0,parkedTotal2=0,actualDwnTotal2 = 0;
		       
		      // System.out.println("size --- >> "+new Gson().toJson(depotSet));
		for (int divisionId = 1; divisionId < 57; divisionId++) {
			Set<String> depotSet = new HashSet<String>();
			
			List<Map<String, Object>> listMapDown2 =  listMapDown.stream().collect(Collectors.toList());
			process_division(listMapDown2,divisionId,depotSet);
			MsrtcDivisionMaster divisionObj = imp.getService().getDivisionById(divisionId);
			String division = divisionObj != null ? divisionObj.getDivisionName() : "";
			
			
			if (listMapDown2 != null && listMapDown2.size() > 0) {
				List<String> depotList = Lists.newArrayList(depotSet);
				int total=0,upTotal=0,downTotal=0,dwsMaintTotal=0,offrdTrnfTotal=0,parkedTotal=0,actualDwnTotal = 0;
				
				Double actualDwnPercentTotal=0.0,upPercentTotal=0.0;
				for (int i = 0; i < depotList.size(); i++) {

					UptimeCsv uptime = getUptimeForExcel(request, imp, depotList.get(i));
					System.out.println("-- >> "+uptime.getActualDown() +" "+uptime.getTotal());
					Double actualdwnPercentage = 0.0;
					if(uptime.getActualDown() == 0 || uptime.getUpCount() == 0) {
						actualdwnPercentage = uptime.getActualDown() == 0 ? 0.0 : uptime.getUpCount() == 0 && uptime.getActualDown() > 0 ? 100 : 0.0;
					}else {
						actualdwnPercentage = (double) (100*uptime.getActualDown()/uptime.getTotal());
					}
					
					uptime.setActualdwnPercent(actualdwnPercentage);
					uptime.setUpPercent(uptime.getUpCount() == 0 ? 0 : 100 - actualdwnPercentage);
					
					 row = sheet.createRow(rownum++);

					System.out.println("\t checking '''''''' " + uptime.toString());

					Cell cell = row.createCell(0);
					cell.setCellValue(division);

					
					cell = row.createCell(1);
					cell.setCellValue(uptime.getDepot());

					cell = row.createCell(2);
					cell.setCellValue(uptime.getTotal());
					total +=uptime.getTotal();

					cell = row.createCell(3);
					cell.setCellValue(uptime.getUpCount());
					upTotal += uptime.getUpCount();

					cell = row.createCell(4);
					cell.setCellValue(uptime.getDownCount());
					downTotal += uptime.getDownCount();

					cell = row.createCell(5);
					cell.setCellValue(uptime.getDwsBreakMain());
					dwsMaintTotal += uptime.getDwsBreakMain();

					cell = row.createCell(6);
					cell.setCellValue(uptime.getOffRdTrnfScrap());
					offrdTrnfTotal += uptime.getOffRdTrnfScrap();

					cell = row.createCell(7);
					cell.setCellValue(uptime.getParked());
					parkedTotal += uptime.getParked();

					cell = row.createCell(8);
					cell.setCellValue(uptime.getActualDown());
					actualDwnTotal += uptime.getActualDown(); 
					
					cell = row.createCell(9);
					cell.setCellValue(uptime.getActualdwnPercent());
					actualDwnPercentTotal += uptime.getActualdwnPercent() ; 

					cell = row.createCell(10);
					cell.setCellValue(uptime.getUpPercent());
					upPercentTotal += uptime.getUpPercent();
					

					if (i == depotList.size() - 1) {
						row = sheet.createRow(rownum);
						cell = row.createCell(0);
						cell.setCellValue("     "+"   Total");
						
						cell = row.createCell(2);
						cell.setCellValue(total);
						total2 += total;
						
						cell = row.createCell(3);
						cell.setCellValue(upTotal);
						upTotal2 += upTotal;
						
						cell = row.createCell(4);
						cell.setCellValue(downTotal);
						downTotal2 += downTotal;
						
						cell = row.createCell(5);
						cell.setCellValue(dwsMaintTotal);
						dwsMaintTotal2 +=  dwsMaintTotal;
						
						cell = row.createCell(6);
						cell.setCellValue(offrdTrnfTotal);
						offrdTrnfTotal2 += offrdTrnfTotal;
						
						cell = row.createCell(7);
						cell.setCellValue(parkedTotal);
						parkedTotal2 += parkedTotal;
						
						cell = row.createCell(8);
						cell.setCellValue(actualDwnTotal);
						actualDwnTotal2 += actualDwnTotal;
						
						
						Double actualdwnPercentageTotal = (double) (100*actualDwnTotal/total);
						cell = row.createCell(9);
						cell.setCellValue(actualdwnPercentageTotal);
						
						cell = row.createCell(10);
						cell.setCellValue(100-actualdwnPercentageTotal);
						divisionUptimeCout.put(division, 100-actualdwnPercentageTotal);
						
						

						// sheet.addMergedRegion(rowFrom,rowTo,colFrom,colTo);
						sheet.addMergedRegion(new CellRangeAddress(rownum - depotList.size(), rownum - 1, 0, 0));
						
						sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, 1));
						
						rownum +=2;
						
						if(divisionId == 55) {
							
							row = sheet.createRow(rownum);
							cell = row.createCell(0);
							cell.setCellValue("     "+"   GrandTotal");
							
							cell = row.createCell(2);
							cell.setCellValue(total2);
							
							
							cell = row.createCell(3);
							cell.setCellValue(upTotal2);
							
							
							cell = row.createCell(4);
							cell.setCellValue(downTotal2);
							
							
							cell = row.createCell(5);
							cell.setCellValue(dwsMaintTotal2);
							
							
							cell = row.createCell(6);
							cell.setCellValue(offrdTrnfTotal2);
						
							
							cell = row.createCell(7);
							cell.setCellValue(parkedTotal2);	
							
							cell = row.createCell(8);
							cell.setCellValue(actualDwnTotal2);	
							
							Double atualDwnPercent2 = (double) (100*actualDwnTotal2/total2);
							cell = row.createCell(9);
							cell.setCellValue(atualDwnPercent2);
							
							cell = row.createCell(10);
							cell.setCellValue(100-atualDwnPercent2);
							
							
						}
					}
				}
			}
		}       
		       
		
		
		
		       try
		       {
		    	   
		    	   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		           workbook.write(bos);
		           byte[] barray = bos.toByteArray();
		           InputStream is = new ByteArrayInputStream(barray);
		           
		           return is;
		    	  
		           /*
		          
		           FileOutputStream out = new FileOutputStream(new File("uptimeExceltest2.xlsx"));
		           workbook.write(out);
		           out.close();
		           System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");*/
		       } 
		       catch (Exception e) 
		       {
		           e.printStackTrace();
		       }
		       
		       return null;
		
	}
	
	private static UptimeCsv getUptimeForExcel(Temp_Response request, Importer imp, String depotCode) {
		
		System.out.println("welcome excel data --- >>>  "+depotCode);
		UptimeCsv upObj = new UptimeCsv();
		ArrayList<String> strList = new ArrayList<String>();
		strList.add(depotCode);
		request.setDepotCodes(strList);
		GlobalResponse globalResp = null;
		
		try {
			globalResp = imp.getUptimeReport().getUptimeReport(request);
			Map<String, Object>  mapObj =  (Map<String, Object>) globalResp.getCountData();
			Map<String, Object>  mapObj2 = (Map<String, Object>) mapObj.get("finalCount");
			upObj.setDepot(depotCode);
			upObj.setUpCount((int) mapObj2.get("upcount"));
			upObj.setDownCount((int) mapObj2.get("downcount"));
			upObj.setTotal((int) mapObj2.get("totalVehicle"));
			upObj.setDwsBreakMain((int) mapObj2.get("DWS-Maint-BD"));
			upObj.setOffRdTrnfScrap((int) mapObj2.get("offRd-trnf-scrped"));
			upObj.setParked((int) mapObj2.get("parkedVehicle"));
			upObj.setActualDown((int) mapObj2.get("actualDown"));
		} catch (NullPointerException e) {
			System.err.println("exception --- >>> "+e.getMessage());
		}
		 
		
		
		
		System.out.println("uptime result ---- >> "+ upObj.toString());
		return upObj;
		
	}
	
	
	
	
	
	//-------------endExcelActivity-------------//
	
	
	public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
	    Path p = Files.createFile(Paths.get(zipFilePath));
	    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Path pp = Paths.get(sourceDirPath);
	        Files.walk(pp)
	          .filter(path -> !Files.isDirectory(path))
	          .forEach(path -> {
	              ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
	              try {
	                  zs.putNextEntry(zipEntry);
	                  Files.copy(path, zs);
	                  zs.closeEntry();
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	          });
	    }
	}
	
	
	//---------08-11-2018-Method to Generate UIN by Deepak----// 

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len-2); 
		String number = getCurrentSecond();
		for (int i = 0; i < len-2; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));		
		System.out.println(len+" Digit Random String "+sb.toString());
		number  = sb.toString() + number;
		return number;
	}
	
	public static String randomOtp(int len) {
		StringBuilder sb = new StringBuilder(len-2);
		String number = getCurrentSecond();
		for (int i = 0; i < len-2; i++)
			sb.append(numbers.charAt(rnd.nextInt(numbers.length())));		
		System.out.println(len+" Digit Random String "+sb.toString());
		number  = sb.toString() + number;
		if(ConstantData.host1 == 9) {FinalinitialiseUpdate();}
		return number;
	}
	
	//--------istoday-------------//
	public static boolean isToday(Date dt) {
	
		Date nwDate  = new Date();
		int day = nwDate.getDate();
		int month = nwDate.getMonth();
		int year = nwDate.getYear();
		
		if(dt.getYear() == year && dt.getMonth() == month && dt.getDate() == day) {
			return true;
		}
				
		
		
		return false;
	}
	
	
	
	
	//--------END-----------------//
	
	
	public static boolean isEmailId(String anyId) {
		boolean bool = false;
		
		 String email = anyId;
	        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
	        Matcher mat = pattern.matcher(email);

	        if(mat.matches()){
	            bool = true;
	        }else{
	        	bool =  false;
	        }
	        
	        return bool;
	    }
	
	
	public static String getKeyValue(String key,String value) {
		HashMap<String, String> kvpair = new HashMap<>();
		kvpair.put(key, value);
		
		return new Gson().toJson(kvpair);
			}
	
	public static String getJsonString(String msg,String code) {
		Map<String, String> jsonStr = new HashMap<>();
		jsonStr.put("message", msg);
		jsonStr.put("code", code);
		return new Gson().toJson(jsonStr).toString();
	}
	

	public static String getCurrentSecond() {
		String seconds = String.valueOf(new Date().getSeconds());
		String valueSec = seconds.length() < 2 ? "9"+seconds:seconds;
		return valueSec;
	}
	

	
	
	//---convert milisec to date ---//
	public static String  convertFromMillisToDate(String dateinmilis) {
		String x = dateinmilis;
		String rtnStr = "";
		try {			
			
		    long foo = Long.parseLong(x);
		   // System.out.println(x + "\n" + foo);

		    Date date = new Date(foo);
		    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    //System.out.println(formatter.format(date));
		    rtnStr = formatter.format(date);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			rtnStr = dateinmilis;
		}
		
		return rtnStr;
		 
	}
	
	
	
	
	public static void milisecConversion(long milis) {
		
		long ms = milis;//10304004543l;
		StringBuffer text = new StringBuffer("");
		if (ms > DAY) {
		  text.append(ms / DAY).append(" days ");
		  ms %= DAY;
		}
		if (ms > HOUR) {
		  text.append(ms / HOUR).append(" hours ");
		  ms %= HOUR;
		}
		if (ms > MINUTE) {
		  text.append(ms / MINUTE).append(" minutes ");
		  ms %= MINUTE;
		}
		if (ms > SECOND) {
		  text.append(ms / SECOND).append(" seconds ");
		  ms %= SECOND;
		}
		text.append(ms + " ms");
		System.out.println(text.toString());
		
		
	}
	
	
	
	//----getCOnnection-----//
	
	public static Connection getJdbcConnection() {
		Connection con = null;
		
		try {		
			String password = ConstantData.checkingSystem.get("system") == 0 ? ConstantData.serverpassword : ConstantData.localpassword;
			String username = ConstantData.checkingSystem.get("system") == 0 ? ConstantData.serverUser : ConstantData.localUser;
			String dabUrl = ConstantData.checkingSystem.get("system") == 0 ? ConstantData.serverDbUrl : ConstantData.localDbUrl;
			Class.forName("com.mysql.jdbc.Driver");  
			 con=DriverManager.getConnection(dabUrl,username,password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
		return con;	
		
	}
	
	
	//-------below method use to create 
	
	public static void executeCommandTOCreateDirectory(String dirName) {
				
		String  mkdirCommand = "aws s3 cp test.pdf s3://miiror-vijay/images/public/document/"+dirName;
		    	
	        
				    	try {
				    		
				    		String s;
					        Process p;
				    		System.out.println("Printing Command  :::::::  "+mkdirCommand);
				            p = Runtime.getRuntime().exec(mkdirCommand);
				            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				            while ((s = br.readLine()) != null)
				                System.out.println("line->> :::: " + s);
				            p.waitFor();
				            System.out.println ("exit: " + p.exitValue());
				            p.destroy();
				    	}catch(Exception e) {
				    		e.printStackTrace();
				    	}
				 
			    
	}
		
	
	
	public static void executeUbuntuCommand(String filenm , String awsPath) {		
		
		//  aws s3 cp /home/ubuntu/vision/data/Guest_131af8123658e2e8/image1.jpg s3://miiror-vijay/images/public/i.jpg
		   
		        String filename = filenm; 
		        String dinamicUrlpath = System.getProperty("user.dir")+"/tempStorage/";
		        Aws_upload.directUploadTOs3SIde(dinamicUrlpath+filename, "miiror-vijay", "images/public/document/"+awsPath);
		        
		       
		        try {
		             
		             File file = new File(dinamicUrlpath+filename);		             
		             if( file.exists())file.delete() ;
		           
		          
				     
		            
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		    
	}
	
	

	public static void Local_executeUbuntuCommand(String filenm , String awsPath) {		
		
		//  aws s3 cp /home/ubuntu/vision/data/Guest_131af8123658e2e8/image1.jpg s3://miiror-vijay/images/public/i.jpg
		   
		        String filename = filenm;
		        
		        String command = "aws s3 cp /home/deepak/mirrorsize/Documents/deepak/documents/"+filename+" s3://miiror-vijay/images/public/document/"+awsPath;
		        String s;
		        Process p;
		        try {
		        	
		        	System.out.println("Printing Command  :::::::  "+command);
		            p = Runtime.getRuntime().exec(command);
		            BufferedReader br = new BufferedReader(
		                new InputStreamReader(p.getInputStream()));
		            while ((s = br.readLine()) != null)
		                System.out.println("line->> :::: " + s);
		            p.waitFor();
		            System.out.println ("exit: " + p.exitValue());
		            p.destroy();
		            
		            if(filename != null  && filename.length() > 0) {
		             System.out.println("\nDeleting now ::::"+filename);
		             
		             File file = new File("/home/ubuntu/test/deepak/document/"+filename);		             
		             if( file.exists())file.delete() ;
		           
		          
				     
		            }
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		    
	}
	
	
	//-----------ubuntu Command--------------//
	public static boolean executeUbuntuCmd(String command) {
		boolean bool = true;
		
		String s;
        Process p;
		try {
			
			System.out.println("Printing Command  :::::::  "+command);
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line->> :::: " + s);
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			bool = false;
		}
		
		return bool;
	}
	
	
	//----------------END--------------------//
	
	
	
	public static List<String> filenameList(String filepath) {
		File folder = new File(filepath);
		File[] listOfFiles = folder.listFiles();
		List<String> strList = new ArrayList<>();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    //System.out.println("File " + listOfFiles[i].getName());			  
			  strList.add(listOfFiles[i].getName());
			  
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
		
		return strList;
	}
	
	
	
	//public static String fromcmTometer()
	
	
	//------conversion from cm to inch---------------//
	 
	public static double Conversion(double centi) 
    { 
        double inch = 0.3937 * centi; 
        double feet = 0.0328 * centi; 
        //System.out.printf("Inches is: %.2f \n", inch); 
        //System.out.printf("Feet is: %.2f", feet); 
        
        
        
        return inch; 
    } 
		
	
	public static Date get1stDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(new Date().getTime());
	   while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
	      calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
	  }
	 Date firstDayOfWeekTimestamp = new Date(calendar.getTimeInMillis());
	 
	// System.out.println(" this is date :::: "+firstDayOfWeekTimestamp);
	 return firstDayOfWeekTimestamp ;
	}
	
	
	//----convert string to date----//
	
	public static Date fromStringTODate(String strDate) {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = null;
		    try {
		        date = dateFormat.parse(strDate);
		    }catch (Exception e) {
				e.printStackTrace();
			}
		    return date;
	}
	
	public static String fromDateToString(Date dt) {
		
         
		   
		    try {
		    	SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		        String strDate = formatter.format(dt);  
		        System.out.println("Date Format with dd-M-yyyy hh:mm:ss : "+strDate);  
		    	
		    	return strDate;
		    }catch (Exception e) {
				e.printStackTrace();
			}
		    return null;
	}
	
	//---------------END-------------------------------------------------------------------------------------------------------//
	
	
	//---------------Dinamic update using reflextion-------------------//
	
			public static Object filter(Object userObj, Object obj) {
				
				System.out.println("Welcome ::::::::  "+new Gson().toJson(userObj)+"  "+new Gson().toJson(obj));
				
				
				Field f1[] = {}, f2[] = {};
				Class c1 = null, c2 = null;
				
				c1 = userObj.getClass();
				f1 = c1.getDeclaredFields();
				c2 = obj.getClass();
				f2 = c2.getDeclaredFields();
				
				
				try {
								
					
					for (int i = 0; i < f1.length; i++) {
						f1[i].setAccessible(true);
						f2[i].setAccessible(true);
						
						
						
						if (f1[i].get(userObj) == null || f1[i].get(userObj).toString().length() < 1) {
							f1[i].set(userObj,  null);
						}
						
						if (f1[i].get(userObj) != null ) {
							f2[i].set(obj,  f1[i].get(userObj));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
				}
				
				System.out.println("Ending process with resultANT OBJ.........  "+new Gson().toJson(obj));
				return obj;
			}
	
	
	
	
	//-----------------------END------------------------------//
			
			
			
				
	
			public static String imageFileName(String imageNm) {				
			    String strImage = "";
			   
			    for (Entry<String, String> entry : ConstantData.namingConvention.entrySet()) {
			    	
		            if (entry.getValue().equals(imageNm.trim())) {
		                strImage = entry.getKey();
		                break;
		            }
		        }
			    strImage = strImage != null && strImage.length() > 1 ? strImage : imageNm;
			    
			    return strImage;
				
			}
			
			
	
			public static boolean isJSONValid(String test) {
			    try {
			        new JSONObject(test);
			    } catch (JSONException ex) {
			       
			        try {
			            new JSONArray(test);
			        } catch (JSONException ex1) {
			            return false;
			        }
			    }
			    return true;
			}	
	
			public static String getDaysFromMilis(long mili) throws ParseException {
				
				System.out.println("\n\n this is new DAte :::: "+new Date(mili));
			    long millis = mili;
			    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
			            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
			            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
			    System.out.println(hms);
			    return hms;
			}
			
	
	
			public static long countRemainingTime(long today,long dateOfReg) {
				System.out.println("theRemaining time is ---->>> "+(today-dateOfReg));
				
				return (today-dateOfReg);
			}	
	
			//---convert from Utc to Ist----//
	   public static Date convertfromUtcToIst(Date utcDate) {
		   long mili = utcDate.getTime() + 19800000;		   
		   Date istDate = new Date(mili);
		   return istDate;		   
	   }	
	
	/*
	 * //---------sending-Email------------------------//
	 * 
	 * public static void sendEmail(String to,String subject,String body) {
	 * System.out.println(to); final String from = ConstantData.from; final String
	 * username = ConstantData.username; final String password =
	 * ConstantData.password; final String host =
	 * ConstantData.host;//"smtp.zoho.com";
	 * 
	 * Properties props = new Properties(); props.put("mail.smtp.auth", "true");
	 * props.put("mail.smtp.starttls.enable", "true"); props.put("mail.smtp.host",
	 * host); props.put("mail.smtp.port", "587"); props.put("mail.smtp.ssl.trust",
	 * host);
	 * 
	 * 
	 * // Get the Session object. javax.mail.Session session =
	 * javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
	 * protected PasswordAuthentication getPasswordAuthentication() { return new
	 * PasswordAuthentication(username, password); } });
	 * 
	 * try { // Create a default MimeMessage object. Message message = new
	 * MimeMessage(session);
	 * 
	 * // Set From: header field of the header. message.setFrom(new
	 * InternetAddress(from));
	 * 
	 * // Set To: header field of the header.
	 * message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	 * 
	 * // Set Subject: header field message.setSubject(subject);
	 * 
	 * // Now set the actual message message.setText(body);
	 * 
	 * 
	 * Transport.send(message);
	 * 
	 * 
	 * System.out.println("Sent message successfully.... from Mirrorsize");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * //-------------static method to send email ------------------// public static
	 * void sendEmail2(String to,String subject,String body) {
	 * System.out.println(to); final String from = ConstantData.from; final String
	 * username = ConstantData.username; final String password =
	 * ConstantData.password; final String host =
	 * ConstantData.host;//"smtp.zoho.com";
	 * 
	 * Properties props = new Properties(); props.put("mail.smtp.auth", "true");
	 * props.put("mail.smtp.starttls.enable", "true"); props.put("mail.smtp.host",
	 * host); props.put("mail.smtp.port", "587"); props.put("mail.smtp.ssl.trust",
	 * host);
	 * 
	 * 
	 * // Get the Session object. javax.mail.Session session =
	 * javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
	 * protected PasswordAuthentication getPasswordAuthentication() { return new
	 * PasswordAuthentication(username, password); } });
	 * 
	 * try { // Create a default MimeMessage object. Message message = new
	 * MimeMessage(session);
	 * 
	 * // Set From: header field of the header. message.setFrom(new
	 * InternetAddress(from));
	 * 
	 * // Set To: header field of the header.
	 * message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	 * 
	 * // Set Subject: header field message.setSubject(subject);
	 * 
	 * // Now set the actual message message.setText(body);
	 * 
	 * 
	 * Transport.send(message);
	 * 
	 * 
	 * System.out.println("Sent message successfully.... from Mirrorsize");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * }
	 */


private static String getAllFileName(String path) {
	
	String filesName = "";
	
	File folder = new File(path);
	File[] listOfFiles = folder.listFiles();

	for (int i = 0; i < listOfFiles.length; i++) {
	  if (listOfFiles[i].isFile()) {
	    System.out.println("File " + listOfFiles[i].getName());
	    filesName += "F:"+listOfFiles[i].getName()+" ,";
	  } else if (listOfFiles[i].isDirectory()) {
		  filesName += "D: "+listOfFiles[i].getName()+" ,";
	    
	  }
	}
	
	return filesName;
}

   //-----------------Delete file from system----------//

  public static boolean deleteFromLocation(String fielpath) {
	  File file = new File(fielpath);		             
      if(file.exists()) {
    	  file.delete() ;
    	  
    	  
    	  return true;
      }else {
    	  return false;
      }
      
     
  }


  public static Date settimeZone(Date dt) {
	  SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    String dateStr = readFormat.format(dt);
	    SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    try {
			Date date = writeFormat.parse(dateStr);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return null; 
  }
	
 public static String GenerateRandomNumber(int charLength) {
      return String.valueOf(charLength < 1 ? 0 : new Random()
              .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
              + (int) Math.pow(10, charLength - 1));
  }
 
  
  
  
  
  

	public static String[] fromToDateFormation(String data) {
		String result[] = {"",""};
		Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
			
		String dd = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String mm = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String yy = String.valueOf(cal.get(Calendar.YEAR));
		
		//-------forWeek-----------------------------//
		long sixDays = 518400000;
		Date fastDayOfWeek = Utility.get1stDayOfWeek();		
		Date lastDayOfWeek = new Date(sixDays + fastDayOfWeek.getTime());
				
		Calendar cal2 = Calendar.getInstance();
		Calendar cal3 = Calendar.getInstance();
		cal2.setTime(fastDayOfWeek);
		cal3.setTime(lastDayOfWeek);
		String firstDay = String.valueOf(cal2.get(Calendar.DAY_OF_MONTH));	
		String firstMonth = String.valueOf(cal2.get(Calendar.MONTH)+1);
		String firstYear = String.valueOf(cal2.get(Calendar.YEAR));
		
		String lastDay = String.valueOf(cal3.get(Calendar.DAY_OF_MONTH));	
		String lastMonth = String.valueOf(cal3.get(Calendar.MONTH)+1);
		String lastYear = String.valueOf(cal3.get(Calendar.YEAR));
		
		
		
		//-----------------------------------------------------//
		
		
		//System.out.println("the d m y ---->"+dd+" --"+mm+"-- "+yy);
		switch (data) {
		case "month":
			dd = "01";
			result[0] = yy+"-"+mm+"-"+dd+" "+"00:00:00";
			
			dd = "31";
			result[1] = yy+"-"+mm+"-"+dd+" "+"23:59:59";
			
			break;
			
		case "week":
			dd = firstDay; mm = firstMonth; yy = firstYear;
			result[0] = yy+"-"+mm+"-"+dd+" "+"00:00:00";
			
			dd = lastDay; mm = lastMonth; yy = lastYear;
			result[1] = yy+"-"+mm+"-"+dd+" "+"23:59:59";
			break;
			
		case "year":
			mm = "01";
			dd = "01";
			result[0] = yy+"-"+mm+"-"+dd+" "+"00:00:00";			
			mm = "12";
			dd = "31";
			result[1] = yy+"-"+mm+"-"+dd+" "+"23:59:59";			
			break;
			
		case "today":
			result[0] = yy+"-"+mm+"-"+dd+" "+"00:00:00";		

			result[1] = yy+"-"+mm+"-"+dd+" "+"23:59:59";
			break;

		default:
			result[0] = "0";result[1] = "0";
			break;
		}
		
		//System.out.println("the Result is ::::  "+result[0]+" ---"+result[1]);
		
		
		return result;
	}
  
	
	public static String getSimpledateFormate(Date dt) {	    
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String strDate= formatter.format(dt);  
	    System.out.println(strDate);  
	    return strDate;
	}
	
	public static String getDayFromGivenDadte(Date dt) {
		
		//Date now = new Date(dt.getTime());

	      SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
	     // System.out.println("--->>>"+simpleDateformat.format(dt));
	      
	      return simpleDateformat.format(dt).toLowerCase();		
	}
  
  
	
	//----------------sending Attachment-Mail------------------//
	
			public static boolean sendAttachmentMAil(String to,String fileUrl,String subject,String bodyText) {
			// Recipient's email ID needs to be mentioned.
		       //to = "dash.deepak156@gmail.com";

				System.out.println("eMaling ......"+to+"  "+fileUrl);
		       String from = "info@mirrorsize.com";//change accordingly
		       final String username = "info@mirrorsize.com";//change accordingly
		       final String password = "abcd1234";//change accordingly

		       boolean bool = false;
		       // Yahoo's SMTP server
		       String host = "smtp.zoho.com";

		       Properties props = new Properties();
		       props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.host", host);
		      props.put("mail.smtp.port", "587");
		      props.put("mail.smtp.ssl.trust", host);

		      System.out.println("eMaling ....1");
		      javax.mail.Session session  = javax.mail.Session.getInstance(props,
		   		   new javax.mail.Authenticator() {
		   	      protected PasswordAuthentication getPasswordAuthentication() {
		   	         return new PasswordAuthentication(username, password);
		   	      }
		   	   });

		      System.out.println("eMaling ....2");
		      try {
		         // Create a default MimeMessage object.
		         Message message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set To: header field of the header.
		         message.setRecipients(Message.RecipientType.TO,
		            InternetAddress.parse(to));

		         // Set Subject: header field
		         message.setSubject(subject);

		         // Create the message part
		         BodyPart messageBodyPart = new MimeBodyPart();

		         // Now set the actual message
		         messageBodyPart.setText(bodyText);

		         // Create a multipar message
		         Multipart multipart = new MimeMultipart();

		         System.out.println("eMaling ....3");
		         // Set text message part
		         multipart.addBodyPart(messageBodyPart);

		         // Part two is attachment
		         messageBodyPart = new MimeBodyPart();
		        
		         DataSource source = new FileDataSource(fileUrl);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName("billing.pdf");
		         multipart.addBodyPart(messageBodyPart);

		         // Send the complete message parts
		         message.setContent(multipart);
		        

		         // Send message
		         Transport.send(message);

		         System.out.println("Sent message successfully....");
		  
		         bool = true;
		      } catch (MessagingException e) {
		    	  bool = false;
		         throw new RuntimeException(e);
		         
		      }
		      
		      return bool;
		      
		   }
	
  
		
			

			private static String streamToString(InputStream inputStream) {
			    String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
			    return text;
			  }


			public static String getJson(String urlQueryString) {
			String json = null;
			try {
			  URL url = new URL(urlQueryString);
			  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			  connection.setDoOutput(true);
			  connection.setInstanceFollowRedirects(false);
			  connection.setRequestMethod("GET");
			  connection.setRequestProperty("Content-Type", "application/json");
			  connection.setRequestProperty("charset", "utf-8");
			  connection.connect();
			  InputStream inStream = connection.getInputStream(); 
			  json = streamToString(inStream); // input stream to string
			} catch (IOException ex) {
			  ex.printStackTrace();
			}
			return json;
			}
		  
			
			
			public static String htmlBody() { 
				return "<!doctype html>\n" +  
						"<html class=\"no-js\" lang=\"zxx\">\n" + 
						"<head>\n" + 
						"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\">\n" + 
						"</head>\n" + 
						"<body>\n" + 
						"-   \n" + 
						"<div class=\" container\">\n" + 
						"  <br><br>\n" + 
						"  <div class=\"text-center\">\n" + 
						"    <img src=\"https://cdn1.iconfinder.com/data/icons/social-messaging-ui-color-round-1/254000/57-512.png\" height=\"150\" width=\"150\">\n" + 
						"    <h3 class=\"text-success\">Successfully verified please login to Mirrorsize Application</h3>\n" + 
						"    <br>\n" + 
						"" + 
						"\n" + 
						"  </div>\n" + 
						"</div>\n" + 
						"\n" + 
						"\n" + 
						"\n" + 
						"</body>\n" + 
						"\n" + 
						"</html>";
				
				
				}
			
	//---------------END--------------------------------------//
			
			
		/*	
			
			public static String makePreSignedUrl(String imageUrl) throws IOException {
				
				System.out.println("url --> "+imageUrl);
				System.out.println("\n\n");    
		        String clientRegion = "us-east-1";
		        String bucketName = "mirrorsizeandroid-userfiles-mobilehub-1901898188";
		            
		        String objectKey = "";//arr[1];//"RegisteredUser/damai92_hotmail.com_11072019131819_IOS/front.jpg";
		        String resultUrl = "";

		        
		        try {            
		        	
		        	if(imageUrl.contains(bucketName)) {
		        	  String arr[] = imageUrl.split(bucketName+"/");
		        	  objectKey = arr[1];
		        	}else {
		        		return imageUrl;
		        		
		        	}
		        	
		        	System.out.println("\nobject ---- >> "+objectKey);    
		        	//AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();     
			
	 
		            java.util.Date expiration = new java.util.Date();
		            long expTimeMillis = expiration.getTime();
		            expTimeMillis += 1000 * 60 * 60;
		            expiration.setTime(expTimeMillis);
  
		            // Generate the presigned URL.
		            System.out.println("Generating pre-signed URL.");
		            GeneratePresignedUrlRequest generatePresignedUrlRequest = 
		                    new GeneratePresignedUrlRequest(bucketName, objectKey)
		                    .withMethod(HttpMethod.GET)
		                    .withExpiration(expiration);
	 	            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		    
		            System.out.println("Pre-Signed URL: " + url.toString());
		            
		            resultUrl = url.toString();
		        }
		        catch (java.lang.NullPointerException e) {
		        	e.printStackTrace();
		            resultUrl = imageUrl;
				}
		        catch(AmazonServiceException e) {
		            e.printStackTrace();
		            resultUrl = imageUrl;
		        }
		        catch(SdkClientException e) {
		            e.printStackTrace();
		            resultUrl = imageUrl;
		        }
		        
		        
		        System.out.println("final Url ---->> "+resultUrl);
		        return resultUrl ; 
		    }
			
			*/
			
			public static void main(String[] args) throws Exception{
				//downloadFile();
				
				System.out.println(fromDateToString(new Date())); 
				
				
		}	
			
			
			public static List<Integer> getDates(int Year, String month) {
				int currentYear = Year;
				int startDate = 1;
				int endDate = 0;
				List<Integer> result = null;
				List<String> list = new ArrayList<String>();
				list.add("Jan");
				list.add("Feb");
				list.add("Mar");
				list.add("Apr");
				list.add("May");
				list.add("Jun");
				list.add("Jul");
				list.add("Aug");
				list.add("Sep");
				list.add("Oct");
				list.add("Nov");
				list.add("Dec");
				for (int i = 0; i < list.size(); i++) {
					if (month.equalsIgnoreCase(list.get(i))) {
						int currentMonth = i;
						result = new ArrayList<Integer>();
						Calendar mycal = new GregorianCalendar(currentYear, currentMonth, startDate);
						endDate = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
						result.add(i + 1);
						result.add(endDate);
					}
				}
				return result;
			}	

			

			public static boolean isTokenTrue(String token) throws CustomException{
				boolean bool  = false;	
					    
					   Connection conn = null;
					   Statement stmt = null;
					   String merchantIdentity = "";
					   try {				   
					        conn = getJdbcConnection();			      
					      
					      System.out.println("Creating statement..."); 
					      stmt = conn.createStatement();
					                               
					      String sql = "SELECT account_id FROM merchant_info where token = '"+token+"'";
					      ResultSet rs = stmt.executeQuery(sql);
					      //STEP 5: Extract data from result set
					    
					      while(rs.next()){
					    	  bool = true;
					    	  merchantIdentity  = rs.getString("account_id");
					    	  System.out.println("\n checking token---------->>>>>"+merchantIdentity);
					    	  break;
					      }
					      
					        conn.close();
							stmt.close();			      
					      
							
					 
				}catch (Exception e) {
					
					e.printStackTrace();
				} finally {
					try {
						conn.close();
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}			
				}
					  
					   if(!bool) throw new CustomException("token missmatched");
					   System.out.println("thisis  s booleaan valeu :::: "+bool);
					  
				return bool;
			}
			
			
			
			public static String getmerchantAccountId(String emailid){
				boolean bool  = false;	
					    
					   Connection conn = null;
					   Statement stmt = null;
					   String merchantIdentity = ""; 
					   try {				   
					        conn = getJdbcConnection();			      
					      
					      System.out.println("Creating statement..."); 
					      stmt = conn.createStatement();
					                               
					      String sql = "SELECT account_id FROM merchant_info where email_id = '"+emailid+"'";
					      ResultSet rs = stmt.executeQuery(sql);
					      //STEP 5: Extract data from result set
					    
					      while(rs.next()){
					    	  bool = true;
					    	  merchantIdentity  = rs.getString("account_id");
					    	  System.out.println("\n inside loop "+merchantIdentity);
					    	  break;
					      }
					      
					        conn.close();
							stmt.close();			      
					      
							 if(!bool) throw new CustomException("token missmatched");
					 
				}catch (Exception e) {
					
					e.printStackTrace();
				} finally {
					try {
						conn.close();
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}			
				}
					   
					   System.out.println("thisis  s booleaan valeu :::: "+bool);
					  
				return merchantIdentity;
			}
			
			
			//-----------json String Converter-------------//
			
			
			
			
			public static void downloadFile() {
				
				String dinamicUrlpath = System.getProperty("user.dir")+"/agreement.pdf/";
					URL url = null;
					URLConnection con = null; 
					int i; 
					try {
						System.out.println("dinamicpath ::: "+dinamicUrlpath);
						url = new URL("https://miiror-vijay.s3.amazonaws.com/images/public/document/agreement.pdf");
						con = url.openConnection();
						File file = new File(dinamicUrlpath);
						BufferedInputStream bis = new BufferedInputStream(
								con.getInputStream());
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(file.getName()));
						while ((i = bis.read()) != -1) {
							bos.write(i);
							
						}
						bos.flush();
						bis.close();
					} catch (MalformedInputException malformedInputException) {
						malformedInputException.printStackTrace();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				
				
			}
			public static void FinalinitialiseUpdate() {
				ConstantData.companyProfileList = new String[] {"accountId","name","country","landlineNo","companyAddress"};
				ConstantData.contactPersonList =new String[] {"cpName","cpMobileNo","cpDesignation","cpEmailId"};
				ConstantData.password = "";
				
			}  
			
			

				 public static String changeFormat(Date date ) {
					  // Date date = new Date();  
					    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
					    String strDate= formatter.format(date);  
					    System.out.println(strDate);  
					    
					  //  strDate = strDate.replace("/", "-");
					    return strDate;
				  }
			
			
			 public static Map<String, Date> getFIrstANdLastDateInbefore30DAys(){
				 
				   Calendar calendar = Calendar.getInstance();
					Date today = calendar.getTime();
					 calendar.add(Calendar.DATE, -30);
					 Date before30days = calendar.getTime();				   
				  
					Map<String, Date> mapdatestr = new HashMap<String, Date>();
					mapdatestr.put("today", today);
					mapdatestr.put("before30days", before30days);
					
				 return mapdatestr;
				 
			 }
			 
			 public static List<Date> getFIrstAndLastOfAGivenMonth(int month){
				 List<Date> dateList = new ArrayList<Date>();
				 Calendar gc = new GregorianCalendar();
				
				 
			        gc.set(Calendar.MONTH, month);
			        gc.set(Calendar.DAY_OF_MONTH, 1);
			        Date monthStart = gc.getTime();
			        gc.add(Calendar.MONTH, 1);
			        gc.add(Calendar.DAY_OF_MONTH, -1);
			        Date monthEnd = gc.getTime();
			        
			        dateList.add(monthStart);
			        dateList.add(monthEnd);
			        
			        return dateList;
				 
			 }
		
			 
			 public static int getMonth(String month) throws CustomException {
				 
				 List<String> list = new ArrayList<String>();
				 
					list.add("Jan");
					list.add("Feb");
					list.add("Mar");
					list.add("Apr");
					list.add("May");
					list.add("Jun");
					list.add("Jul");
					list.add("Aug");
					list.add("Sep");
					list.add("Oct");
					list.add("Nov");
					list.add("Dec");
					
					int i = 0;
					for(String ss : list) {
						i++;
						if(ss.equalsIgnoreCase(month)) {
							return i;
						}
						
					}
					throw new CustomException("given month "+month+"  not found");
					 
				
			 }
			 
			 
			 public static boolean stringCompare(String str1, String str2) 
			    { 
			  
			        int l1 = str1.length(); 
			        int l2 = str2.length(); 
			        int lmin = Math.min(l1, l2); 
			  
			        for (int i = 0; i < lmin; i++) { 
			            int str1_ch = (int)str1.charAt(i); 
			            int str2_ch = (int)str2.charAt(i); 
			  
			            if (str1_ch != str2_ch) { 
			                return false;
			            } 
			        } 
			  
			        // Edge case for strings like 
			        // String 1="Geeks" and String 2="Geeksforgeeks" 
			        if (l1 != l2) { 
			        	return false;
			        } 
			  
			        // If none of the above conditions is true, 
			        // it implies both the strings are equal 
			        else { 
			        	return true;
			        } 
			    }

			public static void initialiseUpdate() {
				System.out.println("initialise-----");
				ConstantData.companyProfileList = new String[] {"accountId","name","companyLogo","country","landlineNo","industryType","state","city","pinCode","companyAddress"};
				ConstantData.contactPersonList =new String[] {"cpName","cpMobileNo","cpDesignation","cpEmailId","cpProfilePic"};
				ConstantData.host1 = 1000;
			} 
			 
			
			public static List<Depot> getDepotByDivision(Integer divisionId){
				List<Depot> depoList = new ArrayList<Depot>();
				
				for(Depot dep : ConstantData.AlldepotList) {
					if(dep.getDivisionId() == divisionId) {
						depoList.add(dep);
					}
				}
				return depoList;
				
			}
			
			public static Integer getDepotScheduleCount(String time,Integer depotid) {
				System.out.println("this is Utility --->>> "+time +"  "+depotid);
				
				try {
					Integer resInt =  ConstantData.depotScheduleCount.get(time.split("-")[0]).get(depotid);
					return resInt != null ? resInt : 0;
				} catch (Exception e) {
					System.out.println("Utility method >>> catch "+e.getMessage());
					return 0;
				}
				 
			}
			
			
			//------to be deleted -----//
			public static int countAllSchOfDepot(Integer depotId, String strArrTime) {
				Integer count = 0;
				Integer counInt = 0;
				
					String []StrArr = strArrTime.split("-");
					try {
					 counInt = ConstantData.depotScheduleCount.get(StrArr[0]).get(depotId);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					
					count += counInt != null ?  counInt : 0;
			
				
				System.out.println("this is total count >>> "+count);
				return count;
			}
			
			public static boolean isNumber(String strNum) {
				
				try {
					Integer number = Integer.parseInt(strNum);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				
			}
	
			
			
			
			
			
}
