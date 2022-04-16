package com.tec.dslunittests.models;

public class UnitTestData {
	
	private String packageName, className, functionName, testName;
	
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

}
