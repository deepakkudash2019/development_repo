package com.ms.WebPlatform.model;

import java.util.ArrayList;

public class Temp_Response {

		  private String search;
		  private int pageNo;
		  private int rowCount;
		  private String startDate;
		  private String endDate;
		  //private float endDate;
		//  ArrayList<Object> sortFileds = new ArrayList<Object>();
		 // ArrayList<String> routeCodes = new ArrayList<String>();
		  ArrayList<String> vehicleNos = new ArrayList<String>();
		  ArrayList<String> depotCodes = new ArrayList<String>();
		  ArrayList<String> fromDepotCodes = new ArrayList<String>();
		  ArrayList<String> toDepotCodes = new ArrayList<String>();
		  private int is_export;
		  private String imei;
		  private String exportType;

		//----getterSetter------//
		  

			public Integer getIs_export() {
			return is_export;
		}
		public ArrayList<String> getToDepotCodes() {
				return toDepotCodes;
			}
			public void setToDepotCodes(ArrayList<String> toDepotCodes) {
				this.toDepotCodes = toDepotCodes;
			}
		public ArrayList<String> getFromDepotCodes() {
				return fromDepotCodes;
			}
			public void setFromDepotCodes(ArrayList<String> fromDepotCodes) {
				this.fromDepotCodes = fromDepotCodes;
			}
		public String getExportType() {
				return exportType;
			}
			public void setExportType(String exportType) {
				this.exportType = exportType;
			}
			public void setIs_export(int is_export) {
				this.is_export = is_export;
			}
		public String getImei() {
				return imei;
			}
			public void setImei(String imei) {
				this.imei = imei;
			}
		public void setIs_export(Integer is_export) {
			this.is_export = is_export;
		}
			public String getSearch() {
				return search;
			}
			public String getEndDate() {
				return endDate;
			}
			public void setEndDate(String endDate) {
				this.endDate = endDate;
			}
			public void setSearch(String search) {
				this.search = search;
			}
			public int getPageNo() {
				return pageNo;
			}
			public void setPageNo(int pageNo) {
				this.pageNo = pageNo;
			}
			public int getRowCount() {
				return rowCount;
			}
			public void setRowCount(int rowCount) {
				this.rowCount = rowCount;
			}
			public String getStartDate() {
				return startDate;
			}
			public void setStartDate(String startDate) {
				this.startDate = startDate;
			}
			public ArrayList<String> getVehicleNos() {
				return vehicleNos;
			}
			public void setVehicleNos(ArrayList<String> vehicleNos) {
				this.vehicleNos = vehicleNos;
			}
			public ArrayList<String> getDepotCodes() {
				return depotCodes;
			}
			public void setDepotCodes(ArrayList<String> depotCodes) {
				this.depotCodes = depotCodes;
			}

		  

		
}
