package com.altaelimia.todoapp.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.altaelimia.todoapp.CallBack.TaskDao;
import com.altaelimia.todoapp.Class.Task;

@Database(entities = {Task.class}, version = 5)
public abstract class RoomAppDatabase extends RoomDatabase {

    private static RoomAppDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized RoomAppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RoomAppDatabase.class,
                            "todoapp_databases"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}

