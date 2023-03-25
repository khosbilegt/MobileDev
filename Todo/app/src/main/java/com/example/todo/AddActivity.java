package com.example.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        if(intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        DatePicker datePicker = findViewById(R.id.datePicker);
        EditText editText = findViewById(R.id.editText);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                String finishDate = String.valueOf(year) + "-" + String.valueOf(month) + "-"
                        + String.valueOf(day);

                Calendar calendar = Calendar.getInstance();
                String currentString = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
                        + String.valueOf(calendar.get(Calendar.MONTH)) + "-"
                        + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

                Task task = new Task(editText.getText().toString(), currentString, finishDate);

                DatabaseHelper dbHandler = new DatabaseHelper(AddActivity.this);
                if(id > 0) {
                    dbHandler.modifyTask(id, task);
                } else {
                    dbHandler.addTask(task);
                }
                finish();
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        if(id < 0) {
            deleteButton.setBackgroundColor(Color.parseColor("#D6D6D6"));
            deleteButton.setClickable(false);
            return;
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
    }

    private void deleteTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this task ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int idx) {
                        DatabaseHelper dbHandler = new DatabaseHelper(AddActivity.this);
                        dbHandler.deleteTask(id);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Delete Task");
        alert.show();
    }
}