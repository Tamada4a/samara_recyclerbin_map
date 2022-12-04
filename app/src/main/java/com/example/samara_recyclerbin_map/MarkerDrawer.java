package com.example.samara_recyclerbin_map;

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

import com.yandex.mapkit.geometry.Point;
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

    private Context context;

    private MapObjectTapListener placeMarkTapListener;
    private MapObjectCollection mapObjects;

    private final String[] types = {"Paper", "Glass", "Plastic", "Metal", "Clothes", "Other", "Dangerous",
            "Batteries", "Lamp", "Appliances", "Tetra", "Lid", "Tires"};

    public MarkerDrawer(Context mainContext, MapObjectCollection mainMapObjects, MapObjectTapListener mainPlaceMarkTapListener){
        context = mainContext;
        mapObjects = mainMapObjects;
        placeMarkTapListener = mainPlaceMarkTapListener;
    }

    //отрисовываем маркеры по-дефолту
    public MapObjectCollection drawDefaultMarkers(){
//Батарейки DNS Русь
        String[] batteries = {"Batteries"}; //батарейки
        Bitmap bitmapBatteries = drawRingChartMarker(batteries); //получаем битмап из функции
        PlacemarkMapObject markerTest1 = mapObjects.addPlacemark(new Point(53.212857,50.182195), ImageProvider.fromBitmap(bitmapBatteries));
        listMarkers.add(markerTest1);
        //маркеру добавляем информацию
        RecyclingPoint point1 = new RecyclingPoint(new Point(53.212857,50.182195), "Московское ш., 29. ТЦ Русь. Магазин DNS", "Контейнер для старых батарей", "Контейнер для батареек Duracell находится на входе в гипермаркет DNS", batteries);
        markerTest1.setUserData(point1);
        listPoints.add(point1);
        //добавляем обработку нажатия на него. Потом через это можно будет сделать как на сайте
        markerTest1.addTapListener(placeMarkTapListener);

//Батарейки Пятерочка
        //Bitmap bitmap2 = drawRingChartMarker(batteries);
        PlacemarkMapObject markerTest2 = mapObjects.addPlacemark(new Point(53.209465, 50.175616), ImageProvider.fromBitmap(bitmapBatteries));
        listMarkers.add(markerTest2);
        RecyclingPoint point2 = new RecyclingPoint(new Point(53.209465, 50.175616), "Революционная улица, 64Б. Пятерочка ", "Контейнер для старых батарей", "Контейнер для старых батареек находится на входе в магазин Пятёрочка", batteries);
        markerTest2.setUserData(point2);
        listPoints.add(point2);
        markerTest2.addTapListener(placeMarkTapListener);

//Перекресток добрые крышечки
        String[] lidOther = {"Lid", "Other"}; // крышки, другое
        Bitmap bitmapLidOther = drawRingChartMarker(lidOther);
        PlacemarkMapObject markerTest3 = mapObjects.addPlacemark(new Point(53.210258, 50.173316), ImageProvider.fromBitmap(bitmapLidOther));
        listMarkers.add(markerTest3);
        RecyclingPoint point3 = new RecyclingPoint(new Point(53.210258, 50.173316), "Московское ш., 28, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\"", "Контейнер для крышечек в супермаркет Перекрёсток стоит напротив касс.", lidOther);
        markerTest3.setUserData(point3);
        listPoints.add(point3);
        markerTest3.addTapListener(placeMarkTapListener);

//Контейнер Пакмил
        String[] paper = {"Paper"}; //бумага
        Bitmap bitmapPaper = drawRingChartMarker(paper);
        PlacemarkMapObject markerTest4 = mapObjects.addPlacemark(new Point(53.217380, 50.169432), ImageProvider.fromBitmap(bitmapPaper));
        listMarkers.add(markerTest4);
        RecyclingPoint point4 = new RecyclingPoint(new Point(53.217380, 50.169432), "ул. Мичурина, 154", "Контейнер ПАКМИЛ для макулатуры", "Контейнер для макулатуры стоит по ул. Мичурина, между домами №148 и №150. Принимают все виды макулатуры, кроме цветного картона.", paper);
        markerTest4.setUserData(point4);
        listPoints.add(point4);
        markerTest4.addTapListener(placeMarkTapListener);

//Пакмил для бутылок
        String[] plastic = {"Plastic"}; //пластик
        Bitmap bitmapPlastic = drawRingChartMarker(plastic);
        PlacemarkMapObject markerTest5 = mapObjects.addPlacemark(new Point(53.216614, 50.167584), ImageProvider.fromBitmap(bitmapPlastic));
        listMarkers.add(markerTest5);
        RecyclingPoint point5 = new RecyclingPoint(new Point(53.216614, 50.167584), "ул. Мичурина, 138", "Контейнер для ПЭТ Пакмил", "Контейнер для ПЭТ бутылок. Находится через дорогу от дома по ул. Мичурина, 138, напротив магазина \"Перекресток\".", plastic);
        markerTest5.setUserData(point5);
        listPoints.add(point5);
        markerTest5.addTapListener(placeMarkTapListener);

//Контейнер ЭкоВоз для ПЭТ
        //Bitmap bitmap6 = drawRingChartMarker(plastic);
        PlacemarkMapObject markerTest6 = mapObjects.addPlacemark(new Point(53.216533, 50.166435), ImageProvider.fromBitmap(bitmapPlastic));
        listMarkers.add(markerTest6);
        RecyclingPoint point6 = new RecyclingPoint(new Point(53.216533, 50.166435), "ул. Лукачева, 17", "Контейнер ЭкоВоз для ПЭТ", "Желтый контейнер-сетка находится на территории школы № 58, калитка со стороны ул. Лукачева.", plastic);
        markerTest6.setUserData(point6);
        listPoints.add(point6);
        markerTest6.addTapListener(placeMarkTapListener);

//Контейнер "Добрые крышечки" и контейнер-«собиратор» для старых зубных щёток
        //Bitmap bitmap7 = drawRingChartMarker(lidOther);
        PlacemarkMapObject markerTest7 = mapObjects.addPlacemark(new Point(53.217014, 50.167603), ImageProvider.fromBitmap(bitmapLidOther));
        listMarkers.add(markerTest7);
        RecyclingPoint point7 = new RecyclingPoint(new Point(53.217014, 50.167603), "ул. Мичурина, 138, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\" и контейнер-«собиратор» для старых зубных щёток", "Контейнер для крышечек и контейнер для старых зубных щёток находятся на входе в супермаркет Перекрёсток.", lidOther);
        markerTest7.setUserData(point7);
        listPoints.add(point7);
        markerTest7.addTapListener(placeMarkTapListener);

//Контейнер для макулатуры
        //Bitmap bitmap8 = drawRingChartMarker(paper);
        PlacemarkMapObject markerTest8 = mapObjects.addPlacemark(new Point(53.217004, 50.166444), ImageProvider.fromBitmap(bitmapPaper));
        listMarkers.add(markerTest8);
        RecyclingPoint point8 = new RecyclingPoint(new Point(53.217004, 50.166444), "ул. Лукачева, 10", "Контейнер для макулатуры", "Контейнер для макулатуры возле дома по адресу Лукачева, 10. Принимают все виды макулатуры, кроме цветного картона.", paper);
        markerTest8.setUserData(point8);
        listPoints.add(point8);
        markerTest8.addTapListener(placeMarkTapListener);

//Контейнер "Добрые крышечки" от ЭкоСтройРесурс
        String[] lid = {"Lid"}; //крышки
        Bitmap bitmapLid = drawRingChartMarker(lid);
        PlacemarkMapObject markerTest9 = mapObjects.addPlacemark(new Point(53.223107, 50.166507), ImageProvider.fromBitmap(bitmapLid));
        listMarkers.add(markerTest9);
        RecyclingPoint point9 = new RecyclingPoint(new Point(53.223107, 50.166507), "ул. Ново-Садовая, 156, Станция переливания крови", "Контейнер \"Добрые крышечки\" от ЭкоСтройРесурс", "Контейнер для крышечек находится в вестибюле станции переливания крови, напротив входа.", lid);
        markerTest9.setUserData(point9);
        listPoints.add(point9);
        markerTest9.addTapListener(placeMarkTapListener);

//Экоцентр "Вторсырье на благотворительность"
        String[] all = {"Paper", "Glass", "Plastic", "Metal", "Clothes", "Other", "Dangerous", "Batteries", "Lamp", "Appliances", "Tetra", "Lid"}; //всё
        Bitmap bitmapAll = drawRingChartMarker(all);
        PlacemarkMapObject markerTest10 = mapObjects.addPlacemark(new Point(53.211205, 50.225176), ImageProvider.fromBitmap(bitmapAll));
        listMarkers.add(markerTest10);
        RecyclingPoint point10 = new RecyclingPoint(new Point(53.211205, 50.225176), "ул. 22 Партсъезда, 40а", "Экоцентр \"Вторсырье на благотворительность\"", "Гараж на территориии бывшего детского сада. Подробнее: http://vk.com/rsbor_samara.", all);
        markerTest10.setUserData(point10);
        listPoints.add(point10);
        markerTest10.addTapListener(placeMarkTapListener);

//Контейнер для старых батареек в Пятёрочке
        //Bitmap bitmap11 = drawRingChartMarker(batteries);
        PlacemarkMapObject markerTest11 = mapObjects.addPlacemark(new Point(53.206046, 50.162689), ImageProvider.fromBitmap(bitmapBatteries));
        listMarkers.add(markerTest11);
        RecyclingPoint point11 = new RecyclingPoint(new Point(53.206046, 50.162689), "ул. Масленникова, 40, магазин Пятёрочка", "Контейнер для старых батареек в Пятёрочке", "Контейнер для старых батареек находится на входе в магазин Пятёрочка", batteries);
        markerTest11.setUserData(point11);
        listPoints.add(point11);
        markerTest11.addTapListener(placeMarkTapListener);

//Приемный пункт «ВТОРМАРКЕТ»
        String[] vtormarket = {"Lid","Other","Paper","Metal","Plastic","Glass"}; //втормаркет
        Bitmap bitmapVtormarket = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest12 = mapObjects.addPlacemark(new Point(53.229716, 50.211306), ImageProvider.fromBitmap(bitmapVtormarket));
        listMarkers.add(markerTest12);
        RecyclingPoint point12 = new RecyclingPoint(new Point(53.229716, 50.211306), "ул. Стара Загора 57 А", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное).", vtormarket);
        markerTest12.setUserData(point12);
        listPoints.add(point12);
        markerTest12.addTapListener(placeMarkTapListener);

//М-Видео, приём старой техники и батареек
        String[] mvideo = {"Appliances", "Batteries"}; // мвидео - техника, батарейки
        Bitmap bitmapMVideo = drawRingChartMarker(mvideo);
        PlacemarkMapObject markerTest13 = mapObjects.addPlacemark(new Point(53.233208, 50.200764), ImageProvider.fromBitmap(bitmapMVideo));
        listMarkers.add(markerTest13);
        RecyclingPoint point13 = new RecyclingPoint(new Point(53.233208, 50.200764), "Московское ш., 81б, ТЦ Молл Парк Хаус", "М-Видео, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest13.setUserData(point13);
        listPoints.add(point13);
        markerTest13.addTapListener(placeMarkTapListener);

//Самарастеклотара
        String[] steklotara = {"Metal", "Plastic", "Glass", "Paper"}; // стеклотара - метал, пластик, стекло, бумага
        Bitmap bitmapSteklotara = drawRingChartMarker(steklotara);
        PlacemarkMapObject markerTest14 = mapObjects.addPlacemark(new Point(53.232530, 50.185264), ImageProvider.fromBitmap(bitmapSteklotara));
        listMarkers.add(markerTest14);
        RecyclingPoint point14 = new RecyclingPoint(new Point(53.232530, 50.185264), "ул. Ново-Садовая, 285а (рядом с ПЖРТ)", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest14.setUserData(point14);
        listPoints.add(point14);
        markerTest14.addTapListener(placeMarkTapListener);

//Самарастеклотара
        //Bitmap bitmap15 = drawRingChartMarker(steklotara);
        PlacemarkMapObject markerTest15 = mapObjects.addPlacemark(new Point(53.234519, 50.206248), ImageProvider.fromBitmap(bitmapSteklotara));
        listMarkers.add(markerTest15);
        RecyclingPoint point15 = new RecyclingPoint(new Point(53.234519, 50.206248), "Фадеева 42б", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest15.setUserData(point15);
        listPoints.add(point15);
        markerTest15.addTapListener(placeMarkTapListener);

//Эльдорадо, приём старой техники и батареек
        //Bitmap bitmap16 = drawRingChartMarker(mvideo);
        PlacemarkMapObject markerTest16 = mapObjects.addPlacemark(new Point(53.235090, 50.221969), ImageProvider.fromBitmap(bitmapMVideo));
        listMarkers.add(markerTest16);
        RecyclingPoint point16 = new RecyclingPoint(new Point(53.235090, 50.221969), "ул. Стара-Загора, 139, ТЦ Загорка", "Эльдорадо, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest16.setUserData(point16);
        listPoints.add(point16);
        markerTest16.addTapListener(placeMarkTapListener);

//Приемный пункт «ВТОРМАРКЕТ»
        //Bitmap bitmap17 = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest17 = mapObjects.addPlacemark(new Point(53.221479, 50.229362), ImageProvider.fromBitmap(bitmapVtormarket));
        listMarkers.add(markerTest17);
        RecyclingPoint point17 = new RecyclingPoint(new Point(53.221479, 50.229362), "Матросова, д. 92", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное), поддоны.", vtormarket);
        markerTest17.setUserData(point17);
        listPoints.add(point17);
        markerTest17.addTapListener(placeMarkTapListener);

//Экомобиль
        String[] ecomobil = {"Dangerous", "Other", "Clothes", "Appliance", "Plastic", "Glass", "Paper", "Metal", "Lid"}; // ЭкоМобиль
        Bitmap bitmapEcomobil = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest18 = mapObjects.addPlacemark(new Point(53.213929, 50.257167), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest18);
        RecyclingPoint point18 = new RecyclingPoint(new Point(53.213929, 50.257167), "ул. Физкультурная, 101, Дворец спорта", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest18.setUserData(point18);
        listPoints.add(point18);
        markerTest18.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap19 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest19 = mapObjects.addPlacemark(new Point(53.201332, 50.116049), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest19);
        RecyclingPoint point19 = new RecyclingPoint(new Point(53.201332, 50.116049), "ул. Самарская, 207", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest19.setUserData(point19);
        listPoints.add(point19);
        markerTest19.addTapListener(placeMarkTapListener);

//Ты дома, благотворительный фонд
        String[] tiDoma = {"Clothes", "Other"}; // Ты Дома - одежда + иное
        Bitmap bitmapTiDoma = drawRingChartMarker(tiDoma);
        PlacemarkMapObject markerTest20 = mapObjects.addPlacemark(new Point(53.196471, 50.124044), ImageProvider.fromBitmap(bitmapTiDoma));
        listMarkers.add(markerTest20);
        RecyclingPoint point20 = new RecyclingPoint(new Point(53.196471, 50.124044), "ул. Буянова, 135б", "Ты дома, благотворительный фонд", "Помощь бездомным и малообеспеченным жителям Самары. Подробнее: http://vk.com/tydomasamara", tiDoma);
        markerTest20.setUserData(point20);
        listPoints.add(point20);
        markerTest20.addTapListener(placeMarkTapListener);

//Ты не одна, центр помощи женщинам
        //Bitmap bitmap21 = drawRingChartMarker(tiDoma);
        PlacemarkMapObject markerTest21 = mapObjects.addPlacemark(new Point(53.184521, 50.104092), ImageProvider.fromBitmap(bitmapTiDoma));
        listMarkers.add(markerTest21);
        RecyclingPoint point21 = new RecyclingPoint(new Point(53.184521, 50.104092), "ул. Ленинградская, 100", "Ты не одна, центр помощи женщинам", "Принимают детскую и женскую одежду в хорошем состоянии. Подробнее: http://vk.com/centrtyneodna", tiDoma);
        markerTest21.setUserData(point21);
        listPoints.add(point21);
        markerTest21.addTapListener(placeMarkTapListener);

//Экоцентр "Вторсырье на благотворительность"
        //Bitmap bitmap22 = drawRingChartMarker(all);
        PlacemarkMapObject markerTest22 = mapObjects.addPlacemark(new Point(53.195937, 50.125400), ImageProvider.fromBitmap(bitmapAll));
        listMarkers.add(markerTest22);
        RecyclingPoint point22 = new RecyclingPoint(new Point(53.195937, 50.125400), "ул. Коммунистическая, 4б", "Экоцентр \"Вторсырье на благотворительность\"", "В ряду павильонов по ул. Коммунистическая, сразу после павильона \"Ты дома\" и \"Моя рыбка 63\". Подробнее: http://vk.com/rsbor_samara.", all);
        markerTest22.setUserData(point22);
        listPoints.add(point22);
        markerTest22.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap23 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest23 = mapObjects.addPlacemark(new Point(53.198101, 50.100795), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest23);
        RecyclingPoint point23 = new RecyclingPoint(new Point(53.198101, 50.100795), "ул. Вилоновская, 13", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest23.setUserData(point23);
        listPoints.add(point23);
        markerTest23.addTapListener(placeMarkTapListener);

//Фандомат в общественном центре Сбера
        String[] sber = {"Lid", "Metal", "Plastic"}; // сбер - крышки + метал + пластик
        Bitmap bitmapSber = drawRingChartMarker(sber);
        PlacemarkMapObject markerTest24 = mapObjects.addPlacemark(new Point(53.198047, 50.100912), ImageProvider.fromBitmap(bitmapSber));
        listMarkers.add(markerTest24);
        RecyclingPoint point24 = new RecyclingPoint(new Point(53.198047, 50.100912), "ул. Вилоновская, 13/ул. Чапаевская, 208", "Фандомат в общественном центре Сбера", "Фандомат находится в общественном центре Сбера", sber);
        markerTest24.setUserData(point24);
        listPoints.add(point24);
        markerTest24.addTapListener(placeMarkTapListener);

//Приемный пункт «ВТОРМАРКЕТ»
        //Bitmap bitmap25 = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest25 = mapObjects.addPlacemark(new Point(53.189711, 50.111305), ImageProvider.fromBitmap(bitmapVtormarket));
        listMarkers.add(markerTest25);
        RecyclingPoint point25 = new RecyclingPoint(new Point(53.189711, 50.111305), "ул. Братьев Коростелевых 47", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное), поддоны.", vtormarket);
        markerTest25.setUserData(point25);
        listPoints.add(point25);
        markerTest25.addTapListener(placeMarkTapListener);

//Утилизация шин
        String[] tires = {"Tires"}; // шины
        Bitmap bitmapTires = drawRingChartMarker(tires);
        PlacemarkMapObject markerTest26 = mapObjects.addPlacemark(new Point(53.267787, 50.401955), ImageProvider.fromBitmap(bitmapTires));
        listMarkers.add(markerTest26);
        RecyclingPoint point26 = new RecyclingPoint(new Point(53.267787, 50.401955), "п. Стройкерамика, ул. Свободы, 10а, \"Грузовой шиномонтаж\"", "Утилизация шин", "Самарский резиноперерабатывающий завод принимает на утилизацию и переработку изношенные и поврежденные автомобильные шины.", tires);
        markerTest26.setUserData(point26);
        listPoints.add(point26);
        markerTest26.addTapListener(placeMarkTapListener);

//Пункт приёма ВтоЦветЧерМет
        String[] vcchm = {"Appliance", "Batteries", "Other", "Metal", "Plastic", "Paper"}; // ВтоЦветЧерМет - техника + батарейки + иное + метал + пластик + бумага
        Bitmap bitmapVtoCvet = drawRingChartMarker(vcchm);
        PlacemarkMapObject markerTest27 = mapObjects.addPlacemark(new Point(53.256595, 50.365241), ImageProvider.fromBitmap(bitmapVtoCvet));
        listMarkers.add(markerTest27);
        RecyclingPoint point27 = new RecyclingPoint(new Point(53.256595, 50.365241), "пгт. Смышляевка ул. Механиков, 3 (напротив Деловых Линий)", "Пункт приёма ВтоЦветЧерМет", "Приём лома чёрных, цветных металлов, пленки, пластика, электронного лома, макулатуры", vcchm);
        markerTest27.setUserData(point27);
        listPoints.add(point27);
        markerTest27.addTapListener(placeMarkTapListener);

//Приемный пункт «ВТОРМАРКЕТ»
        //Bitmap bitmap28 = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest28 = mapObjects.addPlacemark(new Point(53.246067, 50.301299), ImageProvider.fromBitmap(bitmapVtormarket));
        listMarkers.add(markerTest28);
        RecyclingPoint point28 = new RecyclingPoint(new Point(53.246067, 50.301299), "ул. Товарная, 70", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное).", vtormarket);
        markerTest28.setUserData(point28);
        listPoints.add(point28);
        markerTest28.addTapListener(placeMarkTapListener);

//Пункт приёма ВтоЦветЧерМет
        String[] vcchm2 = {"Appliance",  "Metal", "Plastic", "Paper"}; // ВтоЦветЧерМет - техника + батарейки + иное + метал + пластик + бумага
        Bitmap bitmapVtoCvet2 = drawRingChartMarker(vcchm2);
        PlacemarkMapObject markerTest29 = mapObjects.addPlacemark(new Point(53.251574, 50.308575), ImageProvider.fromBitmap(bitmapVtoCvet2));
        listMarkers.add(markerTest29);
        RecyclingPoint point29 = new RecyclingPoint(new Point(53.251574, 50.308575), "Магистральная, 154Б", "Пункт приёма ВтоЦветЧерМет", "Приём лома чёрных, цветных металлов, пленки, пластика, электронного лома, макулатуры", vcchm2);
        markerTest29.setUserData(point29);
        listPoints.add(point29);
        markerTest29.addTapListener(placeMarkTapListener);

//Самарастеклотара
        //Bitmap bitmap30 = drawRingChartMarker(steklotara);
        PlacemarkMapObject markerTest30 = mapObjects.addPlacemark(new Point(53.238070, 50.282866), ImageProvider.fromBitmap(bitmapSteklotara));
        listMarkers.add(markerTest30);
        RecyclingPoint point30 = new RecyclingPoint(new Point(53.238070, 50.282866), "Свободы 236Б", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest30.setUserData(point30);
        listPoints.add(point30);
        markerTest30.addTapListener(placeMarkTapListener);

//Самарастеклотара
        //Bitmap bitmap31 = drawRingChartMarker(steklotara);
        PlacemarkMapObject markerTest31 = mapObjects.addPlacemark(new Point(53.225975, 50.276533), ImageProvider.fromBitmap(bitmapSteklotara));
        listMarkers.add(markerTest31);
        RecyclingPoint point31 = new RecyclingPoint(new Point(53.225975, 50.276533), "ул. Победы 164", "Самарастеклотара", "Пункт приёма вторсырья за деньги. Режим работы:пн-сб с 8 до 13:00", steklotara);
        markerTest31.setUserData(point31);
        listPoints.add(point31);
        markerTest31.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap32 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest32 = mapObjects.addPlacemark(new Point(53.233953, 50.274700), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest32);
        RecyclingPoint point32 = new RecyclingPoint(new Point(53.233953, 50.274700), "ул. Марии Авейде, 35, тц Октябрь", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest32.setUserData(point32);
        listPoints.add(point32);
        markerTest32.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap33 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest33 = mapObjects.addPlacemark(new Point(53.224692, 50.271960), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest33);
        RecyclingPoint point33 = new RecyclingPoint(new Point(53.224692, 50.271960), "ул. Советская, 2", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest33.setUserData(point33);
        listPoints.add(point33);
        markerTest33.addTapListener(placeMarkTapListener);

//Бак для энергосберегающих ламп
        String[] lamp = {"Lamp"}; // лампы
        Bitmap bitmapLamp = drawRingChartMarker(lamp);
        PlacemarkMapObject markerTest34 = mapObjects.addPlacemark(new Point(53.224692, 50.271960), ImageProvider.fromBitmap(bitmapLamp));
        listMarkers.add(markerTest34);
        RecyclingPoint point34 = new RecyclingPoint(new Point(53.224692, 50.271960), "ул. Ново-Вокзальная, 2а", "Бак для энергосберегающих ламп", "Контейнер для сбора отработанных энергосберегающих ламп располагается внутри здания ТЦ «На птичке», сразу около входа на 1 этаже (вход со стороны стоянки) под лестницей.", lamp);
        markerTest34.setUserData(point34);
        listPoints.add(point34);
        markerTest34.addTapListener(placeMarkTapListener);

//Бак для энергосберегающих ламп
        //Bitmap bitmap35 = drawRingChartMarker(lamp);
        PlacemarkMapObject markerTest35 = mapObjects.addPlacemark(new Point(53.202022, 50.223730), ImageProvider.fromBitmap(bitmapLamp));
        listMarkers.add(markerTest35);
        RecyclingPoint point35 = new RecyclingPoint(new Point(53.202022, 50.223730), "ул. Гагарина, д. 122", "Бак для энергосберегающих ламп", "Контейнер для энергосберегающих ламп можно обнаружить спустившись по лестнице в магазин электроматериалов «Светосила - М».", lamp);
        markerTest35.setUserData(point35);
        listPoints.add(point35);
        markerTest35.addTapListener(placeMarkTapListener);

//КРОНА
        String[] krona = {"Appliance", "Metal", "Plastic"}; // крона - техника + метал + пластик
        Bitmap bitmapKrona = drawRingChartMarker(krona);
        PlacemarkMapObject markerTest36 = mapObjects.addPlacemark(new Point(53.172600, 50.194610), ImageProvider.fromBitmap(bitmapKrona));
        listMarkers.add(markerTest36);
        RecyclingPoint point36 = new RecyclingPoint(new Point(53.172600, 50.194610), "Заводское шоссе, 1г", "КРОНА, ООО", "Принимает металлолом, пластик. В том числе электронный лом", krona);
        markerTest36.setUserData(point36);
        listPoints.add(point36);
        markerTest36.addTapListener(placeMarkTapListener);

//Тайммет Вторчермет
        String[] timemet = {"Appliance", "Metal"}; // тайммет - техника + метал
        Bitmap bitmapTimemet = drawRingChartMarker(timemet);
        PlacemarkMapObject markerTest37 = mapObjects.addPlacemark(new Point(53.179308, 50.208755), ImageProvider.fromBitmap(bitmapTimemet));
        listMarkers.add(markerTest37);
        RecyclingPoint point37 = new RecyclingPoint(new Point(53.179308, 50.208755), "Заводское ш., 5 к.1", "Тайммет Вторчермет", "Принимает металлолом, пластик. В том числе электронный лом", timemet);
        markerTest37.setUserData(point37);
        listPoints.add(point37);
        markerTest37.addTapListener(placeMarkTapListener);

//ООО «ВТОРМАРКЕТ»
        String[] ooovtor = {"Plastic", "Paper", "Other"}; // ООО втормаркет - пластик + бумага + другое
        Bitmap bitmapOVtor = drawRingChartMarker(ooovtor);
        PlacemarkMapObject markerTest38 = mapObjects.addPlacemark(new Point(53.179238, 50.207461), ImageProvider.fromBitmap(bitmapOVtor));
        listMarkers.add(markerTest38);
        RecyclingPoint point38 = new RecyclingPoint(new Point(53.179238, 50.207461), "Заводское шоссе, 5Б лит T", "Приемный пункт «ВТОРМАРКЕТ»", "Макулатура, лом пластмасс, поддоны деревянные, текстиль (ватные матрасы б/у, отходы одеял, отходы стежки, обрезь синтепона.)Пленка", ooovtor);
        markerTest38.setUserData(point38);
        listPoints.add(point38);
        markerTest38.addTapListener(placeMarkTapListener);

//Приемный пункт «ВТОРМАРКЕТ»
        //Bitmap bitmap39 = drawRingChartMarker(vtormarket);
        PlacemarkMapObject markerTest39 = mapObjects.addPlacemark(new Point(53.176178, 50.209051), ImageProvider.fromBitmap(bitmapVtormarket));
        listMarkers.add(markerTest39);
        RecyclingPoint point39 = new RecyclingPoint(new Point(53.176178, 50.209051), "Заводское Шоссе, 5Б к7", "Приемный пункт «ВТОРМАРКЕТ»", "Приём вторсырья весом от 1 кг за деньги. Макулатура, книги, журналы, картон, канистры, флаконы, ведра белые из-под пищевых продуктов, ПНД И ПВХ трубы, фруктовые ящики ПП, бытовой металлолом, ПЭТ бутылки, алюминиевые банки, стекло (бутылки, банки, оконное).", vtormarket);
        markerTest39.setUserData(point39);
        listPoints.add(point39);
        markerTest39.addTapListener(placeMarkTapListener);

//Зеленый городок, пункт приёма вторсырья
        String[] greenTown = {"Lid", "Appliance", "Other", "Metal", "Plastic", "Glass", "Paper"}; // зеленый городок - крышки + техника + другое + метал + пластик + стекло + бумага
        Bitmap bitmapGreenTown = drawRingChartMarker(greenTown);
        PlacemarkMapObject markerTest40 = mapObjects.addPlacemark(new Point(53.181407, 50.110218), ImageProvider.fromBitmap(bitmapGreenTown));
        listMarkers.add(markerTest40);
        RecyclingPoint point40 = new RecyclingPoint(new Point(53.181407, 50.110218), "ул.Неверова, 1а", "Зеленый городок, пункт приёма вторсырья", "Социальный экологический проект \"Зелёный городок\".", greenTown);
        markerTest40.setUserData(point40);
        listPoints.add(point40);
        markerTest40.addTapListener(placeMarkTapListener);

//Бак для энергосберегающих ламп
        //Bitmap bitmap41= drawRingChartMarker(lamp);
        PlacemarkMapObject markerTest41 = mapObjects.addPlacemark(new Point(53.182449, 50.106167), ImageProvider.fromBitmap(bitmapLamp));
        listMarkers.add(markerTest41);
        RecyclingPoint point41 = new RecyclingPoint(new Point(53.182449, 50.106167), "ул. Братьев Коростелевых, д.3", "Бак для энергосберегающих ламп", "Бак жёлтого цвета для энергосберегающих ламп стоит рядом со стеной автомойки ТД «СамараЭлектро»", lamp);
        markerTest41.setUserData(point41);
        listPoints.add(point41);
        markerTest41.addTapListener(placeMarkTapListener);

//Родные люди, общественная организация
        //Bitmap bitmap42 = drawRingChartMarker(tiDoma);
        PlacemarkMapObject markerTest42 = mapObjects.addPlacemark(new Point(53.186857, 50.099421), ImageProvider.fromBitmap(bitmapTiDoma));
        listMarkers.add(markerTest42);
        RecyclingPoint point42 = new RecyclingPoint(new Point(53.186857, 50.099421), "ул. Высоцкого, 8, 4 эт., оф. 419", "Родные люди, общественная организация", "Перед тем, как принести помощь, время и дату предварительно согласовать по тел 89033028888. Подробнее: http://vk.com/rodnye_ludi_samara", tiDoma);
        markerTest42.setUserData(point42);
        listPoints.add(point42);
        markerTest42.addTapListener(placeMarkTapListener);

//Эльдорадо, приём старой техники и батареек
        //Bitmap bitmap43 = drawRingChartMarker(mvideo);
        PlacemarkMapObject markerTest43 = mapObjects.addPlacemark(new Point(53.186668, 50.128131), ImageProvider.fromBitmap(bitmapMVideo));
        listMarkers.add(markerTest43);
        RecyclingPoint point43 = new RecyclingPoint(new Point(53.186668, 50.128131), "ул. Красноармейская, 131, ТРЦ Good`Ok", "Эльдорадо, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest43.setUserData(point43);
        listPoints.add(point43);
        markerTest43.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap44 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest44 = mapObjects.addPlacemark(new Point(53.190985, 50.141507), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest44);
        RecyclingPoint point44 = new RecyclingPoint(new Point(53.190985, 50.141507), "ул. Владимирская, 35", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest44.setUserData(point44);
        listPoints.add(point44);
        markerTest44.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap45 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest45 = mapObjects.addPlacemark(new Point(53.197507, 50.157030), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest45);
        RecyclingPoint point45 = new RecyclingPoint(new Point(53.197507, 50.157030), "ул. Тухачевского, 92", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest45.setUserData(point45);
        listPoints.add(point45);
        markerTest45.addTapListener(placeMarkTapListener);

//Контейнер "Добрые крышечки" и контейнер-«собиратор» для старых зубных щёток
        //Bitmap bitmap46 = drawRingChartMarker(lidOther);
        PlacemarkMapObject markerTest46 = mapObjects.addPlacemark(new Point(53.198268, 50.160794), ImageProvider.fromBitmap(bitmapLidOther));
        listMarkers.add(markerTest46);
        RecyclingPoint point46 = new RecyclingPoint(new Point(53.198268, 50.160794), "ул. Тухачевского, 233, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\" и контейнер-«собиратор» для старых зубных щёток", "Контейнер для крышечек и контейнер для старых зубных щёток находятся на входе в супермаркет Перекрёсток.", lidOther);
        markerTest46.setUserData(point46);
        listPoints.add(point46);
        markerTest46.addTapListener(placeMarkTapListener);

//Вагон, сервис по вывозу ненужных вещей
        String[] vagon = {"Appliance", "Other", "Clothes"}; //вагон - техника + одежда + другое
        Bitmap bitmapVagon = drawRingChartMarker(vagon);
        PlacemarkMapObject markerTest47 = mapObjects.addPlacemark(new Point(53.200490, 50.172292), ImageProvider.fromBitmap(bitmapVagon));
        listMarkers.add(markerTest47);
        RecyclingPoint point47 = new RecyclingPoint(new Point(53.200490, 50.172292), "ул. Гагарина, 24а", "Вагон, сервис по вывозу ненужных вещей", "Сервис по вывозу ненужных вещей. Принимают одежду, посуду, книги, технику, бижутерию, аксессуары, предметы интерьера, технику, в тч. крупную, мебель и многое другое.", vagon);
        markerTest47.setUserData(point47);
        listPoints.add(point47);
        markerTest47.addTapListener(placeMarkTapListener);

//Экомобиль
        //Bitmap bitmap48 = drawRingChartMarker(ecomobil);
        PlacemarkMapObject markerTest48 = mapObjects.addPlacemark(new Point(53.190914, 50.179752), ImageProvider.fromBitmap(bitmapEcomobil));
        listMarkers.add(markerTest48);
        RecyclingPoint point48 = new RecyclingPoint(new Point(53.190914, 50.179752), "ул. Аэродромная, 13", "Экомобиль", "ЭкоМобиль – мобильный пункт бесплатного приема вторичного сырья. Уточняйте время работы на https://vk.com/ekovoz63", ecomobil);
        markerTest48.setUserData(point48);
        listPoints.add(point48);
        markerTest48.addTapListener(placeMarkTapListener);

//Контейнер "Добрые крышечки" и контейнер-«собиратор» для старых зубных щёток
        //Bitmap bitmap49 = drawRingChartMarker(lidOther);
        PlacemarkMapObject markerTest49 = mapObjects.addPlacemark(new Point(53.190672, 50.179254), ImageProvider.fromBitmap(bitmapLidOther));
        listMarkers.add(markerTest49);
        RecyclingPoint point49 = new RecyclingPoint(new Point(53.190672, 50.179254), "ул. Аэродромная, 13, супермаркет Перекрёсток", "Контейнер \"Добрые крышечки\" и контейнер-«собиратор» для старых зубных щёток", "Контейнер для крышечек находится на входе в супермаркет Перекрёсток, контейнер для старых зубных щёток стоит в торговом зале, в отделе косметики.", lidOther);
        markerTest49.setUserData(point49);
        listPoints.add(point49);
        markerTest49.addTapListener(placeMarkTapListener);

//М-Видео, приём старой техники и батареек
        //Bitmap bitmap50 = drawRingChartMarker(mvideo);
        PlacemarkMapObject markerTest50 = mapObjects.addPlacemark(new Point(53.190323, 50.190476), ImageProvider.fromBitmap(bitmapMVideo));
        listMarkers.add(markerTest50);
        RecyclingPoint point50 = new RecyclingPoint(new Point(53.190323, 50.190476), "ул. Аэродромная, 47а, ТРК Аврора Молл", "М-Видео, приём старой техники и батареек", "В магазин можно приносить мелкую и среднюю бытовую технику и электронику. Для сдачи техники следует обратиться на стойку информации. Также в магазине установлен контейнер Duracell для отработанных батареек.", mvideo);
        markerTest50.setUserData(point50);
        listPoints.add(point50);
        markerTest50.addTapListener(placeMarkTapListener);

//Бокс для старых телефонов в Tele2
        String[] appliance = {"Appliance"}; // техника
        Bitmap bitmapAppliance = drawRingChartMarker(appliance);
        PlacemarkMapObject markerTest51 = mapObjects.addPlacemark(new Point(53.190323, 50.190476), ImageProvider.fromBitmap(bitmapAppliance));
        listMarkers.add(markerTest51);
        RecyclingPoint point51 = new RecyclingPoint(new Point(53.190323, 50.190476), "ул. Аэродромная, 47, ТРЦ Аврора Молл", "Бокс для старых телефонов в Tele2", "Бокс для старых телефонов в салоне Tele2.", appliance);
        markerTest51.setUserData(point51);
        listPoints.add(point51);
        markerTest51.addTapListener(placeMarkTapListener);

        return mapObjects;
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

    //отображение маркеров с выбранными типами мусорок
    public void searchTypes(boolean[] checked){
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
    }
}
