package com.ms.WebPlatform.model;

public class GraphData {
	
	private String division;
	private String divisionCode;
	private String depot;
	private String depotCode;
	private Integer total;
	private Integer assigned;
	private Integer unAssigned;
	
	//--------getterSetter-------------//
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDivisionCode() {
		return divisionCode;
	}
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	public String getDepot() {
		return depot;
	}
	public void setDepot(String depot) {
		this.depot = depot;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getAssigned() {
		return assigned;
	}
	public void setAssigned(Integer assigned) {
		this.assigned = assigned;
	}
	public Integer getUnAssigned() {
		return unAssigned;
	}
	public void setUnAssigned(Integer unAssigned) {
		this.unAssigned = unAssigned;
	}
	

}
