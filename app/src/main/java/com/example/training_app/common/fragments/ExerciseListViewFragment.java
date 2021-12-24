package com.example.training_app.common.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.training_app.common.adapters.ExerciseListViewAdapter;
import com.example.training_app.R;
import com.example.training_app.mvp.workingout.WorkingoutPresenter;

public class ExerciseListViewFragment extends Fragment {

    private ExerciseListViewAdapter exerciseListViewAdapter;

    private final WorkingoutPresenter presenter;
    private final Context context;

    public ExerciseListViewFragment(WorkingoutPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        initializeAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list_view, container, false);
        initialize(view);
        return view;
    }

    public ExerciseListViewAdapter getAdapter() {
        return exerciseListViewAdapter;
    }

    private void initializeAdapter() {
        exerciseListViewAdapter = new ExerciseListViewAdapter(context, presenter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initialize(View view) {
        ListView listViewExercise = view.findViewById(R.id.listViewExercise);
        listViewExercise.setAdapter(exerciseListViewAdapter);
    }

    public void filterItems(String query) {
        exerciseListViewAdapter.getFilter().filter(query);
    }
}