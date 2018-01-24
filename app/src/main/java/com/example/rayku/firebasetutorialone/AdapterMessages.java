package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public final class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolder> {

    private final List<Message> messages;
    private final Context context;
    private final String currentUser;

    AdapterMessages(List<Message> messages, Context context, String currentUser) {
        this.messages = messages;
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recieved, parent, false);

        return ViewHolder.newInstance(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = messages.get(position).content;
        String senderDisplayName = messages.get(position).senderDisplayName;

        holder.setSender(senderDisplayName);
        holder.setContent(content);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).sender.equals(currentUser)) return 1;
        else return 2;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView senderTextView;
        private final TextView contentTextView;
        Context context;

        static ViewHolder newInstance(View itemView, Context context) {
            TextView senderTextView = itemView.findViewById(R.id.senderTextView);
            TextView contentTextView = itemView.findViewById(R.id.contentTextView);
            return new ViewHolder(itemView, senderTextView, contentTextView, context);
        }

        private ViewHolder(View itemView, TextView senderTextView, TextView contentTextView, Context context) {
            super(itemView);
            this.senderTextView = senderTextView;
            this.contentTextView = contentTextView;
            this.context = context;
        }

        public void setSender(CharSequence sender){ senderTextView.setText(sender); }
        public void setContent(CharSequence content) {
            contentTextView.setText(content);
        }
    }
}
