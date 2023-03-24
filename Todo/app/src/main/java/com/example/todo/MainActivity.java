package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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
        //dbHandler.addTask(new Task("Test", "2020-12-20", "2022-12-20"));
        List<Task> list = dbHandler.getTasks();
        System.out.println("Main Size:" + list.size());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TaskAdapter recyclerAdapter = new TaskAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(manager);
    }
}