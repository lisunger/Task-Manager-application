package com.nkanev.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public static void createTask(Context context, final Task task) {
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);

        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", task.getName());
            values.put("contents", task.getContents());
            values.put("complete", task.isComplete());
            values.put("categoryId", task.getCategoryId());

            db.insert(
                    TasksSQLiteHelper.TABLE_TASKS,
                    null,
                    values);

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database error", Toast.LENGTH_LONG);
            toast.show();
        } finally {
            if (db != null) {
                db.close();
            }
            databaseHelper.close();
        }
    }

    public static void deleteTask(Context context, int taskId) {
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);

        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            db.delete(
                    TasksSQLiteHelper.TABLE_TASKS,
                    "_id = ?",
                    new String[]{String.valueOf(taskId)});

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database error", Toast.LENGTH_LONG);
            toast.show();
        } finally {
            if (db != null) {
                db.close();
            }
            databaseHelper.close();
        }
    }

    public static Task loadTaskFromDB(Context context, final int taskId) {
        Log.d("DAO", "loadTaskFromDB(), Id = " + taskId);
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);
        Task task = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = databaseHelper.getReadableDatabase();

            cursor = db.query(
                    TasksSQLiteHelper.TABLE_TASKS,
                    new String[]{"_id, categoryId, name, contents, complete"},
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

    /**
     * @param filter return the tasks that are either complete, incomplete or all of them
     * @return List with tasks
     */
    public static List<Task> loadTasksFromDB(Context context, final int categoryId, TasksFilter filter) {

        List<Task> taskList = new ArrayList<Task>();

        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);
        SQLiteDatabase db = null;

        String tasksFilter;
        switch (filter) {
            case ALL:
                tasksFilter = "";
                break;
            case COMPLETE:
                tasksFilter = " and complete = 1";
                break;
            case INCOMPLETE:
                tasksFilter = " and complete = 0";
                break;
            default:
                tasksFilter = "";
        }
        try {
            db = databaseHelper.getReadableDatabase();
            if (db != null) {
                Cursor cursor = db.query(
                        TasksSQLiteHelper.TABLE_TASKS,
                        new String[]{"_id, categoryId, name, contents, complete"},
                        "categoryId = ?" + tasksFilter,
                        new String[]{String.valueOf(categoryId)},
                        null, null, null, null);

                if (cursor.moveToFirst()) {
                    Task task = new Task(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4).equals("1"));

                    taskList.add(task);

                    while (cursor.moveToNext()) {
                        task = new Task(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4).equals("1"));

                        taskList.add(task);
                    }
                }
                cursor.close();
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_LONG);
            toast.show();
            databaseHelper.close();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
            databaseHelper.close();
        }

        return taskList;
    }

    public static void updateTask(Context context, Task task) {
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);

        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", task.getName());
            values.put("contents", task.getContents());

            db.update(TasksSQLiteHelper.TABLE_TASKS, values, "_id = ?", new String[]{String.valueOf(task.getId())});
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database error", Toast.LENGTH_LONG);
            toast.show();
        } finally {
            if (db != null) {
                db.close();
            }
            databaseHelper.close();
        }
    }

    public static void changeTaskCompleteness(Context context, int taskId, boolean completed) {
        ContentValues values = new ContentValues();
        values.put("complete", (completed == true) ? 1 : 0);

        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();
            db.update(
                    TasksSQLiteHelper.TABLE_TASKS,
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(taskId)});

        } catch (SQLiteException e) {
            Toast.makeText(context, "Database unavailable", Toast.LENGTH_LONG).show();
            databaseHelper.close();
        } finally {
            if(db != null) {
                db.close();
            }
        }
    }

    public static void changeTaskCategory(Context context, int taskId, int newCategoryId) {

        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);

        SQLiteDatabase db = null;
        ContentValues values = new ContentValues();
        values.put("categoryId", newCategoryId);

        try {
            db = databaseHelper.getReadableDatabase();
            db.update(
                    TasksSQLiteHelper.TABLE_TASKS,
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(taskId)});
        } catch (SQLiteException e) {
            Toast.makeText(context, "Database unavailable", Toast.LENGTH_LONG).show();
            databaseHelper.close();
        } finally {
            if(db != null) {
                db.close();
            }
        }
    }

    public enum TasksFilter {
        COMPLETE, INCOMPLETE, ALL
    }
}
