package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ActivityTopic extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Message> messages;
    AdapterMessages adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref, lastMessageRef;
    Button emojiBtn, attachBtn, sendBtn;
    String currentUser;
    Toolbar toolbar;
    String forumID, topicID, topicTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        recyclerView = findViewById(R.id.recyclerView);
        emojiBtn = findViewById(R.id.emojiBtn);
        attachBtn = findViewById(R.id.attachBtn);
        sendBtn = findViewById(R.id.sendBtn);
        toolbar = findViewById(R.id.toolbar);
        emojiBtn.setOnClickListener(this);
        attachBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        forumID = extras.get("forumID").toString();
        topicID = extras.get("topicID").toString();
        topicTitle = extras.get("topicTitle").toString();

        toolbar.setTitle(topicTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        messages = new ArrayList<>();
        adapter = new AdapterMessages(messages, this, currentUser);
        recyclerView.setAdapter(adapter);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("topicMessages/"+topicID);
        lastMessageRef = database.getReference("forumTopics/"+forumID+"/"+topicID);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String sender = dataSnapshot.child("sender").getValue(String.class);
                String content = dataSnapshot.child("content").getValue(String.class);
                messages.add(new Message(sender, content));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    public void sendMessage(){
        EditText editText = findViewById(R.id.editText);
        String input = editText.getText().toString();

        Message newMessage = new Message(currentUser, input);
        ref.push().setValue(newMessage);
        lastMessageRef.child("lastMessage").setValue(input);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        recyclerView.scrollToPosition(messages.size()-1);
        editText.setText(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.emojiBtn:
                Toast.makeText(this, "Show emojis", Toast.LENGTH_SHORT).show();
                break;
            case R.id.attachBtn:
                Toast.makeText(this, "Show attachments", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sendBtn:
                sendMessage();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
