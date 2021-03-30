package com.mobiquity.weatherreport.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobiquity.weatherreport.R;
import com.mobiquity.weatherreport.WeatherReportActivity;
import com.mobiquity.weatherreport.common.CityAddListener;
import com.mobiquity.weatherreport.common.DBUpdateListener;
import com.mobiquity.weatherreport.database.DBTask;
import com.mobiquity.weatherreport.database.DatabaseClient;
import com.mobiquity.weatherreport.models.CityDo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;
    private Button btnAdd;
    private CityAddListener cityAddListener;
    private Context mContext;

    public MapsFragment(Context context, CityAddListener cityAddListener) {
        this.mContext = context;
        this.cityAddListener =  cityAddListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        btnAdd          = view.findViewById(R.id.btnAdd);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Location Permission Needed")
                        .setMessage("The Weather Report needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 121);
                            }
                        }).create() .show();
            }
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 121);
            }
        }
        else {
            turnGPSOn();
            SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mMapFragment.getMapAsync(this);
            buildGoogleApiClient();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityDo!=null) {
                    cityAddListener.addedCity(cityDo);
                }
                else {
                    Toast.makeText(getActivity(), "Please select valid city", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private CityDo cityDo;
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void turnGPSOn(){
        startActivityForResult( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS), 122); ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 121 ){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 121);
            }
            else {
                turnGPSOn();
                SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                mMapFragment.getMapAsync(this);
                buildGoogleApiClient();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        String address = getAddress(location.getLatitude(), location.getLongitude());
        Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
        markerOptions.title("i'm here");
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setInterval(115 * 60 * 1000);//

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            String address = getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(),
                                                                mLastLocation.getLongitude()), 10));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
//                markerOptions.title(getAddress(latLng.latitude,latLng.longitude));
//                map.clear();
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                map.addMarker(markerOptions);
            }
        });
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = map.getCameraPosition().target;
                cityDo = new CityDo();
                String cityName = getAddress(latLng.latitude, latLng.longitude);
                if(cityName!=null && !cityName.equalsIgnoreCase("")){
                    cityDo.setCityName(cityName);
                    cityDo.setLattitude(latLng.latitude);
                    cityDo.setLongitude(latLng.longitude);
                    Toast.makeText(getActivity(), "Selected city : "+cityName, Toast.LENGTH_SHORT).show();
                }
                else {
                    cityDo = null;
                    Toast.makeText(getActivity(), "Invalid area selected : "+cityName, Toast.LENGTH_SHORT).show();
                }
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        new DBTask(getActivity(), "get", new DBUpdateListener() {
            @Override
            public void getAllCities(ArrayList<CityDo> cityDos) {
                if(cityDos != null && cityDos.size() > 0){
                    for (int i=0; i<cityDos.size(); i++){
                        LatLng latLng = new LatLng(cityDos.get(i).getLattitude(), cityDos.get(i).getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(getAddress(latLng.latitude, latLng.longitude)));
                    }
                }
            }

            @Override
            public void dbUpdate() {
            }
        }).execute();

    }


    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    private String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String cityName = address.getLocality();
                return cityName;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}