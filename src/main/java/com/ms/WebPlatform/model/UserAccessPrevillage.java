package com.ms.WebPlatform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
public class UserAccessPrevillage {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userAccessId; //groupName  groupDesc
	@Column(name="role_name")
	private String groupName;
	@Column(name="role_desc")
	private String groupDesc;
	@Column(name="previllageJson")
	private String privilegeJson;
	private Integer userId;
	@Transient
	private String token;
	@Transient
	private String userPrivilegeStatus;		
	//--------------getterSetter----------------//
	

	public String getUserPrivilegeStatus() {
		return userPrivilegeStatus;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public void setUserPrivilegeStatus(String userPrivilegeStatus) {
		this.userPrivilegeStatus = userPrivilegeStatus;
	}
	

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getUserAccessId() {
		return userAccessId;
	}
	public void setUserAccessId(Integer userAccessId) {
		this.userAccessId = userAccessId;
	}


	public String getPrivilegeJson() {
		return privilegeJson;
	}
	public void setPrivilegeJson(String privilegeJson) {
		this.privilegeJson = privilegeJson;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
