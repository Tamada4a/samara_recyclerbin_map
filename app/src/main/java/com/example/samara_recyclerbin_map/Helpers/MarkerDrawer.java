package com.example.samara_recyclerbin_map.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.samara_recyclerbin_map.R;
import com.example.samara_recyclerbin_map.CustomTypes.RecyclingPoint;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.Arrays;

//в этом классе большинство того, что отвечает за отрисовку маркеров на карте
public class MarkerDrawer {
    private ArrayList<PlacemarkMapObject> listMarkers = new ArrayList<PlacemarkMapObject>();
    private ArrayList<RecyclingPoint> listPoints = new ArrayList<RecyclingPoint>();
    private ArrayList<PlacemarkMapObject> listCustomMarkers = new ArrayList<PlacemarkMapObject>();
    private ArrayList<RecyclingPoint> listCustomPoints = new ArrayList<RecyclingPoint>();

    private SaveAndLoad saveAndLoad;

    private Context context;

    private MapObjectTapListener placeMarkTapListener;
    private MapObjectCollection mapObjects;

    private String[] types = {"Paper", "Glass", "Plastic", "Metal", "Clothes", "Other", "Dangerous",
            "Batteries", "Lamp", "Appliances", "Tetra", "Lid", "Tires"};

    public MarkerDrawer(Context mainContext, MapObjectCollection mainMapObjects, MapObjectTapListener mainPlaceMarkTapListener, Activity mainActivity){
        context = mainContext;
        mapObjects = mainMapObjects;
        placeMarkTapListener = mainPlaceMarkTapListener;
        saveAndLoad = new SaveAndLoad(mainActivity);
    }

    /*
    * инициализируем маркеры на карте, изначально проверяя, не вносил ли
    * пользователь изменения
    */
    public MapObjectCollection initialize(){
        if(!saveAndLoad.isEmpty()){
            ArrayList<ArrayList> loadedArray = saveAndLoad.load();

            listPoints = loadedArray.get(0);
            listCustomPoints = loadedArray.get(1);

            for(int i = 0; i < listPoints.size(); ++i){
                RecyclingPoint point = listPoints.get(i);
                Bitmap bitmapType = drawRingChartMarker(point.getTypes());
                PlacemarkMapObject placemarkMapObject = mapObjects.addPlacemark(point.getPoint(), ImageProvider.fromBitmap(bitmapType));
                placemarkMapObject.setUserData(point);
                placemarkMapObject.addTapListener(placeMarkTapListener);

                listMarkers.add(placemarkMapObject);
            }

            for(int i = 0; i < listCustomPoints.size(); ++i){
                RecyclingPoint point = listCustomPoints.get(i);
                Bitmap bitmapType = drawRingChartMarker(point.getTypes());
                PlacemarkMapObject placemarkMapObject = mapObjects.addPlacemark(point.getPoint(), ImageProvider.fromBitmap(bitmapType));
                placemarkMapObject.setUserData(point);
                placemarkMapObject.addTapListener(placeMarkTapListener);

                listCustomMarkers.add(placemarkMapObject);
            }

        }
        else mapObjects = drawDefaultMarkers();

        return mapObjects;
    }

    //отрисовываем маркеры по-дефолту
    public MapObjectCollection drawDefaultMarkers(){
//Батарейки DNS Русь
        String[] batteries = {"Batteries"}; //батарейки
        Bitmap bitmapBatteries = drawRingChartMarker(batteries); //получаем битмап из функции
        PlacemarkMapObject markerTest1 = mapObjects.addPlacemark(new Point(53.212857,50.182195), ImageProvider.fromBitmap(bitmapBatteries));
        RecyclingPoint point1 = new RecyclingPoint(new Point(53.212857,50.182195), "Московское ш., 29. ТЦ Русь. Магазин DNS", "Контейнер для старых батарей", "Контейнер для батареек Duracell находится на входе в гипермаркет DNS", batteries);
        //маркеру добавляем информацию
        markerTest1.setUserData(point1);
        //добавляем обработку нажатия на него. Потом через это можно будет сделать как на сайте
        markerTest1.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest1);
        listPoints.add(point1);

//Батарейки Пятерочка
        PlacemarkMapObject markerTest2 = mapObjects.addPlacemark(new Point(53.209465, 50.175616), ImageProvider.fromBitmap(bitmapBatteries));
        RecyclingPoint point2 = new RecyclingPoint(new Point(53.209465, 50.175616), "Революционная улица, 64Б. Пятерочка ", "Контейнер для старых батарей", "Контейнер для старых батареек находится на входе в магазин Пятёрочка", batteries);
        markerTest2.setUserData(point2);
        markerTest2.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest2);
        listPoints.add(point2);

//Перекресток добрые крышечки
        String[] lidOther = {"Lid", "Other"}; // крышки, другое
        Bitmap bitmapLidOther = drawRingChartMarker(lidOther);
        PlacemarkMapObject markerTest3 = mapObjects.addPlacemark(new Point(53.210258, 50.173316), ImageProvider.fromBitmap(bitmapLidOther));
        RecyclingPoint point3 = new RecyclingPoint(new Point(53.210258, 50.173316), "Московское ш., 28, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\"", "Контейнер для крышечек в супермаркет Перекрёсток стоит напротив касс.", lidOther);
        markerTest3.setUserData(point3);
        markerTest3.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest3);
        listPoints.add(point3);

