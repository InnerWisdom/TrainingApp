package com.example.training_app.common.interfaces;

import com.example.training_app.common.models.Exercise;

import java.util.ArrayList;

public interface ILoadExerciseCallback {

    void onLoad(ArrayList<Exercise> exercise);
}