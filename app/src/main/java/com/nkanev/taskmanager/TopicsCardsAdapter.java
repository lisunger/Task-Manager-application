package com.nkanev.taskmanager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by n.kanev on 05-Apr-18.
 */

public class TopicsCardsAdapter extends RecyclerView.Adapter<TopicsCardsAdapter.ViewHolder> {

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }

    }
}