//Контейнер Пакмил
        String[] paper = {"Paper"}; //бумага
        Bitmap bitmapPaper = drawRingChartMarker(paper);
        PlacemarkMapObject markerTest4 = mapObjects.addPlacemark(new Point(53.217380, 50.169432), ImageProvider.fromBitmap(bitmapPaper));
        RecyclingPoint point4 = new RecyclingPoint(new Point(53.217380, 50.169432), "ул. Мичурина, 154", "Контейнер ПАКМИЛ для макулатуры", "Контейнер для макулатуры стоит по ул. Мичурина, между домами №148 и №150. Принимают все виды макулатуры, кроме цветного картона.", paper);
        markerTest4.setUserData(point4);
        markerTest4.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest4);
        listPoints.add(point4);

//Пакмил для бутылок
        String[] plastic = {"Plastic"}; //пластик
        Bitmap bitmapPlastic = drawRingChartMarker(plastic);
        PlacemarkMapObject markerTest5 = mapObjects.addPlacemark(new Point(53.216614, 50.167584), ImageProvider.fromBitmap(bitmapPlastic));
        RecyclingPoint point5 = new RecyclingPoint(new Point(53.216614, 50.167584), "ул. Мичурина, 138", "Контейнер для ПЭТ Пакмил", "Контейнер для ПЭТ бутылок. Находится через дорогу от дома по ул. Мичурина, 138, напротив магазина \"Перекресток\".", plastic);
        markerTest5.setUserData(point5);
        markerTest5.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest5);
        listPoints.add(point5);

//Контейнер ЭкоВоз для ПЭТ
        PlacemarkMapObject markerTest6 = mapObjects.addPlacemark(new Point(53.216533, 50.166435), ImageProvider.fromBitmap(bitmapPlastic));
        RecyclingPoint point6 = new RecyclingPoint(new Point(53.216533, 50.166435), "ул. Лукачева, 17", "Контейнер ЭкоВоз для ПЭТ", "Желтый контейнер-сетка находится на территории школы № 58, калитка со стороны ул. Лукачева.", plastic);
        markerTest6.setUserData(point6);
        markerTest6.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest6);
        listPoints.add(point6);

//Контейнер "Добрые крышечки" и контейнер-«собиратор» для старых зубных щёток
        //Bitmap bitmap7 = drawRingChartMarker(lidOther);
        PlacemarkMapObject markerTest7 = mapObjects.addPlacemark(new Point(53.217014, 50.167603), ImageProvider.fromBitmap(bitmapLidOther));
        RecyclingPoint point7 = new RecyclingPoint(new Point(53.217014, 50.167603), "ул. Мичурина, 138, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\" и контейнер-«собиратор» для старых зубных щёток", "Контейнер для крышечек и контейнер для старых зубных щёток находятся на входе в супермаркет Перекрёсток.", lidOther);
        markerTest7.setUserData(point7);
        markerTest7.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest7);
        listPoints.add(point7);

//Контейнер для макулатуры
        PlacemarkMapObject markerTest8 = mapObjects.addPlacemark(new Point(53.217004, 50.166444), ImageProvider.fromBitmap(bitmapPaper));
        RecyclingPoint point8 = new RecyclingPoint(new Point(53.217004, 50.166444), "ул. Лукачева, 10", "Контейнер для макулатуры", "Контейнер для макулатуры возле дома по адресу Лукачева, 10. Принимают все виды макулатуры, кроме цветного картона.", paper);
        markerTest8.setUserData(point8);
        markerTest8.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest8);
        listPoints.add(point8);

//Контейнер "Добрые крышечки" от ЭкоСтройРесурс
        String[] lid = {"Lid"}; //крышки
        Bitmap bitmapLid = drawRingChartMarker(lid);
        PlacemarkMapObject markerTest9 = mapObjects.addPlacemark(new Point(53.223107, 50.166507), ImageProvider.fromBitmap(bitmapLid));
        RecyclingPoint point9 = new RecyclingPoint(new Point(53.223107, 50.166507), "ул. Ново-Садовая, 156, Станция переливания крови", "Контейнер \"Добрые крышечки\" от ЭкоСтройРесурс", "Контейнер для крышечек находится в вестибюле станции переливания крови, напротив входа.", lid);
        markerTest9.setUserData(point9);
        markerTest9.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest9);
        listPoints.add(point9);

//Экоцентр "Вторсырье на благотворительность"
        String[] all = {"Paper", "Glass", "Plastic", "Metal", "Clothes", "Other", "Dangerous", "Batteries", "Lamp", "Appliances", "Tetra", "Lid"}; //всё
        Bitmap bitmapAll = drawRingChartMarker(all);
        PlacemarkMapObject markerTest10 = mapObjects.addPlacemark(new Point(53.211205, 50.225176), ImageProvider.fromBitmap(bitmapAll));
        RecyclingPoint point10 = new RecyclingPoint(new Point(53.211205, 50.225176), "ул. 22 Партсъезда, 40а", "Экоцентр \"Вторсырье на благотворительность\"", "Гараж на территориии бывшего детского сада. Подробнее: http://vk.com/rsbor_samara.", all);
        markerTest10.setUserData(point10);
        markerTest10.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest10);
        listPoints.add(point10);

