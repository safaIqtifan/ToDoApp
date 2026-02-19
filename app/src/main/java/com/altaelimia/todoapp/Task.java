package com.altaelimia.todoapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

//  @PrimaryKey(autoGenerate = true)
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

//    @ColumnInfo(name = "status")
//    public int status;

    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted;

    @ColumnInfo(name = "isChecked")
    public boolean isChecked;


    public Task(String title, boolean isChecked) {
        this.title = title;
        this.isChecked = isChecked;
    }

//    public Task(int id, String title, boolean isChecked) {
//        this.id = id;
//        this.title = title;
//        this.isChecked = isChecked;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    //    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setChecked(boolean isChecked) {
//        this.isChecked = isChecked;
//    }

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
}

