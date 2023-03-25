package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CODE = 0;
    private boolean seeDone = false;
    private boolean seeNotDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        handleSpinner();
        loadData();
    }

    public void loadData() {
        DatabaseHelper dbHandler = new DatabaseHelper(MainActivity.this);
        List<Task> list = dbHandler.getTasks();
        List<Task> relevantList = new ArrayList<Task>();
        for(int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            if(seeDone) {
                if(task.isDone()) {
                    relevantList.add(task);
                }
            }
            if(seeNotDone) {
                if(!task.isDone()) {
                    relevantList.add(task);
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TaskAdapter recyclerAdapter = new TaskAdapter(relevantList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(manager);
    }

    private void handleSpinner() {
        List<String> options = new ArrayList<String>();
        options.add("All");
        options.add("Done");;
        options.add("Not Done");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
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