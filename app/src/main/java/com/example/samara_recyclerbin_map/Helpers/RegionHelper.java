package com.example.samara_recyclerbin_map.Helpers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.samara_recyclerbin_map.Activities.Main;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
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

    private boolean typeOfRestriction;

    private Main main;

    /**
     *
     * @param leftUpperCornerPoint - левая верхняя точка границы допустимой зоны
     * @param leftLowerCornerPoint - левая нижняя точка границы допустимой зоны
     * @param rightUpperCornerPoint - правая верхняя точка границы допустимой зоны
     * @param rightLowerCornerPoint - правая нижняя точка границы допустимой зоны
     * @param startPoint - точка, куда, в случае выхода за границу, будет передвигаться камера
     * @param maxZoomLevel - максиамльный допустимый уровень зума
     * @param comfortableZoom - уровень зума, на который, в случае превышения, опустится камера
     * @param main - объект класса Main
     * @param typeOfRestriction:
     *                         0 - если пользователь вышел за пределы, то просто пишет, что пользователь вышел за границы
     *                         1 - переводит камеру в startPoint
     */
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

    /**
     *
     * @param currentCameraPosition - точка, где в данный момент находится камера
     * @param currentZoom - текущий зум
     * @param mapview - наш mapview. Изменение положения камеры происходит через этот метод
     */
    public void isInRegion(@NonNull Point currentCameraPosition, float currentZoom, MapView mapview){
        boolean coordinateResult = true;
        boolean zoomResult = true;

        if(!isUserInRegion(currentCameraPosition)) coordinateResult = false;
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
            Toast.makeText(main, "Вы вышли за границы допустимого региона", Toast.LENGTH_SHORT).show();
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