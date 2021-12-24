package com.example.training_app.tasks.firebase.day;

import androidx.annotation.NonNull;

import com.example.training_app.common.interfaces.ILoadAllDaysCallback;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.DayModelFirebase;
import com.example.training_app.mvp.models.day.ExtendedDayData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadAllDaysTask {

    private final FirebaseDatabase firebaseDatabase;

    public LoadAllDaysTask(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void execute(ILoadAllDaysCallback callback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(DayModelFirebase.DAY_REFERENCE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<AbstractDayData> list = new ArrayList<>();
                        for (DataSnapshot item: dataSnapshot.getChildren()) {
                            list.add(item.getValue(ExtendedDayData.class));
                        }
                        if (list.size() == 0) {
                            callback.onLoad(new ArrayList<>());
                        }
                        else {
                            callback.onLoad(list);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}