package com.example.training_app.common.models;

public class Exercise {

    private long id;
    private String name;
    private String intensisty;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setIntensisty(String intensisty) {
        this.intensisty = intensisty;
    }

    public String getIntensisty() {
        return intensisty;
    }
}
