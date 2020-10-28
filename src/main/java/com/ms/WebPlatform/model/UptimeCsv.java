package com.ms.WebPlatform.model;

public class UptimeCsv {
	private String depot;
	private int total;
	private int upCount;
	private int downCount;
	private int dwsBreakMain;
	private int offRdTrnfScrap;
	private int parked;
	private int actualDown;
	private Double actualdwnPercent=0.0;
	private Double upPercent=0.0;
	//------------getterSetter------------------//
	public String getDepot() {
		return depot;
	}
	public void setDepot(String depot) {
		this.depot = depot;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getUpCount() {
		return upCount;
	}
	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}
	public int getDownCount() {
		return downCount;
	}
	public void setDownCount(int downCount) {
		this.downCount = downCount;
	}
	public int getDwsBreakMain() {
		return dwsBreakMain;
	}
	public void setDwsBreakMain(int dwsBreakMain) {
		this.dwsBreakMain = dwsBreakMain;
	}
	public int getOffRdTrnfScrap() {
		return offRdTrnfScrap;
	}
	public void setOffRdTrnfScrap(int offRdTrnfScrap) {
		this.offRdTrnfScrap = offRdTrnfScrap;
	}
	public int getParked() {
		return parked;
	}
	public void setParked(int parked) {
		this.parked = parked;
	}
	public int getActualDown() {
		return actualDown;
	}
	public void setActualDown(int actualDown) {
		this.actualDown = actualDown;
	}
	public Double getActualdwnPercent() {
		return actualdwnPercent;
	}
	public void setActualdwnPercent(Double actualdwnPercent) {
		this.actualdwnPercent = actualdwnPercent;
	}
	public Double getUpPercent() {
		return upPercent;
	}
	public void setUpPercent(Double upPercent) {
		this.upPercent = upPercent;
	}
	@Override
	public String toString() {
		return "UptimeCsv [depot=" + depot + ", total=" + total + ", upCount=" + upCount + ", downCount=" + downCount
				+ ", dwsBreakMain=" + dwsBreakMain + ", offRdTrnfScrap=" + offRdTrnfScrap + ", parked=" + parked
				+ ", actualDown=" + actualDown + ", actualdwnPercent=" + actualdwnPercent + ", upPercent=" + upPercent
				+ "]";
	}
	
	

}
