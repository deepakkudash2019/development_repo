package com.ms.WebPlatform.services;

import java.sql.SQLException;

import com.ms.WebPlatform.model.Temp_Response;
import com.ms.WebPlatform.utility.GlobalResponse;

public interface UptimeReport {

	String downLoadUptimeReport();


	GlobalResponse getUptimeReport(Temp_Response request);


	String createDowntime();


	GlobalResponse getCancelTrip(Temp_Response request);


	GlobalResponse getExtraTrip(Temp_Response request);


	GlobalResponse getBreakDownReport(Temp_Response request);


	GlobalResponse getTripSchedule(Temp_Response request);


	String StoreUpDownCount();


	String StoreUpDownCount_divisionwise();


	String StoreData_depot();


	GlobalResponse getImeiLIst(Integer page, Integer rawCount) throws SQLException;
}
