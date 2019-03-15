package com.team2.team2_personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.firebase.FirebaseApp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.team2.team2_personalbest.HomePage.isNumeric;

public class FriendGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_graph);

        //int friend_ID_string = getIntent().getInt("friend_ID");
        Bundle bundle = getIntent().getExtras();
        String userName;
        int friend_id;
        try {
            //userName = bundle.getString("friend'sName");
            friend_id = bundle.getInt("friend_id");
        } catch (NullPointerException e) {
            friend_id = 0;
        }

        FirebaseApp.initializeApp(this);

        new FillEntriesTask().execute(name);
    }



    private class FillEntriesTask extends AsyncTask<String, Void, List<BarEntry>> {

        Context mContext;

        public FillEntriesTask() {
        }
        @Override
        protected List<BarEntry> doInBackground(String... friends) {
            FirestoreUser user = new FirestoreUser("Shardul", "sssaiya@ucsd.edu");
            String friend = friends[0];

            List<Pair<Integer, Integer>> walks = user.getWalks(UserUtilities.emailToUniqueId(friend));
        List<Pair<Integer, Integer>> walks;

        public FillEntriesTask(List<Pair<Integer, Integer>> walks) {
            this.walks = walks;
        }
        @Override
        protected List<BarEntry> doInBackground(String... friends) {
            //String friend = friends[0];

            List<BarEntry> entries = new ArrayList<>();
            for(int i = 0; i < 28; i++) {
                Pair<Integer, Integer> day = walks.get(i);
                if(day != null) {
                    Integer trackedSteps = day.first;
                    Integer untrackedSteps = day.second - trackedSteps;
                    entries.add(new BarEntry(28 - i, new float[]{untrackedSteps, trackedSteps}));
                } else {
                    entries.add(new BarEntry(28 - i, new float[]{0, 0}));
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
        chart.setScaleEnabled(false);

        final ArrayList<String> xLabel = new ArrayList<>();
        String[] days = DateHelper.getLastSevenWeekDays(DateHelper.getDayOfWeek());

        xLabel.add(DateHelper.dayDateToString(DateHelper.previousDay(27)));
        for(int i=1; i < 28; i++) {
            xLabel.add("");
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceMin(.5f);
        xAxis.setSpaceMax(.5f);
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
        lineEntries.add(new Entry(28, goal));

        LineDataSet set = new LineDataSet(lineEntries, "Line Dataset");
        set.setLineWidth(2.5f);
        set.setCircleRadius(1f);

        data.addDataSet(set);

        return data;

    }
}
