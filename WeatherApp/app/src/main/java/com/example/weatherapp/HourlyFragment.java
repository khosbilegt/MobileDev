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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HourlyFragment extends Fragment {
    private final String API_KEY = "d405cb60bda80d5315053714d415c638";
    public List<Weather> weatherList = new ArrayList<Weather>();

    public HourlyFragment() {
        super(R.layout.fragment_hourly);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly, container, false);
    }

    private void setView() {
        int index = setBackground();
        List<Weather> weatherListHourly = new ArrayList<Weather>();
        for(int i = index; i < index + 8; i++) {
            weatherListHourly.add(weatherList.get(i));
        }

        RecyclerView hourlyView = getView().findViewById(R.id.hourlyView);
        HourlyAdapter adapter = new HourlyAdapter(weatherListHourly, this);
        hourlyView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        hourlyView.setLayoutManager(manager);
    }

    private int setBackground() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int index = 0;
        for(int i = 0; i < weatherList.size(); i++) {
            Weather weather = weatherList.get(i);
            if(weather.getMonth() == month && weather.getDay() == day) {
                int multiple = Math.round(Math.abs(hour / 3));
                int multipleOf3 = 3 * multiple;
                if(multipleOf3 == weather.getTime()) {
                    index = i;
                    break;
                }
            }
        }
        String type = weatherList.get(index).getType();
        boolean isDay = hour < 20 && hour > 8;
        setBackgroundImage(type, isDay);
        return index;
    }
    void setBackgroundImage(String type, boolean isDay) {
        ConstraintLayout layout = getActivity().findViewById(R.id.constraint_main);
        Drawable background;
        switch(type) {
            case "Clear":
                if(isDay) {
                    background = getResources().getDrawable(R.drawable.clear_day);
                } else {
                    background = getResources().getDrawable(R.drawable.clear_night);
                }
                break;
            case "Rain":
                if(isDay) {
                    background = getResources().getDrawable(R.drawable.cloudy_day);
                } else {
                    background = getResources().getDrawable(R.drawable.cloudy_night);
                }
                break;
            case "Snow":
                if(isDay) {
                    background = getResources().getDrawable(R.drawable.snow_day);
                } else {
                    background = getResources().getDrawable(R.drawable.snow_day);
                }
                break;
            default:
                if(isDay) {
                    background = getResources().getDrawable(R.drawable.rain_day);
                } else {
                    background = getResources().getDrawable(R.drawable.rain_night);
                }
        }
        layout.setBackground(background);
    }
    private void getInfo() {
        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity());
        int latitude = 48;
        int longitude = 107;
        String units = "metric";
        String url = "https://api.openweathermap.org/data/2.5/forecast?"
                + "lat="+ String.valueOf(latitude)
                + "&lon=" + String.valueOf(longitude)
                + "&appid=" + API_KEY
                + "&units=" + units;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    JsonHelper helper = new JsonHelper();
                    weatherList = helper.parseWeather(response);
                    setView();
                },
                (Response.ErrorListener) error -> {
                    Toast.makeText(getActivity(), "Some error occurred! Cannot fetch dog image", Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}");
                }
        );
        volleyQueue.add(jsonObjectRequest);
    }
}