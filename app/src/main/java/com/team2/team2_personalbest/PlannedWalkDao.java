package com.team2.team2_personalbest;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlannedWalkDao {

    @Insert
    void insertSingleWalk(PlannedWalk walk);

    @Insert
    void insertMultipleDays(List<PlannedWalk> walkList);

    @Query("SELECT * FROM PlannedWalk")
    List<PlannedWalk> getAllWalks();

}
