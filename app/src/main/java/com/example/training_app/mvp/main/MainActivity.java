package com.example.training_app.mvp.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.training_app.R;
import com.example.training_app.common.activities.WorkingoutActivity;
import com.example.training_app.common.storage.ConstantStorage;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "TITLE";
    public static final String SELECTED_DATE = "SELECTED_DATE";
    public static final String APP_NAME = "APP_NAME";

    private TextView textViewSelectedDate;

    private DatePickerDialog datePickerDialog;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        presenter = new MainPresenter(this);
        presenter.attachView(this);
        presenter.initialize();
    }

    public void initialize() {
        textViewSelectedDate = findViewById(R.id.textViewCalendarTitle);
        findViewById(R.id.calendarItem).setOnClickListener(v -> presenter.setDate());

        findViewById(R.id.layoutAddMorning).setOnClickListener(v -> addMorning());
        findViewById(R.id.buttonAddMorning).setOnClickListener(v -> addMorning());

        findViewById(R.id.layoutAddNoon).setOnClickListener(v -> addNoon());
        findViewById(R.id.buttonAddLaunch).setOnClickListener(v -> addNoon());

        findViewById(R.id.layoutAddAfternoon).setOnClickListener(v -> addAfternoon());
        findViewById(R.id.buttonAddAfternoon).setOnClickListener(v -> addAfternoon());

        findViewById(R.id.layoutAddDusk).setOnClickListener(v -> addDusk());
        findViewById(R.id.buttonAddDusk).setOnClickListener(v -> addDusk());
    }

    public void addMorning() { presenter.clickOnItem(getString(R.string.morningTitle)); }

    public void addNoon() {
        presenter.clickOnItem(getString(R.string.noonTitle));
    }

    public void addAfternoon() {
        presenter.clickOnItem(getString(R.string.afternoonTitle));
    }

    public void addDusk() {
        presenter.clickOnItem(getString(R.string.duskTitle));
    }

    public void openNewActivity(String title) {
        ConstantStorage.TITLE = title;

        startActivity(new Intent(this, WorkingoutActivity.class));
    }

    public void setCalendarTitle(String date)  {
        textViewSelectedDate.setText(date);
    }

    public void showDatePickerDialog(DatePickerDialog dialog) {
        datePickerDialog = dialog;
        datePickerDialog.show();
    }

    public DatePicker getDatePicker() {
        return datePickerDialog.getDatePicker();
    }

    public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            presenter.onDateSet();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}