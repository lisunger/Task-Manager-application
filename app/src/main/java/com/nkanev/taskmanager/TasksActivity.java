package com.nkanev.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TasksActivity extends AppCompatActivity {

    private String TOPIC_ID;
    public static final String EXTRA_TOPIC_ID = "topicId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        this.TOPIC_ID = getIntent().getStringExtra(EXTRA_TOPIC_ID, -1);
        TextView text = findViewById(R.id.myLittleId);
        int a = 2;
        text = null;
        //text.setText(this.TOPIC_ID);
    }
}
