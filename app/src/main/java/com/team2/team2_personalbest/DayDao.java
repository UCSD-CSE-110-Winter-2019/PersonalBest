package com.team2.team2_personalbest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DayDao {

    @Insert
    void insertSingleDay(Day day);

    @Insert
    void insertMultipleDays(List<Day> dayList);

    @Query("SELECT * FROM Day WHERE dayId = (:dayId)")
    Day getDayById(String dayId);

    @Query("DELETE FROM Day")
    void deleteDay();

    @Update
    void updateDay(Day day);

    /*@Query("SELECT * from day_step_table ORDER BY day ASC")
    List<Day> getAllWords(); */


}
