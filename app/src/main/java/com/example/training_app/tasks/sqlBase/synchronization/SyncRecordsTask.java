package com.example.training_app.tasks.sqlBase.synchronization;

import android.content.ContentValues;

import com.example.training_app.common.models.Exercise;
import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.DayExerciseTable;
import com.example.training_app.database.tables.DayTable;
import com.example.training_app.database.tables.ExerciseTable;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day_exercise.DayExerciseData;

import java.util.ArrayList;

public class SyncRecordsTask {

    private final DBHelper dbHelper;

    public SyncRecordsTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ArrayList<AbstractDayData> forInsert, ArrayList<Exercise> listOfExercise, ArrayList<DayExerciseData> listOfDayExerciseData) {
        for (AbstractDayData dayData : forInsert) {
            ContentValues cv = new ContentValues(6);
            cv.put(DayTable.COLUMN.ID, dayData.getId());
            cv.put(DayTable.COLUMN.DATE, dayData.getDate());
            cv.put(DayTable.COLUMN.CALORIES, dayData.getCalories());
            cv.put(DayTable.COLUMN.CARDS, dayData.getCards());
            cv.put(DayTable.COLUMN.STRENGTHS, dayData.getStrengths());
            cv.put(DayTable.COLUMN.AGILITYS, dayData.getAgilitys());
            dbHelper.getWritableDatabase().insert(DayTable.TABLE, null, cv);
        }


        for (Exercise exercise : listOfExercise) {
            ContentValues cv = new ContentValues(7);
            cv.put(ExerciseTable.COLUMN.ID, exercise.getId());
            cv.put(ExerciseTable.COLUMN.NAME, exercise.getName());
            cv.put(ExerciseTable.COLUMN.CALORIES, exercise.getCalories());
            cv.put(ExerciseTable.COLUMN.CARDS, exercise.getCards());
            cv.put(ExerciseTable.COLUMN.STRENGTHS, exercise.getStrengths());
            cv.put(ExerciseTable.COLUMN.AGILITYS, exercise.getAgilitys());
            cv.put(ExerciseTable.COLUMN.SERVING, exercise.getIntensisty());
            dbHelper.getWritableDatabase().insert(ExerciseTable.TABLE, null, cv);
        }

        for (DayExerciseData dayExerciseData : listOfDayExerciseData) {
            ContentValues cv = new ContentValues(3);
            cv.put(DayExerciseTable.COLUMN.EXERCISE_ID, dayExerciseData.getExerciseId());
            cv.put(DayExerciseTable.COLUMN.DAY_ID, dayExerciseData.getDayId());
            cv.put(DayExerciseTable.COLUMN.WORKINGOUT, dayExerciseData.getWorkingout());
            dbHelper.getWritableDatabase().insert(DayExerciseTable.TABLE, null, cv);
        }
    }
}