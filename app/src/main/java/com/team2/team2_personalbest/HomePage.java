package com.team2.team2_personalbest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team2.team2_personalbest.fitness.FitnessService;
import com.team2.team2_personalbest.fitness.FitnessServiceFactory;
import com.team2.team2_personalbest.fitness.GoogleFitAdapter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;



public class HomePage extends AppCompatActivity {
    //TODO Variables
    /* Constants */
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    String fitnessServiceKey = "GOOGLE_FIT";
    private final int FIVE_HUNDRED_INCREMENT = 500;
    private final int INITIAL_GOAL = 5000;
    private final int UPDATE_LENGTH = 5000; //update step count every 5 seconds
    private final double TO_GET_AVERAGE_STRIDE = 0.413;
    private static final String TAG = "HomePage";

    private long currentTimeMilli = System.currentTimeMillis();
    private DayDatabase dayDatabase;
    /* Textviews */
    private TextView textViewStepCount;
    private TextView textViewDistance;
    private TextView textViewPlannedSteps, textViewPlannedDistance;
    private TextView TextViewStepsLeft;
    /* Buttons */
    private Button toggle_walk;
    private Button add500StepsButton;
    private Button submitButton;

    private FitnessService fitnessService;
    public static boolean planned_walk = false;
    final Handler handler = new Handler();
    private static double userHeight;
    public double averageStrideLength;

    // This is used to be able to track how many steps were added manually via HomePage
    // by the user
    private int manualStepsAddedTotal;

    private int goal;
    private int stepsLeft;

    /* Vars for planned walk data storage */
    private int psBaseline = 0; //daily steps at time planned steps turned on
    private int psDailyTotal = 0; //total planned steps before current planned walk
    private int psStepsThisWalk = 0; //holder for planned steps during current walk

    //TODO OnCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        final String DATABASE_NAME = "days_db";
        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();

        setTestValues();

        //Getting XML elements
        textViewStepCount = findViewById(R.id.step_taken); //daily step counter
        textViewDistance = findViewById(R.id.miles_taken); //daily mile counter
        textViewPlannedSteps = findViewById(R.id.planned_steps); //planned step counter
        textViewPlannedDistance = findViewById(R.id.planned_distance); //planned mile counter
        toggle_walk = findViewById(R.id.toggle_walk); //planned walk button

        averageStrideLength = calculateAveStrideLength(userHeight);

