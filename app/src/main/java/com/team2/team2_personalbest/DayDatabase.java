package com.team2.team2_personalbest;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Day.class}, version = 1, exportSchema = false)
public abstract class DayDatabase extends RoomDatabase {

    public abstract DayDao dayDao();

    private static volatile DayDatabase INSTANCE;

    static DayDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (DayDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DayDatabase.class, "day_step_table").build();
                }
            }
        }
        return INSTANCE;
    }

}
