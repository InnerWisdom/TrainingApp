package com.example.training_app.mvp.exercise;

import android.content.Context;

import com.example.training_app.R;
import com.example.training_app.mvp.workingout.WorkingoutPresenter;
import com.example.training_app.mvp.models.exercise.ExerciseData;
import com.example.training_app.common.interfaces.IExerciseModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateExercisePresenter {

    private CreateExerciseFragment view;

    private final String SUCCESSFULLY = "SUCCESSFULLY";
    private final String SUCCESSFULLY_MESSAGE = "The exercise has been successfully created!";

    private final String defaultIntensisty;
    private final String customIntensisty;

    private final String[] typesOfIntensistys;
    private final IExerciseModel exerciseModel;

    public CreateExercisePresenter(Context context, IExerciseModel exerciseModel) {
        this.exerciseModel = exerciseModel;

        typesOfIntensistys = context.getResources().getStringArray(R.array.typesOfIntensistys);
        defaultIntensisty = context.getResources().getString(R.string.defaultIntensityHint);
        customIntensisty = context.getResources().getString(R.string.customIntensityTitle);
    }

    public void attachView(CreateExerciseFragment createExerciseActivity) {
        view = createExerciseActivity;
    }

    public void detachView() {
        view = null;
    }

    public void onBack() {
        view.onClose();
    }

    public void onCreateExercise() {
        ExerciseData exerciseData = view.getExerciseData();
        String message = getResult(exerciseData);

        if (message.equals(SUCCESSFULLY)) {
            addExersize(exerciseData);
        }
        else {
            view.onCreateDialogForError(message);
        }
    }

    public void onBeforeChangeTypeOfIntensisty() {
        view.onCreateDialogForCrworkingoutExercise();
    }

    public void onAfterChangeTypeOfIntensisty() {
        int position = view.getPosition();
        String item = typesOfIntensistys[position];

        if (item.toLowerCase().equals(defaultIntensisty)) {
            view.setVisibilityIsGone(defaultIntensisty);
        }
        else if (item.equals(customIntensisty)){
            view.setVisibilityIsVisible(item);
        }
    }

    private String getResult(ExerciseData exerciseData) {
        if (exerciseData.getName().equals("")) {
            return "Please, fill out Title field!";
        }
        else if (exerciseData.getIntensisty().equals("") && view.isVisibility()) {
            return "Please, fill out Intensisty field!";
        }
        else if (exerciseData.getCalories() == 0) {
            return "Please, fill out Calories field!";
        }
        else if (exerciseData.getCards() == 0) {
            return "Please, fill out Cards field!";
        }
        else if (exerciseData.getStrengths() == 0) {
            return "Please, fill out Strengths field!";
        }
        else if (exerciseData.getAgilitys() == 0) {
            return "Please, fill out Agilitys field!";
        }
        return SUCCESSFULLY;
    }

    public void addExersize(ExerciseData exersizeData) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                exerciseModel.addExercise(exersizeData);
                WorkingoutPresenter.increment(WorkingoutPresenter.INCREMENTAL_EXERCISE_ID, WorkingoutPresenter.EXERCISE_ID);
                view.onCreateDialogForSuccess(SUCCESSFULLY_MESSAGE);
            }
        });
    }
}