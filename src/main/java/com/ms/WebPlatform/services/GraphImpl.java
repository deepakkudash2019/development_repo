package com.ms.WebPlatform.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.GraphData;
import com.ms.WebPlatform.model.MsrtcDivisionMaster;
import com.ms.WebPlatform.repository.DepotRepository;
import com.ms.WebPlatform.repository.DivisionRepository;
import com.ms.WebPlatform.utility.ConstantData;
import com.ms.WebPlatform.utility.Utility;

@Service
public class GraphImpl implements GraphInterface {
	@Autowired
	private EntityManager em;
	@Autowired
	private DivisionRepository divisionRepo;
	@Autowired
	private DepotRepository depoRepo;

	List<Map<String, Object>> mapList = null;
	private static Map<String, Set<GraphData>> graphMap = null;
	private static List<Map<String, String>> graphMap2 = null;

	@Override
	public Object getGraphData(String value) {

		List<Object> authors = ExecuteQuery();
		graphMap = new LinkedHashMap<String, Set<GraphData>>();

		initialiseMap2();

		for (Object obj : authors) {
			String res = new Gson().toJson(obj);
			res = res.replace("[", "");
			res = res.replace("]", "");
			res = res.replace("\"", "");
			// total, Assigned_trip, depot_id, RoundTOHour, depot_code, depot_name,
			// division_id

			String[] StrArr = res.split(",");
			System.out.println(res);

			modifyGraphData2(graphMap2, StrArr);
		}

		return modified_Devisiondata_OnTime(graphMap2);

	}

	private static void initialiseMap() {
		String[] strArr = ConstantData.strArr2;// {"0-1","1-2","2-3","3-4","4-5","5-6","6-7","7-8","8-9","9-10","10-11","11-12","12-13","13-14","14-15","15-16","16-17","17-18","18-19","19-20","20-21","21-22","22-23","23-0"};

		for (int i = 0; i < strArr.length; i++) {
			graphMap.put(strArr[i], getGraphSet(0, 0, 0));
		}

	}

	private static Set<GraphData> getGraphSet(Integer total, Integer id, Integer assigned) {
		Set<GraphData> setGrp = new HashSet<GraphData>();
		for (MsrtcDivisionMaster division : ConstantData.divListStaticMap.values()) {
			GraphData gdObj = new GraphData();

			gdObj.setDivision(division.getDivisionName());
			gdObj.setDivisionCode(division.getDivisionCode());
			gdObj.setTotal(0);
			gdObj.setAssigned(0);
			gdObj.setUnAssigned(0);

			if (id > 0 && ConstantData.divListStaticMap.get(id).getDivisionCode()
					.equalsIgnoreCase(division.getDivisionCode())) {
				gdObj.setTotal(total);
				gdObj.setAssigned(assigned);
				gdObj.setUnAssigned(total - assigned);
			}
			setGrp.add(gdObj);
		}

		return setGrp;
	}

	private static void modifyGraphData(Map<String, Set<GraphData>> graphMapTobeModifi, String[] dataArr) {
		Integer hour = Integer.parseInt(dataArr[3]);
		String key = ConstantData.strArr2[hour];
		System.out.println("Modifying graph >>>> " + key);
		for (GraphData gp : graphMapTobeModifi.get(key)) {
			Integer devisionId = Integer.parseInt(dataArr[6]);
			String divisionCode = ConstantData.divListStaticMap.get(devisionId).getDivisionCode();
			if (gp.getDivisionCode().equalsIgnoreCase(divisionCode)) {

				Integer totalCount = getTotalBydivision(devisionId, ConstantData.strArr[hour]);
				gp.setTotal(totalCount);
				gp.setAssigned(Integer.parseInt(dataArr[1]));
				gp.setUnAssigned(gp.getTotal() - gp.getAssigned());
			}
		}
	}

