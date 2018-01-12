package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ActivityScrolling extends AppCompatActivity
        implements
        FragmentForum.OnFragmentInteractionListener,
        AdapterView.OnClickListener{

    CollapsingToolbarLayout ctl;
    Toolbar toolbar;
    ImageView titleImageView;
    ViewPager viewPager;
    AdapterSectionsPager adapter;
    ArrayList<String> userForumIDs, userForumTitles;
    DatabaseReference databaseRef;
    StorageReference storageRef;
    FloatingActionButton leftBtn, midBtn, rightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        createFakeDatabase();

        viewPager = findViewById(R.id.viewPager);
        ctl = findViewById(R.id.toolbar_layout);
        titleImageView = findViewById(R.id.imageView);
        leftBtn = findViewById(R.id.leftBtn);
        midBtn = findViewById(R.id.midBtn);
        rightBtn = findViewById(R.id.rightBtn);
        toolbar = findViewById(R.id.toolbar);

        leftBtn.setOnClickListener(this);
        midBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_settings:
                        startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
                        break;
                }
                return false;
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("userForums/"+userID);

        userForumIDs = new ArrayList<>();
        userForumTitles = new ArrayList<>();

        adapter = new AdapterSectionsPager(getSupportFragmentManager(), userForumIDs, userForumTitles);
        viewPager.setAdapter(adapter);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    userForumIDs.add(child.getKey());
                    userForumTitles.add(child.child("title").getValue(String.class));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ctl.setTitle(adapter.getPageTitle(position));
                String bgKey = "forumImages/"+userForumIDs.get(position)+".jpg";
                storageRef.child(bgKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            GlideApp.with(getApplicationContext())
                                    .load(uri)
                                    .fitCenter()
                                    .into(titleImageView);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                });
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

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
            case R.id.leftBtn:
                startActivity(new Intent(getApplicationContext(), ActivityInfo.class));
                break;
            case R.id.midBtn:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.rightBtn:
                startActivity(new Intent(getApplicationContext(), ActivityAddForum.class));
                break;
        }
    }

    private void createFakeDatabase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference refUsers = database.getReference("users");
        DatabaseReference refUserForums = database.getReference("userForums");
        DatabaseReference refForumTopics = database.getReference("forumTopics");
        DatabaseReference refTopicMessages = database.getReference("topicMessages");

        refUserForums.setValue(null);
        refForumTopics.setValue(null);
        refTopicMessages.setValue(null);

        HashMap<String, Boolean> userIDs = new HashMap<>();
        HashMap<String, Boolean> forumIDs = new HashMap<>();
        HashMap<String, Boolean> topicIDs = new HashMap<>();
        HashMap<String, Boolean> messageIDs = new HashMap<>();

        userIDs.put("7etZQqIhtyM3PcFwchWXkF57wq33", true);
        userIDs.put("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", true);
        userIDs.put("cSwlNCbnIpWHJi8Uj5FWz5VOHbB3", true);
        refUsers.setValue(userIDs);

        forumIDs.put("-L2auVWUkz58XFqh5OsZ", true);
        forumIDs.put("-L2auVWVgH6_ocD3d99v", true);
        forumIDs.put("-L2auVWVgH6_ocD3d99y", true);
        forumIDs.put("-L2dW61PVO-TIKUd-C8H", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzi", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzj", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzk", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzl", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzm", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzn", true);
        forumIDs.put("-L2dW61R5oR7K4ATKfzo", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrP", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrQ", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrR", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrS", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrT", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrU", true);
        forumIDs.put("-L2dW61SdpyP-qhsfyrV", true);
        forumIDs.put("-L2dW61TcbYvjWIv3wdF", true);
        forumIDs.put("-L2dW61TcbYvjWIv3wdG", true);
        forumIDs.put("-L2dW61TcbYvjWIv3wdH", true);
        forumIDs.put("-L2dW61TcbYvjWIv3wdI", true);
        forumIDs.put("-L2dW61TcbYvjWIv3wdJ", true);

        topicIDs.put("-L2auVWVgH6_ocD3d99t", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99w", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99z", true);
        topicIDs.put("-L2dar4hnx0Lxbe-exQk", true);
        topicIDs.put("-L2dar4iejJ_RcTpZkTP", true);
        topicIDs.put("-L2dar4iejJ_RcTpZkTQ", true);
        topicIDs.put("-L2dar4iejJ_RcTpZkTR", true);
        topicIDs.put("-L2dar4iejJ_RcTpZkTS", true);
        topicIDs.put("-L2dar4iejJ_RcTpZkTT", true);
        topicIDs.put("-L2dar4j1mbP_JtriQHt", true);
        topicIDs.put("-L2dar4j1mbP_JtriQHu", true);
        topicIDs.put("-L2dar4j1mbP_JtriQHv", true);
        topicIDs.put("-L2dar4j1mbP_JtriQHw", true);
        topicIDs.put("-L2dar4j1mbP_JtriQHx", true);
        topicIDs.put("-L2dar4kuNAICIosp7qG", true);
        topicIDs.put("-L2dar4kuNAICIosp7qH", true);
        topicIDs.put("-L2dar4kuNAICIosp7qI", true);
        topicIDs.put("-L2dar4kuNAICIosp7qJ", true);
        topicIDs.put("-L2dar4kuNAICIosp7qK", true);
        topicIDs.put("-L2dar4lQF0wTdY7-5cW", true);
        topicIDs.put("-L2dar4lQF0wTdY7-5cX", true);
        topicIDs.put("-L2dar4lQF0wTdY7-5cY", true);
        topicIDs.put("-L2dar4lQF0wTdY7-5cZ", true);


        messageIDs.put("-L2auVWVgH6_ocD3d99u", true);
        messageIDs.put("-L2auVWVgH6_ocD3d99x", true);
        messageIDs.put("-L2auVWVgH6_ocD3d9A-", true);

        Random rand = new Random();

        //USER FORUMS
        HashMap<String, HashMap<String, Forum>> userForums = new HashMap<>();
        HashMap<String, Forum> forums = new HashMap<>();
        for (String forumID : forumIDs.keySet()) {
            Forum forum = new Forum("Forum" + Integer.toString(rand.nextInt(100)));
            forums.put(forumID, forum);
        }
        for(String userID : userIDs.keySet())
            userForums.put(userID, forums);
        refUserForums.setValue(userForums);

        //FORUM TOPICS
        HashMap<String, HashMap<String, Topic>> forumTopics = new HashMap<>();
        HashMap<String, Topic> topics = new HashMap<>();
        for (String topicID : topicIDs.keySet()) {
            Topic topic = new Topic("Topic" + Integer.toString(rand.nextInt(100)),
                    "WOW", rand.nextInt(10000), topicID);
            topics.put(topicID, topic);
        }
        for(String forumID : forumIDs.keySet())
            forumTopics.put(forumID, topics);
        refForumTopics.setValue(forumTopics);

        //TOPIC MESSAGES
        HashMap<String, HashMap<String, Message>> topicMessages = new HashMap<>();
        HashMap<String, Message> messages = new HashMap<>();
        int counter = 1;
        for (String messageID : messageIDs.keySet()) {
            if(counter==1)
                messages.put(messageID, new Message("7etZQqIhtyM3PcFwchWXkF57wq33", "WOW"));
            if(counter==2)
                messages.put(messageID, new Message("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", "WOW"));
            if(counter==3)
                messages.put(messageID, new Message("cSwlNCbnIpWHJi8Uj5FWz5VOHbB3", "WOW"));
            counter++;
        }
        for(String topicID : topicIDs.keySet())
            topicMessages.put(topicID, messages);
        refTopicMessages.setValue(topicMessages);


        DatabaseReference forumsRef = database.getReference("forums");
        forumsRef.setValue(null);
        HashMap<String, Forum> newForums = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            Forum newForum = new Forum("New Forum" + Integer.toString(rand.nextInt(1000)+100),
                    "This is the space for an upcoming description of a group so what I am doing right now is filling" +
                            "all of this with a bunch of text. Hopefully when somebody sees the demo template they will be able" +
                            "to see this text inside a pretty nice box or something.");
            newForums.put(forumsRef.push().getKey(), newForum);
        }
        forumsRef.setValue(newForums);


    }

}
