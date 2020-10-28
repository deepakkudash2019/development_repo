package com.ms.WebPlaatform.exceldata;

import io.swagger.models.auth.In;

public class DepotExcel {
	
	private Integer depotId;
	private String depotName;
	private Integer totalsch = 0;
	private Integer timeSch = 0;
	private Integer totalvehicleAssignment =0;
	private Integer totalCrew=0;
	private Integer crewAssignment=0;
	
	//--------------------getterSetter-----------------//
	public Integer getDepotId() {
		return depotId;
	}
	public Integer getTotalvehicleAssignment() {
		return totalvehicleAssignment;
	}
	public void setTotalvehicleAssignment(Integer totalvehicleAssignment) {
		this.totalvehicleAssignment = totalvehicleAssignment;
	}
	public void setDepotId(Integer depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public Integer getTotalsch() {
		return totalsch;
	}
	public void setTotalsch(Integer totalsch) {
		this.totalsch = totalsch;
	}
	public Integer getTimeSch() {
		return timeSch;
	}
	public void setTimeSch(Integer timeSch) {
		this.timeSch = timeSch;
	}
	/*
	public Integer getTotalAssignment() {
		return totalAssignment;
	}
	public void setTotalAssignment(Integer totalAssignment) {
		this.totalAssignment = totalAssignment;
	}*/
	
	public Integer getTotalCrew() {
		return totalCrew;
	}
	public void setTotalCrew(Integer totalCrew) {
		this.totalCrew = totalCrew;
	}
	public Integer getCrewAssignment() {
		return crewAssignment;
	}
	public void setCrewAssignment(Integer crewAssignment) {
		this.crewAssignment = crewAssignment;
	}
	
	

}
