package com.example.training_app.mvp.models.day;

import com.example.training_app.common.models.Exercise;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtendedDayData extends com.example.training_app.mvp.models.day.AbstractDayData {

    private HashMap<String, ArrayList<Exercise>> exerciseList;
    private ArrayList<Exercise> exercise;

    public long getId() {
        return super.getId();
    }

    public void setId(long id) {
        super.setId(id);
    }

    public String getDate() {
        return super.getDate();
    }

    public void setDate(String date) {
        super.setDate(date);
    }

    public int getCalories() {
        return super.getCalories();
    }

    public void setCalories(int calories) {
        super.setCalories(calories);
    }

    public int getCards() {
        return super.getCards();
    }

    public void setCards(int cards) {
        super.setCards(cards);
    }

    public int getStrengths() {
        return super.getStrengths();
    }

    public void setStrengths(int strengths) {
        super.setStrengths(strengths);
    }

    public int getAgilitys() {
        return super.getAgilitys();
    }

    public HashMap<String, ArrayList<Exercise>> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(HashMap<String, ArrayList<Exercise>> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public ArrayList<Exercise> getExercise() {
        return exercise;
    }

    public void setExercise(ArrayList<Exercise> exercise) {
        this.exercise = exercise;
    }
}