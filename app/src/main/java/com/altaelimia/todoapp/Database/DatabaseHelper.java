package com.altaelimia.todoapp.Database;
//
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tasks.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tasks";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void insertTask(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("status", 0);
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getTasksByStatus(int status){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE status=?", new String[]{String.valueOf(status)});
    }

    public void deleteTask(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{id});
    }

    public void updateTitle(String id, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        db.update(TABLE_NAME, values, "id=?", new String[]{id});
    }

    public void updateStatus(String id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        db.update(TABLE_NAME, values, "id=?", new String[]{id});
    }
}
