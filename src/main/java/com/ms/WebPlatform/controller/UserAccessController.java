package com.ms.WebPlatform.controller;

import java.net.URISyntaxException;

import javax.xml.ws.Response;

import org.apache.jasper.tagplugins.jstl.core.Url;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ms.WebPlatform.exception.CommonException;
import com.ms.WebPlatform.exception.ProductNotfoundException;
import com.ms.WebPlatform.model.MembershipGroup;
import com.ms.WebPlatform.model.MembershipUser;
import com.ms.WebPlatform.model.MembershipUserGroup;
import com.ms.WebPlatform.model.ReportVehicleLocation;
import com.ms.WebPlatform.model.UserAccessPrevillage;
import com.ms.WebPlatform.repository.MembershipGroupRepository;
import com.ms.WebPlatform.repository.MembershipUserGroupRepository;
import com.ms.WebPlatform.repository.MembershipUserRepository;
import com.ms.WebPlatform.repository.UserAccessRepository;
import com.ms.WebPlatform.services.RawDataInterface;
import com.ms.WebPlatform.services.ServiceInterface;
import com.ms.WebPlatform.utility.CallApis;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.DataContainer;
import com.ms.WebPlatform.utility.GlobalResponse;
import com.ms.WebPlatform.utility.HeaderResponse;
import com.ms.WebPlatform.utility.Utility;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/rosmerta-dev")
public class UserAccessController {
	
	@Autowired
	private UserAccessRepository userAccPre;
	@Autowired
	private MembershipUserGroupRepository memberUserGroup;
	@Autowired
	private MembershipGroupRepository memberGroupRepo;
	@Autowired
	private MembershipUserRepository membershipUserREpo;
	@Autowired
	private ServiceInterface service;
	@Autowired
	private RawDataInterface rawDataInterface;
	
	private JSONParser parser = new JSONParser();

	
	@RequestMapping(value = "/api/data/userAccess/", method = RequestMethod.GET)
	public String userAccess() {
		System.out.println("welcome ---->>> ");
		
		return new Gson().toJson(userAccPre.findAll());
		
	}
	
	//----saving access privilege for user
	
