package com.example.training_app.common.interfaces;

import com.example.training_app.mvp.models.exercise.ExerciseData;

public interface IExerciseModel {

    void loadExercise(ILoadExerciseCallback callback);

    void addExercise(ExerciseData exerciseData);
}
