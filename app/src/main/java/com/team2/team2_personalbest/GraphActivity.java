package com.team2.team2_personalbest;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        BarChart chart = findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, new float[] {10, 20}));
        entries.add(new BarEntry(5, new float[] {10, 30}));

        BarDataSet barDataSet = new BarDataSet(entries, "Daily walking");
        int color1 = ContextCompat.getColor(this, R.color.colorAccent);
        int color2 = ContextCompat.getColor(this, R.color.colorPrimary);
        int color3 = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        barDataSet.setColors(color1, color2, color3);

        BarData barData = new BarData(barDataSet);

        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        chart.setData(barData);
        chart.invalidate();

    }
}
