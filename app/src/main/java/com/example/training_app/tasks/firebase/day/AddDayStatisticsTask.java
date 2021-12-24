package com.example.training_app.tasks.firebase.day;

import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.DayModelFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDayStatisticsTask {

    private final FirebaseDatabase firebaseDatabase;

    public AddDayStatisticsTask(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void execute(AbstractDayData dayData) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(DayModelFirebase.DAY_REFERENCE + "/" + dayData.getId());
        databaseReference.setValue(dayData);
    }
}