package com.example.rayku.firebasetutorialone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrollingActivity extends AppCompatActivity implements ForumFragment.OnFragmentInteractionListener{

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


        ViewPager viewPager = findViewById(R.id.viewPager);

        ArrayList<ForumFragment> frags = new ArrayList<>();

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), frags, 10);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(5);


    }


    private void createFakeDatabase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("forums");

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

        ref.setValue(forums);


    }



}
