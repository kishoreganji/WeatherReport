package com.mobiquity.weatherreport.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mobiquity.weatherreport.models.CityDo;

@Database(entities = {CityDo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
}