package com.example.training_app.mvp.models.day;

import android.content.ContentValues;

import com.example.training_app.database.tables.DayTable;
import com.example.training_app.database.DBHelper;
import com.example.training_app.mvp.models.day.AbstractDayData;

public class AddDayStatisticsTask {

    private final DBHelper dbHelper;

    public AddDayStatisticsTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(AbstractDayData dayData) {
        ContentValues cv = new ContentValues(6);
        cv.put(DayTable.COLUMN.ID, dayData.getId());
        cv.put(DayTable.COLUMN.DATE, dayData.getDate());
        cv.put(DayTable.COLUMN.CALORIES, dayData.getCalories());
        cv.put(DayTable.COLUMN.CARDS, dayData.getCards());
        cv.put(DayTable.COLUMN.STRENGTHS, dayData.getStrengths());
        cv.put(DayTable.COLUMN.AGILITYS, dayData.getAgilitys());
        dbHelper.getWritableDatabase().insert(DayTable.TABLE, null, cv);
        dbHelper.close();
    }
}