//Контейнер для старых батареек в Пятёрочке
        PlacemarkMapObject markerTest11 = mapObjects.addPlacemark(new Point(53.206046, 50.162689), ImageProvider.fromBitmap(bitmapBatteries));
        RecyclingPoint point11 = new RecyclingPoint(new Point(53.206046, 50.162689), "ул. Масленникова, 40, магазин Пятёрочка", "Контейнер для старых батареек в Пятёрочке", "Контейнер для старых батареек находится на входе в магазин Пятёрочка", batteries);
        markerTest11.setUserData(point11);
        markerTest11.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest11);
        listPoints.add(point11);

//Приемный пункт «ВТОРМАРКЕТ»
        String[] vtormarket = {"Lid","Other","Paper","Metal","Plastic","Glass"}; //втормаркет
        Bitmap bitmapVtormarket = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest12 = mapObjects.addPlacemark(new Point(53.229716, 50.211306), ImageProvider.fromBitmap(bitmapVtormarket));
        RecyclingPoint point12 = new RecyclingPoint(new Point(53.229716, 50.211306), "ул. Стара Загора 57 А", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное).", vtormarket);
        markerTest12.setUserData(point12);
        markerTest12.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest12);
        listPoints.add(point12);

//М-Видео, приём старой техники и батареек
        String[] mvideo = {"Appliances", "Batteries"}; // мвидео - техника, батарейки
        Bitmap bitmapMVideo = drawRingChartMarker(mvideo);
        PlacemarkMapObject markerTest13 = mapObjects.addPlacemark(new Point(53.233208, 50.200764), ImageProvider.fromBitmap(bitmapMVideo));
        RecyclingPoint point13 = new RecyclingPoint(new Point(53.233208, 50.200764), "Московское ш., 81б, ТЦ Молл Парк Хаус", "М-Видео, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest13.setUserData(point13);
        markerTest13.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest13);
        listPoints.add(point13);

//Самарастеклотара
        String[] steklotara = {"Metal", "Plastic", "Glass", "Paper"}; // стеклотара - метал, пластик, стекло, бумага
        Bitmap bitmapSteklotara = drawRingChartMarker(steklotara);
        PlacemarkMapObject markerTest14 = mapObjects.addPlacemark(new Point(53.232530, 50.185264), ImageProvider.fromBitmap(bitmapSteklotara));
        RecyclingPoint point14 = new RecyclingPoint(new Point(53.232530, 50.185264), "ул. Ново-Садовая, 285а (рядом с ПЖРТ)", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest14.setUserData(point14);
        markerTest14.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest14);
        listPoints.add(point14);

//Самарастеклотара
        PlacemarkMapObject markerTest15 = mapObjects.addPlacemark(new Point(53.234519, 50.206248), ImageProvider.fromBitmap(bitmapSteklotara));
        RecyclingPoint point15 = new RecyclingPoint(new Point(53.234519, 50.206248), "Фадеева 42б", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest15.setUserData(point15);
        markerTest15.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest15);
        listPoints.add(point15);

//Эльдорадо, приём старой техники и батареек
        PlacemarkMapObject markerTest16 = mapObjects.addPlacemark(new Point(53.235090, 50.221969), ImageProvider.fromBitmap(bitmapMVideo));
        RecyclingPoint point16 = new RecyclingPoint(new Point(53.235090, 50.221969), "ул. Стара-Загора, 139, ТЦ Загорка", "Эльдорадо, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest16.setUserData(point16);
        markerTest16.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest16);
        listPoints.add(point16);

//Приемный пункт «ВТОРМАРКЕТ»
        PlacemarkMapObject markerTest17 = mapObjects.addPlacemark(new Point(53.221479, 50.229362), ImageProvider.fromBitmap(bitmapVtormarket));
        RecyclingPoint point17 = new RecyclingPoint(new Point(53.221479, 50.229362), "Матросова, д. 92", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное), поддоны.", vtormarket);
        markerTest17.setUserData(point17);
        markerTest17.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest17);
        listPoints.add(point17);

//Экомобиль
        String[] ecomobil = {"Dangerous", "Other", "Clothes", "Appliances", "Plastic", "Glass", "Paper", "Metal", "Lid"}; // ЭкоМобиль
        Bitmap bitmapEcomobil = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest18 = mapObjects.addPlacemark(new Point(53.213929, 50.257167), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point18 = new RecyclingPoint(new Point(53.213929, 50.257167), "ул. Физкультурная, 101, Дворец спорта", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest18.setUserData(point18);
        markerTest18.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest18);
        listPoints.add(point18);

//Экомобиль
        PlacemarkMapObject markerTest19 = mapObjects.addPlacemark(new Point(53.201332, 50.116049), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point19 = new RecyclingPoint(new Point(53.201332, 50.116049), "ул. Самарская, 207", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest19.setUserData(point19);
        markerTest19.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest19);
        listPoints.add(point19);

//Ты дома, благотворительный фонд
        String[] tiDoma = {"Clothes", "Other"}; // Ты Дома - одежда + иное
        Bitmap bitmapTiDoma = drawRingChartMarker(tiDoma);
        PlacemarkMapObject markerTest20 = mapObjects.addPlacemark(new Point(53.196471, 50.124044), ImageProvider.fromBitmap(bitmapTiDoma));
        RecyclingPoint point20 = new RecyclingPoint(new Point(53.196471, 50.124044), "ул. Буянова, 135б", "Ты дома, благотворительный фонд", "Помощь бездомным и малообеспеченным жителям Самары. Подробнее: http://vk.com/tydomasamara", tiDoma);
        markerTest20.setUserData(point20);
        markerTest20.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest20);
        listPoints.add(point20);

//Ты не одна, центр помощи женщинам
        PlacemarkMapObject markerTest21 = mapObjects.addPlacemark(new Point(53.184521, 50.104092), ImageProvider.fromBitmap(bitmapTiDoma));
        RecyclingPoint point21 = new RecyclingPoint(new Point(53.184521, 50.104092), "ул. Ленинградская, 100", "Ты не одна, центр помощи женщинам", "Принимают детскую и женскую одежду в хорошем состоянии. Подробнее: http://vk.com/centrtyneodna", tiDoma);
        markerTest21.setUserData(point21);
        markerTest21.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest21);
        listPoints.add(point21);

