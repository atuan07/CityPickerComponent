package com.atuan.citypicker.model;

import java.io.Serializable;
import java.util.List;

public class CityPicker implements Serializable{

    /**
     * firstLetter : A
     * cityList : [{"code":152900,"name":"阿拉善盟","centerPoint":{"lat":38.85153,"lon":105.72898,"angle":0,"mapType":null},"pinyin":"alashanmeng","fullLetter":"ALSM","mapType":null},{"code":210300,"name":"鞍山市","centerPoint":{"lat":41.10777,"lon":122.9946,"angle":0,"mapType":null},"pinyin":"anshanshi","fullLetter":"ASS","mapType":null},{"code":340800,"name":"安庆市","centerPoint":{"lat":30.54294,"lon":117.06354,"angle":0,"mapType":null},"pinyin":"anqingshi","fullLetter":"AQS","mapType":null},{"code":410500,"name":"安阳市","centerPoint":{"lat":36.09771,"lon":114.3931,"angle":0,"mapType":null},"pinyin":"anyangshi","fullLetter":"AYS","mapType":null},{"code":513200,"name":"阿坝藏族羌族自治州","centerPoint":{"lat":31.8994,"lon":102.22477,"angle":0,"mapType":null},"pinyin":"abazangzuqiangzuzizhizhou","fullLetter":"ABZZQZZZZ","mapType":null},{"code":520400,"name":"安顺市","centerPoint":{"lat":26.25367,"lon":105.9462,"angle":0,"mapType":null},"pinyin":"anshunshi","fullLetter":"ASS","mapType":null},{"code":542500,"name":"阿里地区","centerPoint":{"lat":30.40051,"lon":81.1454,"angle":0,"mapType":null},"pinyin":"alidiqu","fullLetter":"ALDQ","mapType":null},{"code":610900,"name":"安康市","centerPoint":{"lat":32.68486,"lon":109.02932,"angle":0,"mapType":null},"pinyin":"ankangshi","fullLetter":"AKS","mapType":null},{"code":652900,"name":"阿克苏地区","centerPoint":{"lat":41.16842,"lon":80.26008,"angle":0,"mapType":null},"pinyin":"akesudiqu","fullLetter":"AKSDQ","mapType":null},{"code":654300,"name":"阿勒泰地区","centerPoint":{"lat":47.84564,"lon":88.14023,"angle":0,"mapType":null},"pinyin":"aletaidiqu","fullLetter":"ALTDQ","mapType":null},{"code":659002,"name":"阿拉尔市","centerPoint":{"lat":40.54798,"lon":81.28067,"angle":0,"mapType":null},"pinyin":"alaershi","fullLetter":"ALES","mapType":null},{"code":820100,"name":"澳门特别行政区","centerPoint":{"lat":22.19875,"lon":113.54913,"angle":0,"mapType":null},"pinyin":"aomentebiexingzhengqu","fullLetter":"AMTBXZQ","mapType":null}]
     */

    private String firstLetter;
    private List<CityListBean> cityList;

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }
}