	private static void modifyGraphData2(List<Map<String, String>> graphMap_data, String[] dataArr) {
		Integer hour = Integer.parseInt(dataArr[3]);
		String key = ConstantData.strArr2[hour];
		Integer devisionId = Integer.parseInt(dataArr[6]);
		Integer total = getTotalBydivision(devisionId, ConstantData.strArr[hour]);
		Integer assigned_trip = Integer.parseInt(dataArr[1]);
		Integer unassigned_trip = (total - assigned_trip);

		for (Map<String, String> mapType : graphMap_data) {

			if (mapType.get("timing") != null && mapType.get("timing").equalsIgnoreCase(key)) {

				MsrtcDivisionMaster mdm = ConstantData.divListStaticMap.get(devisionId);
				mapType.put(mdm.getDivisionName() + "_unAssigned", String.valueOf(unassigned_trip));
				mapType.put(mdm.getDivisionName() + "_assigned", String.valueOf(assigned_trip));
				mapType.put(mdm.getDivisionName() + "_total", String.valueOf(total));
				mapType.put("Division_" + mdm.getDivisionName(), mdm.getDivisionName());
				break;
			}
		}

	}

	private static void initialiseMap2() {
		graphMap2 = new ArrayList<Map<String, String>>();
		Map<String, String> mapData = null;
		String[] strArr = ConstantData.strArr2;// {"0-1","1-2","2-3","3-4","4-5","5-6","6-7","7-8","8-9","9-10","10-11","11-12","12-13","13-14","14-15","15-16","16-17","17-18","18-19","19-20","20-21","21-22","22-23","23-0"};
		for (int i = 0; i < strArr.length; i++) {
			mapData = new LinkedHashMap<String, String>();
			mapData.put("timing", strArr[i]);

			for (MsrtcDivisionMaster mdm : ConstantData.divListStaticMap.values()) {
				String totalSchedule = getTotalBydivision(mdm.getDivisionId(), ConstantData.strArr[i]).toString();
				mapData.put(mdm.getDivisionName() + "_unAssigned", totalSchedule);
				mapData.put(mdm.getDivisionName() + "_assigned", "0");
				mapData.put("Division_" + mdm.getDivisionName(), mdm.getDivisionName());
				mapData.put(mdm.getDivisionName() + "_total", totalSchedule);
			}

			graphMap2.add(mapData);

		}
	}

	private static Integer getTotalBydivision(Integer divisionId, String strArrTimeee) {
		// DepotRepository dept = ctx.getBean(DepotRepository.class);
		Integer tptalCount = 0;
		System.out.println("this is total Division >>>>> " + divisionId + "  " + strArrTimeee);

		List<Depot> depotList = Utility.getDepotByDivision(divisionId);
		System.out.println("total Div Depot >>>> " + depotList.size());
		for (Depot deptObj : depotList) {
			Integer count = Utility.getDepotScheduleCount(strArrTimeee, deptObj.getDepot_id());
			tptalCount += count;
		}

		System.out.println("Result >>>>>> " + tptalCount);
		return tptalCount;
	}

	@Override
	public List<Object> ExecuteQuery() {

		String query = "SELECT count(*) total,SUM(CASE WHEN t2.vehicle_id >= 1 THEN 1 ELSE 0 END) Assigned_trip,t1.depot_id,HOUR(t1.sch_dep_time) as RoundTOHour,t3.depot_code,t3.depot_name,t3.division_id "
				+ " FROM msrtc_trip_schedule AS t1 "
				+ " INNER JOIN msrtc_trip_entry AS t2 ON t1.trip_schedule_id = t2.trip_id "
				+ " INNER JOIN depot AS t3 ON t1.depot_id = t3.depot_id " + " where t2.trip_date = curdate() "
				+ " group by t1.depot_id,HOUR(t1.sch_dep_time)";

		Query q = em.createNativeQuery(query);

		System.out.println("this is query ---->>> " + query);

		return q.getResultList();
	}

