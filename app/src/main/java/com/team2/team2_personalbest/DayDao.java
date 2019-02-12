package com.team2.team2_personalbest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DayDao {

    @Insert
    void insert(Day day);

    @Query("DELETE FROM day_step_table")
    void deleteAll();

    @Query("SELECT * from day_step_table ORDER BY day ASC")
    List<Day> getAllWords();
}
