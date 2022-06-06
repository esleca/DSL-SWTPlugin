package com.tec.dslunittests.models;

import java.util.ArrayList;
import java.util.List;

public class UnitTestData {
	
	private String packageName, className, functionName, testName, assertType;
	private List<Parameter> parameters;
	private Expected expected;
	
	public UnitTestData() {
		parameters = new ArrayList<Parameter>();
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getFunctionName() {
		return functionName;
	}
	
	public String getTestName() {
		return testName;
	}
	
	public List<Parameter> getParameters() {
		return this.parameters;
	}
	
	public Expected getExpected() {
		return this.expected;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void setExpected(Expected  expected) {
		this.expected = expected;
	}

	public String getAssertType() {
		return assertType;
	}

	public void setAssertType(String assertType) {
		this.assertType = assertType;
	}
}
