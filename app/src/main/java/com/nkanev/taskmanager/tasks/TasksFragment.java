package com.nkanev.taskmanager.tasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nkanev.taskmanager.R;
import com.nkanev.taskmanager.database.Task;
import com.nkanev.taskmanager.database.TaskDAO;

import java.util.ArrayList;
import java.util.List;

//this.selectedCard.setCardBackgroundColor(getResources().getColor(R.color.lightGold));

public class TasksFragment extends Fragment {

    private List<Task> taskList = new ArrayList<Task>();

    public static final String CATEGORY_ID = "categoryId";
    public static final String COMPLETE_LEVEL = "completeness";
    private int categoryId;
    private TaskDAO.TasksFilter filter;

    private CardView clickedItem = null;

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
        this.categoryId = getArguments().getInt(this.CATEGORY_ID);

        if(this.categoryId == -1 && savedInstanceState != null) {
            this.categoryId = savedInstanceState.getInt("CATEGORY_ID");
        }

        // initialize if the topic should display only the comlete/incomplete or all tasks
        this.filter = TaskDAO.TasksFilter.values()[(getArguments().getInt(COMPLETE_LEVEL))];

        // load the tasks for the current topicId
        this.taskList = TaskDAO.loadTasksFromDB(getActivity(), this.categoryId, this.filter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tasksRecycler.setLayoutManager(layoutManager);

        TasksCardsAdapter adapter = new TasksCardsAdapter(this.taskList, (TasksActivity) getActivity());
        tasksRecycler.setAdapter(adapter);

        return tasksRecycler;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CATEGORY_ID", this.categoryId);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        this.clickedItem = ((CardView) v);
        this.clickedItem.setCardBackgroundColor(getResources().getColor(R.color.lightGold));

        menu.add(Menu.NONE, v.getId(), Menu.NONE, "Delete");
        menu.add(Menu.NONE, v.getId(), Menu.NONE, "Move to another category");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("Delete")) {
            // TODO write a delete method in the DAO
        }
        else if(item.getTitle().equals("Move to another category")) {
            // TODO write a method in the DAO
        }
        else {
            Toast.makeText(getActivity(), "Menu closed", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return true;
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

            registerForContextMenu(cardView);
            return new ViewHolder(cardView);
        }

        /**
         * Receives a holder (an empty layout) and fills it with data from the data set
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final CardView cardView = holder.cardView;
            cardView.setId(tasks.get(position).getId());

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
                        TaskDAO.changeTaskCompleteness(getActivity(), tasks.get(position).getId(), false);
                    }
                    else {
                        setIconPositive(icon);
                        TaskDAO.changeTaskCompleteness(getActivity(), tasks.get(position).getId(), true);
                    }

                    ((OnDataChangeListener)getActivity()).onDataChanged();
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


    interface OnDataChangeListener {
        void onDataChanged();
    }

}
