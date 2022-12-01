package com.example.samara_recyclerbin_map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;


public class Main extends Activity implements UserLocationObjectListener, Session.RouteListener, DrivingSession.DrivingRouteListener{
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private PedestrianRouter pedestrianRouter;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private final Point START_POINT = new Point(53.212228298365396, 50.17742481807416);
    private final Point TEST = new Point(53.212857,50.182195);

    private MapView mapview;
    private MapObjectCollection mapObjects;
    private Point clickedPoint;

    private UserLocationLayer userLocationLayer;

    private List<PolylineMapObject> currentPath = new ArrayList<>();
    private String typeOfRoute = "";

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
        userLocationLayer = mapKit.createUserLocationLayer(mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        ImageButton add_button = findViewById(R.id.add_button);
        ImageButton removePath_button = findViewById(R.id.removePath_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userLocationLayer.cameraPosition() != null){
                    Point userPoint = userLocationLayer.cameraPosition().getTarget();
                    mapview.getMap().move(
                            new CameraPosition(userPoint, 15.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 1.2f),
                            null);
                }
                else
                    Toast.makeText(Main.this, "Проблемы с определением местоположения", Toast.LENGTH_LONG).show();
            }
        });

        removePath_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCurrentPath();
            }
        });

        mapObjects = mapview.getMap().getMapObjects().addCollection();

        //просто два примера
        String[] chosenTypes1 = {"Plastic", "Metal", "Lid", "Other", "Lamp"}; //типы, что можно переработать
        Bitmap bitmap1 = drawMarker(chosenTypes1); //получаем битмап из функции
        PlacemarkMapObject markerTest = mapObjects.addPlacemark(TEST, ImageProvider.fromBitmap(bitmap1));
        //маркеру добавляем информацию
        markerTest.setUserData(new RecyclingPoint(TEST, "Русь", "ТЦ Русь", "Что-то", chosenTypes1));
        //добавляем обработку нажатия на него. Потом через это можно будет сделать как на сайте
        markerTest.addTapListener(placeMarkTapListener);

        String[] chosenTypes2 = {"Tetra", "Batteries", "Clothes", "Glass", "Dangerous"};
        Bitmap bitmap2 = drawMarker(chosenTypes2);
        PlacemarkMapObject home = mapObjects.addPlacemark(START_POINT, ImageProvider.fromBitmap(bitmap2));
        home.setUserData(new RecyclingPoint(START_POINT, "Аэрокос", "СГАУ", "Что-то", chosenTypes2));
        home.addTapListener(placeMarkTapListener);

        String[] chosenTypes3 = {"Tetra", "Batteries", "Clothes", "Glass", "Dangerous"};
        Bitmap bitmap3 = drawMarker(chosenTypes2);
        PlacemarkMapObject home2 = mapObjects.addPlacemark(new Point(53.2108275862854, 50.178027993319), ImageProvider.fromBitmap(bitmap3));
        home2.setUserData(new RecyclingPoint(new Point(53.2108275862854, 50.178027993319), "Аэрокос", "СГАУ", "Что-то", chosenTypes2));
        home2.addTapListener(placeMarkTapListener);
    }

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
            if(mapObject instanceof PlacemarkMapObject){
                Object userData = mapObject.getUserData();

                if(userData instanceof RecyclingPoint){
                    RecyclingPoint data = (RecyclingPoint) userData;
                    clickedPoint = data.getPoint();
                    showPointInfo(data);
                }
            }
            return true;
        }
    };

    private void showPointInfo(RecyclingPoint data){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this)
                .setTitle(data.getLocationName())
                .setMessage(data.getInfo())
                .setPositiveButton("Показать маршрут", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(userLocationLayer.cameraPosition().getTarget() != null) {
                            showCreateRouteOptions(userLocationLayer.cameraPosition().getTarget());
                            dialogInterface.cancel();
                        }else{
                            Toast.makeText(Main.this, "Проверьте геолокацию", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialog.show();
    }

    private void showCreateRouteOptions(Point startPoint) {
        AlertDialog.Builder routerOptions = new AlertDialog.Builder(Main.this)
                .setTitle("Как вы хотите добраться?")
                .setPositiveButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setNeutralButton("На машине", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createDrivingRoute(startPoint);
                    }
                })
                .setNegativeButton("Пешком", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createPedestrianRoute(startPoint);
                    }
                });
        routerOptions.show();
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

    private void deleteCurrentPath(){
        if(currentPath.size() != 0){
            for (PolylineMapObject poly : currentPath)
                mapObjects.remove(poly);
            currentPath = new ArrayList<>();
        }else{
            Toast.makeText(this, "Вы не проложили путь!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void createDrivingRoute(Point start) {
        typeOfRoute = "drive";
        List<RequestPoint> points = new ArrayList<>();

        points.add(new RequestPoint(start, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(clickedPoint, RequestPointType.WAYPOINT, null));

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        drivingSession = drivingRouter.requestRoutes(points, new DrivingOptions(), new VehicleOptions(), this);
    }

    private void createPedestrianRoute(Point start) {
        typeOfRoute = "pedestrian";
        List<RequestPoint> points = new ArrayList<>();

        points.add(new RequestPoint(start, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(clickedPoint, RequestPointType.WAYPOINT, null));

        pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter();
        pedestrianRouter.requestRoutes(points, new TimeOptions(), this);
    }

    private void drawPath(Polyline geometry) {
        if(currentPath.size() != 0){
            for (PolylineMapObject poly : currentPath)
                mapObjects.remove(poly);
            currentPath = new ArrayList<>();
        }

        PolylineMapObject polylineMapObject = mapObjects.addPolyline(geometry);
        polylineMapObject.setStrokeColor(R.color.purple_200);

        currentPath.add(polylineMapObject);
    }

    @Override
    public void onMasstransitRoutes(@NonNull List<Route> list) {
        if (list.size() > 0) {
            for (Section section : list.get(0).getSections()) {
                drawPath(SubpolylineHelper.subpolyline(
                        list.get(0).getGeometry(), section.getGeometry()));
            }
        }
    }

    @Override
    public void onMasstransitRoutesError(@NonNull Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        if(currentPath.size() != 0){
            for (PolylineMapObject poly : currentPath) {
                mapObjects.remove(poly);
            }
            currentPath = new ArrayList<>();
        }
        if(list.size() > 0) {
            for (DrivingRoute route : list) {
                PolylineMapObject polylineMapObject = mapObjects.addPolyline(route.getGeometry());
                currentPath.add(polylineMapObject);
            }
        }
        else {
            Toast.makeText(this, "Невозможно добраться на машине", Toast.LENGTH_SHORT).show();
            System.out.println("Невозможно добраться на машине");
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        /*userLocationLayer.setAnchor(
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.5)),
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.83)));*/

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this, R.drawable.navigation_marker));


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
        if (userLocationLayer.cameraPosition().getTarget() != null && currentPath.size() > 0) {
            Point userPoint = userLocationLayer.cameraPosition().getTarget();
            if (typeOfRoute.equals("drive")) {
                createDrivingRoute(userPoint);
            }else if(typeOfRoute.equals("pedestrian")){
                createPedestrianRoute(userPoint);
            }
        }
    }
}
