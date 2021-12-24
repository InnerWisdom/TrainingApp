package com.example.training_app.common.models;

import java.util.Date;

public class Day {

    private long id;
    private Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
