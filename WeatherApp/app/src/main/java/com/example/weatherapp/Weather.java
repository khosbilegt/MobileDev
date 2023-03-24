package com.example.weatherapp;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Weather {
    // Temperature
    double temp;
    double feelsLike;
    double maxTemp;
    double minTemp;
    double pressure;
    double seaLevel;
    double grindLevel;
    double humidity;

    // Weather
    int id;
    String type;
    String description;
    String icon;

    // Wind
    double speed;
    double deg;
    double gust;

    // Info
    String dateTimeString;
    Date date;
    int month;
    int time;
    int day;

    public Weather() {

    }

    public String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch(dayOfWeek) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        if(icon.contains("d")) {
            this.icon = icon.replace("d", "n");
            return;
        }
        this.icon = icon.replace("n", "d");
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public void setGrindLevel(double grindLevel) {
        this.grindLevel = grindLevel;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description.substring(0, 1).toUpperCase() + description.substring(1);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public void setGust(double gust) {
        this.gust = gust;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
        String[] strings = dateTimeString.split("\\s+");
        String[] dateString = strings[0].split("-");
        month = Integer.parseInt(dateString[1]);
        day = Integer.parseInt(dateString[2]);
        this.date = new Date(
                Integer.parseInt(dateString[0]),
                month,
                day
                );
        String[] timeString = strings[1].split(":");
        this.time = Integer.parseInt(timeString[0]);
    }

    public double getTemp() {
        return temp;
    }

    public int getTempInt() {
        return (int)Math.round(temp);
    }


    public double getFeelsLike() {
        return feelsLike;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public double getGrindLevel() {
        return grindLevel;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    public double getGust() {
        return gust;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public Date getDate() {
        return date;
    }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public int getTime() {
        return time;
    }

    public String getTimeAmPm() {
        if(time < 12) {
            return String.valueOf(time) + "AM";
        }
        return String.valueOf(time) + "PM";
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temp=" + temp +
                ", feelsLike=" + feelsLike +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                ", pressure=" + pressure +
                ", seaLevel=" + seaLevel +
                ", grindLevel=" + grindLevel +
                ", humidity=" + humidity +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", speed=" + speed +
                ", deg=" + deg +
                ", gust=" + gust +
                ", dateTimeString='" + dateTimeString +
                '}';
    }
}
