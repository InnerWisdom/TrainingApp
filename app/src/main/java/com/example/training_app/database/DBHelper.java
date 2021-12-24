package com.example.training_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.training_app.database.tables.DayTable;
import com.example.training_app.database.tables.DayExerciseTable;
import com.example.training_app.database.tables.ExerciseTable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String NAME = "EXERCISE_APPLICATION";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ExerciseTable.CREATE_SCRIPT);
        sqLiteDatabase.execSQL(DayTable.CREATE_SCRIPT);
        sqLiteDatabase.execSQL(DayExerciseTable.CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) { }
}