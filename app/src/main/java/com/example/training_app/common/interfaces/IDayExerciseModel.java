package com.example.training_app.common.interfaces;

import android.content.ContentValues;

public interface IDayExerciseModel {

    void loadExerciseWorkingout(ContentValues contentValues, ILoadExerciseWorkingoutCallback callback);

    void addExerciseWorkingout(ContentValues contentValues);

    void deleteExerciseWorkingout(ContentValues contentValues);

    void loadAllExerciseWorkingout(ILoadAllExerciseWorkingoutCallback callback);
}
