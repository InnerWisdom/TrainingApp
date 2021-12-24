package com.example.training_app.tasks.sqlBase.exercise;

import android.database.Cursor;

import com.example.training_app.common.interfaces.ILoadExerciseCallback;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.ExerciseTable;

import java.util.ArrayList;

public class LoadExerciseTask {

    private final DBHelper dbHelper;

    public LoadExerciseTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ILoadExerciseCallback callback) {
        ArrayList<Exercise> exercise = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().query(ExerciseTable.TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Exercise item = new Exercise();
            item.setId(cursor.getLong(cursor.getColumnIndex(ExerciseTable.COLUMN.ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(ExerciseTable.COLUMN.NAME)));
            item.setCalories(cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN.CALORIES)));
            item.setCards(cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN.CARDS)));
            item.setStrengths(cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN.STRENGTHS)));
            item.setAgilitys(cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN.AGILITYS)));
            item.setIntensisty(cursor.getString(cursor.getColumnIndex(ExerciseTable.COLUMN.SERVING)));
            exercise.add(item);
        }
        cursor.close();
        dbHelper.close();
        callback.onLoad(exercise);
    }
}