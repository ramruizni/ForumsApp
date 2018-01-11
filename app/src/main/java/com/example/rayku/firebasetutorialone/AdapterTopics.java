package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public final class AdapterTopics extends RecyclerView.Adapter<AdapterTopics.ViewHolder> {

    private ArrayList<String> topicTitles, lastMessages;
    private String forumID;

    AdapterTopics(ArrayList<String> topicTitles, ArrayList<String> lastMessages,
                  String forumID ) {
        this.topicTitles = topicTitles;
        this.lastMessages = lastMessages;
        this.forumID = forumID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic, parent, false);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityTopic.class);
                intent.putExtra("forumID", forumID);
            }
        });


        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = topicTitles.get(position);
        String lastMessage = lastMessages.get(position);
        holder.setTitle(title);
        holder.setLastMessage(lastMessage);
        holder.setRating();
        holder.setImage();
    }

    @Override
    public int getItemCount() {
        return topicTitles.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView lastMessageView;
        private final TextView ratingView;
        private final ImageView imageView;

        static ViewHolder newInstance(View itemView) {
            TextView titleView = itemView.findViewById(R.id.titleView);
            TextView lastMessageView = itemView.findViewById(R.id.lastMessageView);
            TextView ratingView = itemView.findViewById(R.id.ratingView);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            return new ViewHolder(itemView, titleView, lastMessageView, ratingView, imageView);
        }

        private ViewHolder(View itemView, TextView titleView, TextView lastMessageView,
                           TextView ratingView, ImageView imageView) {
            super(itemView);
            this.titleView = titleView;
            this.lastMessageView = lastMessageView;
            this.ratingView = ratingView;
            this.imageView = imageView;
        }

        public void setTitle(CharSequence text) {
            titleView.setText(text);
        }
        public void setLastMessage(CharSequence text){ lastMessageView.setText(text); }

        void setImage(){

            String url = "https://firebasestorage.googleapis.com/v0/b/fir-tutorialone-157a7.appspot.com/o/profileImages%2Fprofile1.jpg?alt=media&token=74e37d47-2493-4723-becb-e823f50d18c2";
            GlideApp
                    .with(itemView.getRootView().getContext())
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.common_google_signin_btn_icon_light_normal_background)
                    .into(imageView);

        }

        void setRating() {
            ratingView.setText("725");
        }

    }
}