package com.ms.WebPlatform.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MsrtcTripEntry {

	//  trip_end_date, actual_dep_time
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer trip_entry_id;
	private Date tripDate;
	private Integer tripId;
	private Integer vehicleId;
	private Date tripEndDate;	
	//-------getterSetter-------------//
	public Integer getTrip_entry_id() {
		return trip_entry_id;
	}
	public void setTrip_entry_id(Integer trip_entry_id) {
		this.trip_entry_id = trip_entry_id;
	}
	public Date getTripDate() {
		return tripDate;
	}
	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}
	public Integer getTripId() {
		return tripId;
	}
	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Date getTripEndDate() {
		return tripEndDate;
	}
	public void setTripEndDate(Date tripEndDate) {
		this.tripEndDate = tripEndDate;
	}
	
	
	
	
	
	
	
}
