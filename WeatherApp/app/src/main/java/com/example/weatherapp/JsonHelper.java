package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.List;

public class JsonHelper {
    private List<Weather> weatherList = new LinkedList<Weather>();

    public List<Weather> parseWeather(JSONObject response) {
        try {
            JSONArray list = response.getJSONArray("list");
            for(int i = 0; i < list.length(); i++) {
                Weather weather = new Weather();
                JSONObject object = list.getJSONObject(i);
                JSONObject main = object.getJSONObject("main");
                JSONObject weatherJson = object.getJSONArray("weather").getJSONObject(0);
                JSONObject wind = object.getJSONObject("wind");
                weather.setDateTimeString(object.getString("dt_txt"));
                parseMain(main, weather);
                parseWeather(weatherJson, weather);
                parseWind(wind, weather);
                System.out.println(i + ": " + weather.getTime() + " " + weather.getIcon());
                weatherList.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherList;
    }

    void parseMain(JSONObject main, Weather weather) throws JSONException {
        weather.setTemp(main.getDouble("temp"));
        weather.setFeelsLike(main.getDouble("feels_like"));
        weather.setMinTemp(main.getDouble("temp_min"));
        weather.setMaxTemp(main.getDouble("temp_max"));
        weather.setPressure(main.getDouble("pressure"));
        weather.setSeaLevel(main.getDouble("sea_level"));
        weather.setGrindLevel(main.getDouble("grnd_level"));
        weather.setHumidity(main.getDouble("humidity"));
    }

    void parseWeather(JSONObject weatherJson, Weather weather) throws JSONException {
        weather.setId(weatherJson.getInt("id"));
        weather.setType(weatherJson.getString("main"));
        weather.setIcon(weatherJson.getString("icon"));
        weather.setDescription(weatherJson.getString("description"));
    }

    void parseWind(JSONObject wind, Weather weather) throws JSONException {
        weather.setSpeed(wind.getDouble("speed"));
        weather.setDeg(wind.getDouble("deg"));
        weather.setGust(wind.getDouble("gust"));
    }
}
