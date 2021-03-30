package com.mobiquity.weatherreport.common;


import com.mobiquity.weatherreport.models.CityDo;

public interface CityClickListener {

    public void onCityClick(CityDo cityDo);

    public void onRemoveCityClick();

    public void addCity();
}