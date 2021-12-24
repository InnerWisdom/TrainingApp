package com.example.training_app.mvp.models.exercise;

import com.example.training_app.common.interfaces.IExerciseModel;
import com.example.training_app.common.interfaces.ILoadExerciseCallback;
import com.example.training_app.database.DBHelper;
import com.example.training_app.tasks.sqlBase.exercise.AddExerciseTask;
import com.example.training_app.tasks.sqlBase.exercise.LoadExerciseTask;

public class ExerciseModelSQLite implements IExerciseModel {

    public final DBHelper dbHelper;

    public ExerciseModelSQLite(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void loadExercise(ILoadExerciseCallback callback) {
        LoadExerciseTask loadExerciseTask = new LoadExerciseTask(dbHelper);
        loadExerciseTask.execute(callback);
    }

    public void addExercise(ExerciseData exerciseData) {
        AddExerciseTask addExerciseTask = new AddExerciseTask(dbHelper);
        addExerciseTask.execute(exerciseData);
    }
}