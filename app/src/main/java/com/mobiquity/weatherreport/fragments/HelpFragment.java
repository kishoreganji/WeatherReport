package com.mobiquity.weatherreport.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

public class HelpFragment extends Fragment {

    private Context mContext;
    private WebView webView;
    private static final String helpUrl = "https://www.accuweather.com/en/in/hyderabad/202190/hourly-weather-forecast/202190";

    public HelpFragment(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_layout, container, false);
        webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(helpUrl);
        return view;
    }

}