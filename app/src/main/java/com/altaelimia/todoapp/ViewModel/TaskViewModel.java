package com.altaelimia.todoapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.altaelimia.todoapp.CallBack.TaskDao;
import com.altaelimia.todoapp.Class.Task;
import com.altaelimia.todoapp.Database.RoomAppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        RoomAppDatabase db = RoomAppDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> {
            taskDao.insert(task);
        });
    }

    public void update(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> {
            taskDao.update(task);
        });
    }

    public void delete(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> {
            taskDao.delete(task);
        });
    }

    public LiveData<List<Task>> getTasksByStatus(boolean isChecked) {
        return taskDao.getTasksIsChecked(isChecked);
    }

}