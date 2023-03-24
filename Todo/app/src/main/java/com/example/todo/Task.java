package com.example.todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Task {
    private int id;
    private String title;
    private Date startDate;
    private Date finishDate;
    private boolean isDone;

    public Task(String title, String startDate, String finishDate) {
        this.title = title;
        this.isDone = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.startDate = format.parse(startDate);
            this.finishDate = format.parse(finishDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoneFromString(String isDone) {
        if(isDone.equals("true")) {
            this.isDone = true;
        }
        this.isDone = false;
    }
}