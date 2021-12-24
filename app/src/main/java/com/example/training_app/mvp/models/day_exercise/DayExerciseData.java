package com.example.training_app.mvp.models.day_exercise;

public class DayExerciseData {

    private long dayId;
    private long exerciseId;
    private String workingout;

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long productId) {
        this.exerciseId = productId;
    }

    public void setWorkingout(String workingout) {
        this.workingout = workingout;
    }

    public String getWorkingout() {
        return workingout;
    }
}