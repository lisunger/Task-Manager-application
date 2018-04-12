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

    public static final String DB_NAME = "TASKMANAGER";
    public static final String TABLE_TOPICS = "TOPICS";
    private static final int DB_VERSION = 1;

    public TasksSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* create table TOPICS(
            _id integer primary key autoincrement,
            name text);
        */
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TABLE_TOPICS);
        sb.append(" (_id integer primary key autoincrement, name text);");

        db.execSQL(sb.toString());

        // add some predefined topics to the TOPICS table
        insertTopic(db, "Тема 1");
        insertTopic(db, "Тема 2");
        insertTopic(db, "Тема 3");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static void insertTopic(SQLiteDatabase db, String name) {
        ContentValues topic = new ContentValues();
        topic.put("name", name);
        db.insert(TABLE_TOPICS, null, topic);
    }
}
