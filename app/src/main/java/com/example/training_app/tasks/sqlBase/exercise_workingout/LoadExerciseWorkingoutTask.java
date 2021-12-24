package com.example.training_app.tasks.sqlBase.exercise_workingout;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.training_app.common.interfaces.ILoadExerciseWorkingoutCallback;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.DayExerciseTable;
import com.example.training_app.database.tables.ExerciseTable;

import java.util.ArrayList;

public class LoadExerciseWorkingoutTask {

    private final DBHelper dbHelper;

    public LoadExerciseWorkingoutTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ContentValues contentValues, ILoadExerciseWorkingoutCallback callback) {
        ArrayList<Exercise> exercise = new ArrayList<>();
        String query = String.format("SELECT * "
                + "FROM %s AS F "
                + "INNER JOIN %s AS DF "
                + "ON F.id = DF.exercise_id " +
                "WHERE DF.day_id = %s " +
                "AND DF.workingout = '%s'",
                ExerciseTable.TABLE, DayExerciseTable.TABLE, contentValues.get(DayExerciseTable.COLUMN.DAY_ID), contentValues.getAsString(DayExerciseTable.COLUMN.WORKINGOUT));
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
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