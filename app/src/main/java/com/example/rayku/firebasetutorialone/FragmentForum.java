package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FragmentForum extends Fragment {

    private OnFragmentInteractionListener mListener;

    ArrayList<Topic> topics;
    RecyclerView recyclerView;
    AdapterTopics adapter;
    Bundle arguments;
    String forumID;
    SearchView searchView;
    boolean searchViewVisible;
    ViewGroup.MarginLayoutParams recyclerViewLayoutParams;
    private final int SEARCH_PUSH_MARGIN = 80;

    public FragmentForum(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchViewVisible = false;

        topics = new ArrayList<>();

        arguments = getArguments();
        forumID = arguments.getString("forumID");

        adapter = new AdapterTopics(topics, forumID, getActivity());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("forumTopics/"+forumID);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String lastMessage = dataSnapshot.child("lastMessage").getValue(String.class);
                Integer rating = dataSnapshot.child("rating").getValue(Integer.class);
                topics.add(new Topic(title, lastMessage, rating, dataSnapshot.getKey()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String lastMessage = dataSnapshot.child("lastMessage").getValue(String.class);
                Integer rating = dataSnapshot.child("rating").getValue(Integer.class);

                for (int i = 0; i < topics.size(); i++) { //did it like this to make the filter work.
                    if(topics.get(i).ID.equals(dataSnapshot.getKey())){ // will change it when i know how
                        topics.remove(i);                               // to filter HashMaps
                        topics.add(i, new Topic(title, lastMessage, rating, dataSnapshot.getKey()));
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.listView);
        searchView = getView().findViewById(R.id.searchView);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewLayoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();

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

    public void popSearchView(){

        if(searchViewVisible) {

            searchView.setVisibility(View.GONE);

            searchView.setQuery("", false);
            searchView.clearFocus();

            recyclerViewLayoutParams.topMargin = 40;
            searchViewVisible = false;

        }else{

            searchView.setVisibility(View.VISIBLE);
            recyclerViewLayoutParams.topMargin = 120;
            searchViewVisible = true;

            searchView.setIconified(false);
            searchView.requestFocusFromTouch();

            /*
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInputFromWindow(searchView.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            */
        }
    }

    public boolean isSearchViewVisible(){ return searchViewVisible; }

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
    }

}
