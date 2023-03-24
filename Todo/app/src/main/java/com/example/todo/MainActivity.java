package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
    }

    public void loadData() {
        DatabaseHelper dbHandler = new DatabaseHelper(MainActivity.this);
        List<Task> list = dbHandler.getTasks();

    }
}