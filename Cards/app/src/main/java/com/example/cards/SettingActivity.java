package com.example.cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RadioGroup group = findViewById(R.id.settings_radio);
        group.check(R.id.both_radio);
    }

    public void cancel(View view) {
        finish();
    }

    public void setSetting(View view) {
        int isEnglish = 0;
        int isMongolian = 0;
        RadioButton english = findViewById(R.id.english_radio);
        RadioButton mon = findViewById(R.id.mon_radio);
        if(english.isChecked()) {
            isEnglish = 1;
        } else if(mon.isChecked()) {
            isMongolian = 1;
        } else {
            isEnglish = 1;
            isMongolian = 1;
        }
        Context context = getBaseContext();
        SharedPreferences sharedPref = context.getSharedPreferences("CardPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("isEnglish", isEnglish);
        editor.putInt("isMongolian", isMongolian);
        editor.commit();
        finish();
    }
}