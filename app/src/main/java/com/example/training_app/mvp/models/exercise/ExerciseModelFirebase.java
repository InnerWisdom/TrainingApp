package com.example.training_app.mvp.models.exercise;

import com.example.training_app.common.interfaces.IExerciseModel;
import com.example.training_app.common.interfaces.ILoadExerciseCallback;
import com.example.training_app.tasks.firebase.exercise.AddExerciseTask;
import com.example.training_app.tasks.firebase.exercise.LoadExerciseTask;
import com.google.firebase.database.FirebaseDatabase;

public class ExerciseModelFirebase implements IExerciseModel {

    public static final String EXERCISE_REFERENCE = "Exercise";

    private final FirebaseDatabase firebaseDatabase;

    public ExerciseModelFirebase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void loadExercise(ILoadExerciseCallback callback) {
        LoadExerciseTask loadExerciseTask = new LoadExerciseTask(firebaseDatabase);
        loadExerciseTask.execute(callback);
    }

    public void addExercise(ExerciseData exerciseData) {
        AddExerciseTask addExerciseTask = new AddExerciseTask(firebaseDatabase);
        addExerciseTask.execute(exerciseData);
    }
}