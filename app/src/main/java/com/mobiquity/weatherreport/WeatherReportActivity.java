package com.mobiquity.weatherreport;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobiquity.weatherreport.common.CLoseFragmentListener;
import com.mobiquity.weatherreport.common.CityAddListener;
import com.mobiquity.weatherreport.common.CityClickListener;
import com.mobiquity.weatherreport.common.DBUpdateListener;
import com.mobiquity.weatherreport.database.DBTask;
import com.mobiquity.weatherreport.fragments.HelpFragment;
import com.mobiquity.weatherreport.fragments.HomeFragment;
import com.mobiquity.weatherreport.fragments.MapsFragment;
import com.mobiquity.weatherreport.fragments.WeatherDetailsFragment;
import com.mobiquity.weatherreport.models.CityDo;

import java.util.ArrayList;

public class WeatherReportActivity extends AppCompatActivity implements CityClickListener {

    private FrameLayout flContainer;
    private HomeFragment homeFragment;
    private HelpFragment helpFragment;
    private WeatherDetailsFragment weatherDetailsFragment;
    private MapsFragment mapsFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        flContainer   = findViewById(R.id.flContainer);
        loadHomeFragment();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showHelp() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        helpFragment = new HelpFragment(this);
        fragmentTransaction.add(R.id.flContainer, helpFragment, "Hlep").addToBackStack("Help");
        fragmentTransaction.commit();
    }

    private void loadHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment(this,this);
        fragmentTransaction.replace(R.id.flContainer, homeFragment, "Home").addToBackStack("Home");
        fragmentTransaction.commit();
    }
    private void loadDetailsFragment(CityDo cityDo) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        weatherDetailsFragment = new WeatherDetailsFragment(this, cityDo, cLoseFragmentListener);
        fragmentTransaction.add(R.id.flContainer, weatherDetailsFragment, "WeatherDetails").addToBackStack("WeatherDetails");
        fragmentTransaction.commit();
    }
    private void loadMapsFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mapsFragment = new MapsFragment(this, cityAddListener);
        fragmentTransaction.add(R.id.flContainer, mapsFragment, "Map").addToBackStack("Map");
        fragmentTransaction.commit();
    }

    private CLoseFragmentListener cLoseFragmentListener = new CLoseFragmentListener(){
        @Override
        public void closeFragment() {
            loadHomeFragment();
        }
    };
    private CityAddListener cityAddListener = new CityAddListener() {
        @Override
        public void addedCity(final CityDo cityDo) {
            new DBTask(WeatherReportActivity.this, "get", new DBUpdateListener() {
                @Override
                public void getAllCities(ArrayList<CityDo> cityDos) {
                    if(cityDos!=null && cityDos.size() > 0){
                        for (int i=0;i<cityDos.size(); i++){
                            if(cityDos.get(i).getCityName().equalsIgnoreCase(cityDo.getCityName())){
                                Toast.makeText(WeatherReportActivity.this, "This city is already added!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    new DBTask(WeatherReportActivity.this, "insert", new DBUpdateListener() {
                        @Override
                        public void getAllCities(ArrayList<CityDo> cityDos) { }
                        @Override
                        public void dbUpdate() {
                            loadHomeFragment();
                        }
                    }).execute(cityDo);
                }

                @Override
                public void dbUpdate() {
                }
            }).execute();
        }
    };

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getFragments().size() == 1) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            finish();
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public void addCity() {
        loadMapsFragment();
    }

    @Override
    public void onCityClick(CityDo cityDo) {
        loadDetailsFragment(cityDo);
    }

    @Override
    public void onRemoveCityClick() {
        new DBTask(WeatherReportActivity.this, "get", new DBUpdateListener() {
            @Override
            public void getAllCities(ArrayList<CityDo> cityDos) {
                homeFragment.updateAdapter(cityDos);
            }

            @Override
            public void dbUpdate() {
            }
        }).execute();
    }
}
