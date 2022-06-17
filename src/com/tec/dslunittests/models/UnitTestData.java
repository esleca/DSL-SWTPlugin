package com.tec.dslunittests.models;

import java.util.ArrayList;
import java.util.List;

public class UnitTestData {
	
	private String packageName, className, function, testName, assertion, classPath;
	private List<Parameter> parameters;
	private Expected expected;
	
	
	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

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
		return function;
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
		this.function = functionName;
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

	public String getAssertion() {
		return assertion;
	}

	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}
}
