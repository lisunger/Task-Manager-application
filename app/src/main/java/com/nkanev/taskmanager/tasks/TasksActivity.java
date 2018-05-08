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

    public static final String EXTRA_CATEGORY_ID = "categoryId";
    public static final String EXTRA_CATEGORY_NAME = "categoryName";
    private static final int CODE_EDIT_TASK = 1;
    private static final int CODE_CREATE_TASK = 2;

    private int CATEGORY_ID;
    private String CATEGORY_NAME;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        if(getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1) != -1) {
            this.CATEGORY_ID = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);
            this.CATEGORY_NAME = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        }
        else {
            SharedPreferences sharedPrefs = this.getPreferences(MODE_PRIVATE);
            this.CATEGORY_ID = sharedPrefs.getInt("CATEGORY_ID", -1);
            this.CATEGORY_NAME = sharedPrefs.getString("CATEGORY_NAME", "");
        }

        setTitle(this.CATEGORY_NAME);

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
                intent.putExtra(CreateTaskActivity.EXTRA_CATEGORY_ID, CATEGORY_ID);
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
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra(CreateTaskActivity.EXTRA_MODE, CreateTaskActivity.Mode.EDIT);
        intent.putExtra(CreateTaskActivity.EXTRA_TASK_ID, id);
        startActivityForResult(intent, CODE_EDIT_TASK);
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
        editor.putInt("CATEGORY_ID", this.CATEGORY_ID);
        editor.putString("CATEGORY_NAME", this.CATEGORY_NAME);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("categoryId", this.CATEGORY_ID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class TasksPagerAdapter extends FragmentPagerAdapter {

        public TasksPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putInt(TasksFragment.CATEGORY_ID, CATEGORY_ID);


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
