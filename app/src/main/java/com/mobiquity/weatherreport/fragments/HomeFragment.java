package com.mobiquity.weatherreport.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobiquity.weatherreport.R;
import com.mobiquity.weatherreport.adapters.CitiesRecyclerAdapter;
import com.mobiquity.weatherreport.common.CityClickListener;
import com.mobiquity.weatherreport.common.DBUpdateListener;
import com.mobiquity.weatherreport.database.DBTask;
import com.mobiquity.weatherreport.models.CityDo;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private CitiesRecyclerAdapter adapter;
    private CityClickListener cityClickListener;
    private RecyclerView recyclerView;
    private TextView tvNoCities;
    private Context mContext;
    private FloatingActionButton fabAddCity;

    public HomeFragment(Context mContext, CityClickListener cityClickListener){
        this.mContext = mContext;
        this.cityClickListener = cityClickListener;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvCities);
        tvNoCities = view.findViewById(R.id.tvNoCities);
        fabAddCity = view.findViewById(R.id.fabAddCity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        loadAllCities();
        fabAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityClickListener.addCity();
            }
        });

        return view;
    }

    private void loadAllCities() {
        new DBTask(getActivity(), "get", new DBUpdateListener() {
            @Override
            public void getAllCities(ArrayList<CityDo> cityDos) {
                setupUI(cityDos);
            }

            @Override
            public void dbUpdate() {

            }
        }).execute();
    }

    private void setupUI(ArrayList<CityDo> cityDos) {
        if(cityDos != null && cityDos.size() > 0) {
            adapter = new CitiesRecyclerAdapter(mContext, cityDos, cityClickListener);
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            tvNoCities.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            tvNoCities.setVisibility(View.VISIBLE);
        }
    }

    public void updateAdapter(ArrayList<CityDo> cityDos) {
        if(cityDos != null && cityDos.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvNoCities.setVisibility(View.GONE);
            adapter.updateAdapter(cityDos);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            tvNoCities.setVisibility(View.VISIBLE);
        }
    }

    private void insertCityDo() {

    }
}