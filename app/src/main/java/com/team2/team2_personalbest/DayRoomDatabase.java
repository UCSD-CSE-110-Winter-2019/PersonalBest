package com.team2.team2_personalbest;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Day.class}, version = 1)
public abstract class DayRoomDatabase extends RoomDatabase {

    public abstract DayDao dayDao();

    private static volatile DayRoomDatabase INSTANCE;

    static DayRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (DayRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DayRoomDatabase.class, "day_step_table").build();
                }
            }
        }
        return INSTANCE;
    }

}
