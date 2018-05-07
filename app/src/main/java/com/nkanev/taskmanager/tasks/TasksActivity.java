package com.nkanev.taskmanager.tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nkanev.taskmanager.R;
import com.nkanev.taskmanager.database.TaskDAO;

public class TasksActivity extends AppCompatActivity implements TasksFragment.OnItemClickListener, TasksFragment.OnDataChangeListener{

    public static final String EXTRA_TOPIC_ID = "topicId";
    private static final int CODE_EDIT_TASK = 1;
    private static final int CODE_CREATE_TASK = 2;

    private int TOPIC_ID;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TasksActivity++", "onCreate()");
        setContentView(R.layout.activity_tasks);

        if(getIntent().getIntExtra(EXTRA_TOPIC_ID, -1) != -1) {
            this.TOPIC_ID = getIntent().getIntExtra(EXTRA_TOPIC_ID, -1);
        }
        else {
            SharedPreferences sharedPrefs = this.getPreferences(MODE_PRIVATE);
            this.TOPIC_ID = sharedPrefs.getInt("TOPIC_ID", -1);
        }


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TasksPagerAdapter pagerAdapter =
                new TasksPagerAdapter(getSupportFragmentManager());
        this.pager = findViewById(R.id.pager_tasks);
        this.pager.setAdapter(pagerAdapter);
        this.pager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tabs_tasks);
        tabLayout.setupWithViewPager(this.pager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_topic:
                Intent intent = new Intent(this, CreateTaskActivity.class);
                intent.putExtra(CreateTaskActivity.EXTRA_MODE, CreateTaskActivity.Mode.CREATE.ordinal());
                intent.putExtra(CreateTaskActivity.EXTRA_TOPIC_ID, TOPIC_ID);
                startActivityForResult(intent, CODE_CREATE_TASK);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(int id) {
        Log.d("lisko", "ItemClicked: " + id);
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra(CreateTaskActivity.EXTRA_MODE, CreateTaskActivity.Mode.EDIT);
        intent.putExtra(CreateTaskActivity.EXTRA_TASK_ID, id);
        startActivityForResult(intent, this.CODE_EDIT_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.onDataChanged();

    }

    @Override
    public void onDataChanged() {
        this.recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPref.edit();
        editor.putInt("TOPIC_ID", this.TOPIC_ID);
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TasksActivity++", "onSaveInstanceState()");
        Log.d("TasksActivity++", "onSaveInstanceState(), putting " + this.TOPIC_ID);
        outState.putInt("topicId", this.TOPIC_ID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TasksActivity++", "onRestoreInstanceState(), " + (savedInstanceState != null));
    }

    private class TasksPagerAdapter extends FragmentPagerAdapter {

        public TasksPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putInt(TasksFragment.TOPIC_ID, TOPIC_ID);


            switch (position) {
                case 0:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TaskDAO.TasksFilter.ALL.ordinal());
                    break;
                case 1:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TaskDAO.TasksFilter.COMPLETE.ordinal());
                    break;
                case 2:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TaskDAO.TasksFilter.INCOMPLETE.ordinal());
                    break;
                default:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TaskDAO.TasksFilter.ALL.ordinal());
                    break;
            }

            TasksFragment fragment = new TasksFragment();

            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.tab_all);
                case 1:
                    return getResources().getText(R.string.tab_compl);
                case 2:
                    return getResources().getText(R.string.tab_incompl);
                default:
                    return "";
            }
        }
    }
}
