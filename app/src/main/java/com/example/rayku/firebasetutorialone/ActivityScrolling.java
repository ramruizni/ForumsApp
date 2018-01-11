package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ActivityScrolling extends AppCompatActivity
        implements
        FragmentForum.OnFragmentInteractionListener,
        AdapterView.OnClickListener{

    CollapsingToolbarLayout ctl;
    Toolbar toolbar;

    ViewPager viewPager;
    AdapterSectionsPager adapter;

    ArrayList<String> userForumIDs, userForumTitles;

    ImageView titleImageView;

    DatabaseReference databaseRef;
    StorageReference storageRef;

    FloatingActionButton leftBtn, midBtn, rightBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //createFakeDatabase();


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
                String bgKey = "backgrounds/background" + Integer.toString(position)+".jpg";
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

        topicIDs.put("-L2auVWVgH6_ocD3d99t", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99w", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99z", true);

        messageIDs.put("-L2auVWVgH6_ocD3d99u", true);
        messageIDs.put("-L2auVWVgH6_ocD3d99x", true);
        messageIDs.put("-L2auVWVgH6_ocD3d9A-", true);

        Random rand = new Random();

        //USER FORUMS
        HashMap<String, HashMap<String, Forum>> userForums = new HashMap<>();
        HashMap<String, Forum> forums = new HashMap<>();
        for (String forumID : forumIDs.keySet()) {
            Forum forum = new Forum("Forum" + Integer.toString(rand.nextInt(10)));
            forums.put(forumID, forum);
        }
        for(String userID : userIDs.keySet())
            userForums.put(userID, forums);
        refUserForums.setValue(userForums);


        //FORUM TOPICS
        HashMap<String, HashMap<String, Topic>> forumTopics = new HashMap<>();
        HashMap<String, Topic> topics = new HashMap<>();
        for (String topicID : topicIDs.keySet()) {
            Topic topic = new Topic("Topic" + Integer.toString(rand.nextInt(10)), "WOW");
            topics.put(topicID, topic);
        }
        for(String forumID : forumIDs.keySet())
            forumTopics.put(forumID, topics);
        refForumTopics.setValue(forumTopics);

        //TOPIC MESSAGES
        HashMap<String, HashMap<String, Message>> topicMessages = new HashMap<>();
        HashMap<String, Message> messages = new HashMap<>();
        for (String messageID : messageIDs.keySet()) {
            Message message = new Message("7etZQqIhtyM3PcFwchWXkF57wq33", "WOW");
            messages.put(messageID, message);
        }
        for(String topicID : topicIDs.keySet())
            topicMessages.put(topicID, messages);
        refTopicMessages.setValue(topicMessages);


    }

}
