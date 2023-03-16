package com.example.cards;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cardapp";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "words";
    private static final String ID_COL = "id";
    private static final String ENGLISH_COL = "english";
    private static final String MONGOLIAN_COL = "mongolian";

    public WordDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ENGLISH_COL + " TEXT,"
                + MONGOLIAN_COL + " TEXT)";

        db.execSQL(query);
    }

    public void addWord(String englishWord, String mongolianWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ENGLISH_COL, englishWord);
        values.put(MONGOLIAN_COL, mongolianWord);
        db.insert(TABLE_NAME, null, values);
        db.close();
        System.out.println("Added Entry");
    }

    public Map<String, String> getWords() {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor wordCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (wordCursor.moveToFirst()) {
            do {
                map.put(wordCursor.getString(1), wordCursor.getString(2));
            } while (wordCursor.moveToNext());
        }
        wordCursor.close();
        return map;
    }

    public void updateWord(String originalEnglish, String newEnglish, String newMongolian) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ENGLISH_COL, newEnglish);
        values.put(MONGOLIAN_COL, newMongolian);

        db.update(TABLE_NAME, values, "english=?", new String[]{originalEnglish});
        db.close();
    }

    public void deleteWord(String english) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "english=?", new String[]{english});
        db.close();
    }

    public void parseTextFile(Context context, Uri uri) {
        try {
            String content = FileHelper.readUri(context, uri);
            String[] lines = content.split(System.lineSeparator());
            for(int i = 0; i < lines.length; i++) {
                String[] words = lines[i].split("\\s+");
                addWord(words[0], words[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
