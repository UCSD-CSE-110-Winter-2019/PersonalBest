package com.team2.team2_personalbest;
import android.text.format.DateFormat;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity/*(tableName = "day_step_table")*/
public class Day {

    //@NonNull
    //@ColumnInfo(name = "day")
    //private Date date;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "dayId")
    private String dayId;

    //String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
    //String day          = (String) DateFormat.format("dd",   date); // 20
    //String monthString  = (String) DateFormat.format("MMM",  date); // Jun
    //String monthNumber  = (String) DateFormat.format("MM",   date); // 06
    //String year         = (String) DateFormat.format("yyyy", date); // 2013


    @ColumnInfo(name = "steps_tracked")
    private int stepsTracked;

    @ColumnInfo(name = "steps_untracked")
    private int stepsUntracked;

    public Day(String dayId /*, @NonNull Date date*/, int stepsTracked, int stepsUntracked) {
        this.dayId = dayId;
        //this.date = date;
        this.stepsTracked = stepsTracked;
        this.stepsUntracked = stepsUntracked;
    }

    /* getters for all fields */

    public String getDayId(){
        return this.dayId;
    }

    //public Date getDate() { return this.date; }

    public int getStepsTracked() { return this.stepsTracked; }

    public int getStepsUntracked() { return this.stepsUntracked; }


    /* Setters for all fields */

    //public void setDate(@NonNull Date date) { this.date = date; }

    public void setDayId(String dayId) { this.dayId = dayId; }

    public void setStepsTracked(int stepsTracked) { this.stepsTracked = stepsTracked; }

    public void setStepsUntracked(int stepsUntracked) { this.stepsUntracked = stepsUntracked; }


}
