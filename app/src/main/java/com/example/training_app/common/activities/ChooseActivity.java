package com.example.training_app.common.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.example.training_app.R;
import com.example.training_app.mvp.main.MainActivity;

public class ChooseActivity extends AppCompatActivity {

    public static final String CHOOSE = "CHOOSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        findViewById(R.id.buttonLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editor editor = getSharedPreferences(CHOOSE, Context.MODE_PRIVATE).edit();
                editor.putString(CHOOSE, "LOCAL");
                editor.apply();
                startActivity(new Intent(ChooseActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.buttonClientServer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editor editor = getSharedPreferences(CHOOSE, Context.MODE_PRIVATE).edit();
                editor.putString(CHOOSE, "CLIENT-SERVER");
                editor.apply();
                startActivity(new Intent(ChooseActivity.this, MainActivity.class));
            }
        });
    }
}