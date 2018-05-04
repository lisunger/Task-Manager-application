package com.nkanev.taskmanager.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.nkanev.taskmanager.R;
import com.nkanev.taskmanager.database.Task;
import com.nkanev.taskmanager.database.TaskDAO;

public class CreateTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "taskId";
    public static final String EXTRA_TOPIC_ID = "topicId";
    public static final String EXTRA_MODE = "mode";
    private int taskId = -1;
    private int topicId = -1;
    private Mode mode;
    private Task currentTask;

    private EditText editTextName;
    private EditText editTextContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.editTextName = findViewById(R.id.input_task_name);
        this.editTextContents = findViewById(R.id.input_task_content);

        // check if a mode is provided - if a new task is being created or old one is being edited
        try {
            this.mode = Mode.values()[getIntent().getExtras().getInt(EXTRA_MODE)];
        } catch(NullPointerException e) {
            throw new NullPointerException("EXTRA_MODE not set in calling activity");
        }

        // if you edit an existing item you pass here the taskId
        // if you create a task you need to provide it with a topicId
        if(this.mode == Mode.EDIT) {
            try {
                this.taskId = getIntent().getExtras().getInt(EXTRA_TASK_ID);
            } catch(NullPointerException e) {
                throw new NullPointerException("EXTRA_TASK_ID not set in calling activity");
            }
        }
        else if(this.mode == Mode.CREATE) {
            try {
                this.topicId = getIntent().getExtras().getInt(EXTRA_TOPIC_ID);
            } catch(NullPointerException e) {
                throw new NullPointerException("EXTRA_TOPIC_ID not set in calling activity");
            }
        }


        // initialize the currentTask
        if(this.mode == Mode.EDIT) {
            this.currentTask = TaskDAO.loadTaskFromDB(this, this.taskId);
            this.topicId = this.currentTask.getTopicId();
            fillTextFields();
        }
        else if(this.mode == Mode.CREATE){
            this.currentTask = new Task();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save_task:

                this.currentTask.setName(this.editTextName.getText().toString());
                this.currentTask.setContents(this.editTextContents.getText().toString());
                this.currentTask.setComplete(false);
                this.currentTask.setTopicId(this.topicId);

                if(this.mode == Mode.CREATE) {
                    TaskDAO.createTask(this, this.currentTask);
                }
                else if(this.mode == Mode.EDIT) {
                    TaskDAO.updateTask(this, this.currentTask);
                }

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void fillTextFields() {
        EditText fieldName = findViewById(R.id.input_task_name);
        EditText fieldContents = findViewById(R.id.input_task_content);

        fieldName.setText(this.currentTask.getName());
        fieldContents.setText(this.currentTask.getContents());
    }

    public enum Mode {
        EDIT, CREATE
    }
}
