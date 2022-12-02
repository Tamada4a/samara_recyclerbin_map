package com.example.samara_recyclerbin_map;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class Welcome extends AppCompatActivity {

    private ImageView mImageView;
    private Animation mFadeOutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mImageView = (ImageView) findViewById(R.id.logo);
        //Подключаем файл анимации
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        mFadeOutAnimation.setAnimationListener(animationFadeOutListener);
        //Запускаем анимацию
        mImageView.startAnimation(mFadeOutAnimation);

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    sleep(4000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally{
                    PermissionListener permissionlistener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent(Welcome.this, Main.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Intent intent = new Intent(Welcome.this, Main.class);
                            startActivity(intent);
                        }

                    };

                    TedPermission.create()
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("Хорошо. Но это может затруднить сервис ;)\n\nВ любой момент вы можете разрешить местоположение в настройках")
                            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();
                }
            }
        };

        thread.start();

    }



    Animation.AnimationListener animationFadeOutListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            //
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}