        //set button color to green by default
        toggle_walk.setBackgroundColor(Color.GREEN);
        //fixme USE THIS ENCOURAGEMENT
        //sendEncouragement();
        // Set onClick listeners for manually setting time and step increment (+500 steps)
        add500StepsButton();
        submitButton();
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomePage homePage) {
                return new GoogleFitAdapter(homePage);
            }
        });

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
        toggleWalk();
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomePage homePage) {
                return new GoogleFitAdapter(homePage);
            }
        });

        //update step every 5 seconds
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fitnessService.updateStepCount();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0,UPDATE_LENGTH);
        fitnessService.setup();

        // TODO Set up the initial goal
        setInitialGOal();
    }

    //TODO On Resume
    // Update the goal when coming back to homePage
    @Override
    public void onResume() {
        super.onResume();
        TextViewStepsLeft = (TextView) findViewById(R.id.steps_left);
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        String newGoal = sharedPreferences.getString("newgoal", "");
        if(isNumeric(newGoal)){
            this.goal = Integer.parseInt(newGoal);
            this.stepsLeft = this.goal;
            TextViewStepsLeft.setText(newGoal);
        }
    }

    /**
     * Author: josephl310
     *
     * This should end planned walks when the app is quit
     */
    // TODO On Destroy
    @Override
    protected void onDestroy(){
        planned_walk = false;
        super.onDestroy();
    }

    public void setStepCount(long stepCount){
        String stepCountDisplay = String.format(Locale.US, "%d %s", stepCount, getString(R.string.steps_taken));
        double totalDistanceInInch = stepCount * averageStrideLength;
        String milesDisplay = String.format(Locale.US, "%.1g %s", convertInchToMile(totalDistanceInInch),
                                            getString(R.string.miles_taken));

        textViewStepCount.setText(stepCountDisplay);
        textViewDistance.setText(milesDisplay);

        //total daily steps should always be >= to planned
        if (stepCount < psDailyTotal){
            psDailyTotal = 0;
        }

        psStepsThisWalk = (int)stepCount - psBaseline; //Current walk steps
        setPsBaseline((int)stepCount);
        long plannedSteps = psStepsThisWalk + psDailyTotal; //Add current walk steps to total daily steps

        String plannedStepCountDisplay = String.format(Locale.US, "%d %s", plannedSteps,
                getString(R.string.planned_steps));
        double totalPlannedDistanceInInch = plannedSteps * averageStrideLength;
        String plannedMilesDisplay = String.format(Locale.US, "%.1g %s", convertInchToMile(totalPlannedDistanceInInch),
                getString(R.string.planned_distance));


        new Thread(new Runnable() {
            @Override
            public void run() {

                //Initializes a new Day row like this !
                String date = DateHelper.getPreviousDayDateString(0);
                Log.d("HomePage", date);
                Day currentDay = dayDatabase.dayDao().getDayById(date);
                if(currentDay == null) {
                    currentDay = new Day(date, psDailyTotal, (int)stepCount);
                    dayDatabase.dayDao().insertSingleDay(currentDay);
                } else {
                    currentDay.setStepsTracked(psDailyTotal);
                    currentDay.setStepsUntracked((int)stepCount);
                    dayDatabase.dayDao().updateDay(currentDay);
                }

//                loggerForTesting();
                sendSubNotification();

            }
        }) .start();

        textViewPlannedSteps.setText(plannedStepCountDisplay);
        textViewPlannedDistance.setText(plannedMilesDisplay);
        //Update steps left
        this.stepsLeft = this.goal - (int)stepCount;
        //For when reached the goal
        if (this.stepsLeft <= 0) {
            this.stepsLeft = 0;
            launchEncouragementPopup();
            sendNotification();
        }
        String stepsLeft = String.valueOf(this.stepsLeft);
        TextViewStepsLeft.setText(stepsLeft);
    }

    //TODO Buttons
    // Launch the set new goal popup
    public void set_goal(View view) {
        Intent intent = new Intent(this, SetNewGoal.class);
        startActivity(intent);
    }

    public void goToGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }
    public void add500StepsButton(){
        add500StepsButton = (Button) findViewById(R.id.add500Button);

        add500StepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get increment field int value
                int totalNewSteps = psBaseline + FIVE_HUNDRED_INCREMENT;
                manualStepsAddedTotal += FIVE_HUNDRED_INCREMENT;
                setStepCount(totalNewSteps);
            }
        });
    }

    public void submitButton(){
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView inputTimeView = (TextView) findViewById(R.id.currentTimeField);
                String input = inputTimeView.getText().toString();
                if (!input.equals("")) {
                    currentTimeMilli = Long.parseLong(input);
                }
                inputTimeView.setText("");
            }
        });
    }
    public void toggleWalk(){
        toggle_walk.setOnClickListener(new View.OnClickListener() {
            /**
             * author josephl310
             *
             * Implements toggle functionality of button: switches between planned and unplanned
             * walks
             */
            @Override
            public void onClick(View v) {
                if (planned_walk){ //User was on planned walk, wants to end it

                    psDailyTotal += psStepsThisWalk; //update running total of daily planned steps

                    psStepsThisWalk = 0; //reset current walk step counter
                    planned_walk = false; //not on a planned walk anymore

                    /* make planned steps text invisible */
                    textViewPlannedSteps.setVisibility(View.INVISIBLE);
                    textViewPlannedDistance.setVisibility(View.INVISIBLE);

                    /* reset button */
                    toggle_walk.setText("Start Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.GREEN);

                } else { //Turn on planned walk


                    fitnessService.updateStepCount(); //update with newest information
                    planned_walk = true; //start planned walk

                    /* make planned steps text visible */
                    textViewPlannedSteps.setVisibility(View.VISIBLE);
                    textViewPlannedDistance.setVisibility(View.VISIBLE);

                    /* change button */
                    toggle_walk.setText("End Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    //TODO Helper Functions

    public void setPsBaseline(int stepCount){
        psBaseline = stepCount;
    }

    public double calculateAveStrideLength(double height) {
        return height * TO_GET_AVERAGE_STRIDE;
    }

    public double convertInchToMile(double inch) {
        return inch * 1.57828e-5;
    }

    // Check if String is number
    public static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    // To set the initial goal
    public void setInitialGOal(){
        this.goal = INITIAL_GOAL;
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newgoal", "5000");
        editor.apply();
    }

    /*
     Helper method to print DB values and test in LOG
     */
//    private void loggerForTesting(){
//
//        Log.d("change-string", "X\n\nInitial Values\n\n");
//
//        String toLog  = dayToString("Monday");
//        Log.d("DB VALUES", toLog);
//
//        toLog  = dayToString("Tuesday");
//        Log.d("DB VALUES", toLog);
//
//        toLog  = dayToString("Wednesday");
//        Log.d("DB VALUES", toLog);
//
//        Log.d("change-string", "Now\n\nWe change the value of Tuesday\n\n");
//
//        toLog  = dayToString("Monday");
//        Log.d("DB VALUES", toLog);
//
//        toLog  = dayToString("Tuesday");
//        Log.d("DB VALUES", toLog);
//
//        toLog  = dayToString("Wednesday");
//        Log.d("DB VALUES", toLog);
//
//    }
    private void setTestValues() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i < 7; i++) {
                    Log.d("HomePage", "test adding day");
                    Day currentDay = dayDatabase.dayDao().getDayById(DateHelper.getPreviousDayDateString(i));
                    if(currentDay == null) {
                        currentDay = new Day(DateHelper.getPreviousDayDateString(i), i * 30, i * 76);
                        dayDatabase.dayDao().insertSingleDay(currentDay);
                    }
                }
            }
        }).start();
    }

    /*
    Private Helper Method that converts a day entry into readable LOGGER output
     */
//    private String dayToString(String testId){
//        Day outputDay = dayDatabase.dayDao().getDayById(testId);
//        return (" \n\n" +
//                "\n\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n\n" +
//                "Its "+outputDay.getDayId()+"\nYou have walked \n" +outputDay.getStepsTracked()+
//                " tracked steps and\n"+outputDay.getStepsUntracked()+" untracked steps today!");
//    }

    //Launch the encouragement popup
    public void launchEncouragementPopup() {
        Intent intent = new Intent(this, GoalAccomplished.class);
        startActivity(intent);
    }

    // For the popup
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "what";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    // Popup for when goal reached
    private void sendNotification(){
        Intent intent = new Intent(this, GoalAccomplished.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Congratulations!")
                //.setContentText("Do you want to set a new step goal?")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Do you want to set a new step goal?"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1000, mBuilder.build());
    }

    // Popup for encouragement
    private void sendEncouragement(int increase){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                // fixme CHANGE THIS STEPs
                .setContentTitle(increase+ " steps more than yesterday! Wow!" )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1000, mBuilder.build());
    }

    public TextView getTextViewStepCount() {
        return this.textViewStepCount;
    }

    public int getManualStepsAddedTotal() {
        return this.manualStepsAddedTotal;
    }

    public static void setUserHeight(double height) {
        userHeight = height;
    }

    /*
    Sends Notification when we have reached 500 above yesterdays total steps!
 */
    private void sendSubNotification(){
        String currentDayID = DateHelper.getPreviousDayDateString(0);
        Day currentDay = dayDatabase.dayDao().getDayById(currentDayID);
        String yesterdayID = DateHelper.getPreviousDayDateString(1);
        Day yesterday = dayDatabase.dayDao().getDayById(yesterdayID);

        int yesterdayTotal = yesterday.getStepsTracked()+yesterday.getStepsUntracked();
        int currentStepsTotal = currentDay.getStepsTracked()+currentDay.getStepsUntracked();

        //if we have crossed yesterdays threshold
        if (currentStepsTotal > yesterdayTotal){


            //find the difference
            int difference = currentStepsTotal-yesterdayTotal;

            //if difference is 500 or 1000 or 1500 or 2000... we want to show notification
            int FIVE_HUNDRED_INCREMENT = 500;
            int fiveHundredIncrements = difference/FIVE_HUNDRED_INCREMENT;
            int remainder = difference%FIVE_HUNDRED_INCREMENT;

            Log.d("YESTERDAY VS TODAY", "XXXX\nCURRENT: "+currentStepsTotal+"\nYEST: "+yesterdayTotal
                    +"\nDIFF: "+difference+"\nREM: "+remainder);
            if(remainder <= 6){

                Log.d("YESTERDAY NOTIFICATION!", "NOTIFICATION WITH INCREASE OF "
                        +FIVE_HUNDRED_INCREMENT*fiveHundredIncrements);
                sendEncouragement(fiveHundredIncrements*FIVE_HUNDRED_INCREMENT);

            }
        }
    }
}