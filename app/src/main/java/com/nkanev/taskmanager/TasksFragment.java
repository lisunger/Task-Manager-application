package com.nkanev.taskmanager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nkanev.taskmanager.database.TasksSQLiteHelper;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment {

    /**
     * The data the recycler displays:
     *  tasksData[0]: array with the _ID
     *  tasksData[1]: array with the NAME
     *  tasksData[2]: array with the CONTENTS
     *  tasksData[3]: array with the COMPLETE
     */
    private String[][] tasksData = new String[4][];
    public static final String TOPIC_ID = "topicId";
    public static final String COMPLETE_LEVEL = "completeness";
    private int topicId;
    private TasksFilter filter;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView tasksRecycler =
                (RecyclerView) inflater.inflate(R.layout.fragment_tasks_parent, container, false);
        tasksRecycler.setHasFixedSize(true);

        // initialize the id of the current topic
        this.topicId = getArguments().getInt(this.TOPIC_ID);
        // initialize if the topic should display only the comlete/incomplete or all tasks
        this.filter = TasksFilter.values()[(getArguments().getInt(this.COMPLETE_LEVEL))];

        // load the tasks for the current topicId
        this.tasksData = loadTasksFromDB();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tasksRecycler.setLayoutManager(layoutManager);

        TasksCardsAdapter adapter = new TasksCardsAdapter(this.tasksData, new OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                //Intent intent = new Intent(getActivity(), TasksActivity.class);
                //intent.putExtra(TasksActivity.EXTRA_TOPIC_ID, Integer.valueOf(id));
                //startActivity(intent);
            }
        });
        tasksRecycler.setAdapter(adapter);

        return tasksRecycler;
    }

    /**
     * @return Array of arrays with the loaded data
     */
    String[][] loadTasksFromDB() {
        String[] id = new String[0];
        String[] name = new String[0];
        String[] content = new String[0];
        String[] complete = new String[0];
        this.tasksData[1] = new String[0];
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(getActivity());

        String tasksFilter;
        switch(this.filter) {
            case ALL:
                tasksFilter = "";
                break;
            case COMPLETE:
                tasksFilter = " and complete = 1";
                break;
            case INCOMPLETE:
                tasksFilter = " and complete = 0";
                break;
            default:
                tasksFilter = "";
        }
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            if (db != null) {
                Cursor cursor = db.query(
                        TasksSQLiteHelper.TABLE_TASKS,
                        new String[]{"_id, name, contents, complete"},
                        "topicId = ?" + tasksFilter,
                        new String[]{String.valueOf(this.topicId)},
                        null, null, null, null);

                if (cursor.moveToFirst()) {

                    List<String> ids = new ArrayList<String>();
                    List<String> names = new ArrayList<String>();
                    List<String> contents = new ArrayList<String>();
                    List<String> completes = new ArrayList<String>();

                    ids.add(cursor.getString(0));
                    names.add(cursor.getString(1));
                    contents.add(cursor.getString(2));
                    completes.add(cursor.getString(3));

                    while (cursor.moveToNext()) {
                        ids.add(cursor.getString(0));
                        names.add(cursor.getString(1));
                        contents.add(cursor.getString(2));
                        completes.add(cursor.getString(3));
                    }

                    id = ids.toArray(new String[0]);
                    name = names.toArray(new String[0]);
                    content = contents.toArray(new String[0]);
                    complete = completes.toArray(new String[0]);
                }
                cursor.close();
            }
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_LONG);
            toast.show();
            databaseHelper.close();
        }

        return new String[][]{id, name, content, complete};
    }

    interface OnItemClickListener {
        void onItemClick(String id);
    }

    private class TasksCardsAdapter extends RecyclerView.Adapter<TasksFragment.TasksCardsAdapter.ViewHolder> {
        private String[] taskIds;
        private String[] taskNames;
        private String[] taskContents;
        private String[] taskComplete;
        private final TasksFragment.OnItemClickListener listener;

        public TasksCardsAdapter(String[][] data, OnItemClickListener listener) {
            this.taskIds = data[0];
            this.taskNames = data[1];
            this.taskContents = data[2];
            this.taskComplete = data[3];
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return taskNames.length;
        }

        /**
         * Creates an empty layout for each entry and passes it to onBindViewHolder().
         * Called for each entry from the data set.
         */
        @Override
        public TasksCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_task, parent, false);

            return new ViewHolder(cardView);
        }

        /**
         * Receives a holder (an empty layout) and fills it with data from the data set
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            CardView cardView = holder.cardView;

            TextView taskName = cardView.findViewById(R.id.card_task_title);
            taskName.setText(taskNames[position]);
            TextView taskText = cardView.findViewById(R.id.card_task_text);
            taskText.setText(taskContents[position]);

            ImageView icon = cardView.findViewById(R.id.icon_completeness);
            if(taskComplete[position].equals("1")) {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_48dp));
                icon.setBackground(getResources().getDrawable(R.drawable.border_green));
                DrawableCompat.setTint(icon.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorGreen));
                icon.setImageAlpha(127);
            }
            else {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_black_48dp));
                icon.setBackground(getResources().getDrawable(R.drawable.border_red));
                DrawableCompat.setTint(icon.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorRed));
                icon.setImageAlpha(127);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(taskIds[position]);
                }
            });
        }

        /* -------------------------------------------------------------------------------------- */
        public class ViewHolder extends RecyclerView.ViewHolder {

            private CardView cardView;

            public ViewHolder(CardView cardView) {
                super(cardView);
                this.cardView = cardView;
            }
        }
    }

    public enum TasksFilter {
        COMPLETE, INCOMPLETE, ALL
    }
}