//Экоцентр "Вторсырье на благотворительность"
        PlacemarkMapObject markerTest22 = mapObjects.addPlacemark(new Point(53.195937, 50.125400), ImageProvider.fromBitmap(bitmapAll));
        RecyclingPoint point22 = new RecyclingPoint(new Point(53.195937, 50.125400), "ул. Коммунистическая, 4б", "Экоцентр \"Вторсырье на благотворительность\"", "В ряду павильонов по ул. Коммунистическая, сразу после павильона \"Ты дома\" и \"Моя рыбка 63\". Подробнее: http://vk.com/rsbor_samara.", all);
        markerTest22.setUserData(point22);
        markerTest22.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest22);
        listPoints.add(point22);

//Экомобиль
        //Bitmap bitmap23 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest23 = mapObjects.addPlacemark(new Point(53.198101, 50.100795), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point23 = new RecyclingPoint(new Point(53.198101, 50.100795), "ул. Вилоновская, 13", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest23.setUserData(point23);
        markerTest23.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest23);
        listPoints.add(point23);

//Фандомат в общественном центре Сбера
        String[] sber = {"Lid", "Metal", "Plastic"}; // сбер - крышки + метал + пластик
        Bitmap bitmapSber = drawRingChartMarker(sber);
        PlacemarkMapObject markerTest24 = mapObjects.addPlacemark(new Point(53.198047, 50.100912), ImageProvider.fromBitmap(bitmapSber));
        RecyclingPoint point24 = new RecyclingPoint(new Point(53.198047, 50.100912), "ул. Вилоновская, 13/ул. Чапаевская, 208", "Фандомат в общественном центре Сбера", "Фандомат находится в общественном центре Сбера", sber);
        markerTest24.setUserData(point24);
        markerTest24.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest24);
        listPoints.add(point24);

//Приемный пункт «ВТОРМАРКЕТ»
        PlacemarkMapObject markerTest25 = mapObjects.addPlacemark(new Point(53.189711, 50.111305), ImageProvider.fromBitmap(bitmapVtormarket));
        RecyclingPoint point25 = new RecyclingPoint(new Point(53.189711, 50.111305), "ул. Братьев Коростелевых 47", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное), поддоны.", vtormarket);
        markerTest25.setUserData(point25);
        markerTest25.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest25);
        listPoints.add(point25);

//Утилизация шин
        String[] tires = {"Tires"}; // шины
        Bitmap bitmapTires = drawRingChartMarker(tires);
        PlacemarkMapObject markerTest26 = mapObjects.addPlacemark(new Point(53.267787, 50.401955), ImageProvider.fromBitmap(bitmapTires));
        RecyclingPoint point26 = new RecyclingPoint(new Point(53.267787, 50.401955), "п. Стройкерамика, ул. Свободы, 10а, \"Грузовой шиномонтаж\"", "Утилизация шин", "Самарский резиноперерабатывающий завод принимает на утилизацию и переработку изношенные и поврежденные автомобильные шины.", tires);
        markerTest26.setUserData(point26);
        markerTest26.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest26);
        listPoints.add(point26);

//Пункт приёма ВтоЦветЧерМет
        String[] vcchm = {"Appliances", "Batteries", "Other", "Metal", "Plastic", "Paper"}; // ВтоЦветЧерМет - техника + батарейки + иное + метал + пластик + бумага
        Bitmap bitmapVtoCvet = drawRingChartMarker(vcchm);
        PlacemarkMapObject markerTest27 = mapObjects.addPlacemark(new Point(53.256595, 50.365241), ImageProvider.fromBitmap(bitmapVtoCvet));
        RecyclingPoint point27 = new RecyclingPoint(new Point(53.256595, 50.365241), "пгт. Смышляевка ул. Механиков, 3 (напротив Деловых Линий)", "Пункт приёма ВтоЦветЧерМет", "Приём лома чёрных, цветных металлов, пленки, пластика, электронного лома, макулатуры", vcchm);
        markerTest27.setUserData(point27);
        markerTest27.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest27);
        listPoints.add(point27);

