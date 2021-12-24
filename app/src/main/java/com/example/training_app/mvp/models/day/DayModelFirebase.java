package com.example.training_app.mvp.models.day;

import com.example.training_app.common.interfaces.IDayModel;
import com.example.training_app.common.interfaces.ILoadAllDaysCallback;
import com.example.training_app.common.interfaces.ILoadDayCallback;
import com.example.training_app.tasks.firebase.day.AddDayStatisticsTask;
import com.example.training_app.tasks.firebase.day.LoadAllDaysTask;
import com.example.training_app.tasks.firebase.day.LoadDayStatisticsTask;
import com.example.training_app.tasks.firebase.day.UpdateDayStatisticsTask;
import com.google.firebase.database.FirebaseDatabase;

public class DayModelFirebase implements IDayModel {

    public static final String DAY_REFERENCE = "Day";

    private final FirebaseDatabase firebaseDatabase;

    public DayModelFirebase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void addDayStatistics(com.example.training_app.mvp.models.day.AbstractDayData dayData) {
        AddDayStatisticsTask addDayStatisticsTask = new AddDayStatisticsTask(firebaseDatabase);
        addDayStatisticsTask.execute(dayData);
    }

    public void updateDayStatistics(com.example.training_app.mvp.models.day.AbstractDayData dayData) {
        UpdateDayStatisticsTask updateDayStatisticsTask = new UpdateDayStatisticsTask(firebaseDatabase);
        updateDayStatisticsTask.execute(dayData);
    }

    public void loadDayStatistics(com.example.training_app.mvp.models.day.AbstractDayData dayData, ILoadDayCallback callback) {
        LoadDayStatisticsTask loadDayStatisticsTask = new LoadDayStatisticsTask(firebaseDatabase);
        loadDayStatisticsTask.execute(dayData, callback);
    }

    public void loadAllDays(ILoadAllDaysCallback callback) {
        LoadAllDaysTask loadAllDaysTask = new LoadAllDaysTask(firebaseDatabase);
        loadAllDaysTask.execute(callback);
    }
}