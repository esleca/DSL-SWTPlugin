package com.tec.dslunittests.models;

public class UnitTestData {
	
	private String packageName, className, functionName, testName;
	private String[] parameters, expected;
	
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
	
	public String[] getParameters() {
		return this.parameters;
	}
	
	public String[] getExpected() {
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
	
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	
	public void setExpected(String[]  expected) {
		this.expected = expected;
	}
}