//Приемный пункт «ВТОРМАРКЕТ»
        PlacemarkMapObject markerTest28 = mapObjects.addPlacemark(new Point(53.246067, 50.301299), ImageProvider.fromBitmap(bitmapVtormarket));
        RecyclingPoint point28 = new RecyclingPoint(new Point(53.246067, 50.301299), "ул. Товарная, 70", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное).", vtormarket);
        markerTest28.setUserData(point28);
        markerTest28.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest28);
        listPoints.add(point28);

//Пункт приёма ВтоЦветЧерМет
        String[] vcchm2 = {"Appliances",  "Metal", "Plastic", "Paper"}; // ВтоЦветЧерМет - техника + батарейки + иное + метал + пластик + бумага
        Bitmap bitmapVtoCvet2 = drawRingChartMarker(vcchm2);
        PlacemarkMapObject markerTest29 = mapObjects.addPlacemark(new Point(53.251574, 50.308575), ImageProvider.fromBitmap(bitmapVtoCvet2));
        RecyclingPoint point29 = new RecyclingPoint(new Point(53.251574, 50.308575), "Магистральная, 154Б", "Пункт приёма ВтоЦветЧерМет", "Приём лома чёрных, цветных металлов, пленки, пластика, электронного лома, макулатуры", vcchm2);
        markerTest29.setUserData(point29);
        markerTest29.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest29);
        listPoints.add(point29);

//Самарастеклотара
        PlacemarkMapObject markerTest30 = mapObjects.addPlacemark(new Point(53.238070, 50.282866), ImageProvider.fromBitmap(bitmapSteklotara));
        RecyclingPoint point30 = new RecyclingPoint(new Point(53.238070, 50.282866), "Свободы 236Б", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest30.setUserData(point30);
        markerTest30.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest30);
        listPoints.add(point30);

//Самарастеклотара
        PlacemarkMapObject markerTest31 = mapObjects.addPlacemark(new Point(53.225975, 50.276533), ImageProvider.fromBitmap(bitmapSteklotara));
        RecyclingPoint point31 = new RecyclingPoint(new Point(53.225975, 50.276533), "ул. Победы 164", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest31.setUserData(point31);
        markerTest31.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest31);
        listPoints.add(point31);

//Экомобиль
        PlacemarkMapObject markerTest32 = mapObjects.addPlacemark(new Point(53.233953, 50.274700), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point32 = new RecyclingPoint(new Point(53.233953, 50.274700), "ул. Марии Авейде, 35, тц Октябрь", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest32.setUserData(point32);
        markerTest32.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest32);
        listPoints.add(point32);

//Экомобиль
        PlacemarkMapObject markerTest33 = mapObjects.addPlacemark(new Point(53.224692, 50.271960), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point33 = new RecyclingPoint(new Point(53.224692, 50.271960), "ул. Советская, 2", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest33.setUserData(point33);
        markerTest33.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest33);
        listPoints.add(point33);

//Бак для энергосберегающих ламп
        String[] lamp = {"Lamp"}; // лампы
        Bitmap bitmapLamp = drawRingChartMarker(lamp);
        PlacemarkMapObject markerTest34 = mapObjects.addPlacemark(new Point(53.224692, 50.271960), ImageProvider.fromBitmap(bitmapLamp));
        RecyclingPoint point34 = new RecyclingPoint(new Point(53.224692, 50.271960), "ул. Ново-Вокзальная, 2а", "Бак для энергосберегающих ламп", "Контейнер для сбора отработанных энергосберегающих ламп располагается внутри здания ТЦ «На птичке», сразу около входа на 1 этаже (вход со стороны стоянки) под лестницей.", lamp);
        markerTest34.setUserData(point34);
        markerTest34.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest34);
        listPoints.add(point34);

//Бак для энергосберегающих ламп
        PlacemarkMapObject markerTest35 = mapObjects.addPlacemark(new Point(53.202022, 50.223730), ImageProvider.fromBitmap(bitmapLamp));
        RecyclingPoint point35 = new RecyclingPoint(new Point(53.202022, 50.223730), "ул. Гагарина, д. 122", "Бак для энергосберегающих ламп", "Контейнер для энергосберегающих ламп можно обнаружить спустившись по лестнице в магазин электроматериалов «Светосила - М».", lamp);
        markerTest35.setUserData(point35);
        markerTest35.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest35);
        listPoints.add(point35);

//КРОНА
        String[] krona = {"Appliances", "Metal", "Plastic"}; // крона - техника + метал + пластик
        Bitmap bitmapKrona = drawRingChartMarker(krona);
        PlacemarkMapObject markerTest36 = mapObjects.addPlacemark(new Point(53.172600, 50.194610), ImageProvider.fromBitmap(bitmapKrona));
        RecyclingPoint point36 = new RecyclingPoint(new Point(53.172600, 50.194610), "Заводское шоссе, 1г", "КРОНА, ООО", "Принимает металлолом, пластик. В том числе электронный лом", krona);
        markerTest36.setUserData(point36);
        markerTest36.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest36);
        listPoints.add(point36);

//Тайммет Вторчермет
        String[] timemet = {"Appliances", "Metal"}; // тайммет - техника + метал
        Bitmap bitmapTimemet = drawRingChartMarker(timemet);
        PlacemarkMapObject markerTest37 = mapObjects.addPlacemark(new Point(53.179308, 50.208755), ImageProvider.fromBitmap(bitmapTimemet));
        RecyclingPoint point37 = new RecyclingPoint(new Point(53.179308, 50.208755), "Заводское ш., 5 к.1", "Тайммет Вторчермет", "Принимает металлолом, пластик. В том числе электронный лом", timemet);
        markerTest37.setUserData(point37);
        markerTest37.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest37);
        listPoints.add(point37);

