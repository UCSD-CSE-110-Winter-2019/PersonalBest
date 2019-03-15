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

import java.util.ArrayList;
import java.util.List;

public class FirebaseUser{

    private DatabaseReference firebaseDatabaseRef;
    private DayDatabase dayDatabase;
    final String DATABASE_NAME = "days_db";
    private String USER_NAME = "dev";


    public FirebaseUser(Context activityContext){
        dayDatabase = Room.databaseBuilder(activityContext,
                DayDatabase.class, DATABASE_NAME)
                .build();
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }


    private boolean setFriendList(){

        return true;
    }

    IUser.User getAppUser(String email){
        return null;
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

    List<IUser.User> getFriendlist(){

        //return this.friendlist;
        Log.d("GET_FRIEND_LIST_INIT", "Getting friends for user: "+USER_NAME);
        DatabaseReference friendsRef = firebaseDatabaseRef.child(USER_NAME+"/Friends/");

        List<IUser.User> friendlist = new ArrayList<>();

        // Read from the database and add it to dayDataList
        friendsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friend : dataSnapshot.getChildren()) {
                    Log.d("EMAIL", friend.child("email").getValue().toString());
                    Log.d("name", friend.child("name").getValue().toString());
                    Log.d("isPending", friend.child("isPending").getValue().toString());
                    String email = friend.child("email").getValue().toString();
                    String name = friend.child("name").getValue().toString();
                    String isPending = friend.child("isPending").getValue().toString();

                    //IUser.user thisFriend = new IUser.user(name, email, isPending);

                    //friendlist.add(thisFriend);
                    //friendlist.add(thisFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE VAL: UN", "Failed to read value.", error.toException());
            }
        });

        return friendlist;
    }




    public void FirebaseSync() {

        // Write a message to the database
        Log.d("FIREBASE_SYNC_INIT", "firebase-sync initialized");


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


                Log.d("LOCAL_DAY_VAL",
                        "DayID: " + dayId+ "\n" +
                                "Tracked Steps: " + dayStepsTracked+ "\n" +
                                "Untracked Steps: " + dayStepsUntracked+ "\n");

                firebaseDatabaseRef.child(USER_NAME+"/DayData/"+dayId+"/Tracked").setValue(dayStepsTracked);
                firebaseDatabaseRef.child(USER_NAME+"/DayData/"+dayId+"/Untracked").setValue(dayStepsUntracked);

            }
            else{

                //Get dayData for given date from firebase
                DayData tempDay = getStepsForDate(date);

                Float firebaseUntracked = tempDay.unplanned;
                Float firebaseTracked = tempDay.planned;


                Day newDay = new Day(date, firebaseTracked.intValue(), firebaseUntracked.intValue() );
                dayDatabase.dayDao().insertSingleDay(newDay);

                //try again
                i--;
            }

        }
    }


    /* Helper method to get DayData for given date string */
    private DayData getStepsForDate(String date){

        List<DayData> dayDataList = new ArrayList<>();

        DatabaseReference newRefUntracked = firebaseDatabaseRef.child(USER_NAME+"/DayData/"+date);
        float a,b;
        a=0;b=0;

        DayData dayToRet=new DayData( a, b);

        // Read from the database and add it to dayDataList
        newRefUntracked.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Float tracked; Float untracked;

                DataSnapshot untrackedSnapshot = dataSnapshot.child("Untracked");
                DataSnapshot trackedSnapshot = dataSnapshot.child("Tracked");

                tracked = trackedSnapshot.getValue(Float.class);
                untracked = untrackedSnapshot.getValue(Float.class);

                DayData day = new DayData(tracked, untracked);

                dayDataList.add(day);
                Log.d("FIREBASE VAL", "FIREBASE VAL: \n "+date+
                        "\nPlanned:"+day.planned+
                        "\nUnplanned:"+day.unplanned);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE VAL: UN", "Failed to read value.", error.toException());
            }
        });


        if (!dayDataList.isEmpty()) {
            int currsize = dayDataList.size();
            dayToRet = dayDataList.get(currsize);
        }

        return dayToRet;
    }


    private class DayData {
        Float planned;
        Float unplanned;

        DayData(float planned, float unplanned){
            this.planned = planned;
            this.unplanned = unplanned;
        }
    }
}
