package com.example.todo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHelper {
    public static List<Date> getNextMonth(Date date) {
        List<Date> list = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i = 0; i < 30; i++) {
            System.out.println(calendar.getTime().toString());
            list.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }

    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String result = "";
        switch(day) {
            case 1:
                result = "Mon";
                break;
            case 2:
                result = "Tue";
                break;
            case 3:
                result = "Wed";
                break;
            case 4:
                result = "Thu";
                break;
            case 5:
                result = "Fri";
                break;
            case 6:
                result = "Sat";
                break;
            default:
                result = "Sun";
                break;
        }
        return result;
    }
}