//ООО «ВТОРМАРКЕТ»
        String[] ooovtor = {"Plastic", "Paper", "Other"}; // ООО втормаркет - пластик + бумага + другое
        Bitmap bitmapOVtor = drawRingChartMarker(ooovtor);
        PlacemarkMapObject markerTest38 = mapObjects.addPlacemark(new Point(53.179238, 50.207461), ImageProvider.fromBitmap(bitmapOVtor));
        RecyclingPoint point38 = new RecyclingPoint(new Point(53.179238, 50.207461), "Заводское шоссе, 5Б лит T", "Приемный пункт «ВТОРМАРКЕТ»", "Макулатура, лом пластмасс, поддоны деревянные, текстиль (ватные матрасы б/у, отходы одеял, отходы стежки, обрезь синтепона.)Пленка", ooovtor);
        markerTest38.setUserData(point38);
        markerTest38.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest38);
        listPoints.add(point38);

//Приемный пункт «ВТОРМАРКЕТ»
        //Bitmap bitmap39 = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest39 = mapObjects.addPlacemark(new Point(53.176178, 50.209051), ImageProvider.fromBitmap(bitmapVtormarket));
        RecyclingPoint point39 = new RecyclingPoint(new Point(53.176178, 50.209051), "Заводское Шоссе, 5Б к7", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное).", vtormarket);
        markerTest39.setUserData(point39);
        markerTest39.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest39);
        listPoints.add(point39);

//Зеленый городок, пункт приёма вторсырья
        String[] greenTown = {"Lid", "Appliances", "Other", "Metal", "Plastic", "Glass", "Paper"}; // зеленый городок - крышки + техника + другое + метал + пластик + стекло + бумага
        Bitmap bitmapGreenTown = drawRingChartMarker(greenTown);
        PlacemarkMapObject markerTest40 = mapObjects.addPlacemark(new Point(53.181407, 50.110218), ImageProvider.fromBitmap(bitmapGreenTown));
        RecyclingPoint point40 = new RecyclingPoint(new Point(53.181407, 50.110218), "ул.Неверова, 1а", "Зеленый городок, пункт приёма вторсырья", "Социальный экологический проект \"Зелёный городок\".", greenTown);
        markerTest40.setUserData(point40);
        markerTest40.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest40);
        listPoints.add(point40);

//Бак для энергосберегающих ламп
        PlacemarkMapObject markerTest41 = mapObjects.addPlacemark(new Point(53.182449, 50.106167), ImageProvider.fromBitmap(bitmapLamp));
        RecyclingPoint point41 = new RecyclingPoint(new Point(53.182449, 50.106167), "ул. Братьев Коростелевых, д.3", "Бак для энергосберегающих ламп", "Бак жёлтого цвета для энергосберегающих ламп стоит рядом со стеной автомойки ТД «СамараЭлектро»", lamp);
        markerTest41.setUserData(point41);
        markerTest41.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest41);
        listPoints.add(point41);

//Родные люди, общественная организация
        PlacemarkMapObject markerTest42 = mapObjects.addPlacemark(new Point(53.186857, 50.099421), ImageProvider.fromBitmap(bitmapTiDoma));
        RecyclingPoint point42 = new RecyclingPoint(new Point(53.186857, 50.099421), "ул. Высоцкого, 8, 4 эт., оф. 419", "Родные люди, общественная организация", "Перед тем, как принести помощь, время и дату предварительно согласовать по тел 89033028888. Подробнее: http://vk.com/rodnye_ludi_samara", tiDoma);
        markerTest42.setUserData(point42);
        markerTest42.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest42);
        listPoints.add(point42);

//Эльдорадо, приём старой техники и батареек
        PlacemarkMapObject markerTest43 = mapObjects.addPlacemark(new Point(53.186668, 50.128131), ImageProvider.fromBitmap(bitmapMVideo));
        RecyclingPoint point43 = new RecyclingPoint(new Point(53.186668, 50.128131), "ул. Красноармейская, 131, ТРЦ Good`Ok", "Эльдорадо, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest43.setUserData(point43);
        markerTest43.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest43);
        listPoints.add(point43);

