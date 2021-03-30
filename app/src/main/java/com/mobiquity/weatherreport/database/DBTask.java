package com.mobiquity.weatherreport.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mobiquity.weatherreport.common.DBUpdateListener;
import com.mobiquity.weatherreport.models.CityDo;

import java.util.ArrayList;

public class DBTask extends AsyncTask<CityDo, Void, Object> {

    private Context context;
    private DBUpdateListener dbUpdateListener;
    private String dbOP;
    private CityDo cityDo;
    public DBTask(Context context, String dbOP, DBUpdateListener dbUpdateListener){
        this.context = context;
        this.dbOP = dbOP;
        this.dbUpdateListener = dbUpdateListener;
    }

    @Override
    protected void onPreExecute() {
        // show loader
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(CityDo... cityDo) {
        if(dbOP.equalsIgnoreCase("delete")) {
            deleteCity(cityDo[0]);
        }
        else if(dbOP.equalsIgnoreCase("insert")) {
            insertCity(cityDo[0]);
        }
        else if(dbOP.equalsIgnoreCase("get")) {
            return getCities();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(dbOP.equalsIgnoreCase("get")) {
            ArrayList<CityDo> cityDos = (ArrayList<CityDo>) o;
            dbUpdateListener.getAllCities(cityDos);
        }
        else {
            dbUpdateListener.dbUpdate();
        }
    }

    private Object getCities(){
        return DatabaseClient.getInstance(context).getAppDatabase().cityDao().getAll();
    }

    private void insertCity(CityDo cityDo) {
        DatabaseClient.getInstance(context).getAppDatabase().cityDao().insert(cityDo);
    }

    private void deleteCity(CityDo cityDo){
        DatabaseClient.getInstance(context).getAppDatabase().cityDao().delete(cityDo);
    }
}
