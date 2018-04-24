package com.nkanev.taskmanager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class TasksActivity extends AppCompatActivity {

    private int TOPIC_ID;
    public static final String EXTRA_TOPIC_ID = "topicId";
    private final Bundle bundle = new Bundle();

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
        ViewPager pager = findViewById(R.id.pager_tasks);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs_tasks);
        tabLayout.setupWithViewPager(pager);

        this.bundle.putInt(TasksParentFragment.TOPIC_ID, this.TOPIC_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class TasksPagerAdapter extends FragmentPagerAdapter {

        public TasksPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TasksParentFragment fragment;

            switch (position) {
//                case 0:
//                    fragment = new AllTasksFragment(); break;
//                case 1:
//                    fragment = new CompleteTasksFragment(); break;
//                case 2:
//                    fragment = new IncompleteTasksFragment(); break;
                case 0:
                    fragment = new TasksParentFragment(); break;
                default:
                    fragment = new TasksParentFragment();
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
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
