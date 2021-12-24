package com.example.training_app.tasks.firebase.synchronization;

import com.example.training_app.common.models.Exercise;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.DayModelFirebase;
import com.example.training_app.mvp.models.exercise.ExerciseModelFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SyncRecordsTask {

    private final FirebaseDatabase firebaseDatabase;

    public SyncRecordsTask(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void execute(ArrayList<AbstractDayData> listOfDayData, ArrayList<Exercise> listOfExercise) {
        DatabaseReference databaseReference;

        for (AbstractDayData dayData : listOfDayData) {
            databaseReference = firebaseDatabase.getReference(DayModelFirebase.DAY_REFERENCE + "/" + dayData.getId());
            databaseReference.setValue(dayData);
        }

        for (Exercise exercise : listOfExercise) {
            databaseReference = firebaseDatabase.getReference(ExerciseModelFirebase.EXERCISE_REFERENCE + "/" + exercise.getId());
            databaseReference.setValue(exercise);
        }
    }
}