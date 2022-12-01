package com.example.samara_recyclerbin_map;

import com.yandex.mapkit.geometry.Point;

public class RecyclingPoint {

    private Point point;
    private String location;
    private String[] types;
    private String locationName;
    private String info;

    public RecyclingPoint(Point point, String location, String locationName, String info, String[] types){
        this.point = point;
        this.location = location;
        this.locationName = locationName;
        this.info = info;
        this.types = types;
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

    public String[] getTypes() {
        return types;
    }

    public double getLatitude(){
        return point.getLatitude();
    }

    public double getLongitude(){
        return point.getLongitude();
    }

    public String getLocationName() {
        return locationName;
    }
}
