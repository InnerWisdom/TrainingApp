package com.example.training_app.mvp.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.training_app.R;
import com.example.training_app.mvp.models.exercise.ExerciseData;
import com.example.training_app.common.interfaces.IExerciseModel;
import com.example.training_app.mvp.workingout.WorkingoutPresenter;
import com.google.android.material.slider.Slider;

public class CreateExerciseFragment extends Fragment {

    private EditText editTextTitle, editTextNameOfIntensisty;
    private Slider editTextAgilitys, editTextCalories, editTextCards, editTextStrengths;
    private RelativeLayout relativeLayoutNameOfIntensisty;
    private TextView textViewSelectedIntensisty;

    private int position;

    private CreateExercisePresenter presenter;
    private final IExerciseModel exerciseModel;

    public CreateExerciseFragment(IExerciseModel exerciseModel) {
        this.exerciseModel = exerciseModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_exercise, container, false);

        presenter = new CreateExercisePresenter(requireContext(), exerciseModel);
        presenter.attachView(this);

        initialize(view);
        return view;
    }

    private void initialize(View view) {
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextNameOfIntensisty = view.findViewById(R.id.editTextNameOfIntensisty);
        editTextCalories = view.findViewById(R.id.editTextCalories);
        editTextCards = view.findViewById(R.id.editTextCards);
        editTextStrengths = view.findViewById(R.id.editTextStrengths);
        editTextAgilitys = view.findViewById(R.id.editTextAgilitys);

        relativeLayoutNameOfIntensisty = view.findViewById(R.id.relativeLayoutNameOfIntensisty);
        textViewSelectedIntensisty = view.findViewById(R.id.textViewSelectedIntensisty);

        view.findViewById(R.id.relativeLayoutTypeOfIntensisty).setOnClickListener(v -> presenter.onBeforeChangeTypeOfIntensisty());

        view.findViewById(R.id.buttonBack).setOnClickListener(v -> presenter.onBack());

        view.findViewById(R.id.buttonCreateExercise).setOnClickListener(v -> presenter.onCreateExercise());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onCreateDialogForCrworkingoutExercise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.selectionMenuTitle)
                .setIcon(getResources().getDrawable(R.drawable.question))
                .setItems(R.array.typesOfIntensistys, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                        presenter.onAfterChangeTypeOfIntensisty();
                    }
                });
        builder.create().show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onCreateDialogForError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.warningTitle)
                .setIcon(getResources().getDrawable(R.drawable.caution))
                .setMessage(message);
        builder.create().show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onCreateDialogForSuccess(String message) {
        requireActivity().runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.informationTitle)
                    .setIcon(getResources().getDrawable(R.drawable.success))
                    .setMessage(message)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            onClose();
                        }
                    });
            builder.create().show();
        });
    }

    public int getPosition() {
        return position;
    }

    public ExerciseData getExerciseData() {
        ExerciseData exerciseData = new ExerciseData();
        exerciseData.setId(WorkingoutPresenter.getId(WorkingoutPresenter.INCREMENTAL_EXERCISE_ID, WorkingoutPresenter.EXERCISE_ID));
        exerciseData.setName(editTextTitle.getText().toString());

        String intensistyName = isVisibility() ? String.format("%s %s", getResources().getString(R.string.customIntensityValue), editTextNameOfIntensisty.getText().toString()) : getResources().getString(R.string.defaultIntensistyValue);
        int calories = Math.round(editTextCalories.getValue());
        int strengths = Math.round(editTextStrengths.getValue());
        int cards = Math.round(editTextCards.getValue());
        int agilitys = Math.round(editTextAgilitys.getValue());

        exerciseData.setIntensisty(intensistyName);
        exerciseData.setCalories(calories);
        exerciseData.setCards(cards);
        exerciseData.setStrengths(strengths);
        exerciseData.setAgilitys(agilitys);

        return exerciseData;
    }

    public void setVisibilityIsVisible(String typeOfIntensisty) {
        relativeLayoutNameOfIntensisty.setVisibility(View.VISIBLE);
        textViewSelectedIntensisty.setText(typeOfIntensisty);
    }

    public void setVisibilityIsGone(String typeOfIntensisty) {
        relativeLayoutNameOfIntensisty.setVisibility(View.GONE);
        textViewSelectedIntensisty.setText(typeOfIntensisty);
    }

    public void onClose() {
        presenter.detachView();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    public boolean isVisibility() {
        return relativeLayoutNameOfIntensisty.getVisibility() == View.VISIBLE;
    }
}