package com.example.training_app.common.interfaces;

import com.example.training_app.mvp.models.day.AbstractDayData;

public interface ILoadDayCallback {

    void onLoad(AbstractDayData dayData);
}
