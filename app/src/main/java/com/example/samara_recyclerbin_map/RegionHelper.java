package com.example.samara_recyclerbin_map;

import androidx.annotation.NonNull;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.geo.Projection;
import com.yandex.mapkit.geometry.geo.Projections;
import com.yandex.mapkit.geometry.geo.XYPoint;
import com.yandex.mapkit.map.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

public class RegionHelper {
    public final float COMFORTABLE_ZOOM_LEVEL = 14f;

    private Point leftUpperCornerPoint;
    private Point leftLowerCornerPoint;
    private Point rightUpperCornerPoint;
    private Point rightLowerCornerPoint;

    //0 - если пользователь вышел за пределы, то просто пишет, что за пределами
    //1 - переводит камеру на край допустимой зоны
    private boolean typeOfRestriction;


    public RegionHelper(Point leftUpperCornerPoint, Point leftLowerCornerPoint, Point rightUpperCornerPoint, Point rightLowerCornerPoint, boolean typeOfRestriction){
        this.leftUpperCornerPoint = leftUpperCornerPoint;
        this.leftLowerCornerPoint = leftLowerCornerPoint;
        this.rightUpperCornerPoint = rightUpperCornerPoint;
        this.rightLowerCornerPoint = rightLowerCornerPoint;
        this.typeOfRestriction = typeOfRestriction;
    }

    //longitude = x
    //latitude = y
    public void isInRegion(@NonNull VisibleRegion currentRegion, float currentZoom, Point currentCameraPosition){
        boolean result = true;

        if(currentRegion.getBottomLeft().getLatitude() < leftLowerCornerPoint.getLatitude() ||
                currentRegion.getBottomLeft().getLongitude() < leftLowerCornerPoint.getLongitude() ||
                currentRegion.getBottomRight().getLongitude() > rightLowerCornerPoint.getLongitude() ||
                currentRegion.getTopLeft().getLatitude() > leftUpperCornerPoint.getLatitude()) result = false;
        System.out.println("МЫ В РЕГИОНЕ? " + result);
        System.out.println("МЫ ЩАС В ТУТ\n" + currentRegion.getTopLeft().getLatitude() + " " + currentRegion.getTopLeft().getLongitude() + "\n"
                + currentRegion.getTopRight().getLatitude() + " " + currentRegion.getTopRight().getLongitude() + "\n"
                + currentRegion.getBottomLeft().getLatitude() + " " + currentRegion.getBottomLeft().getLongitude() + "\n"
                + currentRegion.getBottomRight().getLatitude() + " " + currentRegion.getBottomRight().getLongitude());
        //System.out.println("ТОЧКА НАША " + currentCameraPosition.getLatitude() + " " + currentCameraPosition.getLongitude());
        //System.out.println("ЗУМ " + currentZoom);
        if(!result){
            if(typeOfRestriction){

            }
        }
    }

    private boolean isCameraInRegion(Point currentCameraPosition){
        boolean result = true;

        if(currentCameraPosition.getLongitude() > rightUpperCornerPoint.getLongitude()
                || currentCameraPosition.getLongitude() < leftUpperCornerPoint.getLongitude()
                || currentCameraPosition.getLatitude() > leftUpperCornerPoint.getLatitude()
                || currentCameraPosition.getLatitude() < leftLowerCornerPoint.getLatitude()) result = false;

        return result;
    }

}