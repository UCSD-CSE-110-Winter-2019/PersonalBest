package com.team2.team2_personalbest;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatRoomActivity;
import com.team2.team2_personalbest.fitness.FitnessService;
import com.team2.team2_personalbest.fitness.FitnessServiceFactory;
import com.team2.team2_personalbest.fitness.GoogleFitAdapter;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main activity of the app
 */
public class HomePage extends AppCompatActivity {
    //TODO Variables
    /* Constants */
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    String fitnessServiceKey = "GOOGLE_FIT";
    private final int FIVE_HUNDRED_INCREMENT = 500;
    private final int INITIAL_GOAL = 5000;
    private final int UPDATE_LENGTH = 15000; //update step count every 5 seconds
    private static final String TAG = "HomePage";

    private long currentTimeMilli = System.currentTimeMillis();
    private DayDatabase dayDatabase;
    private WalkDatabase walkDatabase;
    /* Textviews */
    private TextView textViewStepCount;
    private TextView textViewDistance;
    private TextView textViewPlannedSteps, textViewPlannedDistance;
    private TextView TextViewStepsLeft;
    private TextView textViewPlannedStepsTaken;
    private TextView textViewTimeElapsed;
    private TextView textViewPlannedTitle;
    /* Buttons */
    private Button toggle_walk;
    private Button add500StepsButton;
    private Button submitButton;

    private FitnessService fitnessService;
    public static boolean planned_walk = false;
    final Handler handler = new Handler();
    private static double userHeight = 60;
    public double averageStrideLength;

    // This is used to be able to track how many steps were added manually via HomePage
    // by the user
    private int manualStepsAddedTotal;

    private int goal;
    private int stepsLeft;

    //SharePreferences
    SharedPref firstTime;
    SharedPref goalReached;
    SharedPref userName;

    /* Vars for planned walk data storage */
    private int psBaseline = 0; //daily steps at time planned steps turned on
    private int psDailyTotal = 0; //total planned steps before current planned walk
    private int psStepsThisWalk = 0; //holder for planned steps during current walk

    /*var for stats*/
    public double elapsedTime;
    public double start;


    /* for testing purposes */
    public boolean isTesting = false;

    /*Firebase User*/
    //probably wont work because FirestoreUser must be called from a separate thread
    private FirestoreUser user;
    Context context;

    // TODO Possible bug
    @Override
    protected void onNewIntent(Intent intent) {
        notificationToChat();
    }

    //TODO OnCreate
    protected void onCreate(Bundle savedInstanceState) {
        notificationToChat();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        context = this;

        if (getIntent().getExtras() != null) {
            isTesting = getIntent().getExtras().getBoolean("TESTING");
        }

        final String DATABASE_NAME = "days_db";
        dayDatabase = Room.databaseBuilder(getApplicationContext(),
                DayDatabase.class, DATABASE_NAME)
                .build();


        final String DATABASE_NAME1 = "walk_db";
        walkDatabase = Room.databaseBuilder(getApplicationContext(),
                WalkDatabase.class, DATABASE_NAME1)
                .build();

        setTestValues();
        SharedPref PS = new SharedPref(this);
        this.psDailyTotal = PS.getInt("psDailyTotal");
        this.psStepsThisWalk = PS.getInt("psStepsThisWalk");
        this.psBaseline = PS.getInt("psBaseline");


        //Getting XML elements
        textViewStepCount = findViewById(R.id.step_taken); //daily step counter
        textViewDistance = findViewById(R.id.miles_taken); //daily mile counter
        textViewPlannedSteps = findViewById(R.id.planned_steps); //planned step counter
        textViewPlannedDistance = findViewById(R.id.planned_distance); //planned mile counter
        textViewPlannedStepsTaken = findViewById(R.id.planned_stepsTaken);
        textViewTimeElapsed = findViewById(R.id.timeElapsed);
        textViewPlannedTitle = findViewById(R.id.planned_stats_title);
        toggle_walk = findViewById(R.id.toggle_walk); //planned walk button

        averageStrideLength = StatisticsUtilities.calculateAveStrideLength(userHeight);

        //set button color to green by default
        toggle_walk.setBackgroundColor(Color.GREEN);
        //fixme USE THIS ENCOURAGEMENT
        //sendEncouragement();
        // Set onClick listeners for manually setting time and step increment (+500 steps)
        add500StepsButton();
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomePage homePage) {
                return new GoogleFitAdapter(homePage);
            }
        });

        // TODO Check if it's the first time running the appp


        firstTime = new SharedPref(this);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        boolean hasRun = firstTime.getBool("init");
        if (!hasRun) {
            if (!isTesting) {
                goToSetupActivity();
                fitnessService.setupInit();
                firstTime.setBool("init", true);
            }
            setInitialGoal();
        } else {
            fitnessService.setup();
        }
        toggleWalk();