//Экомобиль
        PlacemarkMapObject markerTest44 = mapObjects.addPlacemark(new Point(53.190985, 50.141507), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point44 = new RecyclingPoint(new Point(53.190985, 50.141507), "ул. Владимирская, 35", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest44.setUserData(point44);
        markerTest44.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest44);
        listPoints.add(point44);

//Экомобиль
        PlacemarkMapObject markerTest45 = mapObjects.addPlacemark(new Point(53.197507, 50.157030), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point45 = new RecyclingPoint(new Point(53.197507, 50.157030), "ул. Тухачевского, 92", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest45.setUserData(point45);
        markerTest45.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest45);
        listPoints.add(point45);

//Контейнер "Добрые крышечки" и контейнер-«собиратор» для старых зубных щёток
        PlacemarkMapObject markerTest46 = mapObjects.addPlacemark(new Point(53.198268, 50.160794), ImageProvider.fromBitmap(bitmapLidOther));
        RecyclingPoint point46 = new RecyclingPoint(new Point(53.198268, 50.160794), "ул. Тухачевского, 233, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\" и контейнер-«собиратор» для старых зубных щёток", "Контейнер для крышечек и контейнер для старых зубных щёток находятся на входе в супермаркет Перекрёсток.", lidOther);
        markerTest46.setUserData(point46);
        markerTest46.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest46);
        listPoints.add(point46);

//Вагон, сервис по вывозу ненужных вещей
        String[] vagon = {"Appliances", "Other", "Clothes"}; //вагон - техника + одежда + другое
        Bitmap bitmapVagon = drawRingChartMarker(vagon);
        PlacemarkMapObject markerTest47 = mapObjects.addPlacemark(new Point(53.200490, 50.172292), ImageProvider.fromBitmap(bitmapVagon));
        RecyclingPoint point47 = new RecyclingPoint(new Point(53.200490, 50.172292), "ул. Гагарина, 24а", "Вагон, сервис по вывозу ненужных вещей", "Сервис по вывозу ненужных вещей. Принимают одежду, посуду, книги, технику, бижутерию, аксессуары, предметы интерьера, технику, в тч. крупную, мебель и многое другое.", vagon);
        markerTest47.setUserData(point47);
        markerTest47.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest47);
        listPoints.add(point47);

//Экомобиль
        PlacemarkMapObject markerTest48 = mapObjects.addPlacemark(new Point(53.190914, 50.179752), ImageProvider.fromBitmap(bitmapEcomobil));
        RecyclingPoint point48 = new RecyclingPoint(new Point(53.190914, 50.179752), "ул. Аэродромная, 13", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest48.setUserData(point48);
        markerTest48.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest48);
        listPoints.add(point48);

//Контейнер "Добрые крышечки" и контейнер-«собиратор» для старых зубных щёток
        PlacemarkMapObject markerTest49 = mapObjects.addPlacemark(new Point(53.190672, 50.179254), ImageProvider.fromBitmap(bitmapLidOther));
        RecyclingPoint point49 = new RecyclingPoint(new Point(53.190672, 50.179254), "ул. Аэродромная, 13, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\" и контейнер-«собиратор» для старых зубных щёток", "Контейнер для крышечек находится на входе в супермаркет Перекрёсток, контейнер для старых зубных щёток стоит в торговом зале, в отделе косметики.", lidOther);
        markerTest49.setUserData(point49);
        markerTest49.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest49);
        listPoints.add(point49);

//М-Видео, приём старой техники и батареек
        PlacemarkMapObject markerTest50 = mapObjects.addPlacemark(new Point(53.190323, 50.190476), ImageProvider.fromBitmap(bitmapMVideo));
        RecyclingPoint point50 = new RecyclingPoint(new Point(53.190323, 50.190476), "ул. Аэродромная, 47а, ТРК Аврора Молл", "М-Видео, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest50.setUserData(point50);
        markerTest50.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest50);
        listPoints.add(point50);

