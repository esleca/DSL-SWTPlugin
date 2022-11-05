package com.tec.dslunittests.models;

public class Message {
    private String requestCode;
    private String request;
    private String listCode;

    public Message (String requestCode, String request, String listCode){
        this.requestCode = requestCode;
        this.request =  request;
        this.listCode = listCode;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getListCode() {
        return listCode;
    }

    public void setListCode(String listCode) {
        this.listCode = listCode;
    }
}