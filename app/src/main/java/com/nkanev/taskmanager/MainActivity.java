package com.nkanev.taskmanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nkanev.taskmanager.database.TasksSQLiteHelper;

public class MainActivity extends AppCompatActivity implements CreateTopicFragment.CreateTopicDialogListener {

    private static final String TAG = "MainActivity";
    private Toast toast;
    private TopicsFragment topicsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The fragment is added programmatically so that it can be reattached later
        this.topicsFragment = new TopicsFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_fragment_topics, this.topicsFragment);
        transaction.commit();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_topic:
                CreateTopicFragment f = new CreateTopicFragment();
                f.show(getSupportFragmentManager(), TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add the title to the TOPICS table as a new topic
     *
     * @param title the name of the new topic
     * @return <code>true</code> if the topic has been added successfully
     */
    private boolean addTopicToDB(String title) {
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(this);
        long newId = -1;
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", title);
            newId = db.insert(TasksSQLiteHelper.TABLE_TOPICS, null, values);
        } catch (SQLiteException e) {
            showToast("Database error");
        } finally {
            databaseHelper.close();
        }
        return newId != -1;
    }

    private void refreshTopicsFragment() {
        // You cannot reattach fragments that have been added to the layout with the
        // <fragment> tag, that's why it has been added programmatically in a FrameLayout
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(this.topicsFragment);
        fragmentTransaction.attach(this.topicsFragment);
        fragmentTransaction.commit();
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onDialogPositiveClick(String text) {
        Log.i(TAG, text);
        // Create a new topic in the database with the given name
        if (text != null && text.trim().length() > 0) {
            boolean topicCreated = addTopicToDB(text);

            if (topicCreated) {
                showToast("Topic created");
            } else {
                showToast("Error when creating new topic");
            }
            refreshTopicsFragment();
        }
    }


}
