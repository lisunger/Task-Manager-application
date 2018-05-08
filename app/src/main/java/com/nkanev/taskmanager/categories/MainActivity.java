package com.nkanev.taskmanager.categories;

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

import com.nkanev.taskmanager.R;
import com.nkanev.taskmanager.database.TasksSQLiteHelper;

public class MainActivity extends AppCompatActivity implements CreateCategoryFragment.CreateCategoryDialogListener {

    private static final String TAG = "MainActivity";
    private Toast toast;
    private CategoriesFragment categoriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The fragment is added programmatically so that it can be reattached later
        this.categoriesFragment = new CategoriesFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_fragment_categories, this.categoriesFragment);
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
            case R.id.menu_new_category:
                CreateCategoryFragment f = new CreateCategoryFragment();
                f.show(getSupportFragmentManager(), TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add the title to the CATEGORIES table as a new topic
     *
     * @param title the name of the new category
     * @return <code>true</code> if the category has been added successfully
     */
    private boolean addCategoryToDB(String title) {
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(this);
        long newId = -1;
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", title);
            newId = db.insert(TasksSQLiteHelper.TABLE_CATEGORIES, null, values);
        } catch (SQLiteException e) {
            showToast("Database error");
        } finally {
            databaseHelper.close();
        }
        return newId != -1;
    }

    private void refreshCategoriesFragment() {
        // You cannot reattach fragments that have been added to the layout with the
        // <fragment> tag, that's why it has been added programmatically in a FrameLayout
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(this.categoriesFragment);
        fragmentTransaction.attach(this.categoriesFragment);
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
        // Create a new category in the database with the given name
        if (text != null && text.trim().length() > 0) {
            boolean categoryCreated = addCategoryToDB(text);

            if (categoryCreated) {
                showToast("Topic created");
            } else {
                showToast("Error when creating new Category");
            }
            refreshCategoriesFragment();
        }
    }


}
