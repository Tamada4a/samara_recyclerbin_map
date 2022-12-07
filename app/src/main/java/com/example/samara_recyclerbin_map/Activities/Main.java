package com.example.samara_recyclerbin_map.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.samara_recyclerbin_map.Helpers.MarkerDrawer;
import com.example.samara_recyclerbin_map.Helpers.RegionHelper;
import com.example.samara_recyclerbin_map.CustomListeners.NetworkStateReceiver;
import com.example.samara_recyclerbin_map.CustomTypes.RecyclingPoint;

import com.example.samara_recyclerbin_map.R;

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
import java.util.List;


public class Main extends AppCompatActivity implements UserLocationObjectListener, Session.RouteListener, DrivingSession.DrivingRouteListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final float COMFORTABLE_ZOOM_LEVEL = 14.0f;
    private static final float MAX_ZOOM_LEVEL = 11.0f;

    private final Point START_POINT = new Point(53.212228298365396, 50.17742481807416);
    private final Point leftUpperCornerPoint = new Point(53.4234,50.0015);
    private final Point leftLowerCornerPoint = new Point(53.0928, 50.0015);
    private final Point rightUpperCornerPoint = new Point(53.4234,50.3840);
    private final Point rightLowerCornerPoint = new Point(53.0928,50.3840);

    private PedestrianRouter pedestrianRouter;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private MapView mapview;
    private MapObjectCollection mapObjects;
    private Point clickedPoint;
    private PlacemarkMapObject destination;
    private PlacemarkMapObject customMarker;

    private UserLocationLayer userLocationLayer;

    private List<PolylineMapObject> currentPath = new ArrayList<>();

    private MarkerDrawer markerDrawer;
    private RegionHelper regionHelper;
    private NetworkStateReceiver networkStateReceiver;

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
    private ImageButton reset_button;

    private DrawerLayout drawerLayout;
    private NavigationView sideMenu;
    private View sideMenuHeader;
    //private View createPointView;

    private boolean[] checked = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    //checked2 - массив для кнопочек в окошке создания пункта. true - кнопочка нажата, false - не нажата
    private boolean[] checked2 = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private boolean isCustomPoint = false; //нужна для проверки в tapListener'e карты
    private boolean isCreatingWithCustomPoint = false; //по ней удаляю маршрут, есл построен через кастомный маркер
    private boolean isCreatingRecyclePonit = false; // переменная, которая показывает состояние - создаём ли мы сейчас кастомный пункт или нет

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

        regionHelper = new RegionHelper(leftUpperCornerPoint, leftLowerCornerPoint, rightUpperCornerPoint, rightLowerCornerPoint, START_POINT, MAX_ZOOM_LEVEL, COMFORTABLE_ZOOM_LEVEL, this, true);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        markerDrawer = new MarkerDrawer(this, mapObjects, placeMarkTapListener, this);

        mapObjects = markerDrawer.initialize();
        //mapObjects = markerDrawer.drawDefaultMarkers();

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
        reset_button = sideMenuHeader.findViewById(R.id.reset_button);

        if(markerDrawer.getSaveAndLoad().isEmpty())
            reset_button.setVisibility(View.GONE); //изначально кнопка сброса не видна

        pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userLocationLayer.cameraPosition() != null){
                    Point userPoint = userLocationLayer.cameraPosition().getTarget();
                    if(regionHelper.isUserInRegion(userPoint)) {
                        mapview.getMap().move(
                                new CameraPosition(userPoint, 15.0f, 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 1.2f),
                                null);
                    }
                    else
                        Toast.makeText(Main.this, "Вы вне обслуживаемого региона!", Toast.LENGTH_SHORT).show();
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

        //обработка кликания на "СБРОС"
        //вылезает Alert с просьбой подтвердить
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this)
                        .setTitle("Сброс до базовых настроек")
                        .setMessage("Вы уверены, что хотите удалить все созданные пользовательские пункты и вернуться к базовым настройкам?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //по новой рисуем все наши маркеры
                                mapObjects = markerDrawer.resetMarkers(mapObjects);
                                reset_button.setVisibility(View.GONE); // кнопка СБРОСА опять пропадает 0_0
                                dialogInterface.cancel();

                            }
                        })
                        //не хотим удалять -> закрываем окно
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                dialog.show();
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
            }
        });


        ok_button.setOnClickListener(new View.OnClickListener() { //когда кликаем на ок строится маршрут
            @Override
            public void onClick(View view){
                if(!isCreatingRecyclePonit) {//если мы не создаём кастомный пункт => мы прокладываем маршрут => работает алгоритм с прокладыванием маршрута
                    isCustomPoint = false;
                    destination.setDraggable(false);
                    ok_button.setVisibility(View.GONE);//убираем кнопку
                    showCreateRouteOptions(destination.getGeometry());//строим маршрут по точке
                } else { //Если создаём кастомный пункт => вылезает окно для создания
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.AlertDialogCustom);
                    builder.setTitle("Новый пункт");

                    //создаю View в функции чтобы всё работало НоРмАльНо
                    View createPointView = LayoutInflater.from(Main.this).inflate(R.layout.create_point, null);
                    //вытаскиваю переменные из TextInput'ов
                    final EditText name = (EditText) createPointView.findViewById(R.id.input_name);
                    final EditText info = (EditText) createPointView.findViewById(R.id.input_info);
                    final EditText location = (EditText) createPointView.findViewById(R.id.input_address);
                    //Инициализирую все наши кнопочки с типами мусора
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

                    //по новой пишу методы, чтобы они меняли цвет
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
                        }
                    });

                    //присваиваю View
                    builder.setView(createPointView);

                    //создать -> создаём пункт
                    builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //проверочка на "user не выбрал ни один тип"
                            boolean exceptionNoTypes = true;
                            for (int i = 0; i < checked2.length; i ++){
                                if (checked2[i]) exceptionNoTypes = false;//если checked2[i]=true -> хотя бы одна кнопочка нажата => всё за**ись
                            }
                            if (exceptionNoTypes){ //если ничо не нажато выводим текст и окно закрывается
                                Toast.makeText(Main.this, "Вы не выбрали тип", Toast.LENGTH_SHORT).show();

                            } else {//если всё норм и ошибки нет, создаём пункт
                                mapObjects = markerDrawer.drawCustomMarker(checked2, customMarker.getGeometry(), location.getText().toString(),
                                        name.getText().toString(), info.getText().toString(), mapObjects);

                                customMarker.setVisible(false); //убираем метку, которую ставили на карте
                                ok_button.setVisibility(View.GONE); //кнопочка "ок" пропадает
                                cancel_button.setVisibility(View.GONE); //кнопочка "крестик" пропадает
                                addCustomPoint_button.setVisibility(View.VISIBLE); //кнопочка "плюсик" появляется. Короче, возвращаемся к первичному интерфейсу
                                reset_button.setVisibility(View.VISIBLE); //кнопка СБРОСА появляется, т.к. мы добавили пункт и уже можем ресетнуть
                                isCreatingRecyclePonit = false; //показываем, что мы больше не находимся в состоянии создания кастомного пункта

                                for (int i = 0; i < checked2.length; i++) //возвращаем checked2 на места, чтобы всё было false (то есть все кнопки не нажаты)
                                    checked2[i] = false;
                                dialog.cancel();
                            }
                        }
                    });
                    //закрыть -> закрываем окно
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
                isCreatingRecyclePonit = true; //активируем состояние создания кастомного пункта
                addCustomPoint_button.setVisibility(View.GONE); // появляется кнопка "крестика" и пропадает "плюсик"
                cancel_button.setVisibility(View.VISIBLE);
                Toast.makeText(Main.this, "Выберите точку на карте, где хотите добавить пункт переработки мусора", Toast.LENGTH_SHORT).show();
            }
        });

        //кнопка "крестик", если вдруг передумали создавать пункт
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                isCreatingRecyclePonit = false; //убираем состояние создания кастомного пункта
                cancel_button.setVisibility(View.GONE); //откатываемся к первичному интерфейсу
                if (customMarker != null && customMarker.isVisible()) //удаляем маркер, если ставили
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
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
            System.out.println("Тут это, того " + map.getVisibleRegion().getTopRight().getLongitude() + " " + map.getVisibleRegion().getTopRight().getLatitude() + "\n"
                    + map.getVisibleRegion().getBottomLeft().getLongitude() + " " + map.getVisibleRegion().getBottomLeft().getLatitude());
            System.out.println("ZOOM " + cameraPosition.getZoom());
            if (b) {
                regionHelper.isInRegion(cameraPosition.getTarget(), cameraPosition.getZoom(), mapview);
            }
        }
    };

    //обработка нажатия на карту
    private InputListener mapTapListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            if(isCustomPoint){ //ну это короче для маршрута - ты помнишь знаешь
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
            if(isCreatingRecyclePonit){ //если мы в состоянии создания пункта
                if (customMarker == null) { //если маркера нет, то создаём
                    Bitmap bitmapDest = markerDrawer.drawDestinationMarker();
                    customMarker = mapObjects.addPlacemark(point, ImageProvider.fromBitmap(bitmapDest));
                } else { //если маркер есть, то передвигаем
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

    //тут немного добавил. помимо data передаём еще и сам MapObject. А именно Placemark. это надо для удаления
    private void showPointInfo(MapObject mapObject, @NonNull RecyclingPoint data){
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
                //добавил кнопку удаления
                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //выскакивает новый Alert с подтверждением
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this)
                                .setTitle("Удалить пункт")
                                .setMessage("Вы уверены, что хотите удалить пункт?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mapObjects = markerDrawer.removeMarker(mapObject, data, mapObjects);
                                        reset_button.setVisibility(View.VISIBLE); //появляется кнопка СБРОСА, т.к. мы удалили пункт
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
    public void networkAvailable() {
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(this, "Потеряно интернет-соединение, приложение может работать некорректно!", Toast.LENGTH_SHORT).show();
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