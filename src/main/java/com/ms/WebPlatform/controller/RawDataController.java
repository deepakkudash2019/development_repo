package com.ms.WebPlatform.controller;

import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ms.WebPlatform.database.MySQLWorldBankDbConnection;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MsrtcDeviceLog;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.model.UserAccessPrevillage;
import com.ms.WebPlatform.repository.UserAccessRepository;
import com.ms.WebPlatform.services.RawDataInterface;
import com.ms.WebPlatform.utility.CallApis;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.QueryClass;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/rosmerta-dev")
public class RawDataController {
  
	@Autowired
	private RawDataInterface rawDataInterface;
	
	@RequestMapping(value = "/api/data/readCsv/", method = RequestMethod.GET)
	public GlobalResponse readCsv() {
		return rawDataInterface.rawdataCsv();
	
	}
	
	@RequestMapping(value="/api/api/",method = RequestMethod.GET)
	public String testTest2() {
		//SpringNativQuery snq = new SpringNativQuery();
		rawDataInterface.secureQuery("select * from msrtc_device_log where device_imei = '867322030792550' limit 100");
		
		return "success";
		
	}
	
	
	
	@RequestMapping(value="/api/test/test/",method = RequestMethod.GET)
	public String testTest() {
		Connection conn2 = QueryClass.getConnection();
		 try {
			 Statement stmt = conn2.prepareStatement("select * from msrtc_device_log where device_imei = '867322030792550' limit 100");
			 ResultSet rs = stmt.executeQuery("select * from msrtc_device_log where device_imei = '867322030792550' limit 100");
			while(rs.next()) {
				System.out.println("loop --- >>>> ");
			}
		 } catch (SQLException e) {			
			e.printStackTrace();
		}
		 
		 return "success";
	}
	
