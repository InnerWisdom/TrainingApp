package com.example.training_app.common.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.training_app.R;
import com.example.training_app.common.interfaces.ICreateReportCallback;
import com.example.training_app.common.reports.PdfReport;
import com.example.training_app.mvp.workingout.WorkingoutFragment;

public class WorkingoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workingout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView, WorkingoutFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case R.integer.PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyApp", "External_storage permission accessed");
                }
                else {
                    Log.i("MyApp", "External_storage permission denied");
                }
                break;
            case R.integer.PERMISSION_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyApp", "Internet permission accessed");
                }
                else {
                    Log.i("MyApp", "Internet permission denied");
                }
                break;
        }
    }
}