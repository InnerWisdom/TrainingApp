package com.example.training_app.mvp.models.day;

public abstract class AbstractDayData {

    private long id;
    private String date;
    private int calories;
    private int cards;
    private int strengths;
    private int agilitys;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }

    public int getStrengths() {
        return strengths;
    }

    public void setStrengths(int strengths) {
        this.strengths = strengths;
    }

    public int getAgilitys() {
        return agilitys;
    }

    public void setAgilitys(int agilitys) {
        this.agilitys = agilitys;
    }
}