package com.atuan.citypicker.model;


/**
 * 城市配置对象
 */
public class CityPickerConfig {

    private int errRetryCont = 3;  //错误重试次数

    private String baseUrl;  //请求地址配置的url

    private boolean debug = true;  //Debug true为显示log

    private Object cityPickerResp; //响应体配置类

    private String body; //请求参数

    public void setErrRetryCont(int errRetryCont) {
        this.errRetryCont = errRetryCont;
    }

    public int getErrRetryCont() {
        return errRetryCont;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Object getCityPickerResp() {
        return cityPickerResp;
    }

    public void setCityPickerResp(Object cityPickerResp) {
        this.cityPickerResp = cityPickerResp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
