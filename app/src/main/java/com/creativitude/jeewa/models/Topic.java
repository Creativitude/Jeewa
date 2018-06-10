package com.creativitude.jeewa.models;

/**
 * Created by naveen on 10/06/2018.
 */

public class Topic {

    private String id;
    private Double ht_lat;
    private Double ht_long;
    private String name;
    private String number;

    public Topic(String id, Double ht_lat, Double ht_long, String name, String number) {
        this.id = id;
        this.ht_lat = ht_lat;
        this.ht_long = ht_long;
        this.name = name;
        this.number = number;
    }

    public Topic() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getHt_lat() {
        return ht_lat;
    }

    public void setHt_lat(Double ht_lat) {
        this.ht_lat = ht_lat;
    }

    public Double getHt_long() {
        return ht_long;
    }

    public void setHt_long(Double ht_long) {
        this.ht_long = ht_long;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
