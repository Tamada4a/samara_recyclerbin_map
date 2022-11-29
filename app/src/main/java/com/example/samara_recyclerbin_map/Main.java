package com.example.samara_recyclerbin_map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;


public class Main extends AppCompatActivity {

    private MapView mapview;
    private final Point START_POINT = new Point(53.212228298365396, 50.17742481807416);
    private final Point TEST = new Point(53.212857,50.182195);
    private final int picSize = 80;
    //private final String[] types = {"Paper", "Glass", "Plastic", "Metal", "Clothes", "Other", "Dangerous",
    //"Batteries", "Lamp", "Appliances", "Tetra", "Lid", "Tires"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(getString(R.string.API_KEY));
        MapKitFactory.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(START_POINT, 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 3),
                null);
        MapObjectCollection mapObjects = mapview.getMap().getMapObjects().addCollection();

        //String[] chosenTypes = {"Plastic", "Metal", "Lid", "Other", "Lamp"};
        String[] chosenTypes = {"Lamp"};
        Bitmap bitmap = drawMarker(chosenTypes);
        PlacemarkMapObject markerTest = mapObjects.addPlacemark(TEST, ImageProvider.fromBitmap(bitmap));
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

    private Bitmap drawMarker(String[] chosenTypes){

        int length = chosenTypes.length;

        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float sweepAngel = 360 / length;
        float startAngel = 0;

        int color = 0;

        for(int i = 0; i < length; ++i){
            switch (chosenTypes[i]){
                case "Paper":
                    color = ContextCompat.getColor(this, R.color.paper_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Glass":
                    color = ContextCompat.getColor(this, R.color.glass_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Plastic":
                    color = ContextCompat.getColor(this, R.color.plastic_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Metal":
                    color = ContextCompat.getColor(this, R.color.metal_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Clothes":
                    color = ContextCompat.getColor(this, R.color.clothes_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Other":
                    color = ContextCompat.getColor(this, R.color.other_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Dangerous":
                    color = ContextCompat.getColor(this, R.color.dangerous_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Batteries":
                    color = ContextCompat.getColor(this, R.color.batteries_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Lamp":
                    color = ContextCompat.getColor(this, R.color.lamp_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Appliances":
                    color = ContextCompat.getColor(this, R.color.appliances_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Tetra":
                    color = ContextCompat.getColor(this, R.color.tetra_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Lid":
                    color = ContextCompat.getColor(this, R.color.lid_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
                case "Tires":
                    color = ContextCompat.getColor(this, R.color.tires_color);
                    startAngel = drawArc(color, sweepAngel, startAngel, canvas);
                    break;
            }
        }

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.TRANSPARENT);
        circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        canvas.drawCircle((float)(picSize / 2), (float)(picSize / 2), (float)(picSize / 3), circlePaint);
        
        return bitmap;
    }

    private float drawArc(int color, float sweepAngel, float startAngel, Canvas canvas){

        RectF pi = new RectF(0, 0, picSize, picSize);

        Paint paint = new Paint();

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawArc(pi, startAngel, sweepAngel, true, paint);
        startAngel += sweepAngel;

        return startAngel;
    }
}
