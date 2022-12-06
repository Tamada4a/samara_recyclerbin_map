package com.example.samara_recyclerbin_map;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.VisibleRegion;
import com.yandex.mapkit.mapview.MapView;

//Данный класс используется для ограничения области пользователя до определенной зоны
public class RegionHelper {
    private float maxZoomLevel;
    private float comfortableZoom;

    private Point startPoint;
    private Point leftUpperCornerPoint;
    private Point leftLowerCornerPoint;
    private Point rightUpperCornerPoint;
    private Point rightLowerCornerPoint;

    //0 - если пользователь вышел за пределы, то просто пишет, что за пределами
    //1 - переводит камеру в стартовую допустимой зоны
    private boolean typeOfRestriction;

    private Main main;


    public RegionHelper(Point leftUpperCornerPoint, Point leftLowerCornerPoint, Point rightUpperCornerPoint, Point rightLowerCornerPoint, Point startPoint, float maxZoomLevel, float comfortableZoom, Main main, boolean typeOfRestriction){
        this.leftUpperCornerPoint = leftUpperCornerPoint;
        this.leftLowerCornerPoint = leftLowerCornerPoint;
        this.rightUpperCornerPoint = rightUpperCornerPoint;
        this.rightLowerCornerPoint = rightLowerCornerPoint;
        this.startPoint = startPoint;
        this.maxZoomLevel = maxZoomLevel;
        this.comfortableZoom = comfortableZoom;
        this.main = main;
        this.typeOfRestriction = typeOfRestriction;
    }


    public void isInRegion(@NonNull Point currentCameraPosition, float currentZoom, MapView mapview){
        boolean coordinateResult = true;
        boolean zoomResult = true;

        if(currentCameraPosition.getLongitude() > rightLowerCornerPoint.getLongitude() ||
        currentCameraPosition.getLongitude() < leftLowerCornerPoint.getLongitude() ||
        currentCameraPosition.getLatitude() > rightUpperCornerPoint.getLatitude() ||
        currentCameraPosition.getLatitude() < leftLowerCornerPoint.getLatitude()) coordinateResult = false;
        else if (currentZoom < maxZoomLevel) zoomResult = false;

        if(!coordinateResult || !zoomResult){
            if(typeOfRestriction){
                Point movePoint = currentCameraPosition;
                float zoom = currentZoom;
                if(!zoomResult)
                    zoom = comfortableZoom;
                if(!coordinateResult)
                    movePoint = startPoint;
                mapview.getMap().move(
                        new CameraPosition(movePoint, zoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0.5f),
                        null);
            }
            Toast.makeText(main, "Вы вышли за границы допустимого региона!", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isUserInRegion(@NonNull Point currentUserLocation){
        boolean result = true;
        if(currentUserLocation.getLongitude() > rightLowerCornerPoint.getLongitude() ||
                currentUserLocation.getLongitude() < leftLowerCornerPoint.getLongitude() ||
                currentUserLocation.getLatitude() > rightUpperCornerPoint.getLatitude() ||
                currentUserLocation.getLatitude() < leftLowerCornerPoint.getLatitude()) result = false;

        return result;
    }
}