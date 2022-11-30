package com.example.samara_recyclerbin_map;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;


public class Main extends Activity implements UserLocationObjectListener {
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private MapView mapview;
    private final Point START_POINT = new Point(53.212228298365396, 50.17742481807416);
    private final Point TEST = new Point(53.212857,50.182195);
    private UserLocationLayer userLocationLayer;
    private Point clickedPoint;
    //private final String[] types = {"Paper", "Glass", "Plastic", "Metal", "Clothes", "Other", "Dangerous",
    //"Batteries", "Lamp", "Appliances", "Tetra", "Lid", "Tires"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(getString(R.string.API_KEY));
        MapKitFactory.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().setRotateGesturesEnabled(true);
        mapview.getMap().move(
                new CameraPosition(START_POINT, 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 3),
                null);

        requestLocationPermission();

        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault(); //хз что это - нашел в оф.примере на гите
        userLocationLayer = mapKit.createUserLocationLayer(mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        MapObjectCollection mapObjects = mapview.getMap().getMapObjects().addCollection();

        //просто два примера
        String[] chosenTypes1 = {"Plastic", "Metal", "Lid", "Other", "Lamp"}; //типы, что можно переработать
        Bitmap bitmap1 = drawMarker(chosenTypes1); //получаем битмап из функции
        PlacemarkMapObject markerTest = mapObjects.addPlacemark(TEST, ImageProvider.fromBitmap(bitmap1));
        //маркеру добавляем информацию
        markerTest.setUserData(new RecyclingPoint(TEST, "Русь", "У нас много мусорок", "Что-то"));
        //добавляем обработку нажатия на него. Потом через это можно будет сделать как на сайте
        markerTest.addTapListener(placeMarkTapListener);

        String[] chosenTypes2 = {"Tetra", "Batteries", "Clothes", "Glass", "Dangerous"};
        Bitmap bitmap2 = drawMarker(chosenTypes2);
        PlacemarkMapObject home = mapObjects.addPlacemark(START_POINT, ImageProvider.fromBitmap(bitmap2));
        home.setUserData(new RecyclingPoint(START_POINT, "Аэрокос", "У нас есть суперкомпьютер", "Что-то"));
        home.addTapListener(placeMarkTapListener);

    }

    //проверка нужных разрешений на геолокацию
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.ACCESS_COARSE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_COARSE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    private MapObjectTapListener placeMarkTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            //проверяем на то, маркер ли это(в апи есть и другие виды объектов на картах)
            if(mapObject instanceof PlacemarkMapObject){
                System.out.println("ОН ТЫКНУЛ");
                Object userData = mapObject.getUserData();

                //является ли userData объектом нашего класса
                if(userData instanceof RecyclingPoint){
                    clickedPoint = ((RecyclingPoint) userData).getPoint();
                    System.out.println("Выбранная точка " + clickedPoint.getLatitude() + " " + clickedPoint.getLongitude());
                    System.out.println("Информация о точке " + ((RecyclingPoint) userData).getInfo() + " "
                            + ((RecyclingPoint) userData).getType() + " " + ((RecyclingPoint) userData).getLocation());
                }
            }
            return true;
        }
    };

    @Override
    protected void onStop() {
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }

    private Bitmap drawMarker(@NonNull String[] chosenTypes){

        int length = chosenTypes.length;
        int picSize = 80;

        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float sweepAngel = (float)360 / length;
        float startAngel = 0;

        RectF pi = new RectF(0, 0, picSize, picSize);

        int color = 0;

        for (String chosenType : chosenTypes) {
            switch (chosenType) {
                case "Paper":
                    color = ContextCompat.getColor(this, R.color.paper_color);
                    break;
                case "Glass":
                    color = ContextCompat.getColor(this, R.color.glass_color);
                    break;
                case "Plastic":
                    color = ContextCompat.getColor(this, R.color.plastic_color);
                    break;
                case "Metal":
                    color = ContextCompat.getColor(this, R.color.metal_color);
                    break;
                case "Clothes":
                    color = ContextCompat.getColor(this, R.color.clothes_color);
                    break;
                case "Other":
                    color = ContextCompat.getColor(this, R.color.other_color);
                    break;
                case "Dangerous":
                    color = ContextCompat.getColor(this, R.color.dangerous_color);
                    break;
                case "Batteries":
                    color = ContextCompat.getColor(this, R.color.batteries_color);
                    break;
                case "Lamp":
                    color = ContextCompat.getColor(this, R.color.lamp_color);
                    break;
                case "Appliances":
                    color = ContextCompat.getColor(this, R.color.appliances_color);
                    break;
                case "Tetra":
                    color = ContextCompat.getColor(this, R.color.tetra_color);
                    break;
                case "Lid":
                    color = ContextCompat.getColor(this, R.color.lid_color);
                    break;
                case "Tires":
                    color = ContextCompat.getColor(this, R.color.tires_color);
                    break;
            }

            Paint paint = new Paint();

            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawArc(pi, startAngel, sweepAngel, true, paint);
            startAngel += sweepAngel;
        }

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.TRANSPARENT);
        circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        canvas.drawCircle((float)(picSize / 2), (float)(picSize / 2), (float)(picSize / 3), circlePaint);
        
        return bitmap;
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.5)),
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.search_result),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }
}
