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
}