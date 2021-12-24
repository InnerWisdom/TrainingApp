package com.example.training_app.tasks.sqlBase.exercise_workingout;

import android.content.ContentValues;

import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.DayExerciseTable;

public class AddExerciseWorkingoutTask {

    private final DBHelper dbHelper;

    public AddExerciseWorkingoutTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ContentValues contentValues) {
        dbHelper.getWritableDatabase().insert(DayExerciseTable.TABLE, null, contentValues);
    }
}