package com.example.training_app.common.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.training_app.common.adapters.ExerciseMadeListViewAdapter;
import com.example.training_app.R;
import com.example.training_app.common.models.Day;
import com.example.training_app.common.storage.ConstantStorage;
import com.example.training_app.mvp.workingout.WorkingoutPresenter;

public class DailyStatisticsFragment extends Fragment {

    private TextView textViewCalories, textViewCards, textViewStrengths, textViewAgilitys;
    private ProgressBar progressBarCalories, progressBarCards, progressBarStrengths, progressBarAgilitys;

    private ExerciseMadeListViewAdapter exerciseMadeListViewAdapter;

    private final int maxCalories, maxCards, maxStrengths, maxAgilitys;

    private final WorkingoutPresenter presenter;
    private final Context context;

    private Day day;

    private final Handler progressBarHandler;

    public ExerciseMadeListViewAdapter getAdapter() {
        return exerciseMadeListViewAdapter;
    }

    public DailyStatisticsFragment(WorkingoutPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;

        day = new Day();
        progressBarHandler = new Handler();

        maxCalories = ConstantStorage.maxCalories;
        maxCards = ConstantStorage.maxCards;
        maxStrengths = ConstantStorage.maxStrengths;
        maxAgilitys = ConstantStorage.maxAgilitys;

        initializeAdapter();
    }

    public void setDay(Day day) {
        this.day = day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initialize(view);
        setGoal();
        setStatisticsWindow();
        return view;
    }

    public void updateStatistics() {
        setGoal();
        setStatisticsWindow();
    }

    private void initializeAdapter() {
        exerciseMadeListViewAdapter = new ExerciseMadeListViewAdapter(context, presenter);
    }

    public void setStatisticsWindow() {
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> {
                setStatisticsOnTextView();
                setStatisticsOnProgressBar();
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private void setStatisticsOnTextView() {
        textViewCalories.setText(String.format("%d / %d kcal", day.getCalories(), maxCalories));
        textViewCards.setText(String.format("%d  / %d ", day.getCards(), maxCards));
        textViewStrengths.setText(String.format("%d  / %d ", day.getStrengths(), maxStrengths));
        textViewAgilitys.setText(String.format("%d  / %d ", day.getAgilitys(), maxAgilitys));
    }

    private void initialize(View view) {
        ListView listViewExerciseMade = view.findViewById(R.id.listViewExerciseMade);
        listViewExerciseMade.setAdapter(exerciseMadeListViewAdapter);
        textViewCalories = view.findViewById(R.id.textViewCalories);
        textViewCards = view.findViewById(R.id.textViewCards);
        textViewStrengths = view.findViewById(R.id.textViewStrengths);
        textViewAgilitys = view.findViewById(R.id.textViewAgilitys);
        progressBarCalories = view.findViewById(R.id.progressBarCalories);
        progressBarCards = view.findViewById(R.id.progressBarCards);
        progressBarStrengths = view.findViewById(R.id.progressBarStrengths);
        progressBarAgilitys = view.findViewById(R.id.progressBarAgilitys);
    }

    private void setGoal() {
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> {
                progressBarCalories.setMax(maxCalories);
                progressBarCards.setMax(maxCards);
                progressBarStrengths.setMax(maxStrengths);
                progressBarAgilitys.setMax(maxAgilitys);
            });
        }
    }

    private void setStatisticsOnProgressBar() {
        progressBarHandler.post(new Runnable() {
            @Override
            public void run() {
                progressBarCalories.setProgress(day.getCalories());
                progressBarCards.setProgress(day.getCards());
                progressBarStrengths.setProgress(day.getStrengths());
                progressBarAgilitys.setProgress(day.getAgilitys());
            }
        });
    }
}