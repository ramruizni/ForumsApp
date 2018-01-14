package com.example.rayku.firebasetutorialone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityNewForum extends AppCompatActivity {

    ImageView imageView;
    TextView titleView, descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forum);

        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        descView = findViewById(R.id.descView);



    }
}