//Бокс для старых телефонов в Tele2
        String[] appliance = {"Appliances"}; // техника
        Bitmap bitmapAppliance = drawRingChartMarker(appliance);
        PlacemarkMapObject markerTest51 = mapObjects.addPlacemark(new Point(53.190323, 50.190476), ImageProvider.fromBitmap(bitmapAppliance));
        RecyclingPoint point51 = new RecyclingPoint(new Point(53.190323, 50.190476), "ул. Аэродромная, 47, ТРЦ Аврора Молл", "Бокс для старых телефонов в Tele2", "Бокс для старых телефонов в салоне Tele2.", appliance);
        markerTest51.setUserData(point51);
        markerTest51.addTapListener(placeMarkTapListener);
        listMarkers.add(markerTest51);
        listPoints.add(point51);

        return mapObjects;
    }

    //добавляем свой маркер
    public MapObjectCollection drawCustomMarker(@NonNull boolean[] checked2, Point point, String location, String name, String info){

        ArrayList<String> typesList = new ArrayList<String>();
        //засовываем в temp типы мусора, который выбрал чел
        for (int i = 0; i < checked2.length; i++) {
            if (checked2[i]) typesList.add(types[i]);
        }

        String[] newTypes = new String[typesList.size()];
        typesList.toArray(newTypes);

        Bitmap newBitmap = drawRingChartMarker(newTypes);

        PlacemarkMapObject newMarker = mapObjects.addPlacemark(point, ImageProvider.fromBitmap(newBitmap));
        newMarker.addTapListener(placeMarkTapListener);
        listCustomMarkers.add(newMarker);
        RecyclingPoint newPoint = new RecyclingPoint(point, location, name, info, newTypes);
        newMarker.setUserData(newPoint);
        listCustomPoints.add(newPoint);

        saveAndLoad.save(listPoints, listCustomPoints);

        return mapObjects;
    }

    //удаляем маркер
    public MapObjectCollection removeMarker(MapObject mapObject, @NonNull RecyclingPoint data){

        //проверяем каждый лист на содержание Placemark'a и RecyclingPoint'а
        //если он хранится именно в том или ином листе - удаляем
        if (listMarkers.contains(mapObject)) listMarkers.remove(mapObject);
        if (listPoints.contains(data)) listPoints.remove(data);
        if (listCustomMarkers.contains(mapObject)) listCustomMarkers.remove(mapObject);
        if (listCustomPoints.contains(data)) listCustomPoints.remove(data);
        //удаляем объект из коллекции mapObjects
        mapObjects.remove(mapObject);

        saveAndLoad.save(listPoints, listCustomPoints);

        return mapObjects;
    }

    //возвращаем все маркеры к исходному положению
    public MapObjectCollection resetMarkers(){

        //проверяем есть ли чо то в каждом ArrayList'е (чтобы не удалять из пустого)
        if(listCustomMarkers.size() > 0) listCustomMarkers.clear();
        //и чистим все точки которые там есть
        if(listCustomPoints.size() > 0) listCustomPoints.clear();
        if (listPoints.size() > 0) listPoints.clear();
        if (listMarkers.size() > 0) listMarkers.clear();
        //чистим коллекцию mapObject
        mapObjects.clear();

        saveAndLoad.reset();

        mapObjects = drawDefaultMarkers();

        return mapObjects;
    }

    //перевод svg в битмап и возвращаем bitmap маркера для выбранной точки на карте
    public Bitmap drawDestinationMarker(){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.custom_point);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    //Получаем id для каждой svg для добавления в информацию о точках
    public int getDrawableId(@NonNull String type){

        int drawableId = 0;

        switch (type) {
            case "Paper":
                drawableId = R.drawable.papers_selected;
                break;
            case "Glass":
                drawableId = R.drawable.glass_selected;
                break;
            case "Plastic":
                drawableId = R.drawable.plastic_selected;
                break;
            case "Metal":
                drawableId = R.drawable.metal_selected;
                break;
            case "Clothes":
                drawableId = R.drawable.cloths_selected;
                break;
            case "Other":
                drawableId = R.drawable.other_selected;
                break;
            case "Dangerous":
                drawableId = R.drawable.dangerous_selected;
                break;
            case "Batteries":
                drawableId = R.drawable.batteries_selected;
                break;
            case "Lamp":
                drawableId = R.drawable.lamp_selected;
                break;
            case "Appliances":
                drawableId = R.drawable.appliances_selected;
                break;
            case "Tetra":
                drawableId = R.drawable.tetra_selected;
                break;
            case "Lid":
                drawableId = R.drawable.lid_selected;
                break;
            case "Tires":
                drawableId = R.drawable.tires_selected;
                break;
        }
        return drawableId;
    }

    //отображение маркеров с выбранными типами мусорок
    public void searchTypes(boolean[] checked){
        //для дефолтных
        for (int i = 0; i < listPoints.size(); i++){
            listMarkers.get(i).setVisible(true);
        }
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < checked.length; i++) {
            if (checked[i]) temp.add(types[i]);
        }
        for (int i = 0; i < listPoints.size(); i++){
            String[] types = listPoints.get(i).getTypes();
            for (String type : temp){
                if(!Arrays.asList(types).contains(type)) listMarkers.get(i).setVisible(false);
            }
        }

        //для кастомных точек
        for (int i = 0; i < listCustomPoints.size(); i++){
            listCustomMarkers.get(i).setVisible(true);
        }
        ArrayList<String> tempCustom = new ArrayList<String>();
        for (int i = 0; i < checked.length; i++) {
            if (checked[i]) tempCustom.add(types[i]);
        }
        for (int i = 0; i < listCustomPoints.size(); i++){
            String[] types = listCustomPoints.get(i).getTypes();
            for (String type : tempCustom){
                if(!Arrays.asList(types).contains(type)) listCustomMarkers.get(i).setVisible(false);
            }
        }
    }

    //функция отрисовки кольцевой диаграммы (ring chart) - оно же обозначение того, какие мусорки в точке
    private Bitmap drawRingChartMarker(@NonNull String[] chosenTypes){

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
                    color = ContextCompat.getColor(context, R.color.paper_color);
                    break;
                case "Glass":
                    color = ContextCompat.getColor(context, R.color.glass_color);
                    break;
                case "Plastic":
                    color = ContextCompat.getColor(context, R.color.plastic_color);
                    break;
                case "Metal":
                    color = ContextCompat.getColor(context, R.color.metal_color);
                    break;
                case "Clothes":
                    color = ContextCompat.getColor(context, R.color.clothes_color);
                    break;
                case "Other":
                    color = ContextCompat.getColor(context, R.color.other_color);
                    break;
                case "Dangerous":
                    color = ContextCompat.getColor(context, R.color.dangerous_color);
                    break;
                case "Batteries":
                    color = ContextCompat.getColor(context, R.color.batteries_color);
                    break;
                case "Lamp":
                    color = ContextCompat.getColor(context, R.color.lamp_color);
                    break;
                case "Appliances":
                    color = ContextCompat.getColor(context, R.color.appliances_color);
                    break;
                case "Tetra":
                    color = ContextCompat.getColor(context, R.color.tetra_color);
                    break;
                case "Lid":
                    color = ContextCompat.getColor(context, R.color.lid_color);
                    break;
                case "Tires":
                    color = ContextCompat.getColor(context, R.color.tires_color);
                    break;
                default:
                    color = ContextCompat.getColor(context, R.color.theme);
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

    public SaveAndLoad getSaveAndLoad() {
        return saveAndLoad;
    }
}
