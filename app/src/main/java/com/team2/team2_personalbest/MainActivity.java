package com.team2.team2_personalbest;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    private DayDatabase dayDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //runDataBase();

        final String DATABASE_NAME = "days_db";
        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();


        String toToast=("");
        new Thread(new Runnable() {
            @Override
            public void run() {


                //Initializes database
                //create a dummy day
                //Day dayTest =new Day("MondayTest2w323", 355053, 50503);
                //day.setDayId("MondayTest");
                //day.setStepsTracked(30);
                //day.setStepsUntracked(500);

                //add to db
                //dayDatabase.dayDao().insertSingleDay (dayTest);

                Day day2 = new Day("Tuesday", 400, 50);
                dayDatabase.dayDao().insertSingleDay(day2);


                String toToast  = testDataBase();
                Log.d("DB VALUES", toToast);

            }
        }) .start();


        Toast.makeText(this, toToast, Toast.LENGTH_LONG).show();

    }


    /*private void runDataBase(){
        final String DATABASE_NAME = "days_db";
        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();
    }*/

    private String testDataBase(){
        Day outputDay = dayDatabase.dayDao().getDayById("Tuesday");
        return ("Its "+outputDay.getDayId()+" and you have walked " +outputDay.getStepsTracked()+" today!");
    }


}
