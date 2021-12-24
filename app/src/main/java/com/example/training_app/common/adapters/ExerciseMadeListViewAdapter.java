package com.example.training_app.common.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.training_app.R;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.mvp.workingout.WorkingoutPresenter;

import java.util.ArrayList;

public class ExerciseMadeListViewAdapter extends AbstractListViewAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;

    private ArrayList<Exercise> listFiltered;

    private final WorkingoutPresenter presenter;

    private int position;

    public ExerciseMadeListViewAdapter(Context context, WorkingoutPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view_item, parent, false);
        }

        Exercise exercise = getExercise(position);

        ((TextView) view.findViewById(R.id.textViewExerciseName)).setText(exercise.getName());
        ((TextView) view.findViewById(R.id.textViewNumberOfCalories)).setText(String.format("%d %s", exercise.getCalories(), exercise.getIntensisty()));
        view.findViewById(R.id.imageButton).setBackground(context.getResources().getDrawable(R.drawable.remove));

        view.findViewById(R.id.imageButton).setOnClickListener(v -> onClick(position));

        return view;
    }

    private void onClick(int position) {
        getActualExerciseList();
        this.position = position;
        presenter.deleteExerciseMade();
    }

    private void getActualExerciseList() {
        listFiltered = super.getListFiltered();
    }

    public Exercise getSelectedExercise() {
        return listFiltered.get(position);
    }

    public Exercise getExercise(int position) {
        return ((Exercise) getItem(position));
    }
}