	@RequestMapping(value = "/api/data/saveUserAccess/", method = RequestMethod.POST)
	public Object SaveuserAccess(@RequestBody UserAccessPrevillage uap) {
		DataContainer data = new DataContainer();
		try {

			System.out.println(uap);
			if(uap == null || uap.getUserId() == null || uap.getPrivilegeJson() == null ) {
				data.setCode(0);
				data.setStatus("fail");
				data.setMessage("insufficient data");
				return data;
			}
			
			UserAccessPrevillage userPrivilge = userAccPre.findByuserId(uap.getUserId());
			if(userPrivilge != null) {
				userPrivilge.setPrivilegeJson(uap.getPrivilegeJson());
				userPrivilge.setGroupName(uap.getGroupName());
				userPrivilge.setUserPrivilegeStatus("Y");
				
				userPrivilge.setGroupDesc(uap.getGroupDesc() != null ? uap.getGroupDesc() : userPrivilge.getGroupDesc());
				userAccPre.save(userPrivilge);
			}else {
				userAccPre.save(uap);
			}
			data.setCode(1);
			data.setStatus("success");
			data.setMessage("saved successfully");
		} catch (Exception e) {
			data.setCode(-1);
			data.setStatus("fail");
			data.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return data;
	}

	
	@RequestMapping(value = "/api/data/getUserprivilege/{token}/{emailId}/", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserprivilege(@PathVariable("emailId") String emailId) {
		DataContainer dataContainer = new DataContainer();
		try {	
			
			if(Utility.isNumber(emailId)) {
				MembershipUserGroup obj = memberUserGroup.findByuserId(Integer.parseInt(emailId));
				MembershipGroup group = obj != null ?  memberGroupRepo.findOne(obj.getGroupId()) : null;
				JSONObject json = (JSONObject) parser.parse(group.getGroupPrivilege());
				
				System.out.println("jsonObject ---- >>>> "+json);
				group.setGroupPrivilegeObj(json);
				//group.setGroupPrivilege(null);
				dataContainer.setCode(group != null ? 1 : 0);
				dataContainer.setData(group);
				dataContainer.setStatus(group != null ? "success" : "fail");
				dataContainer.setMessage(group != null ? "success" : "data not found for this user");				
				
				return new ResponseEntity<Object>(dataContainer, HttpStatus.OK);
			}
			
		   MembershipUser membershipObj = membershipUserREpo.findByuserEmail(emailId);
		   if(membershipObj == null) {
			   dataContainer.setCode(0);
				dataContainer.setStatus("fail");
				dataContainer.setMessage("user not available");
				return new ResponseEntity<Object>(dataContainer, HttpStatus.OK);
		   }
			MembershipUserGroup obj = memberUserGroup.findByuserId(membershipObj.getUserId());
			MembershipGroup group = obj != null ?  memberGroupRepo.findOne(obj.getGroupId()) : null;
			dataContainer.setCode(group != null ? 1 : 0);
			dataContainer.setData(group);
			dataContainer.setStatus(group != null ? "success" : "fail");
			dataContainer.setMessage(group != null ? "success" : "data not found for this user");
		} catch (Exception e) {
			dataContainer.setCode(-1);
			dataContainer.setStatus("fail");
			dataContainer.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(dataContainer, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/api/data/newUserLogin/", method = RequestMethod.POST)
	public ResponseEntity<Object> newUserLogin(@RequestBody Object userObj) {
		String userLoginJsonString  = new Gson().toJson(userObj);
		System.out.println("welcome -->> "+userLoginJsonString);
		
		HeaderResponse headObj = new HeaderResponse();
	
		
		try {
			String resultJson = headObj.callApiWithHeader(userLoginJsonString, userLoginJsonString);
			if(resultJson.equalsIgnoreCase("0")) {
				String wrongCredintial =  "{\"requestId\":null,\"duration\":0,\"status\":\"FAILURE\",\"message\":\"Authentication failed. Username/password could be incorrect.\",\"count\":0,\"user\":null}";
				return new ResponseEntity<Object>(wrongCredintial, HttpStatus.UNAUTHORIZED);
			}
			String loginRes = service.getUserAccessjson(resultJson);
			
			 HttpHeaders headers = new HttpHeaders();
			  headers.add("XSRF-TOKEN", ConstantData.setHeader.get(userLoginJsonString));
			  headers.add(HttpHeaders.SERVER, "MSRTC");
			  //headers.add(HttpHeaders.SET_COOKIE, "kjsdhyr8743yrdfuhewiury398iwudh8437");
			 // headers.add(HttpHeaders.TRANSFER_ENCODING, "chunked");
			  
			  return ResponseEntity.ok().headers(headers).body(loginRes);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	
	@RequestMapping(value = "/api/data/getPrivilegeByName/{token}/{name}/", method = RequestMethod.GET)
	public ResponseEntity<Object> getPrivilegeByName(@PathVariable("token") String token,
			@PathVariable("name") String name) throws ParseException {

		DataContainer data = new DataContainer();
		MembershipGroup group = memberGroupRepo.findBygroupName(name);
		if (group != null) {
			JSONObject json = (JSONObject) parser.parse(group.getGroupPrivilege());
			data.setCode(1);
			data.setData(json);
			data.setMessage("success");
		}else {
			data.setCode(0);
			data.setData(null);
			data.setMessage("No data found");
		}
		return new ResponseEntity<Object>(data, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	   public ResponseEntity<Object> updateProduct(@PathVariable("id") Integer id) { 
		java.net.URI location = null;
		 try {
			location = new java.net.URI("https://www.google.com");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	      return new ResponseEntity<Object>(location, HttpStatus.OK);
	   }

	
	@RequestMapping(method = RequestMethod.GET, value = "api/user/")
	public String index() {
	    return "index.html";
	}
	
	
	//---------getVehicleFrom a range-------//
	
	@RequestMapping(value = "/api/data/getVehicleBylatlan/{km}/", method=RequestMethod.POST)
	public String getVehicleBylatlan (@PathVariable("km") Integer kilometer, @RequestBody ReportVehicleLocation rvlObj){
		GlobalResponse respObj = new GlobalResponse();
		Object obj = rawDataInterface.getVehicleInRange(rvlObj.getEventLat(), rvlObj.getEventLon(), kilometer);
		respObj.setData(obj);
		respObj.setCode(1);
		respObj.setMessage("success");
		
		return new Gson().toJson(respObj);
	}
	
	
	

	
	

}
