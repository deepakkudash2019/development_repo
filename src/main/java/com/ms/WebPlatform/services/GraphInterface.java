package com.ms.WebPlatform.services;

import java.util.List;

public interface GraphInterface {
	
	Object getGraphData(String value);
	List<Object> ExecuteQuery();
	List<Object> ExecuteQueryForDEpot(Integer depotId);
	
	

}
