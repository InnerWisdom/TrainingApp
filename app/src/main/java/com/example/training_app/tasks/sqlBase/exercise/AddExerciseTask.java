package com.example.training_app.tasks.sqlBase.exercise;

import android.content.ContentValues;

import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.ExerciseTable;
import com.example.training_app.mvp.models.exercise.ExerciseData;

public class AddExerciseTask {

    private final DBHelper dbHelper;

    public AddExerciseTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ExerciseData exerciseData) {
        ContentValues cv = new ContentValues(7);
        cv.put(ExerciseTable.COLUMN.ID, exerciseData.getId());
        cv.put(ExerciseTable.COLUMN.NAME, exerciseData.getName());
        cv.put(ExerciseTable.COLUMN.CALORIES, exerciseData.getCalories());
        cv.put(ExerciseTable.COLUMN.CARDS, exerciseData.getCards());
        cv.put(ExerciseTable.COLUMN.STRENGTHS, exerciseData.getStrengths());
        cv.put(ExerciseTable.COLUMN.AGILITYS, exerciseData.getAgilitys());
        cv.put(ExerciseTable.COLUMN.SERVING, exerciseData.getIntensisty());
        dbHelper.getWritableDatabase().insert(ExerciseTable.TABLE, null, cv);
    }
}