//        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
//            @Override
//            public FitnessService create(HomePage homePage) {
//                return new GoogleFitAdapter(homePage);
//            }
//        });


        //update step every 5 seconds
        goalReached = new SharedPref(this);
        boolean goal_reached = goalReached.getBool("goalReached");
        if (goal_reached) {
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
            timer.schedule(doAsynchronousTask, 0, UPDATE_LENGTH);
            fitnessService.setup();
        }


        //FUNCTION TO GET USERNAME AND ADD TO SHARED PREFERENCES
//        setUserName();
    }

    private void notificationToChat() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Object value;
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("from")) {
                    Log.d("launchedFromNoti", key);
                    startActivity(new Intent(this, ChatRoomActivity.class));
                    Log.d("NotificationTag", "........");
                }
            }

        }
    }


    //TODO On Resume
    // Update the goal when coming back to homePage
    @Override
    public void onResume() {
        super.onResume();
        TextViewStepsLeft = (TextView) findViewById(R.id.steps_left);
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        String newGoal = sharedPreferences.getString("newgoal", "");
        if (isNumeric(newGoal)) {
            this.goal = Integer.parseInt(newGoal);
            this.stepsLeft = this.goal;
            TextViewStepsLeft.setText(newGoal);
        }
        goalReached = new SharedPref(this);
        goalReached.setBool("goalReached", true);

        //TODO Store the height
        SharedPreferences heightPref = getSharedPreferences("height", MODE_PRIVATE);
        String height = heightPref.getString("height", "");
        if (isNumeric(height)) {
            this.userHeight = Double.parseDouble(height);
        }
    }

    /**
     * Author: josephl310
     * <p>
     * This should end planned walks when the app is quit
     */
    // TODO On Destroy
    @Override
    protected void onDestroy() {
        planned_walk = false;
        SharedPref PS = new SharedPref(this);
        PS.setInt("psDailyTotal", this.psDailyTotal);
        PS.setInt("psBaseline", this.psBaseline);
        PS.setInt("psStepsThisWalk", this.psStepsThisWalk);

        String name = PS.getStr("user name");
        String email = PS.getStr("userID");

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Day> days = dayDatabase.dayDao().getAllDays();
                List<Day> last30Days;
                if (days.size() > 30) {
                    last30Days = days.subList(0, 30);
                } else {
                    last30Days = days;
                }
                FirestoreUser user = new FirestoreUser(name, email);
                user.setWalks(last30Days);
            }
        }).start();
        super.onDestroy();
    }

    /**
     * @param stepCount total steps from googleFit
     */
    public void setStepCount(long stepCount) {
        Log.d("SET_STEP", "setStepCount: called");
        String stepCountDisplay = String.format(Locale.US, "%d %s", stepCount, getString(R.string.steps_taken));
        double totalDistanceInInch = stepCount * averageStrideLength;
        String milesDisplay = String.format(Locale.US, "%.1f %s", StatisticsUtilities.convertInchToMile(totalDistanceInInch),
                getString(R.string.miles_taken));

        textViewStepCount.setText(stepCountDisplay);
        textViewDistance.setText(milesDisplay);

        //total daily steps should always be >= to planned
        if (stepCount < psDailyTotal) {
            psDailyTotal = 0;
        }

        psStepsThisWalk = (int) stepCount - psBaseline; //Current walk steps
        setPsBaseline((int) stepCount);
        int plannedSteps = psStepsThisWalk + psDailyTotal; //Add current walk steps to total daily steps

        String plannedStepCountDisplay = String.format(Locale.US, "%d %s", plannedSteps,
                getString(R.string.planned_steps));
        double totalPlannedDistanceInInch = plannedSteps * averageStrideLength;
        String plannedMilesDisplay = String.format(Locale.US, "%.1f %s", StatisticsUtilities.convertInchToMile(totalPlannedDistanceInInch),
                getString(R.string.planned_distance));


        SharedPreferences userStore = this.getSharedPreferences("appname_prefs", 0);
        String name = userStore.getString("user name", "");
        String email = userStore.getString("userID", "");


        //Update steps left
        this.stepsLeft = this.goal - (int) stepCount;
        //For when reached the goal
        if (this.stepsLeft <= 0) {
            this.stepsLeft = 0;
            goalReached = new SharedPref(this);
            goalReached.setBool("goalReached", true);
            launchEncouragementPopup();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FirestoreUser user = new FirestoreUser(name, email);
                    if (user.getFriendList().size() == 0) {
                        EncouragementNotification.sendNotification(context);
                    }
                }
            }).start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDatabase((int) stepCount, plannedSteps);
            }
        }).start();
        String stepsLeft = String.valueOf(this.stepsLeft);
        TextViewStepsLeft.setText(stepsLeft);

        //update planned walk stats
        if (planned_walk) {
            updateStats();
        }

    }

    /*
    TODO
    GET USERNAME OR ID FROM GOOGLE ACCOUNT ON SIGN IN
     */

    private void setUserName() {
        //here

        userName = new SharedPref(this);
        userName.setStr("username", "DUMMY");

    }

    public void updateDatabase(int stepCount, int plannedSteps) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        Log.d("CREATED_HOMEPAGE", "run: ran updateDatabase");
        //Initializes a new Day row like this !


        String date = DateHelper.dayDateToString(DateHelper.previousDay(0));
        Log.d("HomePage", date);
        Day currentDay = dayDatabase.dayDao().getDayById(date);
        if (currentDay == null) {
            currentDay = new Day(date, plannedSteps, stepCount);
            dayDatabase.dayDao().insertSingleDay(currentDay);
        } else {
            currentDay.setStepsTracked(psDailyTotal);
            currentDay.setStepsUntracked(stepCount);
            dayDatabase.dayDao().updateDay(currentDay);
        }


