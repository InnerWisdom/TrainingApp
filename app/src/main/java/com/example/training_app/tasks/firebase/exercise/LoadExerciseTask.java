package com.example.training_app.tasks.firebase.exercise;

import androidx.annotation.NonNull;

import com.example.training_app.common.interfaces.ILoadExerciseCallback;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.mvp.models.exercise.ExerciseModelFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadExerciseTask {

    private final FirebaseDatabase firebaseDatabase;

    public LoadExerciseTask(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void execute(ILoadExerciseCallback callback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(ExerciseModelFirebase.EXERCISE_REFERENCE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Exercise> exercise = new ArrayList<>();
                        for (DataSnapshot item: dataSnapshot.getChildren()) {
                            exercise.add(item.getValue(Exercise.class));
                        }
                        callback.onLoad(exercise);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}