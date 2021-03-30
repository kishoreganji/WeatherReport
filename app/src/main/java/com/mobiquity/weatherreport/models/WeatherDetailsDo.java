package com.mobiquity.weatherreport.models;

import java.util.ArrayList;

public class WeatherDetailsDo extends BaseDo {

    private ArrayList<WeatherDo> weather;
    private MainDo main;
    private SysDo sysDo;
    private WindDo windDo;
    private CloudsDo cloudsDo;
    private long timezone;
    private long id;
    private String cityName = "";
    private String name = "";
    private String base = "";
    private long dt;
    private int visibility;
    private int cod;
    private CoordDo coordDo;

    public CoordDo getCoordDo() {
        return coordDo;
    }

    public void setCoordDo(CoordDo coordDo) {
        this.coordDo = coordDo;
    }

    public ArrayList<WeatherDo> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherDo> weather) {
        this.weather = weather;
    }

    public MainDo getMain() {
        return main;
    }

    public void setMain(MainDo main) {
        this.main = main;
    }

    public SysDo getSysDo() {
        return sysDo;
    }

    public void setSysDo(SysDo sysDo) {
        this.sysDo = sysDo;
    }

    public WindDo getWindDo() {
        return windDo;
    }

    public void setWindDo(WindDo windDo) {
        this.windDo = windDo;
    }

    public CloudsDo getCloudsDo() {
        return cloudsDo;
    }

    public void setCloudsDo(CloudsDo cloudsDo) {
        this.cloudsDo = cloudsDo;
    }

    public Long getTimezone() {
        return timezone;
    }

    public void setTimezone(Long timezone) {
        this.timezone = timezone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}