//                loggerForTesting();
        SharedPreferences preferences = HomePage.this.getSharedPreferences("appname_prefs", 0);
        String email = preferences.getString("userID", "aopanis@gmail.com");
        String name = preferences.getString("user name", "panis");
        FirestoreUser user = new FirestoreUser(name, email);
        if (user.getFriendList().size() == 0) {
            EncouragementNotification.sendSubNotification(HomePage.this, dayDatabase);
        }
    }
//        }).start();

    //TODO Buttons

    /**
     * Launch setUpActivity
     */
    public void goToSetupActivity(){
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

//    public void manageFriendsButtonOnClick(View view) {
//        Button manageFriendsButton = (Button) findViewById(R.id.manageFriendsButton);
//
//        manageFriendsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchManageFriendsActivity();
//            }
//        });
//    }

    public void launchManageFriendsActivity(View view) {
        Intent intent = new Intent(this, ManageFriendsActivity.class);
        startActivity(intent);
    }

    /**
     * Launch SetNewGoal Activity
     * @param view
     */
    public void set_goal(View view) {
        Intent intent = new Intent(this, SetNewGoal.class);
        startActivity(intent);
    }

    /**
     * Launch GraphActivity
     * @param view
     */
    public void goToGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("TESTING", isTesting);
        startActivity(intent);
    }

    /**
     * Button to add 500 steps to total steps
     */
    public void add500StepsButton() {
        add500StepsButton = (Button) findViewById(R.id.add500Button);

        add500StepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get increment field int value
//                int totalNewSteps = psBaseline + FIVE_HUNDRED_INCREMENT;
//                if (planned_walk) psDailyTotal += FIVE_HUNDRED_INCREMENT;
//                manualStepsAddedTotal += FIVE_HUNDRED_INCREMENT;
                int totalNewSteps = psBaseline + 5;
                if (planned_walk) psDailyTotal += 5;
                manualStepsAddedTotal += 5;
                setStepCount(totalNewSteps);
            }
        });
    }
  
      
    /**
     * author josephl310
     *
     * Implements toggle functionality of button: switches between planned and unplanned
     * walks
     */
    public void toggleWalk() {
        toggle_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planned_walk){ //user was on planned walk, wants to end it

                    psDailyTotal += psStepsThisWalk; //update running total of daily planned steps

                    Log.d("insertVal", "STEPS: "+psDailyTotal);

                    addWalk(psDailyTotal, elapsedTime);
                    psStepsThisWalk = 0; //reset current walk step counter
                    planned_walk = false; //not on a planned walk anymore

                    /* make planned steps text invisible */
                    textViewPlannedSteps.setText("");
                    textViewPlannedDistance.setText("");
                    textViewPlannedStepsTaken.setText("");
                    textViewTimeElapsed.setText("");
                    textViewPlannedTitle.setText("");


                    /* reset button */
                    toggle_walk.setText("Start Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.GREEN);


                } else { //Turn on planned walk
                    //start measuring start time
                    start=System.currentTimeMillis();

                    fitnessService.updateStepCount(); //update with newest information
                    planned_walk = true; //start planned walk

                    updateStats();

                    /* change button */
                    toggle_walk.setText("End Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    /**
     * Log steps and time
     * @param steps
     * @param time
     */
    public void addWalk(int steps, double time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlannedWalk walk = new PlannedWalk(steps, time);

                Log.d("Database Entry", "run: inserting " + steps + " " + time);
                walkDatabase.walkDao().insertSingleWalk(walk);

            }
        }).start();

    }

    //TODO Helper Functions

    /**
     * Set the psBaseline to current stepCount
     * @param stepCount
     */

    public void setPsBaseline(int stepCount){
        this.psBaseline = stepCount;
    }

    /**
     * Check if a String is a number
     */
    public static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * set initial goal to 5000
     */
    // To set the initial goal
    public void setInitialGoal(){
        this.goal = INITIAL_GOAL;
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newgoal", "5000");
        editor.apply();
    }


    /**
     * log test values
     */
    private void setTestValues() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i < 31; i++) {
                    Log.d("HomePage", "test adding day");
                    Day currentDay = dayDatabase.dayDao().getDayById(DateHelper.dayDateToString(DateHelper.previousDay(i)));
                    if(currentDay == null) {
                        currentDay = new Day(DateHelper.dayDateToString(DateHelper.previousDay(i)), i * 30, i * 76);
                        dayDatabase.dayDao().insertSingleDay(currentDay);
                    }
                }
            }
        }).start();
    }


    /**
     * Launch the encouragement popup
     */
    public void launchEncouragementPopup() {
        Intent intent = new Intent(this, GoalAccomplished.class);
        startActivity(intent);
    }




    /**
     * Update statistics of planned walk
     */
    private void updateStats() {
        double finish = System.currentTimeMillis();
        double timeElapsed = finish - start;
        elapsedTime = timeElapsed / 1000 / 3600;
        double plannedWalkDistance = StatisticsUtilities.convertInchToMile(psDailyTotal * StatisticsUtilities.calculateAveStrideLength(userHeight));
        double averagePlannedWalkSpeed = plannedWalkDistance / elapsedTime;
        textViewPlannedSteps = findViewById(R.id.planned_steps);
        textViewPlannedDistance = findViewById(R.id.planned_distance);
        textViewPlannedTitle.setText("Planned Walk Stats");
        textViewPlannedSteps.setText(String.format("%.2f", averagePlannedWalkSpeed) + "  MPH");
        textViewPlannedDistance.setText(String.format("%.2f", plannedWalkDistance) + " miles planned walking");
        textViewTimeElapsed.setText(String.format("%.0f", elapsedTime * 60) + " minutes");
        textViewPlannedStepsTaken.setText(Integer.toString(psDailyTotal) + " steps taken");
    }

    public TextView getTextViewStepCount() {
        return this.textViewStepCount;
    }

    public int getManualStepsAddedTotal() {
        return this.manualStepsAddedTotal;
    }

}
