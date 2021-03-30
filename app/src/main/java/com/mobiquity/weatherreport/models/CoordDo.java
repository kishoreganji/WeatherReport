package com.mobiquity.weatherreport.models;

public class CoordDo extends BaseDo {

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    private double longitude;
    private double lattitude;
}
