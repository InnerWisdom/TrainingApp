package com.example.training_app.mvp.models.day;

import com.example.training_app.common.interfaces.IDayModel;
import com.example.training_app.common.interfaces.ILoadAllDaysCallback;
import com.example.training_app.common.interfaces.ILoadDayCallback;
import com.example.training_app.database.DBHelper;

public class DayModelSQLite implements IDayModel {

    private final DBHelper dbHelper;

    public DayModelSQLite(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void addDayStatistics(AbstractDayData dayData) {
        AddDayStatisticsTask addDayStatisticsTask = new AddDayStatisticsTask(dbHelper);
        addDayStatisticsTask.execute(dayData);
    }

    public void updateDayStatistics(AbstractDayData dayData) {
        UpdateDayStatisticsTask updateDayStatisticsTask = new UpdateDayStatisticsTask(dbHelper);
        updateDayStatisticsTask.execute(dayData);
    }

    public void loadDayStatistics(AbstractDayData dayData, ILoadDayCallback callback) {
        LoadDayStatisticsTask loadDayStatisticsTask = new LoadDayStatisticsTask(dbHelper);
        loadDayStatisticsTask.execute(dayData, callback);
    }

    public void loadAllDays(ILoadAllDaysCallback callback) {
        LoadAllDaysTask loadAllDaysTask = new LoadAllDaysTask(dbHelper);
        loadAllDaysTask.execute(callback);
    }
}