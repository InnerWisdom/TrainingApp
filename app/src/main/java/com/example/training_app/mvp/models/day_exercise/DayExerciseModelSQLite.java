package com.example.training_app.mvp.models.day_exercise;

import android.content.ContentValues;

import com.example.training_app.common.interfaces.IDayExerciseModel;
import com.example.training_app.common.interfaces.ILoadAllExerciseWorkingoutCallback;
import com.example.training_app.common.interfaces.ILoadExerciseWorkingoutCallback;
import com.example.training_app.database.DBHelper;
import com.example.training_app.tasks.sqlBase.exercise_workingout.AddExerciseWorkingoutTask;
import com.example.training_app.tasks.sqlBase.exercise_workingout.DeleteExerciseWorkingoutTask;
import com.example.training_app.tasks.sqlBase.exercise_workingout.LoadAllExerciseWorkingoutTask;
import com.example.training_app.tasks.sqlBase.exercise_workingout.LoadExerciseWorkingoutTask;

public class DayExerciseModelSQLite implements IDayExerciseModel {

    public final DBHelper dbHelper;

    public DayExerciseModelSQLite(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void loadExerciseWorkingout(ContentValues contentValues, ILoadExerciseWorkingoutCallback callback) {
        LoadExerciseWorkingoutTask loadExerciseWorkingoutTask = new LoadExerciseWorkingoutTask(dbHelper);
        loadExerciseWorkingoutTask.execute(contentValues, callback);
    }

    public void addExerciseWorkingout(ContentValues contentValues) {
        AddExerciseWorkingoutTask addExerciseWorkingoutTask = new AddExerciseWorkingoutTask(dbHelper);
        addExerciseWorkingoutTask.execute(contentValues);
    }

    public void deleteExerciseWorkingout(ContentValues contentValues) {
        DeleteExerciseWorkingoutTask deleteExerciseWorkingoutTask = new DeleteExerciseWorkingoutTask(dbHelper);
        deleteExerciseWorkingoutTask.execute(contentValues);
    }

    public void loadAllExerciseWorkingout(ILoadAllExerciseWorkingoutCallback callback) {
        LoadAllExerciseWorkingoutTask loadAllExerciseWorkingoutTask = new LoadAllExerciseWorkingoutTask(dbHelper);
        loadAllExerciseWorkingoutTask.execute(callback);
    }
}