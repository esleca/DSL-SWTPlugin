package com.tec.dslunittests.models;

public class Parameter{ //JSON array, null si no hay
	
	
	private String name, type, value;
	
	public Parameter() {
		
	}
	
	public Parameter(String name, String type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public String getData() {
		return name + " " + type + " " + value;
	}


}
