package com.tec.dslunittests.models;

import java.io.Serializable;

public class Expected implements Serializable{//Value type
	
	private static final long serialVersionUID = 859999098267757690L;
	
	private String type, value;
	
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
