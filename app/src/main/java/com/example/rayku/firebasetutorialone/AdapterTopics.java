package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public final class AdapterTopics extends RecyclerView.Adapter<AdapterTopics.ViewHolder> {

    private final List<String> items;
    private final String forumTitle;
    private final Context context;

    AdapterTopics(List<String> items, String forumTitle, Context context) {
        this.items = items;
        this.forumTitle = forumTitle;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityTopic.class);
                intent.putExtra("forumTitle", forumTitle);

                TextView titleView = view.findViewById(R.id.titleView);

                intent.putExtra("topicTitle", titleView.getText());
                view.getContext().startActivity(intent);
            }
        });

        return ViewHolder.newInstance(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = items.get(position);
        holder.setText(text);
        holder.setRating();
        holder.setImage();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView ratingView;
        private final ImageView imageView;

        Context context;

        static ViewHolder newInstance(View itemView, Context context) {
            TextView textView = itemView.findViewById(R.id.titleView);
            TextView ratingView = itemView.findViewById(R.id.ratingView);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            return new ViewHolder(itemView, textView, ratingView, imageView, context);
        }

        private ViewHolder(View itemView, TextView textView, TextView ratingView, ImageView imageView,
                           Context context) {
            super(itemView);
            this.textView = textView;
            this.ratingView = ratingView;
            this.imageView = imageView;
            this.context = context;
        }

        public void setText(CharSequence text) {
            textView.setText(text);
        }

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