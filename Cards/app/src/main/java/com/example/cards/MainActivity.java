package com.example.cards;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final int ADD_CODE = 0;
    private final int UPDATE_CODE = 1;
    private final int OPTIONS_CODE = 2;
    private final int FILE_SELECT_CODE = 3;
    public List<String> englishWords = new ArrayList<String>();
    public List<String> monWords = new ArrayList<String>();
    private int currentWord = 0;
    private boolean isMongolian = true;
    private boolean isEnglish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView englishView = findViewById(R.id.english_text);
        TextView monView = findViewById(R.id.mon_text);

        englishView.setText(readFromFile(getBaseContext()));

        englishView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   updateWord(englishView);
                   return true;
               }
           }
        );
        monView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   updateWord(monView);
                   return true;
               }
           }
        );

        loadData();
        showData();
    }

    void loadData() {
        WordDatabaseHelper dbHandler = new WordDatabaseHelper(MainActivity.this);
        Map<String, String> keys = new HashMap<String, String>(dbHandler.getWords());

        englishWords.clear();
        monWords.clear();

        for(Map.Entry<String, ?> entry: keys.entrySet()) {
            String eng = "";
            String mon = "";
            if(entry.getKey().equals("isMongolian")) {
                if (Integer.valueOf(entry.getValue().toString()) == 1) {
                    isMongolian = true;
                } else {
                    isMongolian = false;
                }
            } else if(entry.getKey().equals("isEnglish")) {
                if (Integer.valueOf(entry.getValue().toString()) == 1) {
                    isEnglish = true;
                } else {
                    isEnglish = false;
                }
            } else {
                eng += entry.getKey();
                mon += entry.getValue().toString();
                englishWords.add(eng);
                monWords.add(mon);
            }
        }

    }

    void showData() {
        TextView enText = findViewById(R.id.english_text);
        TextView monText = findViewById(R.id.mon_text);
        if(englishWords.size() == 0 || monWords.size() == 0) {
            setActive(false);
            setMoveState(false);
            enText.setText("");
            monText.setText("");
            return;
        }
        if(englishWords.size() == 1) {
            setMoveState((false));
        } else {
            setMoveState(true);
        }
        setActive(true);

        if(isEnglish) {
            enText.setText(englishWords.get(currentWord));
        } else {
            enText.setText("");
        }
        if(isMongolian) {
            monText.setText(monWords.get(currentWord));
        } else {
            monText.setText("");
        }
    }

    public void openAddActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        MainActivity.this.startActivityForResult(intent, ADD_CODE);
    }

    public void nextWord(View view) {
        if(currentWord >= englishWords.size() - 1) {
            currentWord = 0;
        } else {
            currentWord++;
        }
        showData();
    }

    public void previousWord(View view) {
        if(currentWord == 0) {
            currentWord = englishWords.size() - 1;
        } else {
            currentWord--;
        }
        showData();
    }

    public void updateWord(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        intent.putExtra("key", englishWords.get(currentWord));
        intent.putExtra("value", monWords.get(currentWord));
        MainActivity.this.startActivityForResult(intent, UPDATE_CODE);
    }

    public void deleteWord(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Та энэ үгийг устгахыг хүсэж байна уу?");
        builder.setTitle("УСТГАХ");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            WordDatabaseHelper dbHandler = new WordDatabaseHelper(MainActivity.this);
            dbHandler.deleteWord(englishWords.get(currentWord));
            loadData();
            showData();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void chooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_SELECT_CODE) {
            if(resultCode == RESULT_OK) {
                Uri uri = data.getData();
                WordDatabaseHelper dbHandler = new WordDatabaseHelper(MainActivity.this);
                dbHandler.parseTextFile(getBaseContext(), uri);
            }
        }
        loadData();
        showData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        MainActivity.this.startActivityForResult(intent, OPTIONS_CODE);
        return true;
    }

    void setActive(boolean state) {
        List<Button> buttons = new ArrayList<Button>();;
        buttons.add((Button)findViewById(R.id.delete_button));
        buttons.add((Button)findViewById(R.id.update_button));
        if(state) {
            for(int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.purple_500));
                buttons.get(i).setActivated(true);
            }
            return;
        }
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setBackgroundColor(Color.GRAY);
            buttons.get(i).setActivated(false);
        }
    }

    void setMoveState(boolean state) {
        List<Button> buttons = new ArrayList<Button>();;
        buttons.add((Button)findViewById(R.id.prev_button));
        buttons.add((Button)findViewById(R.id.next_button));
        if(state) {
            for(int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.purple_500));
                buttons.get(i).setActivated(true);
            }
            return;
        }
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setBackgroundColor(Color.GRAY);
            buttons.get(i).setActivated(false);
        }
    }

    public void toggleEnglish(View view) {
        isEnglish = !isEnglish;
        showData();
    }

    public void toggleMongolian(View view) {
        isMongolian = !isMongolian;
        showData();
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("test.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
