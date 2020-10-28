package com.ms.WebPlatform.model;

import java.util.Date;

public class Vehicle {
	
	private String vehicleRegNo;
	private String vehicleStatus;
	private Date createdDt;
	private String fromDepotCode;
	private String toDepotCode;
	
	
	public String getVehicleRegNo() {
		return vehicleRegNo;
	}
	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	public String getFromDepotCode() {
		return fromDepotCode;
	}
	public void setFromDepotCode(String fromDepotCode) {
		this.fromDepotCode = fromDepotCode;
	}
	public String getToDepotCode() {
		return toDepotCode;
	}
	public void setToDepotCode(String toDepotCode) {
		this.toDepotCode = toDepotCode;
	}
	
	

}
