package com.example.rayku.firebasetutorialone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ScrollingActivity extends AppCompatActivity implements ForumFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ViewPager viewPager = findViewById(R.id.viewPager);

        ForumFragment[] frags = new ForumFragment[100];
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), frags);

        try {
            viewPager.setAdapter(adapter);
        } catch (Exception e){
            e.printStackTrace();
        }

        viewPager.setCurrentItem(50);

    }




}
