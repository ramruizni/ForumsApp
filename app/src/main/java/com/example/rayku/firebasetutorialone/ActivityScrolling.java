package com.example.rayku.firebasetutorialone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    ImageView titleImageView;
    ViewPager viewPager;
    AdapterSectionsPager adapter;
    ArrayList<String> userForumIDs, userForumTitles;
    DatabaseReference userForumsRef;
    StorageReference storageRef;
    FloatingActionButton leftBtn, midBtn, rightBtn, newTopicBtn;
    SearchView searchView;
    boolean searchViewVisible = false;
    ViewGroup.MarginLayoutParams viewPagerLayoutParams;
    private final int SEARCH_PUSH_MARGIN = 80;

    String currForumID;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //createFakeDatabase();

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.rayku.firebasetutorialone", Context.MODE_PRIVATE);

        viewPager = findViewById(R.id.viewPager);
        viewPagerLayoutParams = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
        ctl = findViewById(R.id.toolbar_layout);
        titleImageView = findViewById(R.id.imageView);
        leftBtn = findViewById(R.id.leftBtn);
        midBtn = findViewById(R.id.midBtn);
        rightBtn = findViewById(R.id.rightBtn);
        newTopicBtn = findViewById(R.id.newTopicBtn);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);

        leftBtn.setOnClickListener(this);
        midBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        newTopicBtn.setOnClickListener(this);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_profile:
                        startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
                        break;
                    case R.id.action_favorites:
                        break;
                    case R.id.action_network_settings:
                        if(sharedPreferences.getBoolean("loadImages", true)) {
                            sharedPreferences.edit().putBoolean("loadImages", false).apply();
                            try {
                                GlideApp.with(getApplicationContext())
                                        .load(R.color.colorAccent)
                                        .into(titleImageView);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else
                            sharedPreferences.edit().putBoolean("loadImages", true).apply();
                        break;
                    case R.id.action_log_out:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                        break;
                }
                return false;
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userForumsRef = FirebaseDatabase.getInstance().getReference("userForums/"+userID);

        userForumIDs = new ArrayList<>();
        userForumTitles = new ArrayList<>();

        adapter = new AdapterSectionsPager(getSupportFragmentManager(), userForumIDs, userForumTitles);
        viewPager.setAdapter(adapter);

        userForumsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userForumIDs.add(dataSnapshot.getKey());
                userForumTitles.add(dataSnapshot.child("title").getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                userForumIDs.remove(dataSnapshot.getKey());
                userForumTitles.remove(dataSnapshot.child("title").getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currForumID = userForumIDs.get(position);
                ctl.setTitle(adapter.getPageTitle(position));
                if(sharedPreferences.getBoolean("loadImages", true)) {
                    String bgKey = "forumImages/" + userForumIDs.get(position) + ".jpg";
                    storageRef.child(bgKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                GlideApp.with(getApplicationContext())
                                        .load(uri)
                                        .fitCenter()
                                        .into(titleImageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                }
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

    public SearchView getSearchView(){ return searchView; }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.leftBtn:
                Intent intent = new Intent(this, ActivityInfo.class);
                intent.putExtra("forumID", currForumID);
                startActivity(intent);
                break;
            case R.id.midBtn:
                if(searchViewVisible) {
                    viewPagerLayoutParams.topMargin -= SEARCH_PUSH_MARGIN;
                    searchView.setVisibility(View.GONE);
                    searchViewVisible = false;
                }
                else{
                    searchView.setVisibility(View.VISIBLE);
                    viewPagerLayoutParams.topMargin += SEARCH_PUSH_MARGIN;
                    searchViewVisible = true;
                    searchView.requestFocus();

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert inputMethodManager != null;
                    inputMethodManager.toggleSoftInputFromWindow(searchView.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }
                break;
            case R.id.rightBtn:
                startActivity(new Intent(getApplicationContext(), ActivitySearchForum.class));
                break;
            case R.id.newTopicBtn:
                Intent anIntent = new Intent(this, ActivityNewTopic.class);
                anIntent.putExtra("forumID", currForumID);
                startActivity(anIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(searchViewVisible){
            viewPagerLayoutParams.topMargin -= SEARCH_PUSH_MARGIN;
            searchView.setVisibility(View.GONE);
            searchViewVisible = false;
        } else{
            super.onBackPressed();
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
        HashMap<String, Integer> forumIDs = new HashMap<>();
        HashMap<String, Boolean> topicIDs = new HashMap<>();
        HashMap<String, Boolean> messageIDs = new HashMap<>();

        userIDs.put("7etZQqIhtyM3PcFwchWXkF57wq33", true);
        userIDs.put("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", true);
        userIDs.put("cSwlNCbnIpWHJi8Uj5FWz5VOHbB3", true);
        refUsers.setValue(userIDs);

        forumIDs.put("-L2auVWUkz58XFqh5OsZ", 1);
        forumIDs.put("-L2auVWVgH6_ocD3d99v", 2);
        forumIDs.put("-L2auVWVgH6_ocD3d99y", 3);
        forumIDs.put("-L2dW61PVO-TIKUd-C8H", 4);
        forumIDs.put("-L2dW61R5oR7K4ATKfzi", 5);
        forumIDs.put("-L2dW61R5oR7K4ATKfzj", 6);
        forumIDs.put("-L2dW61R5oR7K4ATKfzk", 7);
        forumIDs.put("-L2dW61R5oR7K4ATKfzl", 8);
        forumIDs.put("-L2dW61R5oR7K4ATKfzm", 9);
        forumIDs.put("-L2dW61R5oR7K4ATKfzn", 10);
        forumIDs.put("-L2dW61R5oR7K4ATKfzo", 11);
        forumIDs.put("-L2dW61SdpyP-qhsfyrP", 12);
        forumIDs.put("-L2dW61SdpyP-qhsfyrQ", 13);
        forumIDs.put("-L2dW61SdpyP-qhsfyrR", 14);
        forumIDs.put("-L2dW61SdpyP-qhsfyrS", 15);
        forumIDs.put("-L2dW61SdpyP-qhsfyrT", 16);
        forumIDs.put("-L2dW61SdpyP-qhsfyrU", 17);
        forumIDs.put("-L2dW61SdpyP-qhsfyrV", 18);
        forumIDs.put("-L2dW61TcbYvjWIv3wdF", 19);
        forumIDs.put("-L2dW61TcbYvjWIv3wdG", 20);
        forumIDs.put("-L2dW61TcbYvjWIv3wdH", 21);
        forumIDs.put("-L2dW61TcbYvjWIv3wdI", 22);
        forumIDs.put("-L2dW61TcbYvjWIv3wdJ", 23);

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
            Forum forum = new Forum("Forum" + Integer.toString(forumIDs.get(forumID)));
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

        for(String forumID : forumIDs.keySet()){ // to sync with the user ones
            Forum newForum = new Forum("Forum" + Integer.toString(forumIDs.get(forumID)),
                    "This is the space for an upcoming description of a group so what I am doing right now is filling " +
                            "all of this with a bunch of text. Hopefully when somebody sees the demo template they will be able " +
                            "to see this text inside a pretty nice box or something.");
            forumsRef.child(forumID).setValue(newForum);
        }


        DatabaseReference userNamesRef = database.getReference("userNames");
        HashMap<String, String> userNames = new HashMap<>();
        userNames.put("cSwlNCbnIpWHJi8Uj5FWz5VOHbB3", "mister Pimples");
        userNames.put("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", "Mogatu");
        userNames.put("7etZQqIhtyM3PcFwchWXkF57wq33", "Oprah");
        userNames.put("2eHWTwChdDcl7qE1WSMnYOx9Ox53", "Brock Obama");
        userNamesRef.setValue(userNamesRef);



    }

}
