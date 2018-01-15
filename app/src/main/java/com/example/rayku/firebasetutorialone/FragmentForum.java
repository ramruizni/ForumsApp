package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentForum extends Fragment {

    private OnFragmentInteractionListener mListener;

    HashMap<String, Topic> topics;
    ArrayList<String> topicIDs;
    View rootView;
    RecyclerView recyclerView;
    AdapterTopics adapter;
    Bundle arguments;
    String forumID;
    SearchView searchView;

    public FragmentForum(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topics = new HashMap<>();
        topicIDs = new ArrayList<>();

        arguments = getArguments();
        forumID = arguments.getString("forumID");

        adapter = new AdapterTopics(topics, topicIDs, forumID, getActivity());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("forumTopics/"+forumID);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String lastMessage = dataSnapshot.child("lastMessage").getValue(String.class);
                Integer rating = dataSnapshot.child("rating").getValue(Integer.class);
                topics.put(dataSnapshot.getKey(), new Topic(title, lastMessage, rating, dataSnapshot.getKey()));
                topicIDs.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String lastMessage = dataSnapshot.child("lastMessage").getValue(String.class);
                Integer rating = dataSnapshot.child("rating").getValue(Integer.class);
                topics.put(dataSnapshot.getKey(), new Topic(title, lastMessage, rating, dataSnapshot.getKey()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        searchView = mListener.getSearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = rootView.findViewById(R.id.listView);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

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

    interface OnFragmentInteractionListener {
        SearchView getSearchView();
    }

}
