package com.ms.WebPlatform.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Depot {

	//--  depot_id, agency_id, depot_code, depot_name, depot_lat, depot_lon, depot_radius, depot_polygon, is_valid, division_id, devnagiri_name, kiran_name

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer depot_id;
	private String depot_name;
	private String depotCode;
	private Integer divisionId;
	@Transient
	private String token;
	@Transient
	private String division;
	//--------getterSetter-------------------//

	
	public String getDivision() {
		return division;
	}
	
	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public void setDivision(String division) {
		this.division = division;
	}
	public Integer getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public Integer getDepot_id() {
		return depot_id;
	}
	public void setDepot_id(Integer depot_id) {
		this.depot_id = depot_id;
	}
	public String getDepot_name() {
		return depot_name;
	}
	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}
	
	
	
	
	
}
