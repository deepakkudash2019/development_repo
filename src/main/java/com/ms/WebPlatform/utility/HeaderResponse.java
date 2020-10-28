package com.ms.WebPlatform.utility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


public class HeaderResponse {
	
	private String xstrToken = "";
	private String sessionToken = "";
	
	@Test
	public String callApiWithHeader( String jsonString,String tkn) throws URISyntaxException, JsonParseException, JsonMappingException, IOException 
	{
	    RestTemplate restTemplate = new RestTemplate();
	     
	    final String baseUrl = "http://127.0.0.1/app/auth/login.json";
	    URI uri = new URI(baseUrl);
	 
	    
	    ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();		
			map = mapper.readValue(jsonString, Map.class);	    
	    ResponseEntity<String> result = restTemplate.postForEntity(uri, map, String.class);
	    
	    System.out.println("check Header -->> "+result.getHeaders());
	    ConstantData.setHeader.put(tkn, result.getHeaders().getFirst("XSRF-TOKEN"));
	    xstrToken = result.getHeaders().getFirst("XSRF-TOKEN");
	    System.out.println("rsttemplete example ---- >>>> "+result.getHeaders().getFirst("XSRF-TOKEN")+"  "+result.getBody());
	    
	    
	    Map<String, String> map2 = new HashMap<String, String>();		
		map2 = mapper.readValue(result.getBody(), Map.class);
		
		Map<String, String> map3 = mapper.readValue(new Gson().toJson(map2.get("user")), Map.class);
	    
	   // sessionToken = map2.get("sessionToken");
	    System.out.println("session --- >> "+map3.get("sessionToken"));
	    sessionToken = map3.get("sessionToken");
	   // callApiWithHeader2();
	     return result.getBody();
	}
	
	@Test
	public String callApiWithHeader2() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		 final String uri = "http://103.197.121.83:18080/app/auth/getUserPreferences.json";
		   
	     System.out.println(">>>>>>>> "+xstrToken+"  "+sessionToken);
		 HttpHeaders headers = new HttpHeaders();
		  headers.add("XSRF-TOKEN", xstrToken);
		  headers.add(HttpHeaders.SERVER, "MSRTC");
		  headers.add("sessionToken", sessionToken);
		  headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
	    
		 
		 String dataSend = "{\"userLogin\":\"shubham\"}";
		     
		   
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    HttpEntity<String> entity = new HttpEntity<String>(dataSend, headers);
		     
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    System.out.println(result.getBody());
		 
		 
		    
		    
		    return "";
		
	}
	
	
	
	public static void main(String[] args) {
		HeaderResponse headerObj = new HeaderResponse();
		
		try {
			 String jsonString = "{\"user\":{\"userLogin\":\"shubham\",\"userPassword\":\"26dc318942685872cf79c5eb96c9bb13\"}}";
			headerObj.callApiWithHeader(jsonString,"909");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("catch block ---->>>"+e.getMessage());
		}
	}

}
