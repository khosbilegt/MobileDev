package com.example.weatherapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DailyFragment extends Fragment {
    private final String API_KEY = "d405cb60bda80d5315053714d415c638";
    public List<Weather> weatherList = new ArrayList<Weather>();
    public List<Weather> dailyWeatherList = new ArrayList<Weather>();

    public DailyFragment() {
        super(R.layout.fragment_daily);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    public void setView(List<Weather> weatherList) {
        this.weatherList = weatherList;
        getAverageWeathers();
        RecyclerView dailyView = getView().findViewById(R.id.dailyView);
        DailyAdapter adapter = new DailyAdapter(dailyWeatherList, this);
        dailyView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        dailyView.setLayoutManager(manager);
    }

    private void getAverageWeathers() {
        List<List<Weather>> weatherPerDays = new ArrayList<List<Weather>>();
        int day = weatherList.get(0).getDay();
        List<Weather> tempList = new LinkedList<Weather>();
        for(int i = 0; i < weatherList.size(); i++) {
            Weather weather = weatherList.get(i);
            if(day != weather.getDay()) {
                weatherPerDays.add(tempList);
                tempList = new LinkedList<Weather>();
                day++;
            } else {
                tempList.add(weather);
            }
        }
        weatherPerDays.add(tempList);
        for(int i = 0; i < weatherPerDays.size(); i++) {
            List<Weather> weatherDay = weatherPerDays.get(i);
            List<String> iconDay = new ArrayList<String>();
            Weather newWeather = new Weather();
            float totalTemp = 0;
            int count = 0;

            for(int j = 0; j < weatherDay.size(); j++) {
                Weather weather = weatherDay.get(j);
                if(j == 0) {
                    newWeather.setDateTimeString(weather.getDateTimeString());
                }
                totalTemp += weather.getTemp();
                iconDay.add(weather.getIcon());
                count++;
            }
            newWeather.setTemp(totalTemp / count);
            newWeather.setIcon(getIconDay(iconDay));
            dailyWeatherList.add(newWeather);
            System.out.println("Sizes:" + weatherDay.size());
        }
    }

    private String getIconDay(List<String> icons) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Map<String,Long> counts = icons
                            .stream()
                            .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
            String string = counts
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
            if(string.endsWith("d"))
            {
                string = string.substring(0,string.length() - 1) + "n";
            }
            return string;
        }
        return icons.get(0);
    }
}