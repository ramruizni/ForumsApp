package com.example.rayku.firebasetutorialone;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivitySearchForum extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    SearchView searchView;
    AdapterForums adapter;
    ArrayList<Forum> forums;
    ArrayList<String> forumIDs;
    FirebaseDatabase database;
    FloatingActionButton newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        newBtn = findViewById(R.id.newBtn);
        newBtn.setOnClickListener(this);

        forums = new ArrayList<>();
        forumIDs = new ArrayList<>();

        adapter = new AdapterForums(forums, forumIDs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        database = FirebaseDatabase.getInstance();
        DatabaseReference forumsRef = database.getReference("forums");
        forumsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String title = child.child("title").getValue(String.class);
                    String description = child.child("description").getValue(String.class);
                    forums.add(new Forum(title, description));
                    forumIDs.add(child.getKey());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.newBtn:
                Toast.makeText(this, "NEW FORUM", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
