package com.example.training_app.common.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.training_app.common.models.Exercise;

import java.util.ArrayList;

public abstract class AbstractListViewAdapter extends BaseAdapter {

    private ArrayList<Exercise> listFiltered; // Displayed list
    private ArrayList<Exercise> originalList; // Original list

    public AbstractListViewAdapter() {
        listFiltered = new ArrayList<>();
        originalList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return listFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setListFiltered(ArrayList<Exercise> _listFiltered) {
        listFiltered = _listFiltered;
        notifyDataSetChanged();
    }

    public void setOriginalList(ArrayList<Exercise> _originalList) {
        originalList = _originalList;
    }

    public ArrayList<Exercise> getListFiltered() {
        return listFiltered;
    }
    public ArrayList<Exercise> getOriginalList() {
        return originalList;
    }
}