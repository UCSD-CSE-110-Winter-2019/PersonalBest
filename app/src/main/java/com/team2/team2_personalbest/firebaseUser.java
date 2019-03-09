package com.team2.team2_personalbest;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class firebaseUser extends IUser {

    private DatabaseReference firebaseDatabaseRef;
    private DayDatabase dayDatabase;
    final String DATABASE_NAME = "days_db";
    private String USER_NAME = "dev";


    public firebaseUser(Context activityContext){
        dayDatabase = Room.databaseBuilder(activityContext,
                DayDatabase.class, DATABASE_NAME)
                .build();
    }


    private boolean setFriendList(){

        return true;
    }



    boolean addFriend(String email){

        return true;
    }

    boolean removeFriend(String email){

        return true;
    }

    List<Pair<Float, Float>> getWalks(String email){

        List<Pair<Float, Float>> walkList = new ArrayList<Pair<Float, Float>>(30);
        return walkList;
    }

    List<Friend> getFriendlist(){

        return this.friendlist;
    }




    public void FirebaseSync() {

        // Write a message to the database
        Log.d("FIREBASE_INIT", "Writing to firebase");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseRef = firebaseDatabase.getReference();
        firebaseDatabaseRef.child("messages").child("TestMssg2").setValue("ValueTest2");

        //get Day values
        for (int i = 0; i < 30; i++) {
            String date = DateHelper.dayDateToString(DateHelper.previousDay(i));

            Day currentDay = dayDatabase.dayDao().getDayById(date);
            //String dayId = currentDay.getDayId();


            //if day exists in local db
            if(currentDay != null) {
                String dayId = currentDay.getDayId();

                int dayStepsTracked = currentDay.getStepsTracked();
                int dayStepsUntracked = currentDay.getStepsUntracked();


                Log.d("FIREBASE_DAY_VAL",
                        "DayID: " + dayId+ "\n" +
                                "Tracked Steps: " + dayStepsTracked+ "\n" +
                                "Untracked Steps: " + dayStepsUntracked+ "\n");

                firebaseDatabaseRef.child(USER_NAME+"/DayData/"+dayId+"/Tracked").setValue(dayStepsTracked);
                firebaseDatabaseRef.child(USER_NAME+"/DayData/"+dayId+"/Untracked").setValue(dayStepsUntracked);

            }
            else{
                //create day in local db if doesn't exist and update with firebase values

                DatabaseReference newRefTracked = firebaseDatabaseRef.child(USER_NAME+"/DayData/"+date+"/Tracked");

                Long tracked_firebase;
                Long untracked_firebase;

                // Read from the database
                newRefTracked.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Long value = dataSnapshot.getValue(Long.class);
                        //tracked_firebase = value;
                        Log.d("FIREBASE VAL: TR", "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("FIREBASE VAL: TR", "Failed to read value.", error.toException());
                    }
                });

                DatabaseReference newRefUntracked = firebaseDatabaseRef.child(USER_NAME+"/DayData/"+date+"/Untracked");

                // Read from the database
                newRefUntracked.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Long value = dataSnapshot.getValue(Long.class);
                        Log.d("FIREBASE VAL UN: ", "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("FIREBASE VAL: UN", "Failed to read value.", error.toException());
                    }
                });


                //hardcoded for now
                int firebaseUntracked = -1;
                int firebaseTracked = -10;
                Day newDay = new Day(date, firebaseTracked, firebaseUntracked );
                dayDatabase.dayDao().insertSingleDay(newDay);

                //try again
                i--;
            }

        }
    }
}
