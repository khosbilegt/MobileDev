package com.example.todo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Task {
    private int id;
    private String title;
    private Date startDate;
    private Date finishDate;
    private boolean isDone;

    public Task(String title, String startDate, String finishDate) {
        this.title = title;
        this.isDone = false;
        String[] tempStartDate = startDate.split("-");
        String[] tempFinishDate = finishDate.split("-");
        this.startDate = new GregorianCalendar(Integer.valueOf(tempStartDate[0]),
                Integer.valueOf(tempStartDate[1]) - 1,
                Integer.valueOf(tempStartDate[2])).getTime();
        this.finishDate = new GregorianCalendar(Integer.valueOf(tempFinishDate[0]),
                Integer.valueOf(tempFinishDate[1]) - 1,
                Integer.valueOf(tempFinishDate[2])).getTime();
        System.out.println("Dates: " + this.startDate.toString());
    }

    public String getTitle() {
        return title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getId() {
        return id;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoneFromString(String isDone) {
        this.isDone = Boolean.parseBoolean(isDone);
    }

    public static String timeLeft(Date date) {
        String currentString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String[] tempCurrentDate = currentString.split("-");
        Date currentDate = new GregorianCalendar(Integer.valueOf(tempCurrentDate[0]),
                Integer.valueOf(tempCurrentDate[1]) - 1,
                Integer.valueOf(tempCurrentDate[2])).getTime();

        long different = date.getTime() - currentDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        String result = "";
        if(elapsedDays > 0) {
            result += String.valueOf(elapsedDays) + "d";
        } else if (elapsedHours > 0 ) {
            result += String.valueOf(elapsedHours) + "h";
        } else if(elapsedMinutes > 0) {
            result += String.valueOf(elapsedMinutes) + "m";
        } else {
            result += "Late";
        }
        return result;
    }
}