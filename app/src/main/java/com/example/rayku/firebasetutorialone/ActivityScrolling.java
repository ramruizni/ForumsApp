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

    ViewPager viewPager;
    AdapterSectionsPager adapter;

    ArrayList<String> userForums;

    ImageView titleImageView;

    DatabaseReference databaseRef;
    StorageReference storageRef;

    String userEmail;

    FloatingActionButton leftBtn, midBtn, rightBtn;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        createFakeDatabase();
        /*

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

        userForums = new ArrayList<>();
        adapter = new AdapterSectionsPager(getSupportFragmentManager(), userForums);
        viewPager.setAdapter(adapter);

        storageRef = FirebaseStorage.getInstance().getReference();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "%");
        databaseRef = FirebaseDatabase.getInstance().getReference("userPicks/"+userEmail);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    userForums.add(child.getValue().toString());
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
        */
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
        DatabaseReference refForums = database.getReference("forums");
        DatabaseReference refTopics = database.getReference("topics");
        DatabaseReference refMessages = database.getReference("messages");

        DatabaseReference refUserForums = database.getReference("userForums");
        DatabaseReference refForumTopics = database.getReference("forumTopics");
        DatabaseReference refTopicMessages = database.getReference("topicMessages");

        refUsers.setValue(null);
        refForums.setValue(null);
        refTopics.setValue(null);
        refMessages.setValue(null);

        HashMap<String, Boolean> users = new HashMap<>();
        users.put("7etZQqIhtyM3PcFwchWXkF57wq33", true);
        users.put("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", true);
        users.put("cSwlNCbnIpWHJi8Uj5FWz5VOHbB3", true);
        refUsers.setValue(users);

        HashMap<String, Boolean> userIDs = new HashMap<>();
        HashMap<String, Boolean> forumIDs = new HashMap<>();
        HashMap<String, Boolean> topicIDs = new HashMap<>();
        HashMap<String, Boolean> messageIDs = new HashMap<>();

        userIDs.put("7etZQqIhtyM3PcFwchWXkF57wq33", true);
        userIDs.put("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", true);
        userIDs.put("cSwlNCbnIpWHJi8Uj5FWz5VOHbB3", true);
        forumIDs.put("-L2auVWUkz58XFqh5OsZ", true);
        forumIDs.put("-L2auVWVgH6_ocD3d99v", true);
        forumIDs.put("-L2auVWVgH6_ocD3d99y", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99t", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99w", true);
        topicIDs.put("-L2auVWVgH6_ocD3d99z", true);
        messageIDs.put("-L2auVWVgH6_ocD3d99u", true);
        messageIDs.put("-L2auVWVgH6_ocD3d99x", true);
        messageIDs.put("-L2auVWVgH6_ocD3d9A-", true);

        refUsers.setValue(userIDs);
        refForums.setValue(forumIDs);
        refTopics.setValue(topicIDs);
        refMessages.setValue(messageIDs);

        HashMap<String, HashMap<String, Boolean>> userForums = new HashMap<>();
        for(String userID : userIDs.keySet())
            userForums.put(userID, forumIDs);
        refUserForums.setValue(userForums);

        HashMap<String, HashMap<String, Boolean>> forumTopics = new HashMap<>();
        for(String forumID : forumIDs.keySet())
            forumTopics.put(forumID, topicIDs);
        refForumTopics.setValue(forumTopics);

        HashMap<String, HashMap<String, Boolean>> topicMessages = new HashMap<>();
        for(String topicID : topicIDs.keySet())
            topicMessages.put(topicID, messageIDs);
        refTopicMessages.setValue(topicMessages);

        Random rand = new Random();

        for(String forumID : forumIDs.keySet()){
            Forum forum = new Forum("Forum"+Integer.toString(rand.nextInt(10)));
            refForums.child(forumID).setValue(forum);
        }

        for(String topicID : topicIDs.keySet()){
            Topic topic = new Topic("Topic"+Integer.toString(rand.nextInt(10)), "lastMessage");
            refTopics.child(topicID).setValue(topic);
        }

        for(String messageID : messageIDs.keySet()){
            Message message = new Message("ak0NfGgAL1PLJeAdlji5Ve0R7Yn2", "Message"+Integer.toString(rand.nextInt(10)));
            refMessages.child(messageID).setValue(message);
        }



    }

}
