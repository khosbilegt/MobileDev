package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todoapp";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String ID_COL = "id";
    private static final String TITLE_COL = "title";
    private static final String START_COL = "start_date";
    private static final String END_COL = "end_date";
    private static final String DONE_COL = "is_done";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT,"
                + START_COL + " TEXT,"
                + END_COL + " TEXT,"
                + DONE_COL + " TEXT)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.getStartDate());
        String startDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(task.getFinishDate());
        calendar.add(Calendar.MONTH, 1);
        String endDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);
        values.put(TITLE_COL, task.getTitle());
        values.put(START_COL, startDate);
        values.put(END_COL, endDate);
        values.put(DONE_COL, Boolean.toString(task.isDone()));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void modifyTask(int id, Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.setTime(task.getStartDate());
        String startDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH - 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(task.getFinishDate());
        calendar.add(Calendar.MONTH, 1);
        String endDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);

        values.put(TITLE_COL, task.getTitle());
        values.put(START_COL, startDate);
        values.put(END_COL, endDate);
        values.put(DONE_COL, Boolean.toString(task.isDone()));

        db.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor wordCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (wordCursor.moveToFirst()) {
            do {
                Task task = new Task(wordCursor.getString(1), wordCursor.getString(2),
                        wordCursor.getString(3));
                task.setDoneFromString(wordCursor.getString(4));
                task.setId(wordCursor.getInt(0));
                tasks.add(task);
            } while (wordCursor.moveToNext());
        }
        wordCursor.close();
        return tasks;
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
