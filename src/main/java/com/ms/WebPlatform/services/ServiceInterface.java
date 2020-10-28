package com.ms.WebPlatform.services;

import java.util.List;
import java.util.Map;

import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MembershipGroup;
import com.ms.WebPlatform.model.MsrtcDivisionMaster;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.model.Vehicle;
import com.ms.WebPlatform.utility.GlobalResponse;

public interface ServiceInterface {
	
	Object getGraphDataForDepo(String strVal);

	Object getDepotGraphForManager(Depot object);
	
	String getUserAccessjson(String resultXml);

	GlobalResponse updtaeMembershipGroup(MembershipGroup mgrpObj);

	GlobalResponse saveVehicleActivity(Vehicle rvlObj);

	GlobalResponse getVehicleActivity(Temp_Response tempObj);

	GlobalResponse getmappUnmapped(Temp_Response tempObj);

	GlobalResponse getDevicePing(Temp_Response tempObj);

	void csvenerator();

	MsrtcDivisionMaster getDivisionById(Integer id);

	

	
}
