package com.mobiquity.weatherreport.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CityDo extends BaseDo{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "cityId")
    private int cityId;
    @ColumnInfo(name = "cityName")
    private String cityName = "";
    @ColumnInfo(name = "lattitude")
    private double lattitude;
    @ColumnInfo(name = "longitude")
    private double longitude;

    public CityDo(){}

    public CityDo(int cityId, String cityName, double lattitude, double longitude) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.lattitude = lattitude;
        this.longitude = longitude;

    }
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
