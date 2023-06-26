package com.codebykt.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tasks";
    public static final String COL_ID = "id";
    public static final String COL_TASK_NAME = "task_name";
    public static final String COL_TASK_DESCRIPTION = "task_description";
    public static final String COL_TASK_DATE = "task_date";
    public static final String COL_TASK_DATE_TIME = "task_date_time";
    public static final String COL_IS_COMPLETED = "is_completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TASK_NAME + " TEXT, "
                + COL_TASK_DESCRIPTION + " TEXT, "
                + COL_TASK_DATE + " TEXT, "
                + COL_TASK_DATE_TIME + " TEXT, "
                + COL_IS_COMPLETED + " INTEGER DEFAULT 0)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public long insertTask(String taskName, String taskDescription, String taskDateTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_NAME, taskName);
        values.put(COL_TASK_DESCRIPTION, taskDescription);
        values.put(COL_TASK_DATE_TIME, taskDateTime);
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void markTaskAsCompleted(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IS_COMPLETED, 1);
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(taskId)});
    }
}
