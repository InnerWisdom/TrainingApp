package com.example.training_app.common.interfaces;

import com.example.training_app.mvp.models.day_exercise.DayExerciseData;

import java.util.ArrayList;

public interface ILoadAllExerciseWorkingoutCallback {

    void onLoad(ArrayList<DayExerciseData> list);
}
