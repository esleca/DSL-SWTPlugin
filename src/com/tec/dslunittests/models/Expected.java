package com.tec.dslunittests.models;

public class Expected {//Value type
	
	
	private String type, value;
	
	public Expected() {
		this.type = " ";
		this.value = " ";
	}
	public Expected(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
