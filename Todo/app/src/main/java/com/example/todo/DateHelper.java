package com.example.todo;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

    public static String timeLeft(Date date) {
        String currentString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String[] tempCurrentDate = currentString.split("-");
        Date currentDate = new GregorianCalendar(Integer.valueOf(tempCurrentDate[0]),
                Integer.valueOf(tempCurrentDate[1]) - 1,
                Integer.valueOf(tempCurrentDate[2])).getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //calendar.add(Calendar.MONTH, 2);
        long different = calendar.getTime().getTime() - currentDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        return String.valueOf(elapsedDays) + "d";
    }
}
