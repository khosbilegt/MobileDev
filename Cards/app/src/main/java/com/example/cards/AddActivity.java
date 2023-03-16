package com.example.cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private String updateKey = "";
    private String key = "";
    private String value = "";
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        EditText enText = findViewById(R.id.english_textedit);
        EditText monText = findViewById(R.id.mon_textedit);

        Intent intent = getIntent();
        if(intent.hasExtra("key")) {
            key = intent.getStringExtra("key");
            value = intent.getStringExtra("value");
            isUpdate = true;
        }
        enText.setText(key);
        monText.setText(value);
    }

    public void onClick(View view) {
        if(isUpdate) {
            updateWord(view);
            return;
        }
        addWord(view);
    }

    public void addWord(View view) {
        EditText enText = findViewById(R.id.english_textedit);
        EditText monText = findViewById(R.id.mon_textedit);
        if(enText.getText().length() == 0 || monText.getText().length() == 0){
            return;
        }
        WordDatabaseHelper dbHandler = new WordDatabaseHelper(AddActivity.this);
        dbHandler.addWord(enText.getText().toString(), monText.getText().toString());
        finish();
    }

    public void updateWord(View view) {
        EditText enText = findViewById(R.id.english_textedit);
        EditText monText = findViewById(R.id.mon_textedit);
        if(enText.getText().length() == 0 || monText.getText().length() == 0){
            return;
        }
        WordDatabaseHelper dbHandler = new WordDatabaseHelper(AddActivity.this);
        dbHandler.updateWord(key, enText.getText().toString(), monText.getText().toString());
        finish();
    }

    public void returnToMain(View view) {
        finish();
    }
}