package com.example.training_app.mvp.models.day;


import android.database.Cursor;

import com.example.training_app.common.interfaces.ILoadDayCallback;
import com.example.training_app.database.tables.DayTable;
import com.example.training_app.database.DBHelper;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.StandardDayData;

public class LoadDayStatisticsTask {

    private final DBHelper dbHelper;

    public LoadDayStatisticsTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(AbstractDayData dayData, ILoadDayCallback callback) {
        StandardDayData day = new StandardDayData();
        String query = String.format("SELECT * FROM %s WHERE %s LIKE '%s'", DayTable.TABLE, DayTable.COLUMN.DATE, dayData.getDate());
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            day.setId(cursor.getLong(cursor.getColumnIndex(DayTable.COLUMN.ID)));
            day.setDate(cursor.getString(cursor.getColumnIndex(DayTable.COLUMN.DATE)));
            day.setCalories(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.CALORIES)));
            day.setCards(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.CARDS)));
            day.setStrengths(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.STRENGTHS)));
            day.setAgilitys(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.AGILITYS)));
        }
        cursor.close();
        dbHelper.close();
        callback.onLoad(day);
    }
}