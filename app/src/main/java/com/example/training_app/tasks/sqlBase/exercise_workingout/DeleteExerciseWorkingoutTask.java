package com.example.training_app.tasks.sqlBase.exercise_workingout;

import android.content.ContentValues;

import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.DayExerciseTable;

public class DeleteExerciseWorkingoutTask {

    private final DBHelper dbHelper;

    public DeleteExerciseWorkingoutTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ContentValues contentValues) {
        dbHelper.getWritableDatabase().delete(DayExerciseTable.TABLE, "id = ?", new String[] { contentValues.getAsString(DayExerciseTable.COLUMN.ID) });
    }
}