package com.example.rayku.firebasetutorialone;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener{

    TabLayout tabLayout;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;

    BlankFragment[] frags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        frags = new BlankFragment[5];

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), frags);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(2);
        tabLayout.setupWithViewPager(viewPager);


    }




}
