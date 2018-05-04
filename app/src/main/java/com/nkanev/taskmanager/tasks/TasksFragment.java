package com.nkanev.taskmanager.tasks;


import android.content.ContentValues;
import android.content.Intent;
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

import com.nkanev.taskmanager.R;
import com.nkanev.taskmanager.database.Task;
import com.nkanev.taskmanager.database.TaskDAO;
import com.nkanev.taskmanager.database.TasksSQLiteHelper;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment {

    private List<Task> taskList = new ArrayList<Task>();

    public static final String TOPIC_ID = "topicId";
    public static final String COMPLETE_LEVEL = "completeness";
    private int topicId;
    private TaskDAO.TasksFilter filter;

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

        if(this.topicId == -1 && savedInstanceState != null) {
            this.topicId = savedInstanceState.getInt("TOPIC_ID");
        }
        Log.d("TasksFragment", "TopicId: " + this.topicId);

        // initialize if the topic should display only the comlete/incomplete or all tasks
        this.filter = TaskDAO.TasksFilter.values()[(getArguments().getInt(this.COMPLETE_LEVEL))];

        // load the tasks for the current topicId
        this.taskList = TaskDAO.loadTasksFromDB(getActivity(), this.topicId, this.filter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tasksRecycler.setLayoutManager(layoutManager);

        TasksCardsAdapter adapter = new TasksCardsAdapter(this.taskList, (TasksActivity) getActivity());
        tasksRecycler.setAdapter(adapter);

        return tasksRecycler;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("TOPIC_ID", this.topicId);
    }

    private class TasksCardsAdapter extends RecyclerView.Adapter<TasksFragment.TasksCardsAdapter.ViewHolder> {
        private List<Task> tasks;
        private final TasksFragment.OnItemClickListener listener;

        public TasksCardsAdapter(List<Task> tasks, OnItemClickListener listener) {
            this.tasks = tasks;
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return tasks.size();
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
            taskName.setText(tasks.get(position).getName());
            TextView taskText = cardView.findViewById(R.id.card_task_text);
            taskText.setText(tasks.get(position).getContents());

            ImageView icon = cardView.findViewById(R.id.icon_completeness);
            if(tasks.get(position).isComplete()) {
                setIconPositive(icon);
            }
            else {
                setIconNegative(icon);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(tasks.get(position).getId());
                }
            });

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView icon = (ImageView) v;

                    if(tasks.get(position).isComplete()){
                        setIconNegative(icon);
                        changeTaskCompleteness(tasks.get(position).getId(), false);
                    }
                    else {
                        setIconPositive(icon);
                        changeTaskCompleteness(tasks.get(position).getId(), true);
                    }

                    getActivity().recreate();
                }
            });
        }

        private void setIconPositive(ImageView icon) {
            setIcon(icon, R.drawable.ic_check_black_48dp, R.drawable.border_green, R.color.colorGreen, 127);
        }

        private void setIconNegative(ImageView icon) {
            setIcon(icon, R.drawable.ic_close_black_48dp, R.drawable.border_red, R.color.colorRed, 127);
        }

        private void setIcon(ImageView icon, int imageId, int borderId, int colorId, int alpha) {
            icon.setImageDrawable(getResources().getDrawable(imageId));
            icon.setBackground(getResources().getDrawable(borderId));
            DrawableCompat.setTint(icon.getDrawable(), ContextCompat.getColor(getActivity(), colorId));
            icon.setImageAlpha(alpha);
        }

        private void changeTaskCompleteness(int id, boolean completed) {
            ContentValues values = new ContentValues();
            values.put("complete", (completed == true) ? 1 : 0);

            SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(getActivity());

            try {
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                db.update(
                        TasksSQLiteHelper.TABLE_TASKS,
                        values,
                        "_id = ?",
                        new String[] { String.valueOf(id) });

            } catch (SQLiteException e) {
                Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_LONG);
                toast.show();
                databaseHelper.close();
            }
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

    interface OnItemClickListener {
        void onItemClick(int id);
    }

    /*
    interface OnDataChangeListener {
        public void onDataChanged();
    }
    */
}