	@Override
	public List<Object> ExecuteQueryForDEpot(Integer depotId) {
		String query = "SELECT count(*) total,SUM(CASE WHEN t2.vehicle_id >= 1 THEN 1 ELSE 0 END) Assigned_trip,t1.depot_id,HOUR(t1.sch_dep_time) as RoundTOHour,t3.depot_code,t3.depot_name,t3.division_id  FROM msrtc_trip_schedule AS t1  INNER JOIN msrtc_trip_entry AS t2 ON t1.trip_schedule_id = t2.trip_id  INNER JOIN depot AS t3 ON t1.depot_id = t3.depot_id  where t2.trip_date = curdate() and"
				+ " t1.depot_id =" + depotId.toString() + " group by t1.depot_id,HOUR(t1.sch_dep_time)\r\n" + "";

		Query q = em.createNativeQuery(query);

		System.out.println("this is query ---->>> " + query);

		return q.getResultList();

	}

	private List<Map<String, String>> modified_Devisiondata_OnTime(List<Map<String, String>> graphMap22) {

		List<Map<String, String>> graphMapList = null;
		ConstantData.countMapList_forAll = new ArrayList<Map<String, String>>();
		try {
			DateTime dt = new DateTime();
			int hours = dt.getHourOfDay();
			graphMapList = new ArrayList<Map<String, String>>();
			for (Map<String, String> mapObj : graphMap22) {
				countTotal(mapObj); // -----for counting -------//
				String timeHour = mapObj.get("timing").split("-")[0].trim().split(":")[0].trim();

				if (Integer.parseInt(timeHour) == hours) {
					graphMapList.add(mapObj);
					break;
				} else {
					graphMapList.add(mapObj);
				}

			}

			System.out.println("this is count data >>>>>>>>  " + new Gson().toJson(ConstantData.countMapList_forAll));
			return graphMapList;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return graphMap22;
	}

	// ----------------Counting--------------------------//

	private void countTotal(Map<String, String> mapObj) {
		// Set<String> mapSet = mapObj.keySet();

		for (MsrtcDivisionMaster divObj : ConstantData.divListStaticMap.values()) {
			String key4Total = divObj.getDivisionName() + "_total";
			String key4Assigned = divObj.getDivisionName() + "_assigned";
			String key4UnAssigned = divObj.getDivisionName() + "_unAssigned";

			countEngine(divObj.getDivisionName(), mapObj.get(key4Assigned), mapObj.get(key4UnAssigned),mapObj.get(key4Total));

		}
		
		

	}

	private static void countEngine(String divsionName, String assigned, String unAssigned, String total) {

		int countLoop = 0;
		int checker = 0;

		for (Map<String, String> mapObj : ConstantData.countMapList_forAll) {
			if (mapObj.get("divisionNm").equalsIgnoreCase(divsionName)) {
				Integer assignedVal = Integer.parseInt(mapObj.get(divsionName+"_assigned")) + Integer.parseInt(assigned);
				Integer unAssignedVal = Integer.parseInt(mapObj.get(divsionName+"_unAssigned")) + Integer.parseInt(unAssigned);
				Integer totalVal = Integer.parseInt(mapObj.get(divsionName+"_total")) + Integer.parseInt(total);

				mapObj.put(divsionName+"_assigned", assignedVal.toString());
				mapObj.put(divsionName+"_unAssigned", unAssignedVal.toString());
				mapObj.put(divsionName+"_total", totalVal.toString());
				checker = 1;
				break;
			}

			countLoop++;
		}

		if (checker == 0) {

			Map<String, String> newMapObj = new HashMap<String, String>();

			newMapObj.put(divsionName+"_assigned", "0");
			newMapObj.put(divsionName+"_unAssigned", "0");
			newMapObj.put(divsionName+"_total", "0");
			newMapObj.put("divisionNm", divsionName);

			ConstantData.countMapList_forAll.add(newMapObj);

			countEngine(divsionName, assigned, unAssigned, total);  // recursiv calling ----

		}

	}

}
