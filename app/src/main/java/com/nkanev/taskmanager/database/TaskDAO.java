package com.nkanev.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TaskDAO {

    public static Task loadTaskFromDB(Context context, int taskId) {
        Log.d("DAO", "loadTaskFromDB(), Id = " + taskId);
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);
        Task task = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = databaseHelper.getReadableDatabase();

            cursor = db.query(
                    TasksSQLiteHelper.TABLE_TASKS,
                    new String[]{"_id, topicId, name, contents, complete"},
                    "_id = ?",
                    new String[]{String.valueOf(taskId)},
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                task = new Task(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4).equals("1")
                );
            }

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database error", Toast.LENGTH_LONG);
            toast.show();
            databaseHelper.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            databaseHelper.close();
        }

        return task;
    }

    public static void updateTask(Context context, int taskId, String name, String contents) {
        Log.d("DAO", "updateTask(), Id = " + taskId);
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);
        Task task = null;

        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("contents", contents);

            db.update(TasksSQLiteHelper.TABLE_TASKS, values, "_id = ?", new String[] { String.valueOf(taskId)});
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database error", Toast.LENGTH_LONG);
            toast.show();
            databaseHelper.close();
        } finally {
            if (db != null) {
                db.close();
            }
            databaseHelper.close();
        }
    }
}
