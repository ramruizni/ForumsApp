package com.example.rayku.firebasetutorialone;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        createFakeDatabase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewPager = findViewById(R.id.viewPager);

        ctl = findViewById(R.id.toolbar_layout);

        userForums = new ArrayList<>();

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "%");

        adapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), userForums);
        viewPager.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("userPicks/"+userEmail);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                downloadImage("backgrounds/background" + Integer.toString(position)+".jpg");
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageScrollStateChanged(int state) { }
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


    public void downloadImage(String bgKey){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();

        ref.child(bgKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageDownloader task = new ImageDownloader();
                try {

                    Bitmap bitmap = task.execute(uri.toString()).get();
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                    ctl.setBackground(bitmapDrawable);
                } catch(Exception e){
                    e.printStackTrace();
                }


            }

        });
    }


}
