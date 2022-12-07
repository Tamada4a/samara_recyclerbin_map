package com.example.samara_recyclerbin_map.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.samara_recyclerbin_map.CustomTypes.RecyclingPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/*
* Класс, отвечающий за сохранение и загрузку списков точек из sharedPreference
*/
public class SaveAndLoad {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    public SaveAndLoad(@NonNull Activity activity){
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void save(ArrayList<RecyclingPoint> listPoints, ArrayList<RecyclingPoint> listCustomPoints){

        String stringListPoints = new Gson().toJson(listPoints);
        String stringListCustomPoints = new Gson().toJson(listCustomPoints);

        editor.putString( "SavedListPoints", stringListPoints);
        editor.putString( "SavedListCustomPoints", stringListCustomPoints);
        editor.commit();
    }

    public ArrayList<ArrayList> load(){

        String jsonListPoints = sharedPref.getString("SavedListPoints", "");
        String jsonListCustomPoints = sharedPref.getString("SavedListCustomPoints", "");

        Type typePoints = new TypeToken<ArrayList<RecyclingPoint>>(){}.getType();

        ArrayList<RecyclingPoint> loadedListPoints = new Gson().fromJson(jsonListPoints, typePoints);
        ArrayList<RecyclingPoint> loadedListCustomPoints = new Gson().fromJson(jsonListCustomPoints, typePoints);


        ArrayList<ArrayList> loadedArray = new ArrayList<>();

        loadedArray.add(loadedListPoints);
        loadedArray.add(loadedListCustomPoints);

        return loadedArray;
    }

    public void reset(){
        editor.clear().commit();
    }

    public boolean isEmpty(){
        return !(sharedPref.contains("SavedListPoints") &&
                sharedPref.contains("SavedListCustomPoints"));
    }
}
