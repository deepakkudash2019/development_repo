package com.ms.WebPlatform.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.json.simple.JSONObject;

@Entity
public class MembershipGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer groupId;
	private String groupName;
	private String groupDesc;
	private String groupPrivilege;
	private int isDynamic;
	@Transient
	private JSONObject groupPrivilegeObj;
	
	
	//----------------------------------------//

	public int getIsDynamic() {
		return isDynamic;
	}
	public void setIsDynamic(int isDynamic) {
		this.isDynamic = isDynamic;
	}
	public JSONObject getGroupPrivilegeObj() {
		return groupPrivilegeObj;
	}
	public void setGroupPrivilegeObj(JSONObject groupPrivilegeObj) {
		this.groupPrivilegeObj = groupPrivilegeObj;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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
	public String getGroupPrivilege() {
		return groupPrivilege;
	}
	public void setGroupPrivilege(String groupPrivilege) {
		this.groupPrivilege = groupPrivilege;
	}
	
	
	
	
	
	
}
