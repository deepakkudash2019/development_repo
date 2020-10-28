package com.ms.WebPlatform.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Stop {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer stopId;
	private String stopCode;
	private String stopName;
	private String stopLat;
	private String stopLon;
	@Transient
	private Double kilometer;
	
	//---------getterSetter---------------//
	public Integer getStopId() {
		return stopId;
	}
	public void setStopId(Integer stopId) {
		this.stopId = stopId;
	}
	public String getStopCode() {
		return stopCode;
	}
	public void setStopCode(String stopCode) {
		this.stopCode = stopCode;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getStopLat() {
		return stopLat;
	}
	public void setStopLat(String stopLat) {
		this.stopLat = stopLat;
	}
	public String getStopLon() {
		return stopLon;
	}
	public void setStopLon(String stopLon) {
		this.stopLon = stopLon;
	}
	public Double getKilometer() {
		return kilometer;
	}
	public void setKilometer(Double kilometer) {
		this.kilometer = kilometer;
	}
	
	
	
	
	
	
}
