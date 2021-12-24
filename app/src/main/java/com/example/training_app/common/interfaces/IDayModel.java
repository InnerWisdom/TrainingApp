package com.example.training_app.common.interfaces;

import com.example.training_app.mvp.models.day.AbstractDayData;

public interface IDayModel {

    void addDayStatistics(AbstractDayData dayData);

    void updateDayStatistics(AbstractDayData dayData);

    void loadDayStatistics(AbstractDayData dayData, ILoadDayCallback callback);

    void loadAllDays(ILoadAllDaysCallback callback);
}
