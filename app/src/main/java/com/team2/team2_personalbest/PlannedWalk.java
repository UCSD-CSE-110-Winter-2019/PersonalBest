package com.team2.team2_personalbest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
/**
 * This class is used for database
 */
@Entity
public class PlannedWalk {
    //@NonNull
    //@ColumnInfo(name = "day")
    //private Date date;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "key")
    private int key;

    //String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
    //String day          = (String) DateFormat.format("dd",   date); // 20
    //String monthString  = (String) DateFormat.format("MMM",  date); // Jun
    //String monthNumber  = (String) DateFormat.format("MM",   date); // 06
    //String year         = (String) DateFormat.format("yyyy", date); // 2013


    @ColumnInfo(name = "steps")
    private int steps;

    @ColumnInfo(name = "time")
    private double time;

    public PlannedWalk(int steps, double time) {
        this.steps = steps;
        this.time = time;
    }

    /* getters for all fields */

    public int getKey() {
        return key;
    }

    public double getTime() {
        return time;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}

