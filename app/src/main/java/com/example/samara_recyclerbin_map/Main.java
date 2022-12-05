package com.example.samara_recyclerbin_map;

import static com.example.samara_recyclerbin_map.MarkerDrawer.types;

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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
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
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
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
import java.util.Arrays;
import java.util.List;


public class Main extends AppCompatActivity implements UserLocationObjectListener, Session.RouteListener, DrivingSession.DrivingRouteListener{
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private final Point START_POINT = new Point(53.212228298365396, 50.17742481807416);
    private final Point leftUpperCornerPoint = new Point(53.4234,50.0015);
    private final Point leftLowerCornerPoint = new Point(53.0928, 50.0015);
    private final Point rightUpperCornerPoint = new Point(53.4234,50.3840);
    private final Point rightLowerCornerPoint = new Point(53.0928,50.3840);

    private PedestrianRouter pedestrianRouter;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private ArrayList<PlacemarkMapObject> listCustomMarkers = new ArrayList<PlacemarkMapObject>();
    private ArrayList<RecyclingPoint> listCustomPoints = new ArrayList<RecyclingPoint>();

    private MapView mapview;
    private MapObjectCollection mapObjects;
    private Point clickedPoint;
    private PlacemarkMapObject destination;
    private PlacemarkMapObject customMarker;

    private UserLocationLayer userLocationLayer;

    private List<PolylineMapObject> currentPath = new ArrayList<>();

    private MarkerDrawer markerDrawer;
    private RegionHelper regionHelper;

    private ImageButton ok_button;
    private ImageButton cancel_button;
    private ImageButton papers_menu_button;
    private ImageButton papers_create_button;
    private ImageButton glass_menu_button;
    private ImageButton glass_create_button;
    private ImageButton plastic_menu_button;
    private ImageButton plastic_create_button;
    private ImageButton metal_menu_button;
    private ImageButton metal_create_button;
    private ImageButton cloths_menu_button;
    private ImageButton cloths_create_button;
    private ImageButton other_menu_button;
    private ImageButton other_create_button;
    private ImageButton dangerous_menu_button;
    private ImageButton dangerous_create_button;
    private ImageButton batteries_menu_button;
    private ImageButton batteries_create_button;
    private ImageButton lamp_menu_button;
    private ImageButton lamp_create_button;
    private ImageButton appliances_menu_button;
    private ImageButton appliances_create_button;
    private ImageButton tetra_menu_button;
    private ImageButton tetra_create_button;
    private ImageButton lid_menu_button;
    private ImageButton lid_create_button;
    private ImageButton tires_menu_button;
    private ImageButton tires_create_button;
    private ImageButton pointer;
    private ImageButton removePath_button;
    private ImageButton menu_button;
    private ImageButton addCustomPoint_button;

    private DrawerLayout drawerLayout;
    private NavigationView sideMenu;
    private View sideMenuHeader;
    //private View createPointView;

