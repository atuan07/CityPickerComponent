package com.atuan.citypicker.model;

import java.io.Serializable;

public class CityListBean implements Serializable {

    /**
     * code : 110000
     * name : 北京
     * centerPoint : {"lat":39.908799,"lon":116.39741,"angle":0,"mapType":null}
     * pinyin : beijing
     */

    private String code;//城市code
    private String name;
    private String first;//首字母
    private Point centerPoint;//经纬度
    private String pinyin;//全拼

    private int locationIcon;//定位小图标

    public CityListBean() {
    }

    public CityListBean(String code, String name, String first, Point centerPoint, String pinyin) {
        this.code = code;
        this.name = name;
        this.first = first;
        this.centerPoint = centerPoint;
        this.pinyin = pinyin;
    }

    public CityListBean(String code, String name, String first, Point centerPoint, String pinyin, int locationIcon) {
        this.code = code;
        this.name = name;
        this.first = first;
        this.centerPoint = centerPoint;
        this.pinyin = pinyin;
        this.locationIcon = locationIcon;
    }

    public CityListBean(String code, String name, String first, Point centerPoint, String pinyin, int locationIcon, String SP_CITY_DATA) {
        this.code = code;
        this.name = name;
        this.first = first;
        this.centerPoint = centerPoint;
        this.pinyin = pinyin;
        this.locationIcon = locationIcon;
        this.SP_CITY_DATA = SP_CITY_DATA;
    }

    private String SP_CITY_DATA;//初始化传值用，城市列表数据SP存储KEY（需注意数据格式应用当前组件提供的Bean类型）

    public String getSP_CITY_DATA() {
        return SP_CITY_DATA;
    }

    public void setSP_CITY_DATA(String SP_CITY_DATA) {
        this.SP_CITY_DATA = SP_CITY_DATA;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getLocationIcon() {
        return locationIcon;
    }

    public void setLocationIcon(int locationIcon) {
        this.locationIcon = locationIcon;
    }

    @Override
    public String toString() {
        return "CityListBean{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", centerPoint=" + centerPoint.toString() +
                '}';
    }
}
