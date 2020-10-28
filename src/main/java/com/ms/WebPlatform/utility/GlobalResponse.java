package com.ms.WebPlatform.utility;

import java.util.Map;

public class GlobalResponse {
	
	
	private String status=null;
	private Object data = null;
	private Integer code=null;
	private String message=null;
	private String response;
	private String token;
	private Integer count;
	private Object depot;
	private Object countData;
	
	
	//---------------getterSetter------------//
	

	public Object getCountData() {
		return countData;
	}
	public void setCountData(Object countData) {
		this.countData = countData;
	}
	public Object getDepot() {
		return depot;
	}
	public void setDepot(Object depot) {
		this.depot = depot;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	

}
