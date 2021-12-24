package com.example.training_app.database.tables;

public class DayExerciseTable {

    public static final String TABLE = "day_exercise";

    public static class COLUMN {
        public static final String ID = "id";
        public static final String DAY_ID = "day_id";
        public static final String EXERCISE_ID = "exercise_id";
        public static final String WORKINGOUT = "workingout";
    }

    public static final String CREATE_SCRIPT =
            String.format("create table %s ("
                            + "%s integer primary key autoincrement,"
                            + "%s text,"
                            + "%s integer,"
                            + "%s integer" + ");",
                    TABLE, COLUMN.ID, COLUMN.WORKINGOUT, COLUMN.DAY_ID, COLUMN.EXERCISE_ID);
}