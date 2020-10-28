package com.ms.WebPlatform.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ReportVehicleLocation {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer vehicleLocationId;
	private Integer vehicleId;
	private String vehicleRegNo;
	private String deviceImei;
	private Date eventDate;
	private String eventLat;
	private String eventLon;
	private String vehicleStatus;
	private String eventLocation;
	@Transient
	private String token;
	@Transient
	private Double kilometer;
	//----------getterSetter--------------//
	

	public Double getKilometer() {
		return kilometer;
	}
	public void setKilometer(Double kilometer) {
		this.kilometer = kilometer;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getVehicleLocationId() {
		return vehicleLocationId;
	}
	public void setVehicleLocationId(Integer vehicleLocationId) {
		this.vehicleLocationId = vehicleLocationId;
	}
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleRegNo() {
		return vehicleRegNo;
	}
	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}
	public String getDeviceImei() {
		return deviceImei;
	}
	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventLat() {
		return eventLat;
	}
	public void setEventLat(String eventLat) {
		this.eventLat = eventLat;
	}
	public String getEventLon() {
		return eventLon;
	}
	public void setEventLon(String eventLon) {
		this.eventLon = eventLon;
	}
	public String getVehicleStatus() {
		return vehicleStatus;
	}
	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}
	
	
	
	
}
