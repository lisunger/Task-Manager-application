package com.nkanev.taskmanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nkanev.taskmanager.database.TasksSQLiteHelper;

public class MainActivity extends AppCompatActivity implements CreateTopicFragment.CreateTopicDialogListener{

    private static final String TAG = "MainActivity";
    private Toast toast;
    private int pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        TopicsPagerAdapter pagerAdapter =
                new TopicsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.pager_topics);
        pager.setAdapter(pagerAdapter);
        pages = pagerAdapter.getCount();
        pager.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int a = 2;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

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

    private void refreshPagerFragments() {
        for(int i = 0; i < pages; i++) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager_topics + ":" + 0);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }
    }

    private void showToast(String message) {
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onDialogPositiveClick(String text) {
        Log.i(TAG, text);
        // Create a new topic in the database with the given name
        if(text != null && text.trim().length() > 0) {
            boolean topicCreated = addTopicToDB(text);

            if (topicCreated) {
                showToast("Topic created");
            }
            else {
                showToast("Error when creating new topic");
            }
            refreshPagerFragments();
        }
    }

    private class TopicsPagerAdapter extends FragmentPagerAdapter {

        public TopicsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TopicsAllFragment();
                case 1:
                    return new TopicsCompleteFragment();
                case 2:
                    return new TopicsIncompleteFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_all);
                case 1:
                    return getResources().getString(R.string.tab_compl);
                case 2:
                    return getResources().getString(R.string.tab_incompl);
                default:
                    return null;
            }
        }

    }

}
