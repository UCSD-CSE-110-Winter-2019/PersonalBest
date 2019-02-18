package com.team2.team2_personalbest;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * This class is used for database
 */
@Database(entities = {PlannedWalk.class}, version = 1, exportSchema = false)
public abstract class WalkDatabase extends RoomDatabase {

    public abstract PlannedWalkDao walkDao();

    private static volatile WalkDatabase INSTANCE;

    static WalkDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (DayDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WalkDatabase.class, "walks_table").build();
                }
            }
        }
        return INSTANCE;
    }
}
