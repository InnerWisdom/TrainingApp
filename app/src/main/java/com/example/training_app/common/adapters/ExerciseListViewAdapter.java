package com.example.training_app.common.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.training_app.R;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.mvp.workingout.WorkingoutPresenter;

import java.util.ArrayList;

public class ExerciseListViewAdapter extends AbstractListViewAdapter implements Filterable {

    private final Context context;
    private final LayoutInflater layoutInflater;

    private final WorkingoutPresenter presenter;

    private ArrayList<Exercise> originalList; // Original list
    private ArrayList<Exercise> listFiltered; // Displayed list

    private int position;

    public ExerciseListViewAdapter(Context context, WorkingoutPresenter presenter) {
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
        view.findViewById(R.id.imageButton).setBackground(context.getResources().getDrawable(R.drawable.plus));

        view.findViewById(R.id.imageButton).setOnClickListener(v -> onClick(position));
        view.setOnClickListener(v -> onClick(position));

        return view;
    }

    private void onClick(int position) {
        this.position = position;
        presenter.addExerciseMade();
    }

    public Exercise getSelectedExercise() {
        return listFiltered.get(position);
    }

    public Exercise getExercise(int position) {
        return ((Exercise) getItem(position));
    }

    @Override
    public void setListFiltered(ArrayList<Exercise> listFiltered) {
        super.setListFiltered(listFiltered);
    }

    private void getActualExercise() {
        originalList = super.getOriginalList();
        listFiltered = super.getListFiltered();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (ArrayList<Exercise>) results.values;
                setListFiltered(listFiltered);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                getActualExercise();
                FilterResults results = new FilterResults();
                ArrayList<Exercise> FilteredList = new ArrayList<Exercise>();
                if (constraint == null || constraint.length() == 0) {
                    results.values = new ArrayList<Exercise>();
                    results.count = 0;
                }
                else {
                    for (Exercise item : originalList) {
                        if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            FilteredList.add(item);
                        }
                    }
                    results.values = FilteredList;
                    results.count = FilteredList.size();
                }
                return results;
            }
        };
    }
}