package com.mobiquity.weatherreport.common;

import com.mobiquity.weatherreport.models.CityDo;

import java.util.ArrayList;

public interface DBUpdateListener {

    public void getAllCities(ArrayList<CityDo> cityDos);

    public void dbUpdate();
}
