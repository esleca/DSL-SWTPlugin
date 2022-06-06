package com.tec.dslunittests.models;

public class Parameter {
	
	private String name, type, value;

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
