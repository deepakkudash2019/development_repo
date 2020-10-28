package com.ms.WebPlatform.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.ms.WebPlaatform.exceldata.DepotExcel;

@Entity
public class MsrtcDivisionMaster {

	//  division_id, division_code, division_name, division_devnagiri_name, region_id
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer divisionId;
	private String divisionCode;
	private String divisionName;
	@Transient
	private List<DepotExcel> depotExcel;
	@Transient
	private List<Depot> depots;
	
	//----getterSetter---------------//
	public Integer getDivisionId() {
		return divisionId;
	}
	public List<DepotExcel> getDepotExcel() {
		return depotExcel;
	}
	public void setDepotExcel(List<DepotExcel> depotExcel) {
		this.depotExcel = depotExcel;
	}
	public List<Depot> getDepots() {
		return depots;
	}
	public void setDepots(List<Depot> depots) {
		this.depots = depots;
	}
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}
	public String getDivisionCode() {
		return divisionCode;
	}
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	
}
