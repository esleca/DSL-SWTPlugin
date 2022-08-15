package com.tec.dslunittests.models;

import java.io.Serializable;

public class Parameter implements Serializable { //JSON array, null si no hay
	
	private static final long serialVersionUID = 87777798267757690L;
	
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
