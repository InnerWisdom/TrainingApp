package com.example.training_app.tasks.firebase.day;

import androidx.annotation.NonNull;

import com.example.training_app.common.interfaces.ILoadDayCallback;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.DayModelFirebase;
import com.example.training_app.mvp.models.day.ExtendedDayData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadDayStatisticsTask {

    private final FirebaseDatabase firebaseDatabase;

    public LoadDayStatisticsTask(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void execute(AbstractDayData dayData, ILoadDayCallback callback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(DayModelFirebase.DAY_REFERENCE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        AbstractDayData data = new ExtendedDayData();
                        for (DataSnapshot item: dataSnapshot.getChildren()) {
                            if (Objects.equals(item.child("date").getValue(), dayData.getDate())) {
                                callback.onLoad(item.getValue(ExtendedDayData.class));
                                return;
                            }
                        }
                        if (data.getId() == 0) callback.onLoad(new ExtendedDayData());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}