package com.nkanev.taskmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static List<Category> getCategories(Context context) {
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);

        List<Category> categories = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query(
                    TasksSQLiteHelper.TABLE_CATEGORIES,
                    new String[] { "_id, name" },
                    null, null, null, null,
                    "_id asc",
                    null);

            if (cursor.moveToFirst()) {
                categories = new ArrayList<Category>();
                categories.add(new Category(Integer.valueOf(cursor.getString(0)), cursor.getString(1)));

                while (cursor.moveToNext()) {
                    categories.add(new Category(Integer.valueOf(cursor.getString(0)), cursor.getString(1)));
                }
            }
            else {
                categories = new ArrayList<Category>(0);
            }

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database error", Toast.LENGTH_LONG);
            toast.show();
        }

        return categories;
    }

    public static void deleteCategory(Context context, int categoryId) {
        TaskDAO.deleteTasksByCategory(context, categoryId);

        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(context);

        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            db.delete(
                    TasksSQLiteHelper.TABLE_CATEGORIES,
                    "_id = ?",
                    new String[]{String.valueOf(categoryId)});

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

}
