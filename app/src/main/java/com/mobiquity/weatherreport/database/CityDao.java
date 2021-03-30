package com.mobiquity.weatherreport.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mobiquity.weatherreport.models.CityDo;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT * FROM CityDo")
    List<CityDo> getAll();

    @Insert
    void insert(CityDo cityDo);

    @Delete
    void delete(CityDo cityDo);

}