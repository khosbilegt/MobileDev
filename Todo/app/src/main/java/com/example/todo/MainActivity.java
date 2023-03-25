package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CODE = 0;
    private boolean seeDone = false;
    private boolean seeNotDone = false;
    private boolean onlyDate = false;
    private Date activeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        handleDoneSpinner();
        handleDateSpinner();
        loadCalendar();
        loadData();
        activateAlarm();
        Calendar calendar = Calendar.getInstance();
        activeDate = calendar.getTime();
    }

    public void loadCalendar() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        List<Date> dates = DateHelper.getNextMonth(today);
        RecyclerView recyclerView = findViewById(R.id.calendarRecycler);
        CalendarAdapter recyclerAdapter = new CalendarAdapter(dates);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(manager);
    }

    public void loadData() {
        DatabaseHelper dbHandler = new DatabaseHelper(MainActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        List<Task> list = dbHandler.getTasks();
        List<Task> relevantList = new ArrayList<Task>();
        for(int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            if(seeDone) {
                if(onlyDate) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(activeDate);
                    int chosenDay = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTime(task.getFinishDate());
                    int finishDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if(task.isDone() && finishDay == chosenDay) {
                        relevantList.add(task);
                    }
                } else {
                    if(task.isDone()) {
                        relevantList.add(task);
                    }
                }
            }
            if(seeNotDone) {
                if(onlyDate) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(activeDate);
                    int chosenDay = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTime(task.getFinishDate());
                    int finishDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if(!task.isDone() && finishDay == chosenDay) {
                        relevantList.add(task);
                    }
                } else {
                    if(!task.isDone()) {
                        relevantList.add(task);
                    }
                }
            }
        }

        TaskAdapter recyclerAdapter = new TaskAdapter(relevantList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(manager);
    }

    private void handleDoneSpinner() {
        List<String> options = new ArrayList<String>();
        options.add("All");
        options.add("Done");;
        options.add("Not Done");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner, options);
        arrayAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        Spinner spinner = findViewById(R.id.spinner);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    seeDone = true;
                    seeNotDone = true;
                }
                else if (position == 1) {
                    seeDone = true;
                    seeNotDone = false;
                } else {
                    seeDone = false;
                    seeNotDone = true;
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("spinner", position);
                editor.apply();
                loadData();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        int selection = sharedPref.getInt("spinner", 0);
        spinner.setSelection(selection);
        loadData();
    }

    private void handleDateSpinner() {
        List<String> options = new ArrayList<String>();
        options.add("Only Date");
        options.add("All Time");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner, options);
        arrayAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        Spinner spinner = findViewById(R.id.spinner2);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    onlyDate = true;
                }
                else {
                    onlyDate = false;
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("date", position);
                editor.apply();
                loadData();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        int selection = sharedPref.getInt("date", 0);
        spinner.setSelection(selection);
        loadData();
    }

    public void changeDate(Date date) {
        activeDate = date;
        System.out.print("Changed Date: " + date.toString());
        loadData();
    }

    public void activateAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 11);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CODE) {
            loadData();
        }
    }
}