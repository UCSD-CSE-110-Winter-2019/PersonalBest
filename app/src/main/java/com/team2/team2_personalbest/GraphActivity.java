package com.team2.team2_personalbest;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.team2.team2_personalbest.HomePage.isNumeric;

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
//            generateBarChart(entries);
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
                    int trackedSteps = currentDay.getStepsTracked();
                    int untrackedSteps = currentDay.getStepsUntracked() - trackedSteps;
                    entries.add(new BarEntry(6 - i, new float[]{untrackedSteps, untrackedSteps}));
                }
            }

            return entries;

        }
        @Override
        protected void onPostExecute(List<BarEntry> entries) {
            generateBarChart(entries);
        }
    }

    public void generateBarChart(List<BarEntry> entries) {
        CombinedChart chart = findViewById(R.id.chart);

        final ArrayList<String> xLabel = new ArrayList<>();
        String[] days = DateHelper.getLastSevenWeekDays(DateHelper.getDayOfWeek());

        for(String i : days) {
            xLabel.add(i);
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });

        BarDataSet barDataSet = new BarDataSet(entries, "Daily walking");
        int color1 = ContextCompat.getColor(this, R.color.colorAccent);
        int color2 = ContextCompat.getColor(this, R.color.colorPrimary);
        //int color3 = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        barDataSet.setColors(color1, color2);

        BarData barData = new BarData(barDataSet);


        CombinedData combinedData = new CombinedData();
        combinedData.setData((barData));
        combinedData.setData(generateLine());


        chart.setData(combinedData);
        chart.invalidate();
    }

    private LineData generateLine() {
        LineData data = new LineData();
        int goal = 5000;

        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        String newGoal = sharedPreferences.getString("newgoal", "");
        if(isNumeric(newGoal)) {
            goal = Integer.parseInt(newGoal);
        }

        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(0, goal));
        lineEntries.add(new Entry(6, goal));

        LineDataSet set = new LineDataSet(lineEntries, "Line Dataset");
        set.setLineWidth(2.5f);
        set.setCircleRadius(1f);

        data.addDataSet(set);

        return data;

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
