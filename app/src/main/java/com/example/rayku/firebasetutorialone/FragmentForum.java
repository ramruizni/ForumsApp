package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FragmentForum extends Fragment {

    private OnFragmentInteractionListener mListener;

    ArrayList<String> topicTitles, lastMessages;
    View rootView;
    RecyclerView recyclerView;
    AdapterTopics adapter;
    Bundle arguments;
    String forumID;

    public FragmentForum(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topicTitles = new ArrayList<>();
        lastMessages = new ArrayList<>();

        arguments = getArguments();
        forumID = arguments.getString("forumID");

        adapter = new AdapterTopics(topicTitles, lastMessages, forumID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("forumTopics/"+forumID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    topicTitles.add(child.child("title").getValue(String.class));
                    lastMessages.add(child.child("lastMessage").getValue(String.class));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = rootView.findViewById(R.id.listView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener { }

}