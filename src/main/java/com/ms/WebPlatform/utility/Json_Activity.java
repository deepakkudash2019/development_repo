package com.ms.WebPlatform.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class Json_Activity {
	
	public static boolean createJsonFile(String jsonString,String fileName) {
	//String filePath = System.getProperty("user.dir")+"//json_activity//"+fileName+".json";
		String filePath = "json_activity/"+fileName+".json";
	 try (FileWriter file = new FileWriter(filePath)) {
		 
         file.write(jsonString);
         file.flush();

         return true;
     } catch (IOException e) {
         e.printStackTrace();
         return false;
     }
	
	}
	
	
	public static List<Map<String, Object>> readFromAJsonFile(String fileDt , Integer type){
		fileDt =type == 1 ? "uptime_"+fileDt : type == 3 ? "count_data_"+fileDt : "downtime_"+fileDt;
		//String filePath = System.getProperty("user.dir")+"//json_activity//"+fileDt+".json";
		String filePath = "json_activity/"+fileDt+".json";
		JSONParser parser = new JSONParser();
		List<Map<String, Object>> dummyData = new ArrayList<Map<String,Object>>();
		try {
			Object obj = parser.parse(new FileReader(filePath));
 
			List<Map<String, Object>> listmapObj = (List<Map<String, Object>>) obj;
		
			return listmapObj;
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (ParseException  e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dummyData;
		
	}
	
	
	
	public static List<Map<String, Object>> readFromAJsonFile_division_count(String fileDt , Integer id){
		fileDt = "count_data_"+id+"_"+fileDt;
		String filePath = "json_activity/"+fileDt+".json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(filePath));
 
			List<Map<String, Object>> listmapObj = (List<Map<String, Object>>) obj;
		
			return listmapObj;
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (ParseException  e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
		
	}
	
	
	
	
	

	public static void main(String[] args) {
	
		readFromAJsonFile("2020-02-27",1);
	}
}
