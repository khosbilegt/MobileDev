package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String API_KEY = "d405cb60bda80d5315053714d415c638";
    public List<Weather> weatherList = new ArrayList<Weather>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getInfo();
    }

    private void getInfo() {
        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
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
                    HourlyFragment hourly = (HourlyFragment) getSupportFragmentManager().findFragmentByTag("hourlyFragment");
                    hourly.setView(weatherList);
                    DailyFragment daily = (DailyFragment) getSupportFragmentManager().findFragmentByTag("dailyFragment");
                    daily.setView(weatherList);
                    findViewById(R.id.daily_fragment_container);
                },
                (Response.ErrorListener) error -> {
                    Toast.makeText(MainActivity.this, "Some error occurred! Cannot fetch dog image", Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}");
                }
        );
        volleyQueue.add(jsonObjectRequest);
    }

    public void refresh(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Refreshed";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        getInfo();
    }
}