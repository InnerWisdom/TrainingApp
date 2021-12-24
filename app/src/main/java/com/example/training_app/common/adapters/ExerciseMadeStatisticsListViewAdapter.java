package com.example.training_app.common.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.training_app.R;
import com.example.training_app.common.models.Exercise;

public class ExerciseMadeStatisticsListViewAdapter extends AbstractListViewAdapter {

    private final LayoutInflater layoutInflater;

    public ExerciseMadeStatisticsListViewAdapter(Context context) {
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
        view.findViewById(R.id.imageButton).setVisibility(View.INVISIBLE);

        return view;
    }

    public Exercise getExercise(int position) {
        return ((Exercise) getItem(position));
    }
}