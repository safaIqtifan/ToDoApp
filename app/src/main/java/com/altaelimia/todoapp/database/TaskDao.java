package com.altaelimia.todoapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.altaelimia.todoapp.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE isChecked = :isChecked")
    List<Task> getTasksIsChecked(boolean isChecked);

    @Query("SELECT * FROM tasks WHERE isChecked = 0")
    List<Task> getPendingTasks();

    @Query("SELECT * FROM tasks WHERE isChecked = 1")
    List<Task> getCompletedTasks();


//    @Query("SELECT * FROM tasks WHERE status = :status")
//    LiveData<List<Task>> getTasksByStatus(int status);

//    @Query("UPDATE tasks SET status = :status WHERE id = :id")
//    void updateStatus(int id, int status);
}

