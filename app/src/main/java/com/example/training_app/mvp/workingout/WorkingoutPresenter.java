package com.example.training_app.mvp.workingout;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

import com.example.training_app.R;
import com.example.training_app.common.adapters.ExerciseMadeListViewAdapter;
import com.example.training_app.common.adapters.ExerciseListViewAdapter;
import com.example.training_app.common.fragments.DailyStatisticsFragment;
import com.example.training_app.common.fragments.ExerciseListViewFragment;
import com.example.training_app.common.interfaces.IDayExerciseModel;
import com.example.training_app.common.interfaces.IDayModel;
import com.example.training_app.common.interfaces.IExerciseModel;
import com.example.training_app.common.interfaces.ILoadAllDaysCallback;
import com.example.training_app.common.interfaces.ILoadAllExerciseWorkingoutCallback;
import com.example.training_app.common.interfaces.ILoadDayCallback;
import com.example.training_app.common.interfaces.ILoadExerciseCallback;
import com.example.training_app.common.interfaces.ILoadExerciseWorkingoutCallback;
import com.example.training_app.common.models.Day;
import com.example.training_app.common.models.Workingout;
import com.example.training_app.common.models.Exercise;
import com.example.training_app.common.storage.ConstantStorage;
import com.example.training_app.database.tables.DayExerciseTable;
import com.example.training_app.mvp.main.MainActivity;
import com.example.training_app.mvp.models.day.AbstractDayData;
import com.example.training_app.mvp.models.day.ExtendedDayData;
import com.example.training_app.mvp.models.day_exercise.DayExerciseData;
import com.example.training_app.mvp.models.day_exercise.DayExerciseModelSQLite;
import com.example.training_app.mvp.models.synchronization.SynchronizationModelFirebase;
import com.example.training_app.mvp.models.synchronization.SynchronizationModelSQLite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WorkingoutPresenter {

    private ExerciseListViewFragment exerciseListViewFragment;
    private DailyStatisticsFragment dailyStatisticsFragment;

    private final List<String> listOfWorkingout;

    private WorkingoutFragment view;
    private final IExerciseModel exerciseModel;
    private final IDayModel dayModel;
    private final IDayExerciseModel dayExerciseModel;

    private ExerciseMadeListViewAdapter exerciseWorkingoutListViewAdapter;

    private final Context context;

    public static SharedPreferences INCREMENTAL_DAY_ID;
    public static SharedPreferences INCREMENTAL_EXERCISE_ID;

    public static final String DAY_ID = "DAY_ID";
    public static final String EXERCISE_ID = "EXERCISE_ID";

    private final String[] menuItems;

    private HashMap<String, ArrayList<Exercise>> exerciseList;

    private final String menuItemCreateExercise;
    private final String menuItemViewStatistics;
    private final String menuItemSyncRecords;

    private final Day selectedDay;

    private final String exerciseAddedMessage;
    private final String exerciseDeletedMessage;

    private final String SELECTED_DATE;

    private ArrayList<AbstractDayData> listOfDaysFromFirebase;
    private ArrayList<Exercise> listOfExerciseFromFirebase;

    private ArrayList<DayExerciseData> listOfExerciseWorkingoutFromSQLite;
    private ArrayList<AbstractDayData> listOfDaysFromSQLite;
    private ArrayList<Exercise> listOfExerciseFromSQLite;

    public WorkingoutPresenter(IExerciseModel exerciseModel, IDayModel dayModel, IDayExerciseModel dayExerciseModel, Context context) {
        this.exerciseModel = exerciseModel;
        this.dayModel = dayModel;
        this.dayExerciseModel = dayExerciseModel;

        this.context = context;

        String[] nameOfWorkingout = context.getResources().getStringArray(R.array.nameOfWorkingout);
        listOfWorkingout = Arrays.asList(nameOfWorkingout);

        menuItems = context.getResources().getStringArray(R.array.menuItems);
        menuItemCreateExercise = context.getResources().getString(R.string.menuItemCreateExercise);
        menuItemViewStatistics = context.getResources().getString(R.string.menuItemViewStatistics);
        menuItemSyncRecords = context.getResources().getString(R.string.menuItemSyncRecords);

        exerciseAddedMessage = context.getResources().getString(R.string.exerciseAddedMessage);
        exerciseDeletedMessage = context.getResources().getString(R.string.exerciseDeletedMessage);

        selectedDay = new Day();
        exerciseList = new HashMap<>();

        INCREMENTAL_DAY_ID = INCREMENTAL_EXERCISE_ID = context.getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE);

        SELECTED_DATE = context.getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE).getString(MainActivity.SELECTED_DATE, "");

        checkSharedPreferences();
    }

    private void checkSharedPreferences() {
        if (INCREMENTAL_DAY_ID.getLong(DAY_ID, 0) == 0) {
            Editor editor = INCREMENTAL_DAY_ID.edit();
            editor.putLong(DAY_ID, 1);
            editor.apply();
        }
        if (INCREMENTAL_EXERCISE_ID.getLong(EXERCISE_ID, 0) == 0) {
            Editor editor = INCREMENTAL_EXERCISE_ID.edit();
            editor.putLong(EXERCISE_ID, 1);
            editor.apply();
        }
    }

    public static long getId(SharedPreferences preferences, String key) {
        return preferences.getLong(key, 0);
    }

    public static void increment(SharedPreferences preferences, String key) {
        Editor editor = preferences.edit();
        long id = getId(preferences, key);
        editor.putLong(key, id + 1);
        editor.apply();
    }

    public void attachView(WorkingoutFragment workingoutActivity) {
        view = workingoutActivity;
    }

    public void initializeFragments() {
        exerciseListViewFragment = new ExerciseListViewFragment(this, context);
        dailyStatisticsFragment = new DailyStatisticsFragment(this, context);
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        view.getChildFragmentManager().beginTransaction().add(R.id.fragmentExerciseWorkingout, dailyStatisticsFragment).commit();
    }

    private void threadSleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDayStatistics();
                if (dayExerciseModel instanceof DayExerciseModelSQLite) loadExerciseWorkingout();
                loadExercise();
                setStatisticsWindowOnDayStatisticsFragment();
            }
        }).start();
    }

    public void loadExercise() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Void> future = service.submit(new Callable<Void>() {
            @Override
            public Void call() {
                exerciseModel.loadExercise(new ILoadExerciseCallback() {
                    @Override
                    public void onLoad(ArrayList<Exercise> exercise) {
                        ExerciseListViewAdapter exerciseListViewAdapter = exerciseListViewFragment.getAdapter();
                        view.showExercise(exerciseListViewAdapter, exercise, new ArrayList<>());
                    }
                });
                return null;
            }
        });
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDayStatistics() {
        ExtendedDayData dayData = new ExtendedDayData();
        dayData.setDate(SELECTED_DATE);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Void> future = service.submit(new Callable<Void>() {
            @Override
            public Void call() {
                dayModel.loadDayStatistics(dayData, new ILoadDayCallback() {
                    @Override
                    public void onLoad(AbstractDayData day) {
                        if (day.getId() == 0) {
                            addDayStatistics();
                        }
                        else {
                            selectedDay.setId(day.getId());
                            try {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                calendar.setTime(Objects.requireNonNull(simpleDateFormat.parse(day.getDate())));
                                selectedDay.setDate(calendar.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            selectedDay.setCalories(day.getCalories());
                            selectedDay.setCards(day.getCards());
                            selectedDay.setStrengths(day.getStrengths());
                            selectedDay.setAgilitys(day.getAgilitys());

                            if (!(dayExerciseModel instanceof DayExerciseModelSQLite)) {
                                exerciseWorkingoutListViewAdapter = dailyStatisticsFragment.getAdapter();
                                exerciseList = ((ExtendedDayData) day).getExerciseList() == null ? new HashMap<>() : ((ExtendedDayData) day).getExerciseList();
                                String key = "key_" + listOfWorkingout.indexOf(ConstantStorage.TITLE);
                                ArrayList<Exercise> exercise = exerciseList.get(key) == null ? new ArrayList<>() : exerciseList.get(key);
                                view.showExercise(exerciseWorkingoutListViewAdapter, exercise, exercise);
                            }
                            updateDayStatistics();
                        }
                    }
                });
                return null;
            }
        });
        try {
            future.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadExerciseWorkingout() {
        String id = String.valueOf(selectedDay.getId());
        ContentValues cv = new ContentValues(2);
        cv.put(DayExerciseTable.COLUMN.DAY_ID, id);
        cv.put(DayExerciseTable.COLUMN.WORKINGOUT, "key_" + listOfWorkingout.indexOf(ConstantStorage.TITLE));
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Void> future = service.submit(new Callable<Void>() {
            @Override
            public Void call() {
                dayExerciseModel.loadExerciseWorkingout(cv, new ILoadExerciseWorkingoutCallback() {
                    @Override
                    public void onLoad(ArrayList<Exercise> exercise) {
                        exerciseWorkingoutListViewAdapter = dailyStatisticsFragment.getAdapter();
                        view.showExercise(exerciseWorkingoutListViewAdapter, exercise, exercise);
                    }
                });
                return null;
            }
        });
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDayStatistics() {
        ExtendedDayData dayData = new ExtendedDayData();
        dayData.setId(getId(INCREMENTAL_DAY_ID, DAY_ID));
        dayData.setDate(SELECTED_DATE);
        dayData.setCalories(0);
        dayData.setCards(0);
        dayData.setStrengths(0);
        dayData.setAgilitys(0);

        dayData.setExerciseList(new HashMap<>());

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                dayModel.addDayStatistics(dayData);
                increment(INCREMENTAL_DAY_ID, DAY_ID);
                loadDayStatistics();
            }
        });
    }

    public void addExerciseMade() {
        Exercise selectedExercise = exerciseListViewFragment.getAdapter().getSelectedExercise();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                view.setIsSubmit(true);
                view.clearFocus();

                ExtendedDayData dayData = new ExtendedDayData();
                dayData.setId(selectedDay.getId());
                dayData.setDate(SELECTED_DATE);
                dayData.setCalories(selectedDay.getCalories() + selectedExercise.getCalories());
                dayData.setCards(selectedDay.getCards() + selectedExercise.getCards());
                dayData.setStrengths(selectedDay.getStrengths() + selectedExercise.getStrengths());
                dayData.setAgilitys(selectedDay.getAgilitys() + selectedExercise.getAgilitys());

                ArrayList<Exercise> exercise = (dailyStatisticsFragment.getAdapter()).getListFiltered();
                exercise.add(selectedExercise);
                exerciseList.put("key_" + listOfWorkingout.indexOf(ConstantStorage.TITLE), exercise);
                dayData.setExerciseList(exerciseList);

                updateDayStatistics(dayData);

                threadSleep();

                if (dayExerciseModel instanceof DayExerciseModelSQLite) {
                    DayExerciseData dayExerciseData = new DayExerciseData();
                    dayExerciseData.setExerciseId(selectedExercise.getId());
                    dayExerciseData.setDayId(selectedDay.getId());
                    dayExerciseData.setWorkingout("key_" + listOfWorkingout.indexOf(ConstantStorage.TITLE));

                    ContentValues cv = new ContentValues(3);
                    cv.put(DayExerciseTable.COLUMN.EXERCISE_ID, dayExerciseData.getExerciseId());
                    cv.put(DayExerciseTable.COLUMN.DAY_ID, dayExerciseData.getDayId());
                    cv.put(DayExerciseTable.COLUMN.WORKINGOUT, dayExerciseData.getWorkingout());

                    dayExerciseModel.addExerciseWorkingout(cv);
                    loadExerciseWorkingout();
                }

                view.replaceFragment(dailyStatisticsFragment);
                view.setIsSubmit(false);
                view.showToast(exerciseAddedMessage);
            }
        });
    }

    public void updateDayStatistics(AbstractDayData dayData) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                dayModel.updateDayStatistics(dayData);
                loadDayStatistics();
            }
        });
    }

    public void deleteExerciseMade() {
        Exercise selectedExercise = dailyStatisticsFragment.getAdapter().getSelectedExercise();

        ExtendedDayData dayData = new ExtendedDayData();
        dayData.setId(selectedDay.getId());
        dayData.setDate(SELECTED_DATE);
        dayData.setCalories(selectedDay.getCalories() - selectedExercise.getCalories());
        dayData.setCards(selectedDay.getCards() - selectedExercise.getCards());
        dayData.setStrengths(selectedDay.getStrengths() - selectedExercise.getStrengths());
        dayData.setAgilitys(selectedDay.getAgilitys() - selectedExercise.getAgilitys());

        ArrayList<Exercise> exercise = (dailyStatisticsFragment.getAdapter()).getListFiltered();
        exercise.remove(selectedExercise);
        exerciseList.put("key_" + listOfWorkingout.indexOf(ConstantStorage.TITLE), exercise);
        dayData.setExerciseList(exerciseList);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                updateDayStatistics(dayData);

                threadSleep();

                if (dayExerciseModel instanceof DayExerciseModelSQLite) {
                    ContentValues cv = new ContentValues(1);
                    cv.put(DayExerciseTable.COLUMN.ID, selectedExercise.getId());

                    dayExerciseModel.deleteExerciseWorkingout(cv);
                    loadExerciseWorkingout();
                }

                setStatisticsWindowOnDayStatisticsFragment();

                view.showToast(exerciseDeletedMessage);
            }
        });
    }

    private void synchronization() {
        IDayExerciseModel dopDayExerciseModelSQLite;
        IDayModel dopDayModelSQLite;
        IExerciseModel dopExerciseModelSQLite;
        SynchronizationModelSQLite synchronizationModelSQLite;

        IDayModel dopDayModelFirebase;
        IExerciseModel dopExerciseModelFirebase;
        SynchronizationModelFirebase synchronizationModelFirebase;

        if (dayExerciseModel instanceof DayExerciseModelSQLite) {
            dopDayModelFirebase = view.getDayModelFirebase();
            dopExerciseModelFirebase = view.getExerciseModelFirebase();

            dopDayExerciseModelSQLite = dayExerciseModel;
            dopDayModelSQLite = dayModel;
            dopExerciseModelSQLite = exerciseModel;
        }
        else {
            dopDayModelFirebase = dayModel;
            dopExerciseModelFirebase = exerciseModel;

            dopDayExerciseModelSQLite = view.getDayExerciseModelSQLite();
            dopDayModelSQLite = view.getDayModelSQLite();
            dopExerciseModelSQLite = view.getExerciseModelSQLite();
        }

        synchronizationModelSQLite = view.getSynchronizationModelSQLite();
        synchronizationModelFirebase = view.getSynchronizationModelFirebase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                view.showProgress();
                ExecutorService service = Executors.newSingleThreadExecutor();
                Future<Void> future = service.submit(new Callable<Void>() {
                    @Override
                    public Void call() {
                        CountDownLatch countDownLatch = new CountDownLatch(5);
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.submit(new Callable<Void>() {
                            @Override
                            public Void call() {

                                dopDayModelFirebase.loadAllDays(new ILoadAllDaysCallback() {
                                    @Override
                                    public void onLoad(ArrayList<AbstractDayData> list) {
                                        listOfDaysFromFirebase = list == null ? new ArrayList<>() : list;
                                        countDownLatch.countDown();
                                    }
                                });

                                dopExerciseModelFirebase.loadExercise(new ILoadExerciseCallback() {
                                    @Override
                                    public void onLoad(ArrayList<Exercise> list) {
                                        listOfExerciseFromFirebase = list == null ? new ArrayList<>() : list;
                                        countDownLatch.countDown();
                                    }
                                });

                                dopDayModelSQLite.loadAllDays(new ILoadAllDaysCallback() {
                                    @Override
                                    public void onLoad(ArrayList<AbstractDayData> list) {
                                        listOfDaysFromSQLite = list == null ? new ArrayList<>() : list;
                                        countDownLatch.countDown();
                                    }
                                });

                                dopExerciseModelSQLite.loadExercise(new ILoadExerciseCallback() {
                                    @Override
                                    public void onLoad(ArrayList<Exercise> list) {
                                        listOfExerciseFromSQLite = list == null ? new ArrayList<>() : list;
                                        countDownLatch.countDown();
                                    }
                                });

                                dopDayExerciseModelSQLite.loadAllExerciseWorkingout(new ILoadAllExerciseWorkingoutCallback() {
                                    @Override
                                    public void onLoad(ArrayList<DayExerciseData> list) {
                                        listOfExerciseWorkingoutFromSQLite = list == null ? new ArrayList<>() : list;
                                        countDownLatch.countDown();
                                    }
                                });

                                return null;
                            }
                        });

                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Firebase list of exercise
                        ArrayList<Exercise> finalListOfExerciseForSQLite = getFinalListOfExercise(new ArrayList<>(getHashSetWithExerciseIds(listOfExerciseFromSQLite)), listOfExerciseFromFirebase);

                        // SQLite list of days
                        ArrayList<Exercise> finalListOfExerciseForFirebase = getFinalListOfExercise(new ArrayList<>(getHashSetWithExerciseIds(listOfExerciseFromFirebase)), listOfExerciseFromSQLite);

                        // Set of day ids based on exercise workingout from SQLite
                        Set<Long> setOfDayIds = new HashSet<>();
                        for (int i = 0; i < listOfExerciseWorkingoutFromSQLite.size(); i++) {
                            setOfDayIds.add(listOfExerciseWorkingoutFromSQLite.get(i).getDayId());
                        }

                        // List of day ids based on exercise workingout from SQLite
                        ArrayList<Long> listOfDayIds = new ArrayList<>(setOfDayIds);

                        // List of extended day from SQLite
                        ArrayList<AbstractDayData> dayDataArrayList = new ArrayList<>();

                        for (int i = 0; i < listOfDayIds.size(); i++) {
                            ExtendedDayData dayData = new ExtendedDayData();
                            ArrayList<Exercise> exerciseList = new ArrayList<>();
                            HashMap<String, ArrayList<Exercise>> mapFromSQLite = new HashMap<>();
                            for (int j = 0; j < listOfExerciseWorkingoutFromSQLite.size(); j++) {
                                DayExerciseData dayExerciseData = listOfExerciseWorkingoutFromSQLite.get(j);
                                if (dayExerciseData.getDayId() == listOfDayIds.get(i)) {
                                    exerciseList = mapFromSQLite.get(dayExerciseData.getWorkingout()) == null ?
                                            new ArrayList<>() : mapFromSQLite.get(dayExerciseData.getWorkingout());
                                    for (int z = 0; z < listOfExerciseFromSQLite.size(); z++) {
                                        Exercise exercise = listOfExerciseFromSQLite.get(z);
                                        if (exercise.getId() == dayExerciseData.getExerciseId()) {
                                            dayData.setCalories(dayData.getCalories() + exercise.getCalories());
                                            dayData.setCards(dayData.getCards() + exercise.getCards());
                                            dayData.setStrengths(dayData.getStrengths() + exercise.getStrengths());
                                            dayData.setAgilitys(dayData.getAgilitys() + exercise.getAgilitys());
                                            exerciseList.add(exercise);
                                            break;
                                        }
                                    }
                                }
                                mapFromSQLite.put(dayExerciseData.getWorkingout(), exerciseList);
                                dayData.setExerciseList(mapFromSQLite);
                            }
                            dayData.setId(listOfDayIds.get(i));
                            dayData.setDate(getDateOnDayId(listOfDaysFromSQLite, dayData.getId()));
                            dayDataArrayList.add(dayData);
                        }

                        ArrayList<DayExerciseData> finalListOfExerciseWorkingoutForSQLite = new ArrayList<>();

                        ArrayList<String> dayIdsFromFirebase = new ArrayList<>(getHashSetWithDays(listOfDaysFromFirebase));
                        ArrayList<String> dayIdsFromSQLite = new ArrayList<>(getHashSetWithDays(dayDataArrayList));

                        HashSet<AbstractDayData> finalSetOfDayForFirebase = new HashSet<>();
                        for (int i = 0; i < dayIdsFromFirebase.size(); i++) {
                            for (AbstractDayData dayData: dayDataArrayList) {
                                if (!dayIdsFromFirebase.contains(dayData.getDate())) {
                                    finalSetOfDayForFirebase.add(dayData);
                                }
                            }
                        }

                        HashSet<AbstractDayData> finalSetOfDayForSQLite = new HashSet<>();
                        for (int i = 0; i < dayIdsFromSQLite.size(); i++) {
                            for (AbstractDayData dayData: listOfDaysFromFirebase) {
                                if (!dayIdsFromSQLite.contains(dayData.getDate())) {
                                    finalSetOfDayForSQLite.add(dayData);
                                }
                            }
                        }

                        if (dayIdsFromFirebase.size() == 0 && dayIdsFromSQLite.size() != 0) {
                            finalSetOfDayForFirebase.addAll(dayDataArrayList);
                        }
                        else if (dayIdsFromFirebase.size() != 0 && dayIdsFromSQLite.size() == 0) {
                            finalSetOfDayForSQLite.addAll(listOfDaysFromFirebase);
                        }

                        // Firebase list of days
                        ArrayList<AbstractDayData> finalListOfDayDataForFirebase = new ArrayList<>(finalSetOfDayForFirebase);

                        // SQLite list of days for insert
                        ArrayList<AbstractDayData> finalListOfDayDataForSQLiteForInsert = new ArrayList<>(finalSetOfDayForSQLite);

                        for (AbstractDayData dayData : finalSetOfDayForSQLite) {
                            HashMap<String, ArrayList<Exercise>> map = ((ExtendedDayData) dayData).getExerciseList() == null ?
                                    new HashMap<>() : ((ExtendedDayData) dayData).getExerciseList();
                            Set<String> set = new HashSet<>(new ArrayList<>(map.keySet()));
                            ArrayList<String> keys = new ArrayList<>(set);
                            for (String key : keys) {
                                ArrayList<Exercise> listOfExercise = map.get(key) == null ? new ArrayList<>() : map.get(key);
                                for (Exercise exercise : listOfExercise) {
                                    DayExerciseData dayExerciseData = new DayExerciseData();
                                    dayExerciseData.setDayId(dayData.getId());
                                    dayExerciseData.setWorkingout(key);
                                    dayExerciseData.setExerciseId(exercise.getId());
                                    finalListOfExerciseWorkingoutForSQLite.add(dayExerciseData);
                                }
                            }
                        }

                        synchronizationModelFirebase.syncRecords(finalListOfDayDataForFirebase, finalListOfExerciseForFirebase);

                        synchronizationModelSQLite.syncRecords(finalListOfDayDataForSQLiteForInsert, finalListOfExerciseForSQLite, finalListOfExerciseWorkingoutForSQLite);

                        return null;
                    }
                });
                try {
                    future.get();
                    loadData();
                    view.hideProgress();
                    view.showToast(context.getResources().getString(R.string.syncronizationCallbackMessage));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public ArrayList<Exercise> getFinalListOfExercise(ArrayList<Long> exerciseIds, ArrayList<Exercise> listOfExerciseForComparison) {
        ArrayList<Exercise> finalListOfExercise = new ArrayList<>();
        if (exerciseIds.size() != 0) {
            for (int i = 0; i < exerciseIds.size(); i++) {
                for (Exercise exercise : listOfExerciseForComparison) {
                    if (!exerciseIds.contains(exercise.getId())) {
                        finalListOfExercise.add(exercise);
                    }
                }
            }
        } else {
            finalListOfExercise.addAll(listOfExerciseForComparison);
        }
        return finalListOfExercise;
    }

    public HashMap<String, ArrayList<Exercise>> getHashMapOnDayId(ArrayList<AbstractDayData> list, Long id) {
        for (AbstractDayData dayData : list) if (dayData.getId() == id) return ((ExtendedDayData) dayData).getExerciseList();
        return new HashMap<>();
    }

    public String getDateOnDayId(ArrayList<AbstractDayData> list, Long id) {
        for (AbstractDayData dayData : list) if (dayData.getId() == id) return dayData.getDate();
        return null;
    }

    public Set<String> getHashSetWithDay(ArrayList<AbstractDayData> listOfDayFromSQLite, ArrayList<AbstractDayData> listOfDayFromFirebase) {
        Set<String> objectsIds = new HashSet<>();

        for (AbstractDayData dayData : listOfDayFromSQLite) {
            objectsIds.add(dayData.getDate());
        }

        for (AbstractDayData dayData : listOfDayFromFirebase) {
            if (objectsIds.contains(dayData.getDate())) {
                objectsIds.remove(dayData.getDate());
            } else {
                objectsIds.add(dayData.getDate());
            }
        }
        return objectsIds;
    }

    public Set<Long> getHashSetWithUniqueExerciseIds(ArrayList<Exercise> listOfExerciseFromSQLite, ArrayList<Exercise> listOfExerciseFromFirebase) {
        Set<Long> objectsIds = new HashSet<>();

        for (Exercise exercise : listOfExerciseFromSQLite) {
            objectsIds.add(exercise.getId());
        }

        for (Exercise exercise : listOfExerciseFromFirebase) {
            if (objectsIds.contains(exercise.getId())) {
                objectsIds.remove(exercise.getId());
            } else {
                objectsIds.add(exercise.getId());
            }
        }
        return objectsIds;
    }

    public Set<String> getHashSetWithDays(ArrayList<AbstractDayData> list) {
        Set<String> objectsIds = new HashSet<>();
        for (AbstractDayData dayData : list) {
            objectsIds.add(dayData.getDate());
        }
        return objectsIds;
    }

    public Set<Long> getHashSetWithExerciseIds(ArrayList<Exercise> list) {
        Set<Long> objectsIds = new HashSet<>();
        for (Exercise exercise : list) {
            objectsIds.add(exercise.getId());
        }
        return objectsIds;
    }

    public void onFocusChange() {
        boolean isFocused = view.getIsFocused();
        boolean isSubmit = view.getIsSubmit();
        if (isFocused && !isSubmit) {
            view.setSearchViewLayoutWidth(android.widget.SearchView.LayoutParams.WRAP_CONTENT);
            view.setLinearLayoutHeadingVisible(View.GONE);
            view.setButtonCancelVisible(View.VISIBLE);
            view.getChildFragmentManager().beginTransaction().replace(R.id.fragmentExerciseWorkingout, exerciseListViewFragment).commit();
        }
        else if (!isSubmit) {
            view.clearFocus();
            view.replaceFragment(dailyStatisticsFragment);
        }
    }

    public boolean onQueryTextSubmit() {
        String query = view.getQuery();
        exerciseListViewFragment.filterItems(query);
        view.setIsSubmit(true);
        view.clearFocus();
        return false;
    }

    public boolean onQueryTextChange() {
        String query = view.getQuery();
        exerciseListViewFragment.filterItems(query);
        return false;
    }

    public void onCancel() {
        boolean isSubmit = view.getIsSubmit();
        if (!isSubmit) {
            view.clearFocus();
        }
        else {
            view.setIsSubmit(false);
            view.replaceFragment(dailyStatisticsFragment);
        }
    }

    public void onButtonMenuClick() {
        view.onCreateDialogMenu();
    }

    public void onAfterSelectMenuItem() {
        int position = view.getPosition();
        String item = menuItems[position];

        if (item.equals(menuItemCreateExercise)) {
            view.createExercise();
        }
        else if (item.equals(menuItemViewStatistics)) {
            ArrayList<Exercise> exerciseWorkingout = view.getListFiltered(exerciseWorkingoutListViewAdapter);
            view.viewStatistics(exerciseWorkingout, getWorkingout(exerciseWorkingout));
        }
        else if (item.equals(menuItemSyncRecords)) {
            synchronization();
        }
    }

    private Workingout getWorkingout(ArrayList<Exercise> exerciseWorkingout) {
        Workingout workingout = new Workingout();
        workingout.setAllCaloriesWorkingout(selectedDay.getCalories());
        workingout.setAllCardsWorkingout(selectedDay.getCards());
        workingout.setAllStrengthsWorkingout(selectedDay.getStrengths());
        workingout.setAllAgilitysWorkingout(selectedDay.getAgilitys());

        int calories = 0;
        int cards = 0;
        int strengths = 0;
        int agilitys = 0;

        for (Exercise item: exerciseWorkingout) {
            calories += item.getCalories();
            cards += item.getCards();
            strengths += item.getStrengths();
            agilitys += item.getAgilitys();
        }

        workingout.setCurrentCaloriesWorkingout(calories);
        workingout.setCurrentCardsWorkingout(cards);
        workingout.setCurrentStrengthsWorkingout(strengths);
        workingout.setCurrentAgilitysWorkingout(agilitys);
        return workingout;
    }

    private void setStatisticsWindowOnDayStatisticsFragment() {
        if (selectedDay.getId() != 0) {
            dailyStatisticsFragment.setStatisticsWindow();
        }
    }

    public void onBack() {
        view.onClose();
    }

    private void updateDayStatistics() {
        dailyStatisticsFragment.setDay(selectedDay);
        dailyStatisticsFragment.updateStatistics();
    }
}