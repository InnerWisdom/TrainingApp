package com.example.training_app.mvp.models.day;

import android.database.Cursor;

import com.example.training_app.common.interfaces.ILoadAllDaysCallback;
import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.DayTable;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.StandardDayData;

import java.util.ArrayList;

public class LoadAllDaysTask {

    private final DBHelper dbHelper;

    public LoadAllDaysTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ILoadAllDaysCallback callback) {
        ArrayList<AbstractDayData> list = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", DayTable.TABLE);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            StandardDayData day = new StandardDayData();
            day.setId(cursor.getLong(cursor.getColumnIndex(DayTable.COLUMN.ID)));
            day.setDate(cursor.getString(cursor.getColumnIndex(DayTable.COLUMN.DATE)));
            day.setCalories(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.CALORIES)));
            day.setCards(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.CARDS)));
            day.setStrengths(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.STRENGTHS)));
            day.setAgilitys(cursor.getInt(cursor.getColumnIndex(DayTable.COLUMN.AGILITYS)));
            list.add(day);
        }
        cursor.close();
        dbHelper.close();
        callback.onLoad(list);
    }
}