package com.example.helloconstraint;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int currentCount = 0;
    private String countString = "0";
    @ColorInt
    private int zeroColor = 0xB3B3B3;

    public static final String EXTRA_MESSAGE = "ToHello";
    TextView textView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentCount", currentCount);
        outState.putInt("zeroColor", zeroColor);
        outState.putString("countString", countString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null)
        {
            currentCount = savedInstanceState.getInt("currentCount");
            countString = savedInstanceState.getString("countString");
            zeroColor = savedInstanceState.getInt("zeroColor");
        }

        textView = (TextView) findViewById(R.id.textView);
        Button clickButton = (Button) findViewById(R.id.button_count);
        Button zeroButton = (Button) findViewById(R.id.button_zero);

        textView.setText(countString);
        zeroButton.setBackgroundColor(zeroColor);

        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentCount++;
                countString = Integer.toString(currentCount);
                textView.setText(countString);
                zeroColor = 0xFFFF0000;
                zeroButton.setBackgroundColor(zeroColor);
            }
        });

        zeroButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentCount = 0;
                countString = "0";
                textView.setText(countString);
                zeroColor = 0xB3B3B3;
                zeroButton.setBackgroundColor(zeroColor);
            }
        });
    }

    public void launchSecondActivity(View view) {
        // Create Intent
        Intent intent = new Intent(this, HelloActivity.class);
        String message = textView.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}