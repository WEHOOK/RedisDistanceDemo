package com.example.demo.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NearbyPO {
    @NotNull(message = "id值不能为空")
    private Integer id;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotNull(message = "城市编码不能为空")
    private Integer cityCode;
    @NotNull(message = "地区编码不能为空")
    private Integer adCode;
    @NotNull(message = "经度不能为空")
    private Double longitude;
    @NotNull(message = "纬度不能为空")
    private Double latitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getAdCode() {
        return adCode;
    }

    public void setAdCode(Integer adCode) {
        this.adCode = adCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
