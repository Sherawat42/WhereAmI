package com.example.sherawat42.whereami;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by sherawat42 on 3/5/17.
 */

@Table(name="MyLocations")
public class MyLocation extends Model implements Serializable{

    @Column(name="latitude")
    public double latitude;

    @Column(name="longitude")
    public double longitude;

    @Column(name="type", index = true)
    public String type;

    @Column(name="name", index = true)
    public String name;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyLocation(){super();}

    MyLocation(String name,String type, double latitude, double longitude){
        super();
        this.type= type;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
