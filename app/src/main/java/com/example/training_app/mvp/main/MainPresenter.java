package com.example.training_app.mvp.main;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.DatePicker;

import com.example.training_app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainPresenter {

    private MainActivity view;

    private final SimpleDateFormat simpleDateFormat;
    private final SimpleDateFormat outFormat;

    private final Calendar calendar = Calendar.getInstance();

    private final SharedPreferences sharedPreferences;
    private Editor editor;

    private final Context context;

    @SuppressLint("SimpleDateFormat")
    public MainPresenter(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE);

        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        outFormat = new SimpleDateFormat("EEE, d MMM", Locale.US);
    }

    public void attachView(MainActivity mainActivity) {
        view = mainActivity;
    }

    public void clickOnItem(String title) {
        view.openNewActivity(title);
    }

    public void initialize() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String date = simpleDateFormat.format(calendar.getTime());
        editor = sharedPreferences.edit();
        editor.putString(MainActivity.SELECTED_DATE, date);
        editor.commit();

        setSelectedDate();
    }

    public void detachView() {
        view = null;
    }

    private void setSelectedDate() {
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(sharedPreferences.getString(MainActivity.SELECTED_DATE, ""));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        long diffDays = getDiffDays(date);
        switch ((int)diffDays)
        {
            case 0:
                view.setCalendarTitle("Today");
                break;
            case 1:
                view.setCalendarTitle("Yesterday");
                break;
            case -1:
                view.setCalendarTitle("Tomorrow");
                break;
            default:
                view.setCalendarTitle(outFormat.format(date));
                break;
        }
    }

    private long getDiffDays(Date date) {
        try {
            Date currentDay = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            long diff = currentDay.getTime() - date.getTime();
            return diff / (24 * 60 * 60 * 1000);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void onDateSet() {
        DatePicker datePicker = view.getDatePicker();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        String date = simpleDateFormat.format(calendar.getTime());
        editor.putString(MainActivity.SELECTED_DATE, date);
        editor.commit();

        setSelectedDate();
    }

    public void setDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, view.dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        view.showDatePickerDialog(datePickerDialog);
    }
}