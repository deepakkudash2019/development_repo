package com.ms.WebPlatform.services;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ms.WebPlatform.model.MsrtcDeviceLog;
import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.utility.DataContainer;
import com.ms.WebPlatform.utility.GlobalResponse;

public interface RawDataInterface {
	Object getLastPing(MsrtcDeviceLog deviceObj);

	GlobalResponse dailyReport(Temp_Response temp_resObj);

	Object getVehicleInRange(String lat, String lan, Integer km);

	Object getRoutData(Integer stopId);

	Connection getConnection();

	GlobalResponse rawdataCsv();

	List<Map<String, Object>> secureQuery(String query);
}
