package com.example.rayku.firebasetutorialone;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

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

public class ScrollingActivity extends AppCompatActivity implements ForumFragment.OnFragmentInteractionListener{

    CollapsingToolbarLayout ctl;

    ViewPager viewPager;
    SectionsPagerAdapter adapter;

    ArrayList<String> userForums;

    ImageView titleImageView;

    DatabaseReference databaseRef;
    StorageReference storageRef;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //createFakeDatabase();

        viewPager = findViewById(R.id.viewPager);
        ctl = findViewById(R.id.toolbar_layout);
        titleImageView = findViewById(R.id.imageView);

        storageRef = FirebaseStorage.getInstance().getReference();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "%");
        databaseRef = FirebaseDatabase.getInstance().getReference("userPicks/"+userEmail);


        userForums = new ArrayList<>();

        adapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), userForums);
        viewPager.setAdapter(adapter);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void createFakeDatabase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refForums = database.getReference("forums");

        HashMap<String, HashMap<String, ArrayList<String>>> forums = new HashMap<>();
        HashMap<String, ArrayList<String>> topics = new HashMap<>();
        ArrayList<String> aTopic = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            aTopic.add("message" + Integer.toString(i));
        }

        for (int i = 0; i < 10; i++) {
            topics.put("topic" + Integer.toString(i), aTopic);
        }

        for (int i = 0; i < 10; i++) {
            forums.put("forum" + Integer.toString(i), topics);
        }

        refForums.setValue(forums);

        DatabaseReference refUserPicks = database.getReference("userPicks");

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","%");

        ArrayList<String> picks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            picks.add("forum"+Integer.toString(i));
        }

        HashMap<String, ArrayList<String>> userPicks = new HashMap<>();
        userPicks.put(userEmail, picks);

        refUserPicks.setValue(userPicks);

    }


}
