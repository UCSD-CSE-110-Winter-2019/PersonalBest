package com.team2.team2_personalbest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "day_step_table")
public class Day {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "day")
    private Date date;

    @ColumnInfo(name = "steps_tracked")
    private int stepsTracked;

    @ColumnInfo(name = "steps_untracked")
    private int stepsUntracked;

    public Day(@NonNull Date date, int stepsTracked, int stepsUntracked) {
        this.date = date;
        this.stepsTracked = stepsTracked;
        this.stepsUntracked = stepsUntracked;
    }

    public Date getDate() {
        return this.date;
    }

    public int getStepsTracked() {
        return this.stepsTracked;
    }

    public int getStepsUntracked() {
        return this.stepsUntracked;
    }

}
