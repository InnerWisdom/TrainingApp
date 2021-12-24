package com.example.training_app.mvp.workingout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.training_app.R;
import com.example.training_app.common.activities.ChooseActivity;
import com.example.training_app.common.adapters.AbstractListViewAdapter;
import com.example.training_app.common.interfaces.IDayExerciseModel;
import com.example.training_app.common.interfaces.IDayModel;
import com.example.training_app.common.interfaces.IExerciseModel;
import com.example.training_app.common.models.Workingout;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.common.storage.ConstantStorage;
import com.example.training_app.database.DBHelper;
import com.example.training_app.mvp.exercise.CreateExerciseFragment;
import com.example.training_app.mvp.models.day.DayModelFirebase;
import com.example.training_app.mvp.models.day.DayModelSQLite;
import com.example.training_app.mvp.models.day_exercise.DayExerciseModelSQLite;
import com.example.training_app.mvp.models.exercise.ExerciseModelFirebase;
import com.example.training_app.mvp.models.exercise.ExerciseModelSQLite;
import com.example.training_app.mvp.models.synchronization.SynchronizationModelFirebase;
import com.example.training_app.mvp.models.synchronization.SynchronizationModelSQLite;
import com.example.training_app.mvp.statistics.ExerciseStatisticsFragment;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WorkingoutFragment extends Fragment {

    public String CHOOSE;

    private TextView textViewTitle;

    private SearchView searchView;
    private Button buttonCancel;
    private LinearLayout linearLayoutHeading;

    private WorkingoutPresenter presenter;

    private boolean isSubmit = false;
    private boolean isFocused = false;

    private ProgressDialog progressDialog;

    private DBHelper dbHelper;
    private FirebaseDatabase firebaseDatabase;

    private IExerciseModel exerciseModelSQLite;
    private IDayExerciseModel dayExerciseModelSQLite;
    private IDayModel dayModelSQLite;
    private SynchronizationModelSQLite synchronizationModelSQLite;

    private IExerciseModel exerciseModelFirebase;
    private IDayModel dayModelFirebase;
    private SynchronizationModelFirebase synchronizationModelFirebase;

    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workingout, container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setTitle(ConstantStorage.TITLE);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }

    public ExerciseModelSQLite getExerciseModelSQLite() { return (ExerciseModelSQLite) exerciseModelSQLite; }
    public DayExerciseModelSQLite getDayExerciseModelSQLite() { return (DayExerciseModelSQLite) dayExerciseModelSQLite; }
    public DayModelSQLite getDayModelSQLite() { return (DayModelSQLite) dayModelSQLite; }
    public SynchronizationModelSQLite getSynchronizationModelSQLite() { return synchronizationModelSQLite; }

    public ExerciseModelFirebase getExerciseModelFirebase() { return (ExerciseModelFirebase) exerciseModelFirebase; }
    public DayModelFirebase getDayModelFirebase() { return (DayModelFirebase) dayModelFirebase; }
    public SynchronizationModelFirebase getSynchronizationModelFirebase() { return synchronizationModelFirebase; }

    private void initialize(View view) {
        textViewTitle = view.findViewById(R.id.textViewTitle);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        linearLayoutHeading = view.findViewById(R.id.linearLayoutHeading);

        searchView = view.findViewById(R.id.searchViewId);
        searchView.clearFocus();
        searchView.setFocusable(false);

        Context context = getContext();

        CHOOSE = context.getSharedPreferences(ChooseActivity.CHOOSE, Context.MODE_PRIVATE).getString(ChooseActivity.CHOOSE, "");

        dbHelper = new DBHelper(context);
        firebaseDatabase = FirebaseDatabase.getInstance(ConstantStorage.firebaseDatabaseUrl);

        exerciseModelSQLite = new ExerciseModelSQLite(dbHelper);
        dayExerciseModelSQLite = new DayExerciseModelSQLite(dbHelper);
        dayModelSQLite = new DayModelSQLite(dbHelper);
        synchronizationModelSQLite = new SynchronizationModelSQLite(dbHelper);

        exerciseModelFirebase = new ExerciseModelFirebase(firebaseDatabase);
        dayModelFirebase = new DayModelFirebase(firebaseDatabase);
        synchronizationModelFirebase = new SynchronizationModelFirebase(firebaseDatabase);

        if (CHOOSE.equals("LOCAL")) {
            presenter = new WorkingoutPresenter(exerciseModelSQLite, dayModelSQLite, dayExerciseModelSQLite, context);
        }
        else {
            presenter = new WorkingoutPresenter(exerciseModelFirebase, dayModelFirebase, null, context);
        }

        presenter.attachView(this);
        presenter.initializeFragments();
        presenter.loadData();
        presenter.viewIsReady();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCancel();
            }
        });

        view.findViewById(R.id.buttonMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonMenuClick();
            }
        });

        view.findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBack();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return presenter.onQueryTextSubmit();
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                return presenter.onQueryTextChange();
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isFocused = hasFocus;
                presenter.onFocusChange();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onCreateDialogMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.selectionMenuTitle)
                .setIcon(getResources().getDrawable(R.drawable.question))
                .setItems(R.array.menuItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                        presenter.onAfterSelectMenuItem();
                    }
                });
        builder.create().show();
    }

    public int getPosition() {
        return position;
    }

    public void clearFocus() {
        requireActivity().runOnUiThread(() -> {
            searchView.clearFocus();
        });
    }

    public void setQuery(String query, boolean isSubmit) {
        searchView.setQuery(query, isSubmit);
    }

    public boolean getIsSubmit() {
        return isSubmit;
    }

    public boolean getIsFocused() {
        return isFocused;
    }

    public void setIsSubmit(boolean value) {
        requireActivity().runOnUiThread(() -> {
            isSubmit = value;
        });
    }

    public void showToast(String text) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show();
        });
    }

    public void setSearchViewLayoutWidth(int value) {
        ViewGroup.LayoutParams layoutParams = searchView.getLayoutParams();
        layoutParams.width = value;
        searchView.setLayoutParams(layoutParams);
    }

    public void showProgress() {
        requireActivity().runOnUiThread(() -> {
            progressDialog = ProgressDialog.show(requireContext(), "", getString(R.string.progressDialogText));
        });
    }

    public void hideProgress() {
        requireActivity().runOnUiThread(() -> {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        });
    }

    public void setLinearLayoutHeadingVisible(int value) {
        linearLayoutHeading.setVisibility(value);
    }

    public void setButtonCancelVisible(int value) {
        buttonCancel.setVisibility(value);
    }

    public String getQuery() {
        return searchView.getQuery().toString();
    }

    public void showExercise(AbstractListViewAdapter adapter, ArrayList<Exercise> originalList, ArrayList<Exercise> listFiltered) {
        requireActivity().runOnUiThread(() -> {
            adapter.setListFiltered(listFiltered);
            adapter.setOriginalList(originalList);
        });
    }

    public ArrayList<Exercise> getListFiltered(AbstractListViewAdapter adapter) {
        return adapter.getListFiltered();
    }

    public void viewStatistics(ArrayList<Exercise> exerciseWorkingout, Workingout workingout) {
        ExerciseStatisticsFragment exerciseStatisticsFragment = new ExerciseStatisticsFragment(requireContext(), exerciseWorkingout, workingout);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, exerciseStatisticsFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void createExercise() {
        IExerciseModel exerciseModel = CHOOSE.equals("LOCAL") ? exerciseModelSQLite : exerciseModelFirebase;
        CreateExerciseFragment createExerciseFragment = new CreateExerciseFragment(exerciseModel);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, createExerciseFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void onClose() {
        presenter.detachView();
        requireActivity().finish();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { onClose(); }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void replaceFragment(Fragment fragment) {
        requireActivity().runOnUiThread(() -> {
            setQuery("", false);
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentExerciseWorkingout, fragment).commit();
            setButtonCancelVisible(View.GONE);
            setLinearLayoutHeadingVisible(View.VISIBLE);
            setSearchViewLayoutWidth(SearchView.LayoutParams.MATCH_PARENT);
        });
    }
}