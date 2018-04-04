package com.nkanev.taskmanager;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        TopicsPagerAdapter pagerAdapter =
                new TopicsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_new_topic :
                Toast t = Toast.makeText(this, "Add new topic", Toast.LENGTH_SHORT);
                t.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private class TopicsPagerAdapter extends FragmentPagerAdapter {

        public TopicsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position) {
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
            switch(position) {
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
