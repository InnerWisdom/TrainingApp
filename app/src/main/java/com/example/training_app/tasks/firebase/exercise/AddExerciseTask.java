package com.example.training_app.tasks.firebase.exercise;

import com.example.training_app.mvp.models.exercise.ExerciseData;
import com.example.training_app.mvp.models.exercise.ExerciseModelFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExerciseTask {

    private final FirebaseDatabase firebaseDatabase;

    public AddExerciseTask(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void execute(ExerciseData exerciseData) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(ExerciseModelFirebase.EXERCISE_REFERENCE + "/" + exerciseData.getId());
        databaseReference.setValue(exerciseData);
    }
}