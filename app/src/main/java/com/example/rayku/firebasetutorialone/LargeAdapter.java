package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

public final class LargeAdapter extends RecyclerView.Adapter<LargeAdapter.ViewHolder> {

    private final List<String> items;
    private final String forumTitle;

    LargeAdapter(List<String> items, String forumTitle) {
        this.items = items;
        this.forumTitle = forumTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TopicActivity.class);
                intent.putExtra("forumTitle", forumTitle);
                intent.putExtra("topicTitle", "topic0");
                view.getContext().startActivity(intent);
            }
        });

        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = items.get(position);
        holder.setText(text);
        holder.setRating();




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView ratingView;
        Random rand;

        public static ViewHolder newInstance(View itemView) {
            TextView textView = itemView.findViewById(R.id.titleView);
            TextView ratingView = itemView.findViewById(R.id.ratingView);
            return new ViewHolder(itemView, textView, ratingView);
        }

        private ViewHolder(View itemView, TextView textView, TextView ratingView) {
            super(itemView);
            this.textView = textView;
            this.ratingView = ratingView;
        }

        public void setText(CharSequence text) {
            textView.setText(text);
        }
        public void setRating(){
            rand = new Random();
            ratingView.setText(Integer.toString(rand.nextInt(10000))); }
    }
}