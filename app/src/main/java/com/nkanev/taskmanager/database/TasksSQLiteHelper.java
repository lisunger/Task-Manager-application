package com.nkanev.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by n.kanev on 03-Apr-18.
 */

public class TasksSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TASKMANAGER";
    public static final String TABLE_CATEGORIES = "CATEGORIES";
    public static final String TABLE_TASKS = "TASKS";
    private static final int DB_VERSION = 1;

    public TasksSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableCategories(db);
        createTableTasks(db);

        // add some predefined categories to the CATEGORIES table
        insertCategory(db, "Забавни");
        insertCategory(db, "Домашни");
        insertCategory(db, "Работни");

        // add some predefined tasks to the TASKS table
        insertTask(db, 1, "Домино", "Да наредя 285000 плочки домино", true);
        insertTask(db, 1, "Колекция", "Да сортирам колекцията си от дъски за рязане на хляб", false);
        insertTask(db, 2, "Сандвичи", "Да намажа филия с лютеница и сирене и да се почувствам като в доброто старо време", true);
        insertTask(db, 2, "Пролетно почистване", "Да избърша от прах долната страна на всяко чекмежде вкъщи", false);
        insertTask(db, 3, "Дебъгване", "Да се дебъгне приложението за миене на коли", true);
        insertTask(db, 3, "Разработка", "Да разбера как да имплементирам шаблона Facade в новото си приложение за месене на тесто", false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static void insertCategory(SQLiteDatabase db, String name) {
        ContentValues category = new ContentValues();
        category.put("name", name);
        db.insert(TABLE_CATEGORIES, null, category);
    }

    private static void insertTask(SQLiteDatabase db, int categoryId, String name, String contents, boolean complete) {
        ContentValues task = new ContentValues();
        task.put("name", name);
        task.put("categoryId", categoryId);
        task.put("contents", contents);
        task.put("complete", (complete == true) ? 1 : 0);
        db.insert(TABLE_TASKS, null, task);
    }

    private void createTableCategories(SQLiteDatabase db) {
        /* create table CATEGORIES(
             _id integer primary key autoincrement,
             name text not null
           );
        */
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TABLE_CATEGORIES);
        sb.append("(_id integer primary key autoincrement, name text not null);");

        db.execSQL(sb.toString());
    }

    private void createTableTasks(SQLiteDatabase db) {
        /* create table TASKS(
             _id integer primary key autoincrement,
             categoryId integer not null,
             name text not null,
             contents text,
             complete integer not null default 0,
             foreign key(categoryId) references CATEGORIES(_id)
           );
        */
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TABLE_TASKS);
        sb.append("(_id integer primary key autoincrement");
        sb.append(", categoryId integer not null");
        sb.append(", name text not null");
        sb.append(", contents text");
        sb.append(", complete integer not null default 0");
        sb.append(", foreign key(categoryId) references CATEGORIES(_id));");

        Log.d("Create table", sb.toString());

        db.execSQL(sb.toString());
    }
}
