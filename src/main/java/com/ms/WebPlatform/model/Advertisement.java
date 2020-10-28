package com.ms.WebPlatform.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Advertisement {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer advId;
	private String type="image";
	private String fileStr;  // base64String 
	private Date advStart;
	private Date advExpire;
	private Date createddt=new Date();
	private String description;
	private String displayType="both";
	private String adUrl;
	private String status="Active";
	private Integer adFrequency;
	private Integer adDuration;
	private String adPosition;
	private String pisInfo;
	private String customerId;
	
	//-----------------getterSetter------------------//
	public Integer getAdvId() {
		return advId;
	}
	public Integer getAdFrequency() {
		return adFrequency;
	}
	public void setAdFrequency(Integer adFrequency) {
		this.adFrequency = adFrequency;
	}
	public Integer getAdDuration() {
		return adDuration;
	}
	public void setAdDuration(Integer adDuration) {
		this.adDuration = adDuration;
	}
	public void setAdvId(Integer advId) {
		this.advId = advId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFileStr() {
		return fileStr;
	}
	public void setFileStr(String fileStr) {
		this.fileStr = fileStr;
	}
	public Date getAdvStart() {
		return advStart;
	}
	public void setAdvStart(Date advStart) {
		this.advStart = advStart;
	}
	public Date getAdvExpire() {
		return advExpire;
	}
	public void setAdvExpire(Date advExpire) {
		this.advExpire = advExpire;
	}
	public Date getCreateddt() {
		return createddt;
	}
	public void setCreateddt(Date createddt) {
		this.createddt = createddt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getAdUrl() {
		return adUrl;
	}
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAdPosition() {
		return adPosition;
	}
	public void setAdPosition(String adPosition) {
		this.adPosition = adPosition;
	}
	public String getPisInfo() {
		return pisInfo;
	}
	public void setPisInfo(String pisInfo) {
		this.pisInfo = pisInfo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
	public String toString() {
		return "Advertisement [advId=" + advId + ", type=" + type + ", advStart=" + advStart + ", advExpire="
				+ advExpire + ", createddt=" + createddt + ", description=" + description + ", displayType="
				+ displayType + ", adUrl=" + adUrl + ", status=" + status + ", adFrequency=" + adFrequency
				+ ", adDuration=" + adDuration + ", adPosition=" + adPosition + ", pisInfo=" + pisInfo + ", customerId="
				+ customerId + "]";
	}
	
	
	
	
	
}
