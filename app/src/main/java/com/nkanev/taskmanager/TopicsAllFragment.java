package com.nkanev.taskmanager;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nkanev.taskmanager.database.TasksSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TopicsAllFragment extends Fragment {

    /**
     * The data the recycler displays:
     *  topicsData[0]: array with the _ID
     *  topicsData[1]: array with the NAME
     */
    private String[][] topicsData = new String[2][];

    public TopicsAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView topicsRecycler =
                (RecyclerView) inflater.inflate(R.layout.fragment_all_topics, container, false);
        topicsRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        topicsRecycler.setLayoutManager(layoutManager);

        this.topicsData = loadTopicsFromDB();

        TopicsCardsAdapter adapter = new TopicsCardsAdapter(this.topicsData, new OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                Log.d("Click!", "Position: " + id);
                Intent intent = new Intent(getActivity(), TasksActivity.class);
                intent.putExtra(TasksActivity.EXTRA_TOPIC_ID, id);
                startActivity(intent);
            }
        });
        topicsRecycler.setAdapter(adapter);

        return topicsRecycler;
    }

    @NonNull
    private String[][] loadTopicsFromDB() {
        String[] id = new String[0];
        String[] name = new String[0];
        this.topicsData[1] = new String[0];
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(getActivity());

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            if (db != null) {
                Cursor cursor = db.query(TasksSQLiteHelper.TABLE_TOPICS, new String[]{"_id, name"}, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    List<String> names = new ArrayList<String>();
                    List<String> ids = new ArrayList<String>();
                    ids.add(cursor.getString(0));
                    names.add(cursor.getString(1));
                    while (cursor.moveToNext()) {
                        ids.add(cursor.getString(0));
                        names.add(cursor.getString(1));
                    }

                    id = ids.toArray(new String[0]);
                    name = names.toArray(new String[0]);
                }
                cursor.close();
            }
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_LONG);
            toast.show();
            databaseHelper.close();
        }

        return new String[][]{id, name};
    }

    interface OnItemClickListener {
        void onItemClick(String id);
    }
    /* ------------------------------------------------------------------------------------------ */
    /**
        The adapter class receives raw data and inserts every entry into a layout (CardView)
     */
    private class TopicsCardsAdapter extends RecyclerView.Adapter<TopicsCardsAdapter.ViewHolder> {

        private String[] topicIds;
        private String[] topicNames;
        private final OnItemClickListener listener;

        public TopicsCardsAdapter(String[][] data, OnItemClickListener listener) {
            this.topicIds = data[0];
            this.topicNames = data[1];
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return topicNames.length;
        }

        /**
         * Creates an empty layout for each entry and passes it to onBindViewHolder().
         * Called for each entry from the data set.
         */
        @Override
        public TopicsCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_topic, parent, false);

            return new ViewHolder(cardView);
        }

        /**
         * Receives a holder (an empty layout) and fills it with data from the data set
         */

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            CardView cardView = holder.cardView;
            TextView topicName = cardView.findViewById(R.id.card_topic_name);
            topicName.setText(topicNames[position]);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(topicIds[position]);
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

}
