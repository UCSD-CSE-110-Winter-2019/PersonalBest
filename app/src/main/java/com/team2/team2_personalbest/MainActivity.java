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


        // Build DB
        final String DATABASE_NAME = "days_db";
        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();

        // Always insert values  into DB from separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Initializes a new Day row like this !
                /*
                Day dayTest =new Day("MondayTest", 355053, 50503);
                dayDatabase.dayDao().insertSingleDay (dayTest);
                */

                loggerForTesting();
            }
        }) .start();


    }

    /*
     Helper method to print DB values and test in LOG
     */
    private void loggerForTesting(){

        Log.d("change-string", "X\n\nInitial Values\n\n");

        String toLog  = dayToString("Monday");
        Log.d("DB VALUES", toLog);

        toLog  = dayToString("Tuesday");
        Log.d("DB VALUES", toLog);

        toLog  = dayToString("Wednesday");
        Log.d("DB VALUES", toLog);

        Log.d("change-string", "Now\n\nWe change the value of Tuesday\n\n");

        Day day2 = new Day("Tuesday", 0, 76);
        dayDatabase.dayDao().updateDay(day2);

        toLog  = dayToString("Monday");
        Log.d("DB VALUES", toLog);

        toLog  = dayToString("Tuesday");
        Log.d("DB VALUES", toLog);

        toLog  = dayToString("Wednesday");
        Log.d("DB VALUES", toLog);

    }


    /*
    Private Helper Method that converts a day entry into readable LOGGER output
     */
    private String dayToString(String testId){
        Day outputDay = dayDatabase.dayDao().getDayById(testId);
        return (" \n\n" +
                "\n\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n\n" +
                "Its "+outputDay.getDayId()+"\nYou have walked \n" +outputDay.getStepsTracked()+
                " tracked steps and\n"+outputDay.getStepsUntracked()+" untracked steps today!");
    }


}
