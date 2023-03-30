package com.example.checkboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CheckBox chocolateBox;
    CheckBox sprinkleBox;
    CheckBox nutBox;
    CheckBox cherryBox;
    CheckBox oreoBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chocolateBox = (CheckBox) findViewById(R.id.chocolate);
        sprinkleBox = (CheckBox) findViewById(R.id.sprinkles);
        nutBox = (CheckBox) findViewById(R.id.nuts);
        cherryBox = (CheckBox) findViewById(R.id.cherries);
        oreoBox = (CheckBox) findViewById(R.id.oreos);
    }

    public void showToast(View view) {
        String string = "";
        if(chocolateBox.isChecked()) {
            string += chocolateBox.getText().toString();
        }
        if(sprinkleBox.isChecked()) {
            string += " ";
            string += sprinkleBox.getText().toString();
        }
        if(nutBox.isChecked()) {
            string += " ";
            string += nutBox.getText().toString();
        }
        if(cherryBox.isChecked()) {
            string += " ";
            string += cherryBox.getText().toString();
        }
        if(oreoBox.isChecked()) {
            string += " ";
            string += oreoBox.getText().toString();
        }

        Toast toast = Toast.makeText(getApplicationContext(),string,Toast.LENGTH_LONG);
        toast.show();
    }
}