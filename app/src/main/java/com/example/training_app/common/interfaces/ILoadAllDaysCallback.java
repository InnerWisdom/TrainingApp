package com.example.training_app.common.interfaces;

import com.example.training_app.mvp.models.day.AbstractDayData;

import java.util.ArrayList;

public interface ILoadAllDaysCallback {

    void onLoad(ArrayList<AbstractDayData> list);
}
