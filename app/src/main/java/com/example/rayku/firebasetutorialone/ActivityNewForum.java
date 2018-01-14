package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityNewForum extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    TextView titleView, descView;
    Toolbar toolbar;
    private Uri uriForumImage;
    Button createBtn;
    FloatingActionButton camBtn;

    private static final int CHOOSE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forum);

        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        descView = findViewById(R.id.descView);
        createBtn = findViewById(R.id.createBtn);
        camBtn = findViewById(R.id.camBtn);
        toolbar = findViewById(R.id.toolbar);

        createBtn.setOnClickListener(this);
        camBtn.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GlideApp.with(this)
                .load(R.drawable.the_forum_bg)
                .centerCrop()
                .into(imageView);
    }

    public void createForum(){
        String title = titleView.getText().toString();
        String desc = descView.getText().toString();
        if(title.isEmpty()){
            titleView.setError("Title required");
            titleView.requestFocus();
            return;
        }
        if(desc.isEmpty()){
            descView.setError("Description required");
            descView.requestFocus();
            return;
        }
        // space to upload data and image
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uriForumImage = data.getData();
            try{
                GlideApp.with(this)
                        .load(uriForumImage)
                        .centerCrop()
                        .into(imageView);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.createBtn:
                createForum();
                break;
            case R.id.camBtn:
                showImageChooser();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
