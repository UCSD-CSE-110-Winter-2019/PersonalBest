package com.team2.team2_personalbest;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

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
import com.google.firebase.database.DatabaseReference;
import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatRoomActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.team2.team2_personalbest.HomePage.isNumeric;

/**
 * This activity displays the graph
 */

public class GraphActivityLong extends AppCompatActivity {

    private DayDatabase dayDatabase;
    private Button walkHist;
    private DatabaseReference firebaseDatabaseRef;
    private String userName = "dev";
    final String DATABASE_NAME = "days_db";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_long);


        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();

        new FillEntriesTask(this).execute(dayDatabase);


        //FirebaseApp.initializeApp(this);

//        Thread thread = new Thread(new Runnable(){
//            @Override
//            public void run(){
//                FirestoreUser user = new FirestoreUser("Shardul", "sssaiya@ucsd.edu");
//                List<Pair<Integer, Integer>> walks = getHistoryAsList();
//                user.setWalks(walks);
//            }
//        });
//        thread.start();

    }


    private class FillEntriesTask extends AsyncTask<DayDatabase, Void, List<BarEntry>> {

        Context mContext;

        public FillEntriesTask(Context context) {
            this.mContext = context;
        }
        @Override
        protected List<BarEntry> doInBackground(DayDatabase... databases) {
            DayDatabase database = databases[0];

            List<BarEntry> entries = new ArrayList<>();
            for(int i = 0; i < 28; i++) {
                String date = DateHelper.dayDateToString(DateHelper.previousDay(i));

                Day currentDay = database.dayDao().getDayById(date);

                if(currentDay != null) {
                    Log.d("GraphActivity", Integer.toString(i));
                    int trackedSteps = currentDay.getStepsTracked();
                    int untrackedSteps = currentDay.getStepsUntracked() - trackedSteps;
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
//    public void goToChat(View view){
//        //setContentView(R.layout.activity_friend_graph);
//        SharedPreferences sharedPreferences = getSharedPreferences("popup", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("openedFromGraph", true).apply();
//        Intent intent = new Intent(this, ChatRoomActivity.class);
//        startActivity(intent);
//        finish();
//    }

}

