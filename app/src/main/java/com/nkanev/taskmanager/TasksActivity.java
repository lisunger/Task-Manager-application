package com.nkanev.taskmanager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TasksActivity extends AppCompatActivity implements TasksFragment.OnItemClickListener, TasksFragment.OnDataChangeListener{

    private int TOPIC_ID;
    public static final String EXTRA_TOPIC_ID = "topicId";
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        this.TOPIC_ID = getIntent().getIntExtra(EXTRA_TOPIC_ID, -1);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TasksPagerAdapter pagerAdapter =
                new TasksPagerAdapter(getSupportFragmentManager());
        this.pager = findViewById(R.id.pager_tasks);
        this.pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs_tasks);
        tabLayout.setupWithViewPager(this.pager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_topic:
                Toast.makeText(this, "Изморих се от задачи!", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(String id) {

    }

    @Override
    public void onDataChanged() {

    }

//    private void refreshTasksFragment() {
//        // You cannot reattach fragments that have been added to the layout with the
//        // <fragment> tag, that's why it has been added programmatically in a FrameLayout
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.detach(this.tasksFragment);
//        fragmentTransaction.attach(this.tasksFragment);
//        fragmentTransaction.commit();
//    }

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
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TasksFragment.TasksFilter.ALL.ordinal());
                    break;
                case 1:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TasksFragment.TasksFilter.COMPLETE.ordinal());
                    break;
                case 2:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TasksFragment.TasksFilter.INCOMPLETE.ordinal());
                    break;
                default:
                    bundle.putInt(TasksFragment.COMPLETE_LEVEL, TasksFragment.TasksFilter.ALL.ordinal());
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
