package com.example.training_app.mvp.models.synchronization;

import com.example.training_app.common.models.Exercise;
import com.example.training_app.database.DBHelper;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day_exercise.DayExerciseData;
import com.example.training_app.tasks.sqlBase.synchronization.SyncRecordsTask;

import java.util.ArrayList;

public class SynchronizationModelSQLite {

    private final DBHelper dbHelper;

    public SynchronizationModelSQLite(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void syncRecords(ArrayList<AbstractDayData> forInsert, ArrayList<Exercise> listOfExercise, ArrayList<DayExerciseData> listOfDayExerciseData) {
        SyncRecordsTask syncRecordsTask = new SyncRecordsTask(dbHelper);
        syncRecordsTask.execute(forInsert, listOfExercise, listOfDayExerciseData);
    }
}