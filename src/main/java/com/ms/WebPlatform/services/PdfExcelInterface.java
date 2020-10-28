package com.ms.WebPlatform.services;

import java.util.List;
import java.util.Map;

public interface PdfExcelInterface {
	
	String dailyReportExport(List<Map<String, Object>> mapList);

	String dailyReportCanceltripExport(List<Map<String, Object>> mapList);

	String dailyReportBreakDown(List<Map<String, Object>> mapListNew);
	
	String getExtraTripReport(List<Map<String, Object>> mapListNew);

	String getAllTripReport(List<Map<String, Object>> mapListNew);

	String uptimeDowntime(List<Map<String, Object>> mapListNew, String fileNm);

	String tranferVehicleReport(List<Map<String, Object>> mapList,String type);

	

	String mappunmapp(List<Map<String, Object>> mapList, String type);
	

}
