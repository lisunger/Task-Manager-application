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
    private int taskId;
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

        this.taskId = getIntent().getExtras().getInt(EXTRA_TASK_ID);

        if(this.taskId != 0) {
            this.currentTask = TaskDAO.loadTaskFromDB(this, this.taskId);
            fillTextFields();
        }
        else{
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

                TaskDAO.updateTask(this, this.taskId,
                        this.editTextName.getText().toString(), this.editTextContents.getText().toString());
                //TODO: navigate back
                break;

            case android.R.id.home:
                Log.d("TasksActivity++", "Up button!");
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpFromSameTask(this);
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

}
