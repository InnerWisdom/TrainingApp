package com.example.training_app.mvp.models.synchronization;

import com.example.training_app.common.models.Exercise;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.tasks.firebase.synchronization.SyncRecordsTask;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SynchronizationModelFirebase {

    private final FirebaseDatabase firebaseDatabase;

    public SynchronizationModelFirebase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void syncRecords(ArrayList<AbstractDayData> listOfDayData, ArrayList<Exercise> listOfExercise) {
        SyncRecordsTask syncRecordsTask = new SyncRecordsTask(firebaseDatabase);
        syncRecordsTask.execute(listOfDayData, listOfExercise);
    }
}