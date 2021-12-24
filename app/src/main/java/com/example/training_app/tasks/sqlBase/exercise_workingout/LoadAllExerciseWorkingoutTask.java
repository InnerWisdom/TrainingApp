package com.example.training_app.tasks.sqlBase.exercise_workingout;

import android.database.Cursor;

import com.example.training_app.common.interfaces.ILoadAllExerciseWorkingoutCallback;
import com.example.training_app.database.DBHelper;
import com.example.training_app.database.tables.DayExerciseTable;
import com.example.training_app.mvp.models.day_exercise.DayExerciseData;

import java.util.ArrayList;

public class LoadAllExerciseWorkingoutTask {

    private final DBHelper dbHelper;

    public LoadAllExerciseWorkingoutTask(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void execute(ILoadAllExerciseWorkingoutCallback callback) {
        ArrayList<DayExerciseData> list = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", DayExerciseTable.TABLE);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            DayExerciseData data = new DayExerciseData();
            data.setDayId(cursor.getLong(cursor.getColumnIndex(DayExerciseTable.COLUMN.DAY_ID)));
            data.setExerciseId(cursor.getLong(cursor.getColumnIndex(DayExerciseTable.COLUMN.EXERCISE_ID)));
            data.setWorkingout(cursor.getString(cursor.getColumnIndex(DayExerciseTable.COLUMN.WORKINGOUT)));
            list.add(data);
        }
        cursor.close();
        dbHelper.close();
        callback.onLoad(list);
    }
}