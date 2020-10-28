 package com.ms.WebPlatform.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class CallApis {	
	                        //http://103.197.121.83/app/auth/login
							//{"user":{"userLogin":"Depot","userPassword":"26dc318942685872cf79c5eb96c9bb13"}}
	private static String visionRecommendation = "http://103.197.121.83/app/auth/login.json";
	//"https://msrtcvtspis.com/app/auth/login";
	//"http://192.168.1.13:5001/recommendMerchant";
	
	public  String getVisionRecommendation(String  dataJson) {
		
		String output = "";
		String result = "";
		String sizes = "";
		String urlCall = "";
		JSONObject jobj = null;
		
			urlCall = visionRecommendation;
		
		try {
			System.out.println("\n before calling vision api :::::  " + dataJson);
			URL url = new URL(urlCall);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//Map<String, List<String>> map = conn.getHeaderFields();
			String userCredentials = "username:password";
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			
			
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty ("Authorization", basicAuth);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(dataJson);
			wr.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			

			String sss = "";
			System.out.println("Output from Server .... \n");
			//System.out.println("this is header Feild --- >>> "+new Gson().toJson(map));
			while ((output = br.readLine()) != null) {
				sss += output;
			}
			result = "success";
			System.out.println("recomendation Json ::::: " + sss); 
			
			return sss;
		} catch (Exception e) {
			e.printStackTrace();
			result = "Fail";
			return "0";
		}
	
		
	}
	
	
	
	
		
		




		/*
		 public static void ConvertXml(String args) throws Exception {
		       
		        String xmlStr =args;//  "<message>HELLO!</message>";
		       
		        
		        HashMap<String, String> values = new HashMap<String, String>();
		        String xmlString = args;//"<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><kyc>123</kyc><address>test</address><resiFI>asds</resiFI></user>";
		        Document xml = convertStringToDocument(xmlString);
		        Node user = xml.getFirstChild();
		        NodeList childs = user.getChildNodes();
		        Node child;
		        for (int i = 0; i < childs.getLength(); i++) {
		            child = childs.item(i);
		            System.out.println(child.getNodeName());
		            System.out.println(child.getTextContent());
		            values.put(child.getNodeName(), child.getTextContent());
		            
		            System.out.println("result -- >>> "+new Gson().toJson(values));
		        }
		       
		    }

		 private static Document convertStringToDocument(String xmlStr) {
			System.out.println("welcome ::::::::::::  ");
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder;
		        String s=xmlStr;
		       
		        try {
		        	System.out.println("welcome ::::::::::::  ");
		        	   String requiredString = s.substring(s.indexOf("<user>"), s.indexOf("</user>"));
				       

				        
		            builder = factory.newDocumentBuilder();
		            Document doc = builder.parse(new InputSource(new StringReader(
		            		requiredString)));
		            return doc;
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return null;
		    }

	*/
	
		
	
	
	
	
		 
		 
		 //------------------mobileApiMigration---------//
		 @Test
			public String callApiWithHeader(String strJson ) throws URISyntaxException, JsonParseException, JsonMappingException, IOException 
			{
			    RestTemplate restTemplate = new RestTemplate();
			     
			    final String baseUrl = "https://msrtcvtspis.com/app/travel/getVehicleStatusAtStop.json";
			    URI uri = new URI(baseUrl);
			 
			    String jsonString = strJson;
			    ObjectMapper mapper = new ObjectMapper();
				Map<String, String> map = new HashMap<String, String>();		
					map = mapper.readValue(jsonString, Map.class);	    
			    ResponseEntity<String> result = restTemplate.postForEntity(uri, map, String.class);
			  
			    
			    Map<String, String> map2 = new HashMap<String, String>();		
				map2 = mapper.readValue(result.getBody(), Map.class);
				
				System.out.println("result ==== >>> "+result.getBody());
			    
			   // callApiWithHeader2();
			     return result.getBody();
			}
	
	
	
		 @Test
			public  static GlobalResponse callRawDataApi(String apiStr ) throws URISyntaxException, JsonParseException, JsonMappingException, IOException 
			{
			    RestTemplate restTemplate = new RestTemplate();
			     
			    final String baseUrl = apiStr;//"https://msrtcvtspis.com/app/travel/getVehicleStatusAtStop.json";
			    URI uri = new URI(baseUrl);
			    GlobalResponse resp = restTemplate.getForObject(baseUrl, GlobalResponse.class);
			   // System.out.println("result -- >> "+new Gson().toJson(resp));
			    return resp;
			}
		 
		 
	
		 public static void main(String[] args) throws JsonParseException, JsonMappingException, URISyntaxException, IOException {
			 CallApis obj = new CallApis();
			// String jsonString = "{\"user\":{\"userLogin\":\"Simppi Karan\",\"userPassword\":\"26dc318942685872cf79c5eb96c9bb13\"}}";
			//String jsonString = "{\"user\":{\"userLogin\":\"Depot\",\"userPassword\":\"26dc318942685872cf79c5eb96c9bb13\"}}";
			 String jsonString = "{\"user\":{\"userLogin\":\"shubham\",\"userPassword\":\"26dc318942685872cf79c5eb96c9bb13\"}}";
			// String res =  obj.getVisionRecommendation(jsonString);
			 
			//obj.callApiWithHeader();
			
		}
	
	
	
	
	
	
	
	
	

}