    private boolean[] checked = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private boolean[] checked2 = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private boolean isCustomPoint = false; //нужна для проверки в tapListener'e карты
    private boolean isCreatingWithCustomPoint = false; //по ней удаляю маршрут, есл построен через кастомный маркер
    private boolean isCreatingRecyclePonit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(getString(R.string.API_KEY));
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);
        //createPointView = LayoutInflater.from(Main.this).inflate(R.layout.create_point, null);
        super.onCreate(savedInstanceState);
        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().setRotateGesturesEnabled(true);
        mapview.getMap().move(
                new CameraPosition(START_POINT, 13.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 3),
                null);

        mapview.getMap().addInputListener(mapTapListener);
        mapview.getMap().addCameraListener(cameraListener);

        requestLocationPermission();

        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        mapObjects = mapview.getMap().getMapObjects().addCollection();

        markerDrawer = new MarkerDrawer(this, mapObjects, placeMarkTapListener);
        regionHelper = new RegionHelper(leftUpperCornerPoint, leftLowerCornerPoint, rightUpperCornerPoint, rightLowerCornerPoint, true);

        mapObjects = markerDrawer.drawDefaultMarkers();

        pointer = findViewById(R.id.pointer);
        removePath_button = findViewById(R.id.removePath_button);
        removePath_button.setVisibility(View.GONE); //кнопку "удалить маршрут" сначала не видно
        menu_button = findViewById(R.id.menu_button);
        sideMenu = findViewById(R.id.nav_view);
        addCustomPoint_button = findViewById(R.id.addPoint_button);
        cancel_button = findViewById(R.id.cancelCreatePoint_button);
        cancel_button.setVisibility(View.GONE);
        ok_button = findViewById(R.id.ok_button);
        ok_button.setVisibility(View.GONE);//изначально кнопку OK не видно
        sideMenuHeader = sideMenu.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawerLayout);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        /*papers_create_button = createPointView.findViewById(R.id.papers_menu_button);
        glass_create_button = createPointView.findViewById(R.id.glass_menu_button);
        plastic_create_button = createPointView.findViewById(R.id.plastic_menu_button);
        metal_create_button = createPointView.findViewById(R.id.metal_menu_button);
        cloths_create_button = createPointView.findViewById(R.id.cloths_menu_button);
        other_create_button = createPointView.findViewById(R.id.other_menu_button);
        dangerous_create_button = createPointView.findViewById(R.id.dangerous_menu_button);
        batteries_create_button = createPointView.findViewById(R.id.batteries_menu_button);
        lamp_create_button = createPointView.findViewById(R.id.lamp_menu_button);
        appliances_create_button = createPointView.findViewById(R.id.appliances_menu_button);
        tetra_create_button = createPointView.findViewById(R.id.tetra_menu_button);
        lid_create_button = createPointView.findViewById(R.id.lid_menu_button);
        tires_create_button = createPointView.findViewById(R.id.tires_menu_button);*/

        papers_menu_button = sideMenuHeader.findViewById(R.id.papers_menu_button);
        glass_menu_button = sideMenuHeader.findViewById(R.id.glass_menu_button);
        plastic_menu_button = sideMenuHeader.findViewById(R.id.plastic_menu_button);
        metal_menu_button = sideMenuHeader.findViewById(R.id.metal_menu_button);
        cloths_menu_button = sideMenuHeader.findViewById(R.id.cloths_menu_button);
        other_menu_button = sideMenuHeader.findViewById(R.id.other_menu_button);
        dangerous_menu_button = sideMenuHeader.findViewById(R.id.dangerous_menu_button);
        batteries_menu_button = sideMenuHeader.findViewById(R.id.batteries_menu_button);
        lamp_menu_button = sideMenuHeader.findViewById(R.id.lamp_menu_button);
        appliances_menu_button = sideMenuHeader.findViewById(R.id.appliances_menu_button);
        tetra_menu_button = sideMenuHeader.findViewById(R.id.tetra_menu_button);
        lid_menu_button = sideMenuHeader.findViewById(R.id.lid_menu_button);
        tires_menu_button = sideMenuHeader.findViewById(R.id.tires_menu_button);

        pointer.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Main.this, "У вас выключен GPS", Toast.LENGTH_LONG).show();
            }
        });

        removePath_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCurrentPath();
                removePath_button.setVisibility(View.GONE); //когда удаляем маршрут кнопка пропадает
                addCustomPoint_button.setVisibility(View.VISIBLE);
            }
        });

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        papers_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[0]){
                    checked[0] = true;
                    papers_menu_button.setBackgroundResource(R.drawable.papers_selected);

                }else if(checked[0]){
                    checked[0] = false;
                    papers_menu_button.setBackgroundResource(R.drawable.papers);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });


        glass_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[1]){
                    checked[1] = true;
                    glass_menu_button.setBackgroundResource(R.drawable.glass_selected);
                }else if(checked[1]){
                    checked[1] = false;
                    glass_menu_button.setBackgroundResource(R.drawable.glass);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });


        plastic_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[2]){
                    checked[2] = true;
                    plastic_menu_button.setBackgroundResource(R.drawable.plastic_selected);
                }else if(checked[2]){
                    checked[2] = false;
                    plastic_menu_button.setBackgroundResource(R.drawable.plastic);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        metal_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[3]){
                    checked[3] = true;
                    metal_menu_button.setBackgroundResource(R.drawable.metal_selected);
                }else if(checked[3]){
                    checked[3] = false;
                    metal_menu_button.setBackgroundResource(R.drawable.metal);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        cloths_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[4]){
                    checked[4] = true;
                    cloths_menu_button.setBackgroundResource(R.drawable.cloths_selected);
                }else if(checked[4]){
                    checked[4] = false;
                    cloths_menu_button.setBackgroundResource(R.drawable.cloths);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        other_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[5]){
                    checked[5] = true;
                    other_menu_button.setBackgroundResource(R.drawable.other_selected);
                }else if(checked[5]){
                    checked[5] = false;
                    other_menu_button.setBackgroundResource(R.drawable.other);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        dangerous_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[6]){
                    checked[6] = true;
                    dangerous_menu_button.setBackgroundResource(R.drawable.dangerous_selected);
                }else if(checked[6]){
                    checked[6] = false;
                    dangerous_menu_button.setBackgroundResource(R.drawable.dangerous);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        batteries_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[7]){
                    checked[7] = true;
                    batteries_menu_button.setBackgroundResource(R.drawable.batteries_selected);
                }else if(checked[7]){
                    checked[7] = false;
                    batteries_menu_button.setBackgroundResource(R.drawable.batteries);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        lamp_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[8]){
                    checked[8] = true;
                    lamp_menu_button.setBackgroundResource(R.drawable.lamp_selected);
                }else if(checked[8]){
                    checked[8] = false;
                    lamp_menu_button.setBackgroundResource(R.drawable.lamp);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        appliances_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[9]){
                    checked[9] = true;
                    appliances_menu_button.setBackgroundResource(R.drawable.appliances_selected);
                }else if(checked[9]){
                    checked[9] = false;
                    appliances_menu_button.setBackgroundResource(R.drawable.appliances);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        tetra_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[10]){
                    checked[10] = true;
                    tetra_menu_button.setBackgroundResource(R.drawable.tetra_selected);
                }else if(checked[10]){
                    checked[10] = false;
                    tetra_menu_button.setBackgroundResource(R.drawable.tetra);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        lid_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[11]){
                    checked[11] = true;
                    lid_menu_button.setBackgroundResource(R.drawable.lid_selected);
                }else if(checked[11]){
                    checked[11] = false;
                    lid_menu_button.setBackgroundResource(R.drawable.lid);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        tires_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checked[12]){
                    checked[12] = true;
                    tires_menu_button.setBackgroundResource(R.drawable.tires_selected);
                }else if(checked[12]){
                    checked[12] = false;
                    tires_menu_button.setBackgroundResource(R.drawable.tires);
                }
                markerDrawer.searchTypes(checked);
                searchCustomTypes(checked);
            }
        });



        ok_button.setOnClickListener(new View.OnClickListener() { //когда кликаем на ок строится маршрут
            @Override
            public void onClick(View view){
                if(!isCreatingRecyclePonit) {
                    isCustomPoint = false;
                    destination.setDraggable(false);
                    ok_button.setVisibility(View.GONE);//убираем кнопку
                    showCreateRouteOptions(destination.getGeometry());//строим маршрут по точке
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.AlertDialogCustom);
                    builder.setTitle("Новый пункт");

                    View createPointView = LayoutInflater.from(Main.this).inflate(R.layout.create_point, null);
                    final EditText name = (EditText) createPointView.findViewById(R.id.input_name);
                    final EditText info = (EditText) createPointView.findViewById(R.id.input_info);
                    final EditText location = (EditText) createPointView.findViewById(R.id.input_address);
                    papers_create_button = createPointView.findViewById(R.id.papers_menu_button);
                    glass_create_button = createPointView.findViewById(R.id.glass_menu_button);
                    plastic_create_button = createPointView.findViewById(R.id.plastic_menu_button);
                    metal_create_button = createPointView.findViewById(R.id.metal_menu_button);
                    cloths_create_button = createPointView.findViewById(R.id.cloths_menu_button);
                    other_create_button = createPointView.findViewById(R.id.other_menu_button);
                    dangerous_create_button = createPointView.findViewById(R.id.dangerous_menu_button);
                    batteries_create_button = createPointView.findViewById(R.id.batteries_menu_button);
                    lamp_create_button = createPointView.findViewById(R.id.lamp_menu_button);
                    appliances_create_button = createPointView.findViewById(R.id.appliances_menu_button);
                    tetra_create_button = createPointView.findViewById(R.id.tetra_menu_button);
                    lid_create_button = createPointView.findViewById(R.id.lid_menu_button);
                    tires_create_button = createPointView.findViewById(R.id.tires_menu_button);

                    papers_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[0]){
                                checked2[0] = true;
                                papers_create_button.setBackgroundResource(R.drawable.papers_selected);

                            }else if(checked2[0]){
                                checked2[0] = false;
                                papers_create_button.setBackgroundResource(R.drawable.papers);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    glass_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[1]){
                                checked2[1] = true;
                                glass_create_button.setBackgroundResource(R.drawable.glass_selected);
                            }else if(checked2[1]){
                                checked2[1] = false;
                                glass_create_button.setBackgroundResource(R.drawable.glass);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    plastic_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[2]){
                                checked2[2] = true;
                                plastic_create_button.setBackgroundResource(R.drawable.plastic_selected);
                            }else if(checked2[2]){
                                checked2[2] = false;
                                plastic_create_button.setBackgroundResource(R.drawable.plastic);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    metal_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[3]){
                                checked2[3] = true;
                                metal_create_button.setBackgroundResource(R.drawable.metal_selected);
                            }else if(checked2[3]){
                                checked2[3] = false;
                                metal_create_button.setBackgroundResource(R.drawable.metal);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    cloths_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[4]){
                                checked2[4] = true;
                                cloths_create_button.setBackgroundResource(R.drawable.cloths_selected);
                            }else if(checked2[4]){
                                checked2[4] = false;
                                cloths_create_button.setBackgroundResource(R.drawable.cloths);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    other_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[5]){
                                checked2[5] = true;
                                other_create_button.setBackgroundResource(R.drawable.other_selected);
                            }else if(checked2[5]){
                                checked2[5] = false;
                                other_create_button.setBackgroundResource(R.drawable.other);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    dangerous_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[6]){
                                checked2[6] = true;
                                dangerous_create_button.setBackgroundResource(R.drawable.dangerous_selected);
                            }else if(checked2[6]){
                                checked2[6] = false;
                                dangerous_create_button.setBackgroundResource(R.drawable.dangerous);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    batteries_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[7]){
                                checked2[7] = true;
                                batteries_create_button.setBackgroundResource(R.drawable.batteries_selected);
                            }else if(checked2[7]){
                                checked2[7] = false;
                                batteries_create_button.setBackgroundResource(R.drawable.batteries);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    lamp_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[8]){
                                checked2[8] = true;
                                lamp_create_button.setBackgroundResource(R.drawable.lamp_selected);
                            }else if(checked2[8]){
                                checked2[8] = false;
                                lamp_create_button.setBackgroundResource(R.drawable.lamp);
                            }
                            //markerDrawer.searchTypes(checked2);

                        }
                    });

                    appliances_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[9]){
                                checked2[9] = true;
                                appliances_create_button.setBackgroundResource(R.drawable.appliances_selected);
                            }else if(checked2[9]){
                                checked2[9] = false;
                                appliances_create_button.setBackgroundResource(R.drawable.appliances);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    tetra_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[10]){
                                checked2[10] = true;
                                tetra_create_button.setBackgroundResource(R.drawable.tetra_selected);
                            }else if(checked2[10]){
                                checked2[10] = false;
                                tetra_create_button.setBackgroundResource(R.drawable.tetra);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    lid_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[11]){
                                checked2[11] = true;
                                lid_create_button.setBackgroundResource(R.drawable.lid_selected);
                            }else if(checked2[11]){
                                checked2[11] = false;
                                lid_create_button.setBackgroundResource(R.drawable.lid);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    tires_create_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checked2[12]){
                                checked2[12] = true;
                                tires_create_button.setBackgroundResource(R.drawable.tires_selected);
                            }else if(checked2[12]){
                                checked2[12] = false;
                                tires_create_button.setBackgroundResource(R.drawable.tires);
                            }
                            //markerDrawer.searchTypes(checked2);
                        }
                    });

                    builder.setView(createPointView);

                    builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean exceptionNoTypes = true;
                            for (int i = 0; i < checked2.length; i ++){
                                if (checked2[i]) exceptionNoTypes = false;
                            }
                            if (exceptionNoTypes){
                                Toast.makeText(Main.this, "Вы не выбрали тип", Toast.LENGTH_SHORT).show();

                            } else {
                                ArrayList<String> temp = new ArrayList<String>();
                                for (int i = 0; i < checked2.length; i++) {
                                    if (checked2[i]) temp.add(types[i]);
                                }
                                String[] newTypes = new String[temp.size()];
                                temp.toArray(newTypes);
                                Bitmap newBitmap = MarkerDrawer.drawRingChartMarker(newTypes);
                                PlacemarkMapObject newMarker = mapObjects.addPlacemark(customMarker.getGeometry(), ImageProvider.fromBitmap(newBitmap));
                                listCustomMarkers.add(newMarker);
                                RecyclingPoint newPoint = new RecyclingPoint(customMarker.getGeometry(), location.getText().toString(), name.getText().toString(), info.getText().toString(), newTypes);
                                newMarker.setUserData(newPoint);
                                listCustomPoints.add(newPoint);
                                customMarker.setVisible(false);
                                ok_button.setVisibility(View.GONE);
                                cancel_button.setVisibility(View.GONE);
                                addCustomPoint_button.setVisibility(View.VISIBLE);
                                isCreatingRecyclePonit = false;
                                newMarker.addTapListener(placeMarkTapListener);
                                for (int i = 0; i < checked2.length; i++)
                                    checked2[i] = false;
                                dialog.cancel();
                            }
                        }
                    });
                    builder.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });

        addCustomPoint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                isCreatingRecyclePonit = true;
                addCustomPoint_button.setVisibility(View.GONE);
                cancel_button.setVisibility(View.VISIBLE);
                Toast.makeText(Main.this, "Выберите точку на карте, где хотите добавить пункт переработки мусора", Toast.LENGTH_SHORT).show();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                isCreatingRecyclePonit = false;
                cancel_button.setVisibility(View.GONE);
                if (customMarker != null && customMarker.isVisible())
                    customMarker.setVisible(false);
                addCustomPoint_button.setVisibility(View.VISIBLE);
                ok_button.setVisibility(View.GONE);
            }
        });
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
                if(isCustomPoint){
                    isCustomPoint = false;
                    isCreatingWithCustomPoint = false;
                }
                Object userData = mapObject.getUserData();

                if(userData instanceof RecyclingPoint){
                    RecyclingPoint data = (RecyclingPoint) userData;
                    clickedPoint = data.getPoint();
                    showPointInfo(mapObject, data);
                }
            }
            return true;
        }
    };

    //обработка изменения положения камеры
    private CameraListener cameraListener = new CameraListener() {

        @Override
        public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateReason cameraUpdateReason, boolean b) {
            regionHelper.isInRegion(map.getVisibleRegion(), cameraPosition.getZoom(), cameraPosition.getTarget());
        }
    };

    //обработка нажатия на карту
    private InputListener mapTapListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            if(isCustomPoint){
                if (destination == null) {
                    Bitmap bitmapDest = markerDrawer.drawDestinationMarker();
                    destination = mapObjects.addPlacemark(point, ImageProvider.fromBitmap(bitmapDest));
                } else {
                    destination.setGeometry(point);
                    destination.setVisible(true);
                }
                destination.setDraggable(true);//делаем маркер двигаемым
                ok_button.setVisibility(View.VISIBLE);//показываем кнопку ОК для подтверждения точки
            }
            if(isCreatingRecyclePonit){
                if (customMarker == null) {
                    Bitmap bitmapDest = markerDrawer.drawDestinationMarker();
                    customMarker = mapObjects.addPlacemark(point, ImageProvider.fromBitmap(bitmapDest));
                } else {
                    customMarker.setGeometry(point);
                    customMarker.setVisible(true);
                }
                customMarker.setDraggable(true);
                ok_button.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

        }
    };

    private void showPointInfo(MapObject mapObject, RecyclingPoint data){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this)
                .setTitle(data.getLocationName())
                .setMessage(data.getInfo() + "\n" + data.getLocation())
                .setPositiveButton("Показать маршрут", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            showHowToStart();
                            dialogInterface.cancel();
                    }
                })
                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this)
                                .setTitle("Удалить пункт")
                                .setMessage("Вы уверены, что хотите удалить маршрут?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //mapObjects.remove(mapObject);
                                        //удаление
                                        dialogInterface.cancel();

                                    }
                                })
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        dialog.show();
                        dialogInterface.cancel();
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

    private void showHowToStart(){
        AlertDialog.Builder startOption = new AlertDialog.Builder(Main.this)
                .setTitle("Откуда вы хотите проложить маршрут?")
                .setPositiveButton("Точка на карте", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //очищаю предыдущий маршрут
                        deleteCurrentPath();
                        isCustomPoint = true;
                        isCreatingWithCustomPoint = true;
                        Toast.makeText(Main.this, "Выберите точку на карте", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Моя геолокация", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(userLocationLayer.cameraPosition() != null) {
                            //если выбрали геолокацию, то блочу все, связанное с кастомным, чтоб не ломалось
                            isCustomPoint = false;
                            isCreatingWithCustomPoint = false;
                            showCreateRouteOptions(userLocationLayer.cameraPosition().getTarget());
                        }else{
                            Toast.makeText(Main.this, "Необходимо включить геолокацию!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        startOption.show();
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
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //обрабатываю закрытие окошка. Если маркер был поставлен, то удаляем
                        if(isCreatingWithCustomPoint) {
                            isCustomPoint = false;
                            destination.setVisible(false);
                            isCreatingWithCustomPoint = false;
                        }
                    }
                });
        routerOptions.show();
    }

    private void deleteCurrentPath(){
        if(currentPath.size() != 0){
            for (PolylineMapObject poly : currentPath)
                mapObjects.remove(poly);
            currentPath = new ArrayList<>();
        }
        if (destination != null) destination.setVisible(false);
        isCustomPoint = false;
        isCreatingWithCustomPoint = false;
    }
    
    private void createDrivingRoute(Point start) {
        List<RequestPoint> points = new ArrayList<>();

        points.add(new RequestPoint(start, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(clickedPoint, RequestPointType.WAYPOINT, null));

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        drivingSession = drivingRouter.requestRoutes(points, new DrivingOptions(), new VehicleOptions(), this);
        addCustomPoint_button.setVisibility(View.GONE);
        removePath_button.setVisibility(View.VISIBLE);//когда маршрут строится появляется кнопка и мы можем удалить маршрут
    }

    private void createPedestrianRoute(Point start) {
        List<RequestPoint> points = new ArrayList<>();

        points.add(new RequestPoint(start, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(clickedPoint, RequestPointType.WAYPOINT, null));

        pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter();
        pedestrianRouter.requestRoutes(points, new TimeOptions(), this);
        addCustomPoint_button.setVisibility(View.GONE);
        removePath_button.setVisibility(View.VISIBLE);//когда маршрут строится появляется кнопка и мы можем удалить маршрут
    }

    private void drawPath(Polyline geometry) {
        if(currentPath.size() != 0){
            for (PolylineMapObject poly : currentPath)
                mapObjects.remove(poly);
            currentPath = new ArrayList<>();

            if(!isCreatingWithCustomPoint) {
                if (destination != null) destination.setVisible(false);
                isCustomPoint = false;
            }
        }

        PolylineMapObject polylineMapObject = mapObjects.addPolyline(geometry);
        polylineMapObject.setStrokeColor(R.color.purple_200);

        currentPath.add(polylineMapObject);
        isCreatingWithCustomPoint = false;
    }

    @Override
    public void onMasstransitRoutes(@NonNull List<Route> list) {
        if (list.size() > 0) {
            for (Section section : list.get(0).getSections()) {
                drawPath(SubpolylineHelper.subpolyline(
                        list.get(0).getGeometry(), section.getGeometry()));
            }
        }
        else{
            Toast.makeText(this, "Невозможно добраться пешком", Toast.LENGTH_SHORT).show();
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

            if(!isCreatingWithCustomPoint) {
                if (destination != null) destination.setVisible(false);
                isCustomPoint = false;
            }
        }
        if(list.size() > 0) {
            PolylineMapObject polylineMapObject = mapObjects.addPolyline(list.get(0).getGeometry());
            currentPath.add(polylineMapObject);
        }
        else {
            Toast.makeText(this, "Невозможно добраться на машине", Toast.LENGTH_SHORT).show();
        }
        isCreatingWithCustomPoint = false;
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

    public void searchCustomTypes(boolean[] checked){
        for (int i = 0; i < listCustomPoints.size(); i++){
            listCustomMarkers.get(i).setVisible(true);
        }
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < checked.length; i++) {
            if (checked[i]) temp.add(types[i]);
        }
        for (int i = 0; i < listCustomPoints.size(); i++){
            String[] types = listCustomPoints.get(i).getTypes();
            for (String type : temp){
                if(!Arrays.asList(types).contains(type)) listCustomMarkers.get(i).setVisible(false);
            }
        }
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

        userLocationView.getAccuracyCircle().setFillColor(R.color.theme & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

}
