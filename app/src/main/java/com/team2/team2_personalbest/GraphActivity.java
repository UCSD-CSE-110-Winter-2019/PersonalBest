package com.team2.team2_personalbest;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity {

    private DayDatabase dayDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final String DATABASE_NAME = "days_db";
        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();

//        DayViewModel model = ViewModelProviders.of(this).get(DayViewModel.class);
//
//        model.getDays().observe(this, days -> {
//            List<BarEntry> entries = new ArrayList<>();
//            for(int i = 0; i < days.size(); i++) {
//                entries.add(new BarEntry(i, new float[] {days.get(i).getStepsTracked(), days.get(i).getStepsUntracked()}));
//            }
//            createGraph(entries);
//        });


        new FillEntriesTask(this).execute(dayDatabase);



    }
//    private void addObserver(DayDatabase database, LiveData<Day> day) {
//        DataBaseObserver observer = new DataBaseObserver(database, this);
//        day.observe(this, observer);
//    }

    private class FillEntriesTask extends AsyncTask<DayDatabase, Void, List<BarEntry>> {

        Context mContext;

        public FillEntriesTask(Context context) {
            this.mContext = context;
        }
        @Override
        protected List<BarEntry> doInBackground(DayDatabase... databases) {
            DayDatabase database = databases[0];

            List<BarEntry> entries = new ArrayList<>();
            for(int i = 0; i < 7; i++) {
                String date = DateHelper.getPreviousDayDateString(i);

                Day currentDay = database.dayDao().getDayById(date);

                if(currentDay != null) {
                    Log.d("GraphActivity", Integer.toString(i));
                    entries.add(new BarEntry(i, new float[]{currentDay.getStepsUntracked(), currentDay.getStepsTracked()}));
                }
            }

            return entries;

        }
        @Override
        protected void onPostExecute(List<BarEntry> entries) {
            createGraph(entries);
        }
    }

    public void createGraph(List<BarEntry> entries) {
        BarChart chart = findViewById(R.id.chart);

        BarDataSet barDataSet = new BarDataSet(entries, "Daily walking");
        int color1 = ContextCompat.getColor(this, R.color.colorAccent);
        int color2 = ContextCompat.getColor(this, R.color.colorPrimary);
        //int color3 = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        barDataSet.setColors(color1, color2);

        BarData barData = new BarData(barDataSet);

        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        chart.setData(barData);
        chart.invalidate();
    }

//    public class DayViewModel extends ViewModel {
//        private MutableLiveData<List<Day>> days;
//
//        public DayViewModel(){}
//
//        public MutableLiveData<List<Day>> getDays() {
//            if(days == null) {
//                days = new MutableLiveData<>();
//                loadDays();
//            }
//            return days;
//        }
//
//        private void loadDays() {
//            List<Day> lastWeek = new ArrayList<>();
//            for(int i = 0; i < 7; i++) {
//                String date = DateHelper.getPreviousDayDateString(i);
//
//                Day currentDay = dayDatabase.dayDao().getDayById(date);
//                lastWeek.add(currentDay);
//            }
//            days.postValue(lastWeek);
//        }
//    }

}
