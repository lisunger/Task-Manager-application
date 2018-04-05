package com.nkanev.taskmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TopicsAllFragment extends TopicsListFragment {


    public TopicsAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView topicsRecycler =
                (RecyclerView) inflater.inflate(R.layout.fragment_all_topics, container, false);

        String[] topicNames = new String[] {"eins", "zwei", "drei", "vier", "sechs"};
        TopicsCardsAdapter adapter = new TopicsCardsAdapter(topicNames);
        topicsRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        topicsRecycler.setLayoutManager(layoutManager);

        return topicsRecycler;
    }

}
