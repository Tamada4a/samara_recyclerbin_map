package com.example.samara_recyclerbin_map;

import com.yandex.mapkit.geometry.Point;

public class RecyclingPoint {

    private Point point;
    private String location;
    private String type;
    private String info;

    public RecyclingPoint(Point point, String location, String type, String info){
        this.point = point;
        this.location = location;
        this.type = type;
        this.info = info;
    }

    public Point getPoint() {
        return point;
    }

    public String getInfo() {
        return info;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public double getLatitude(){
        return point.getLatitude();
    }

    public double getLongitude(){
        return point.getLongitude();
    }
}
