package com.ms.WebPlatform.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.repository.DepotRepository;
import com.ms.WebPlatform.utility.ConstantData;



@Component
public class LoadCashe {
	@Autowired 
	private EntityManager em;
	@Autowired
	private DepotRepository depotRepo;
	
	
	public void setCashe() {
		ConstantData.depotScheduleCount = new LinkedHashMap<String, Map<Integer,Integer>>();
		
		if (ConstantData.AlldepotList == null || ConstantData.AlldepotList.size() == 0) {
			System.out.println("Initialising ><..>>>>>>>>>>>  ");
			for (Depot depObj : depotRepo.findAll()) {
				ConstantData.AlldepotList.add(depObj);					
		}			
	}
		
		
		
		for(int i=0;i< ConstantData.strArr.length ; i++) {
			String []strArr = ConstantData.strArr[i].split("-");
		      //String query = "SELECT count(*) FROM msrtc.msrtc_trip_schedule where depot_id = 181 and hour(sch_dep_time) >="+strArr[0]+" and hour(sch_dep_time) <"+strArr[1] +" and is_valid = 1";
			
		      String query = "SELECT depot_id,count(*) FROM msrtc.msrtc_trip_schedule where hour(sch_dep_time) >="+strArr[0]+ " and hour(sch_dep_time) <"+strArr[1]+" and is_valid = 1 group by depot_id";
		      
		      
		      Query q = em.createNativeQuery(query); 		      
		      List<Object> bulkData = q.getResultList();      
		      
		     
		      Map<Integer, Integer> totalMap = new HashMap<Integer, Integer>();
		      for(Object obj : bulkData) {
					String res = new Gson().toJson(obj);
					res = res.replace("[", "");
					res = res.replace("]", "");
					res = res.replace("\"", "");
					// total, Assigned_trip, depot_id, RoundTOHour, depot_code, depot_name, division_id
					
					String []StrArrData = res.split(",");
					
					totalMap.put(Integer.parseInt(StrArrData[0].trim()), Integer.parseInt(StrArrData[1].trim()));
					ConstantData.depotScheduleCount.put(strArr[0], totalMap);					
		      
		}	
		
		
	}
		
	
}
	
}
