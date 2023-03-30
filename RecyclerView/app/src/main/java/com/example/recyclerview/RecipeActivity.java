package com.example.recyclerview;

import static com.example.recyclerview.MainActivity.EXTRA_MESSAGE;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent intent = getIntent();
        int index = intent.getIntExtra(MainActivity.INDEX_MESSAGE, 0);
        ImageView mImage = findViewById(R.id.recipeImage);
        TextView recipe = findViewById(R.id.recipeText);
        Context context = getApplication().getApplicationContext();
        recipe.setMovementMethod(new ScrollingMovementMethod());

        String[] recipeArr = getResources().getStringArray(R.array.item_recipe);
        String[] imageArr = getResources().getStringArray(R.array.item_image);

        int resID = getResources().getIdentifier(imageArr[index] , "drawable", getPackageName());
        Drawable image = context.getResources().getDrawable(resID);
        mImage.setImageDrawable(image);
        recipe.setText(recipeArr[index]);
    }
}