package com.example.training_app.mvp.statistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.training_app.R;
import com.example.training_app.common.adapters.ExerciseMadeStatisticsListViewAdapter;
import com.example.training_app.common.models.Workingout;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.common.storage.ConstantStorage;
import com.example.training_app.mvp.main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseStatisticsFragment extends Fragment {

    private ProgressBar progressBarCaloriesBackground, progressBarTotalCaloriesWorkingout, progressBarCaloriesWorkingout, progressBarCards, progressBarStrengths, progressBarAgilitys;
    private TextView textViewCalories, textViewCards, textViewStrengths, textViewAgilitys, textViewCaloriesWorkingoutValue, textViewCaloriesLeftValue, textViewWorkingoutTitle, textViewWorkingoutDate;

    private ExerciseMadeStatisticsListViewAdapter exerciseWorkingoutStatisticsListViewAdapter;

    private final int maxCalories, maxCards, maxStrengths, maxAgilitys;
    private final ExerciseStatisticsPresenter presenter;
    private final float coefficient;

    private final String g;

    private final ArrayList<Exercise> exerciseWorkingout;
    private final Workingout workingout;
    private final Context context;

    public ExerciseStatisticsFragment(Context context, ArrayList<Exercise> exerciseWorkingout, Workingout workingout) {
        this.context = context;
        this.exerciseWorkingout = exerciseWorkingout;
        this.workingout = workingout;

        g = context.getResources().getString(R.string.gramItem);

        presenter = new ExerciseStatisticsPresenter();
        presenter.attachView(this);

        maxCalories = ConstantStorage.maxCalories;
        maxCards = ConstantStorage.maxCards;
        maxStrengths = ConstantStorage.maxStrengths;
        maxAgilitys = ConstantStorage.maxAgilitys;

        float degrees = 360;
        coefficient = degrees / maxCalories;

        initializeAdapter();
    }

    private void initializeAdapter() {
        exerciseWorkingoutStatisticsListViewAdapter = new ExerciseMadeStatisticsListViewAdapter(context);
        exerciseWorkingoutStatisticsListViewAdapter.setListFiltered(exerciseWorkingout);
    }

    public Workingout getWorkingout() {
        return workingout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_statistics, container, false);
        initialize(view);
        setGoal();
        setRotation();
        setStatisticsOnTextView();
        setStatisticsOnProgressBar();
        return view;
    }

    public void initialize(View view) {
        progressBarCaloriesBackground = view.findViewById(R.id.progressBarCaloriesBackground);
        progressBarTotalCaloriesWorkingout = view.findViewById(R.id.progressBarAllCaloriesMade);
        progressBarCaloriesWorkingout = view.findViewById(R.id.progressBarCurrentCaloriesMade);
        progressBarCards = view.findViewById(R.id.progressBarCardsExerciseStatistics);
        progressBarStrengths = view.findViewById(R.id.progressBarStrengthsExerciseStatistics);
        progressBarAgilitys = view.findViewById(R.id.progressBarAgilitysExerciseStatistics);

        textViewCalories = view.findViewById(R.id.textViewCaloriesValue);
        textViewCards = view.findViewById(R.id.textViewCardsExerciseStatistics);
        textViewStrengths = view.findViewById(R.id.textViewStrengthsExerciseStatistics);
        textViewAgilitys = view.findViewById(R.id.textViewAgilitysExerciseStatistics);
        textViewCaloriesWorkingoutValue = view.findViewById(R.id.textViewCaloriesMadeValue);
        textViewCaloriesLeftValue = view.findViewById(R.id.textViewCaloriesLeftValue);
        textViewWorkingoutTitle = view.findViewById(R.id.textViewWorkingoutTitle);
        textViewWorkingoutDate = view.findViewById(R.id.textViewWorkingoutDate);

        view.findViewById(R.id.buttonDownload).setOnClickListener(v -> presenter.onDownloadPDF());

        view.findViewById(R.id.buttonBack).setOnClickListener(v -> presenter.onBack());

        ListView listViewExerciseWorkingout = view.findViewById(R.id.listViewExerciseWorkingoutExerciseStatistics);
        listViewExerciseWorkingout.setAdapter(exerciseWorkingoutStatisticsListViewAdapter);
    }

    private void setRotation() {
        float rotation1 = -90;
        progressBarTotalCaloriesWorkingout.setRotation(rotation1);
        float rotation = coefficient * (workingout.getAllCaloriesWorkingout() - workingout.getCurrentCaloriesWorkingout()) + rotation1;
        progressBarCaloriesWorkingout.setRotation(rotation);
    }

    private void setGoal() {
        progressBarCaloriesBackground.setMax(maxCalories);
        progressBarTotalCaloriesWorkingout.setMax(maxCalories);
        progressBarCaloriesWorkingout.setMax(maxCalories);
        progressBarCards.setMax(maxCards);
        progressBarStrengths.setMax(maxStrengths);
        progressBarAgilitys.setMax(maxAgilitys);
    }

    @SuppressLint("DefaultLocale")
    private void setStatisticsOnTextView() {
        textViewCalories.setText(String.valueOf(workingout.getCurrentCaloriesWorkingout()));
        textViewCards.setText(String.format("%d %s / %d %s", workingout.getAllCardsWorkingout(), g, maxCards, g));
        textViewStrengths.setText(String.format("%d %s / %d %s", workingout.getAllStrengthsWorkingout(), g, maxStrengths, g));
        textViewAgilitys.setText(String.format("%d %s / %d %s", workingout.getAllAgilitysWorkingout(), g, maxAgilitys, g));
        textViewCaloriesWorkingoutValue.setText(String.valueOf(workingout.getAllCaloriesWorkingout()));
        textViewCaloriesLeftValue.setText(String.valueOf(maxCalories - workingout.getAllCaloriesWorkingout()));
        textViewWorkingoutTitle.setText(ConstantStorage.TITLE);
        textViewWorkingoutDate.setText(getDate());
    }

    private void setStatisticsOnProgressBar() {
        progressBarCaloriesBackground.setProgress(maxCalories);
        progressBarTotalCaloriesWorkingout.setProgress(workingout.getAllCaloriesWorkingout());
        progressBarCaloriesWorkingout.setProgress(workingout.getCurrentCaloriesWorkingout());
        progressBarCards.setProgress(workingout.getCurrentCardsWorkingout());
        progressBarCards.setSecondaryProgress(workingout.getAllCardsWorkingout());
        progressBarStrengths.setProgress(workingout.getCurrentStrengthsWorkingout());
        progressBarStrengths.setSecondaryProgress(workingout.getAllStrengthsWorkingout());
        progressBarAgilitys.setProgress(workingout.getCurrentAgilitysWorkingout());
        progressBarAgilitys.setSecondaryProgress(workingout.getAllAgilitysWorkingout());
    }

    @SuppressLint("SimpleDateFormat")
    private String getDate() {
        SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("dd MMMM, yyyy");
        Date date = new Date();
        try {
            date = simpleDateFormatInput.parse(requireActivity().getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE).getString(MainActivity.SELECTED_DATE, ""));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return simpleDateFormatOut.format(date);
    }

    public void onClose() {
        presenter.detachView();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}