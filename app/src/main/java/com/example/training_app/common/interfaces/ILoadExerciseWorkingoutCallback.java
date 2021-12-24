package com.example.training_app.common.interfaces;

import com.example.training_app.common.models.Exercise;

import java.util.ArrayList;

public interface ILoadExerciseWorkingoutCallback {

    void onLoad(ArrayList<Exercise> exercise);
}
