package com.ms.WebPlatform.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class MsrtcTripSchedule {
	
	//,,, , is_valid

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer tripScheduleId;	
	private String scheduleNo;
	private Integer depotId;
	private Integer vehicleUtil;
	private Double  vehicleKms;
	private String routeName;
	private String schDepTime;
	private String schEndTime;
	private Integer isValid;
	
	//----------getterSetter-----------//
	public Integer getTripScheduleId() {
		return tripScheduleId;
	}
	public void setTripScheduleId(Integer tripScheduleId) {
		this.tripScheduleId = tripScheduleId;
	}
	public String getScheduleNo() {
		return scheduleNo;
	}
	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}
	public Integer getDepotId() {
		return depotId;
	}
	public void setDepotId(Integer depotId) {
		this.depotId = depotId;
	}
	public Integer getVehicleUtil() {
		return vehicleUtil;
	}
	public void setVehicleUtil(Integer vehicleUtil) {
		this.vehicleUtil = vehicleUtil;
	}
	public Double getVehicleKms() {
		return vehicleKms;
	}
	public void setVehicleKms(Double vehicleKms) {
		this.vehicleKms = vehicleKms;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getSchDepTime() {
		return schDepTime;
	}
	public void setSchDepTime(String schDepTime) {
		this.schDepTime = schDepTime;
	}
	public String getSchEndTime() {
		return schEndTime;
	}
	public void setSchEndTime(String schEndTime) {
		this.schEndTime = schEndTime;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	
	
	
	
	
	
	
	
	
}
