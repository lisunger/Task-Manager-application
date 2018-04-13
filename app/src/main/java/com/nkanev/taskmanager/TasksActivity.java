package com.nkanev.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TasksActivity extends AppCompatActivity {

    private int TOPIC_ID;
    public static final String EXTRA_TOPIC_ID = "topicId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        this.TOPIC_ID = getIntent().getIntExtra(EXTRA_TOPIC_ID, -1);
    }
}