	@RequestMapping(value = "api/data/lastPing/", method = RequestMethod.POST)
	public String lastPing(@RequestBody MsrtcDeviceLog msrtcDevLog) throws Exception {
		GlobalResponse data = new GlobalResponse();
		try {
			System.out.println("welcome --->>> "+new Gson().toJson(msrtcDevLog));
			//String result = CallApis.callRawDataApi("http://localhost:8015/api/rawdata/869247043471413/5/");
			Object object  = rawDataInterface.getLastPing(msrtcDevLog);
			
			if(object == null) {
				data.setCode(0);
				data.setData(object);
				System.out.println("device not found ---->>> ");
			}else {
				data.setCode(1);
				data.setData(object);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			data.setCode(-1);
			data.setMessage(e.getMessage()); 
		}
		return new Gson().toJson(data);
	}

	    @RequestMapping(value = "/api/test/prod/", method = RequestMethod.GET)
	   public Object test_prod() { 
		System.out.println("welcome --------->>>>>>>>>  ");
		java.net.URI location = null;
		 try {
			String query = "select * from msrtc_rawdata.msrtc_device_log limit 10";
			Connection connec = getConnection_rawData();
			Statement stmt = connec.createStatement();
			ResultSet rs2 = stmt.executeQuery(query);
			while(rs2.next()) {
				System.out.println("result=== "+rs2.getString("device_imei"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	     
	      return location;
	   }
	

	    
	    public static Connection getConnection_rawData() {		 
			//String hostIp = "192.191.190.10:3306/";		  
			 
				try {
					if(ConstantData.is_server == 1) {
					  Connection con=DriverManager.getConnection("jdbc:mysql://192.191.190.10:3306/msrtc_rawdata","msrtc_read_only","G@nG@P0in7");
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

	@RequestMapping(value = "api/socket/prod/{imei}/{count}/", method = RequestMethod.GET)
	public Object call_via_socket(@PathVariable("imei") String imei, @PathVariable("count") Integer count) {
		Socket s = null; 
		try {
			System.out.println(imei + "  " + count);
			String data = imei + "-" + count;
			 s = new Socket("103.197.121.83", 7777);
			s.getOutputStream().write(data.getBytes());
			StringBuffer sbf = new StringBuffer();
			if (s.getInputStream().available() > 0) {
				System.out.println("got Result proceed to extracting --- >>>> ");
			} else {

				int cnt = 0;
				while(s.getInputStream().available() < 1) {
					System.out.println("waiting ... ");
					Thread.sleep(1000);
					cnt++;
				}
			}
			while (s.getInputStream().available() > 0) {
				sbf.append(String.format("%02X", new Object[] { Integer.valueOf(s.getInputStream().read()) }));

			}

			s.getOutputStream().flush();
			s.close();
			return hexToAscii(sbf.toString());

		} catch (Exception e) {
			e.printStackTrace();
			

		}
		return "success";

	}
	
		public static void main(String[] args)throws Exception {
			/*
			 StringBuffer sbf = new StringBuffer();
			String data = "869247043471413-5";
			Socket s=new Socket("localhost",7777); 
			/
			 * 
			 * /DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			s.getOutputStream().write(data.getBytes());
			Thread.sleep(200000);
			while (s.getInputStream().available() > 0) {
				sbf.append(String.format("%02X", new Object[] { Integer.valueOf(s.getInputStream().read())}));
				
			}
			s.getOutputStream().flush();
			System.out.println("result -- >>> "+hexToAscii(sbf.toString()));*/
			
			hexToAscii("7B22636F6465223A312C22737461747573223A2274727565222C226D657373616765223A2273756363657373222C2264617461223A5B7B226D65737361676554696D65223A22323032302D30352D31302031383A32363A3232222C22726563656976656454696D65223A22323032302D30352D31302031393A30393A3335222C22696D6569223A22383639323437303433343731343133222C226C6F674964223A2231383133383831343236222C2272617744617461223A222452534D2C524131302C53572D56322E302C494E2C372C482C3836393234373034333437313431332C4D4831322D313233342C312C3130303532303230203132353632322C31392E32303137332C4E2C37322E39373332382C452C3030302E30302C302E302C32302C3031302E30342C302E302C302E302C566F6461666F6E65202D204D756D6261692C312C312C32352E392C342E322C302C432C33312C3430342C32302C373161392C666130632C373161392C383137302C2D35322C373161392C663332312C2D35322C373161392C383136662C2D36322C373161392C666130622C2D36322C302C302C302C302C302C302C3031383938302C39442C33323731392E362C30222C226D7367537461747573223A2248227D2C7B226D65737361676554696D65223A22323032302D30352D31302031383A33313A3331222C22726563656976656454696D65223A22323032302D30352D31302031393A30393A3330222C22696D6569223A22383639323437303433343731343133222C226C6F674964223A2231383133383831333938222C2272617744617461223A222452534D2C524131302C53572D56322E302C4E522C322C482C3836393234373034333437313431332C4D4831322D313233342C312C3130303532303230203133303133312C31392E32303838302C4E2C37322E39393734302C452C303036312E302C39392E332C32302C3031352E32302C312E332C302E302C566F6461666F6E65202D204D756D6261692C312C312C32372E302C342E322C302C432C31382C3430342C32302C373161662C623632392C373161662C636232632C2D37392C373161662C653861612C2D38352C373161662C383437312C2D38362C373161662C636232642C2D38382C302C302C302C302C302C302C3031383938312C45362C33323732332E362C30222C226D7367537461747573223A2248227D2C7B226D65737361676554696D65223A22323032302D30352D31302031383A33353A3238222C22726563656976656454696D65223A22323032302D30352D31302031393A30393A3235222C22696D6569223A22383639323437303433343731343133222C226C6F674964223A2231383133383831323734222C2272617744617461223A222452534D2C524131302C53572D56322E302C4E522C322C482C3836393234373034333437313431332C4D4831322D313233342C312C3130303532303230203133303532382C31392E32323138362C4E2C37332E30313730312C452C303036392E362C34352E302C31392C3031382E37302C312E332C302E302C4E4F204F50532C312C312C32362E392C342E322C302C432C31382C3430342C32372C3065332C656437652C3065332C336234612C2D37312C3065332C656437652C2D37382C302C302C302C302C302C302C302C302C302C302C302C302C3031383938352C42432C33323732362E322C30222C226D7367537461747573223A2248227D2C7B226D65737361676554696D65223A22323032302D30352D31302031383A33363A3238222C22726563656976656454696D65223A22323032302D30352D31302031393A30393A3138222C22696D6569223A22383639323437303433343731343133222C226C6F674964223A2231383133383831323439222C2272617744617461223A222452534D2C524131302C53572D56322E302C4E522C322C482C3836393234373034333437313431332C4D4831322D313233342C312C3130303532303230203133303632382C31392E32323738382C4E2C37332E30323437302C452C303035372E322C35352E382C31352C3031312E39362C312E332C302E302C4E4F204F50532C312C312C32372E302C342E322C302C432C31372C3430342C32372C3065332C656437652C3065332C336234612C2D37312C3065332C656437652C2D37382C302C302C302C302C302C302C302C302C302C302C302C302C3031383938362C42382C33323732372E332C30222C226D7367537461747573223A2248227D2C7B226D65737361676554696D65223A22323032302D30352D31302031383A34303A3330222C22726563656976656454696D65223A22323032302D30352D31302031393A30393A3039222C22696D6569223A22383639323437303433343731343133222C226C6F674964223A2231383133383831313534222C2272617744617461223A222452534D2C524131302C53572D56322E302C4E522C322C482C3836393234373034333437313431332C4D4831322D313233342C312C3130303532303230203133313033302C31392E32343734362C4E2C37332E30343935352C452C303034392E382C34392E322C31392C3032352E31322C312E332C302E302C496465612043656C6C756C6172204C74642C312C312C32362E392C342E322C302C432C32352C3430342C32322C346536342C353039372C346536342C316130362C2D37342C346536342C353063622C2D37362C346536342C346565312C2D37362C346536342C353039382C2D38322C302C302C302C302C302C302C3031383939302C39332C33323733302E372C30222C226D7367537461747573223A2248227D5D7D");
				
		}
		
		private static GlobalResponse hexToAscii(String hexStr) {
		    StringBuilder output = new StringBuilder("");
		     
		    for (int i = 0; i < hexStr.length(); i += 2) {
		        String str = hexStr.substring(i, i + 2);
		        output.append((char) Integer.parseInt(str, 16));
		    }
		    // System.out.println(output.toString());
		     
		     Gson g = new Gson();
		     GlobalResponse p = g.fromJson(output.toString(), GlobalResponse.class);
		     //System.out.println("result === >>> "+new Gson().toJson(p));
		    
		    return p;
		}
	
		
}

















