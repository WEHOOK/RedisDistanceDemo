package com.example.demo.pojo;

public class NearbyBO {
    //用户id
    private Integer id;
    //用户名称
    private String name;
    //距离
    private Double distance;

    public NearbyBO(Integer id, String name, double distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
