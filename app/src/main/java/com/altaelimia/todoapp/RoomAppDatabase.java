package com.altaelimia.todoapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 4)
public abstract class RoomAppDatabase extends RoomDatabase {

    private static RoomAppDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized RoomAppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RoomAppDatabase.class,
                            "todoapp_database"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }
}

