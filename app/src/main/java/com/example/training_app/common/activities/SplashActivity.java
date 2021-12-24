package com.example.training_app.common.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.training_app.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {

    ToggleButton toggleButtonOne, toggleButtonTwo, toggleButtonThree, toggleButtonFour, toggleButtonFive, toggleButtonSix, toggleButtonSeven, toggleButtonEight,toggleButtonNine,toggleButtonTen;
    TextView textViewTask;

    private String number = "";
    private int randomInt = (int)(Math.random() * 15 + 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        randomInt = (int) (Math.random() * 6 + 1);

        toggleButtonOne = findViewById(R.id.toggleButtonOne);
        toggleButtonTwo = findViewById(R.id.toggleButtonTwo);
        toggleButtonThree = findViewById(R.id.toggleButtonThree);
        toggleButtonFour = findViewById(R.id.toggleButtonFour);
        toggleButtonFive = findViewById(R.id.toggleButtonFive);
        toggleButtonSix = findViewById(R.id.toggleButtonSix);
        toggleButtonSeven = findViewById(R.id.toggleButtonSeven);
        toggleButtonEight = findViewById(R.id.toggleButtonEight);
        toggleButtonNine = findViewById(R.id.toggleButtonNine);
        toggleButtonTen = findViewById(R.id.toggleButtonTen);

        resetValue();

        textViewTask = findViewById(R.id.textViewTask);
        textViewTask.setText(textViewTask.getText() + " " + randomInt);

        toggleButtonOne.setOnClickListener(v -> getNumber());
        toggleButtonTwo.setOnClickListener(v -> getNumber());
        toggleButtonThree.setOnClickListener(v -> getNumber());
        toggleButtonFour.setOnClickListener(v -> getNumber());
        toggleButtonFive.setOnClickListener(v -> getNumber());
        toggleButtonSix.setOnClickListener(v -> getNumber());
        toggleButtonSeven.setOnClickListener(v -> getNumber());
        toggleButtonEight.setOnClickListener(v -> getNumber());
        toggleButtonNine.setOnClickListener(v -> getNumber());
        toggleButtonTen.setOnClickListener(v -> getNumber());
    }

    private void resetValue() {
        toggleButtonOne.setTextOff("");
        toggleButtonOne.setTextOn("EVIL");
        toggleButtonOne.setText("");

        toggleButtonTwo.setTextOff("");
        toggleButtonTwo.setTextOn("EVIL");
        toggleButtonTwo.setText("");

        toggleButtonThree.setTextOff("");
        toggleButtonThree.setTextOn("EVIL");
        toggleButtonThree.setText("");

        toggleButtonFour.setTextOff("");
        toggleButtonFour.setTextOn("GOOD");
        toggleButtonFour.setText("");

        toggleButtonFive.setTextOff("");
        toggleButtonFive.setTextOn("GOOD");
        toggleButtonFive.setText("");

        toggleButtonSix.setTextOff("");
        toggleButtonSix.setTextOn("EVIL");
        toggleButtonSix.setText("");

        toggleButtonSeven.setTextOff("");
        toggleButtonSeven.setTextOn("EVIL");
        toggleButtonSeven.setText("");

        toggleButtonEight.setTextOff("");
        toggleButtonEight.setTextOn("EVIL");
        toggleButtonEight.setText("");

        toggleButtonNine.setTextOff("");
        toggleButtonNine.setTextOn("GOOD");
        toggleButtonNine.setText("");

        toggleButtonTen.setTextOff("");
        toggleButtonTen.setTextOn("EVIL");
        toggleButtonTen.setText("");
    }

    private void getNumber() {
        number = toggleButtonOne.getText().toString() + toggleButtonTwo.getText().toString() + toggleButtonThree.getText().toString() + toggleButtonFour.getText().toString() +
                toggleButtonFive.getText().toString() +  toggleButtonSix.getText().toString() +  toggleButtonSeven.getText().toString() +  toggleButtonEight.getText().toString() +
                toggleButtonNine.getText().toString() +  toggleButtonTen.getText().toString();
        Pattern pattern = Pattern.compile("[^EVIL]*EVIL");
        Matcher matcher = pattern.matcher(number);
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        Pattern patternWrong = Pattern.compile("[^GOOD]*GOOD");
        Matcher matcherWrong = patternWrong.matcher(number);
        int countWrong = 0;
        while (matcherWrong.find()) {
            countWrong++;
        }

        if (randomInt == count && countWrong==0) {
            reset();
            openPropertiesActivity();
        }
    }


    private void reset() {
        toggleButtonOne.setClickable(false);
        toggleButtonTwo.setClickable(false);
        toggleButtonThree.setClickable(false);
        toggleButtonFour.setClickable(false);
        toggleButtonFive.setClickable(false);
        toggleButtonSix.setClickable(false);
        toggleButtonSeven.setClickable(false);
        toggleButtonEight.setClickable(false);
        toggleButtonNine.setClickable(false);
        toggleButtonTen.setClickable(false);
    }

    private void openPropertiesActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}