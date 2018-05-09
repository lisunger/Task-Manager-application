package com.nkanev.taskmanager.categories;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nkanev.taskmanager.R;
import com.nkanev.taskmanager.database.CategoryDAO;
import com.nkanev.taskmanager.database.TaskDAO;
import com.nkanev.taskmanager.database.TasksSQLiteHelper;
import com.nkanev.taskmanager.tasks.TasksActivity;
import com.nkanev.taskmanager.utils.Utils;

public class MainActivity extends AppCompatActivity implements CategoriesFragment.OnItemClickListener, CreateCategoryFragment.CreateCategoryDialogListener {

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

/*
    new OnItemClickListener() {
    @Override
    public void onItemClick(String id, String name) {
        Intent intent = new Intent(getActivity(), TasksActivity.class);
        intent.putExtra(TasksActivity.EXTRA_CATEGORY_ID, Integer.valueOf(id));
        intent.putExtra(TasksActivity.EXTRA_CATEGORY_NAME, name);
        startActivity(intent);
    }
*/

    @Override
    public void onItemClick(String id, String name) {
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtra(TasksActivity.EXTRA_CATEGORY_ID, Integer.valueOf(id));
        intent.putExtra(TasksActivity.EXTRA_CATEGORY_NAME, name);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(final int categoryId, final CardView cardView) {
        // make cardView yellow
        cardView.setCardBackgroundColor(getResources().getColor(R.color.lightGold));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(new String[] { "Delete" }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        showDeleteDialog(categoryId);
                        break;
                }
            }
        });

        // change the task's color back to white when menu closed
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });

        builder.create().show();
    }

    private void showDeleteDialog(final int categoryId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmDeleteTopic);

        builder.setPositiveButton(R.string.confirmDeleteOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CategoryDAO.deleteCategory(MainActivity.this, categoryId);
                MainActivity.this.refreshCategoriesFragment();
            }
        });

        builder.setNegativeButton(R.string.confirmDeleteCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }
}
