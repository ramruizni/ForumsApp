package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ActivityNewTopic extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    EditText titleView, firstMessageView;
    FloatingActionButton camBtn;
    Button createBtn;
    Toolbar toolbar;
    Uri uriTopicImage;
    String forumID;
    private static final int CHOOSE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        firstMessageView = findViewById(R.id.firstMessageView);
        camBtn = findViewById(R.id.camBtn);
        createBtn = findViewById(R.id.createBtn);
        camBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        forumID = getIntent().getExtras().getString("forumID");

    }

    private void createTopic() {
        String title = titleView.getText().toString();
        String firstMessage = firstMessageView.getText().toString();
        if(title.isEmpty()){
            titleView.setError("Name required");
            titleView.requestFocus();
            return;
        }
        if(firstMessage.isEmpty()){
            firstMessageView.setError("First message required");
            firstMessageView.requestFocus();
            return;
        }


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String topicID = dbRef.child("forumTopics/"+forumID).push().getKey();
        Topic newTopic = new Topic(title, firstMessage, 0, topicID);
        dbRef.child("forumTopics/"+forumID+"/"+topicID).setValue(newTopic);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("topicImages/"+topicID+".jpg");
        if(uriTopicImage!=null) storageRef.putFile(uriTopicImage);

        String firstMessageID = dbRef.child("topicMessages/"+topicID).push().getKey();

        Message newMessage = new Message(FirebaseAuth.getInstance().getUid(), firstMessage);
        dbRef.child("topicMessages/"+topicID+"/"+firstMessageID).setValue(newMessage);

        Toast.makeText(this, "Topic creation successful!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uriTopicImage = data.getData();
            try{
                GlideApp.with(this)
                        .load(uriTopicImage)
                        .centerCrop()
                        .into(imageView);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.camBtn:
                showImageChooser();
                break;
            case R.id.createBtn:
                createTopic();
                break;
        }
    }

}
