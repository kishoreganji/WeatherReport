package com.mobiquity.weatherreport.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiquity.weatherreport.R;
import com.mobiquity.weatherreport.common.CityClickListener;
import com.mobiquity.weatherreport.common.DBUpdateListener;
import com.mobiquity.weatherreport.database.DBTask;
import com.mobiquity.weatherreport.models.CityDo;

import java.util.ArrayList;

public class CitiesRecyclerAdapter extends RecyclerView.Adapter<CitiesRecyclerAdapter.CitiesHolder>{

    private Context context;
    private ArrayList<CityDo> citiesList;
    private CityClickListener cityClickListener;

    public CitiesRecyclerAdapter(Context context, ArrayList<CityDo> cityDos , CityClickListener cityClickListener) {
        this.context = context;
        this.citiesList = cityDos;
        this.cityClickListener = cityClickListener;
    }

    public void updateAdapter(ArrayList<CityDo> cityDOs) {
        this.citiesList = cityDOs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CitiesHolder(LayoutInflater.from(context).inflate(R.layout.city_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesHolder holder, final int position) {
        holder.tvCity.setText(citiesList.get(position).getCityName());
        holder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityClickListener.onCityClick(citiesList.get(position));
            }
        });
        holder.tvRemoveCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DBTask(context, "delete", new DBUpdateListener() {
                    @Override
                    public void getAllCities(ArrayList<CityDo> cityDos) {

                    }

                    @Override
                    public void dbUpdate() {
                        cityClickListener.onRemoveCityClick();
                    }
                }).execute(citiesList.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return citiesList!=null?citiesList.size():0;
    }

    public static class CitiesHolder extends RecyclerView.ViewHolder {

        private TextView tvCity, tvRemoveCity;

        public CitiesHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvRemoveCity = itemView.findViewById(R.id.tvRemoveCity);
        }
    }
}
