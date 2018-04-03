package com.nkanev.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by n.kanev on 03-Apr-18.
 */

public class TasksSQLiteHelper extends SQLiteOpenHelper {

    private String TOPICS_TABLE_NAME = "TOPICS";

    public TasksSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* create table TOPICS with columns:
         * _id: primary key
         * name: text
         */
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TOPICS_TABLE_NAME);
        sb.append("(_id integer primary key autoincrement, ");
        sb.append("name text)");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
