package com.nkanev.taskmanager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nkanev.taskmanager.database.MockData;
import com.nkanev.taskmanager.database.TasksSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TopicsAllFragment extends Fragment {


    public TopicsAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView topicsRecycler =
                (RecyclerView) inflater.inflate(R.layout.fragment_all_topics, container, false);

        String[] topicNames = MockData.topics;
        topicNames = readTopicsFromDB();

        TopicsCardsAdapter adapter = new TopicsCardsAdapter(topicNames);
        topicsRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        topicsRecycler.setLayoutManager(layoutManager);

        return topicsRecycler;
    }

    private String[] readTopicsFromDB() {
        String[] topicNames = new String[0];
        SQLiteOpenHelper databaseHelper = new TasksSQLiteHelper(getActivity());
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            if (db != null) {
                Cursor cursor = db.query(TasksSQLiteHelper.TABLE_TOPICS, new String[]{"name"}, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    List<String> list = new ArrayList<String>();
                    list.add(cursor.getString(0));
                    while (cursor.moveToNext()) {
                        list.add(cursor.getString(0));
                    }

                    topicNames = list.toArray(new String[0]);
                }
                cursor.close();
            }
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_LONG);
            toast.show();
            databaseHelper.close();
        }

        return topicNames;
    }

    private class TopicsCardsAdapter extends RecyclerView.Adapter<TopicsCardsAdapter.ViewHolder> {

        private String[] topicNames;

        public TopicsCardsAdapter(String[] topicNames) {
            this.topicNames = topicNames;
        }

        @Override
        public int getItemCount() {
            return topicNames.length;
        }

        @Override
        public TopicsCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_topic, parent, false);

            return new ViewHolder(cardView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CardView cardView = holder.cardView;
            TextView topicName = cardView.findViewById(R.id.card_topic_name);
            topicName.setText(topicNames[position]);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CardView cardView;

            public ViewHolder(CardView cardView) {
                super(cardView);
                this.cardView = cardView;
            }

        }
    }

}
