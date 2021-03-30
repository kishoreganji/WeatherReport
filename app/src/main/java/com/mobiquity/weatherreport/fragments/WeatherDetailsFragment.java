package com.mobiquity.weatherreport.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiquity.weatherreport.R;
import com.mobiquity.weatherreport.common.AppConstants;
import com.mobiquity.weatherreport.common.CLoseFragmentListener;
import com.mobiquity.weatherreport.models.CityDo;
import com.mobiquity.weatherreport.models.CloudsDo;
import com.mobiquity.weatherreport.models.CoordDo;
import com.mobiquity.weatherreport.models.MainDo;
import com.mobiquity.weatherreport.models.SysDo;
import com.mobiquity.weatherreport.models.WeatherDetailsDo;
import com.mobiquity.weatherreport.models.WeatherDo;
import com.mobiquity.weatherreport.models.WindDo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class WeatherDetailsFragment extends Fragment {

    private Context mContext;
    private CityDo cityDo;
    private WeatherDetailsDo weatherDetailsDo;
    private TextView tvWeather, tvTemp, tvFeelsLike, tvPressure, tvTempMin, tvTempMax, tvHumidity, tvWind, tvSpeed,
            tvDeg, tvSys, tvType, tvId, tvCountry, tvSunrise, tvSunset, tvClose;
    private RecyclerView rvWeather;
    private CLoseFragmentListener cLoseFragmentListener;

    public WeatherDetailsFragment(Context context, CityDo cityDO, CLoseFragmentListener cLoseFragmentListener) {
        this.mContext = context;
        this.cityDo = cityDO;
        this.cLoseFragmentListener = cLoseFragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_details_layout, container, false);
        getActivity().setTitle("City Name : "+cityDo.getCityName());
        initialiseControls(view);

        new CityDataAsyncTask().execute(cityDo);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cLoseFragmentListener.closeFragment();
            }
        });
        return view;
    }

    private void bindData(WeatherDetailsDo weatherDetailsDo) {
        MainDo mainDo = weatherDetailsDo.getMain();

        tvTemp.setText("Temp : "+mainDo.getTemp());
        tvFeelsLike.setText("Feels Like : "+mainDo.getFeels_like());
        tvTempMin.setText("Temp Min : "+mainDo.getTemp_min());
        tvTempMax.setText("Temp Max : "+mainDo.getTemp_max());
        tvPressure.setText("Pressure : "+mainDo.getPressure());
        tvHumidity.setText("Humidity : "+mainDo.getHumidity());

        WindDo windDo = weatherDetailsDo.getWindDo();
        tvSpeed.setText("Speed : "+windDo.getSpeed());
        tvDeg.setText("Degrees : "+windDo.getDeg());

        SysDo sysDo = weatherDetailsDo.getSysDo();
        tvType.setText("Type : "+sysDo.getType());
        tvId.setText("Id : "+sysDo.getId());
        tvCountry.setText("Country : "+sysDo.getCountry());
        tvSunrise.setText("Sunrise : "+sysDo.getSunrise());
        tvSunset.setText("Sunset : "+sysDo.getSunset());


//        rvWeather;
    }

    private void initialiseControls(View view) {
        tvWeather           = view.findViewById(R.id.tvWeather);
        tvTemp              = view.findViewById(R.id.tvTemp);
        tvFeelsLike         = view.findViewById(R.id.tvFeelsLike);
        tvPressure          = view.findViewById(R.id.tvPressure);
        tvTempMin           = view.findViewById(R.id.tvTempMin);
        tvTempMax           = view.findViewById(R.id.tvTempMax);
        tvHumidity          = view.findViewById(R.id.tvHumidity);
        tvWind              = view.findViewById(R.id.tvWind);
        tvSpeed             = view.findViewById(R.id.tvSpeed);
        tvDeg               = view.findViewById(R.id.tvDeg);
        tvSys               = view.findViewById(R.id.tvSys);
        tvType              = view.findViewById(R.id.tvType);
        tvId                = view.findViewById(R.id.tvId);
        tvCountry           = view.findViewById(R.id.tvCountry);
        tvSunrise           = view.findViewById(R.id.tvSunrise);
        tvSunset            = view.findViewById(R.id.tvSunset);
        tvClose           = view.findViewById(R.id.tvClose);
        rvWeather         = view.findViewById(R.id.rvWeather);
        rvWeather.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private class CityDataAsyncTask extends AsyncTask<CityDo,  String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(CityDo... cityDos) {
            try {
                CityDo cityDo = cityDos[0];
                String url = AppConstants.Base_Url+AppConstants.Weather_Url+"?lat="+cityDo.getLattitude()
                        +"&lon="+cityDo.getLattitude()+"&appid="+AppConstants.Api_Id;
                URL mUrl = new URL(url);
                HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setUseCaches(false);
                httpConnection.setAllowUserInteraction(false);
                httpConnection.setConnectTimeout(100000);
                httpConnection.setDoInput(true);
                httpConnection.setReadTimeout(100000);

                httpConnection.connect();
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    String response = sb.toString();
                    return sb.toString();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("WeatherInfo", e.getMessage());
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WeatherDetailsDo weatherDetailsDo = parseResponse(s);
            bindData(weatherDetailsDo);
        }
    }

    private WeatherDetailsDo parseResponse(String response) {
        WeatherDetailsDo weatherDetailsDo = new WeatherDetailsDo();
        try {
            JSONObject jsonObject = new JSONObject(response);
            jsonObject.optString("cod");
            weatherDetailsDo.setBase(jsonObject.optString("base"));
            weatherDetailsDo.setVisibility(jsonObject.optInt("visibility"));
            weatherDetailsDo.setDt(jsonObject.optLong("dt"));
            weatherDetailsDo.setTimezone(jsonObject.optLong("timezone"));
            weatherDetailsDo.setId(jsonObject.optLong("id"));
            weatherDetailsDo.setCityName(jsonObject.optString("name"));

            JSONArray jsonArray = jsonObject.optJSONArray("weather");
            ArrayList<WeatherDo> weatherDos = new ArrayList<>();
            if(jsonArray != null && jsonArray.length() > 0) {
                for (int i=0; i<jsonArray.length(); i++) {
                    WeatherDo weatherDo = new WeatherDo();
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    weatherDo.setId(jsonObj.optInt("id"));
                    weatherDo.setMain(jsonObj.optString("main"));
                    weatherDo.setDescription(jsonObj.optString("description"));
                    weatherDo.setIcon(jsonObj.optString("icon"));
                    weatherDos.add(weatherDo);
                }
            }
            weatherDetailsDo.setWeather(weatherDos);

            JSONObject coordJsonObj = jsonObject.optJSONObject("coord");
            CoordDo coordDo = new CoordDo();
            coordDo.setLongitude(coordJsonObj.optDouble("lon"));
            coordDo.setLattitude(coordJsonObj.optDouble("lat"));
            weatherDetailsDo.setCoordDo(coordDo);

            JSONObject mainJsonObj = jsonObject.optJSONObject("main");
            MainDo mainDo = new MainDo();
            mainDo.setTemp(mainJsonObj.optDouble("temp"));
            mainDo.setFeels_like(mainJsonObj.optDouble("feels_like"));
            mainDo.setTemp_min(mainJsonObj.optDouble("temp_min"));
            mainDo.setTemp_max(mainJsonObj.optDouble("temp_max"));
            mainDo.setPressure(mainJsonObj.optInt("pressure"));
            mainDo.setHumidity(mainJsonObj.optInt("humidity"));
            weatherDetailsDo.setMain(mainDo);

            JSONObject windJsonObj = jsonObject.optJSONObject("wind");
            WindDo windDo = new WindDo();
            windDo.setSpeed(windJsonObj.optDouble("speed"));
            windDo.setDeg(windJsonObj.optInt("deg"));
            weatherDetailsDo.setWindDo(windDo);

            JSONObject cloudJsonObj = jsonObject.optJSONObject("clouds");
            CloudsDo cloudsDo = new CloudsDo();
            cloudsDo.setDt(cloudJsonObj.optLong("dt"));
            weatherDetailsDo.setCloudsDo(cloudsDo);

            JSONObject sysJsonObj = jsonObject.optJSONObject("sys");
            SysDo sysDo = new SysDo();
            sysDo.setType(sysJsonObj.optInt("type"));
            sysDo.setId(sysJsonObj.optInt("id"));
            sysDo.setCountry(sysJsonObj.optString("country"));
            sysDo.setSunrise(sysJsonObj.optLong("sunrise"));
            sysDo.setSunset(sysJsonObj.optLong("sunset"));
            weatherDetailsDo.setSysDo(sysDo);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherDetailsDo;
    }
}