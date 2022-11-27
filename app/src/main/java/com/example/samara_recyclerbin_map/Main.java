package com.example.samara_recyclerbin_map;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.internal.PlacemarkMapObjectBinding;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;


public class Main extends AppCompatActivity {

    private MapView mapview;
    private final Point START_POINT = new Point(53.212228298365396, 50.17742481807416);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(getString(R.string.API_KEY));
        MapKitFactory.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(START_POINT, 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        /*MapObjectCollection mapObjects = mapview.getMap().getMapObjects().addCollection();
        PlacemarkMapObject marker = mapObjects.addPlacemark(START_POINT);
        marker.setIcon(ImageProvider.fromResource(this,R.drawable));*/
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
}
