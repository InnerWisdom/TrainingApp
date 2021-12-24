package com.example.training_app.database.tables;

public class DayTable {

    public static final String TABLE = "days";

    public static class COLUMN {
        public static final String ID = "id";
        public static final String DATE = "date";
        public static final String CALORIES = "calories";
        public static final String CARDS = "cards";
        public static final String STRENGTHS = "strengths";
        public static final String AGILITYS = "agilitys";
    }

    public static final String CREATE_SCRIPT =
            String.format("create table %s ("
                            + "%s integer primary key,"
                            + "%s text,"
                            + "%s integer,"
                            + "%s integer,"
                            + "%s integer,"
                            + "%s integer" + ");",
                    TABLE, COLUMN.ID, COLUMN.DATE, COLUMN.CALORIES, COLUMN.CARDS, COLUMN.STRENGTHS, COLUMN.AGILITYS);
}