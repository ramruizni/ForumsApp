package com.example.rayku.firebasetutorialone;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ActivityInfo extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference dbRef, btnRef;
    StorageReference stRef;
    String forumID, forumTitle, description, userID;
    ImageView imageView;
    TextView titleView, descView;
    Toolbar toolbar;
    Button addBtn;

    boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        descView = findViewById(R.id.descView);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        forumID = getIntent().getExtras().getString("forumID");

        stRef = FirebaseStorage.getInstance().getReference();
        String bgKey = "forumImages/"+forumID+".jpg";
        stRef.child(bgKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    GlideApp.with(getApplicationContext())
                            .load(uri)
                            .fitCenter()
                            .into(imageView);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        dbRef = FirebaseDatabase.getInstance().getReference("forums/"+forumID);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                forumTitle = dataSnapshot.child("title").getValue(String.class);
                description = dataSnapshot.child("description").getValue(String.class);
                titleView.setText(forumTitle);
                descView.setText(description);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        userID = FirebaseAuth.getInstance().getUid();

        btnRef = FirebaseDatabase.getInstance().getReference("userForums/"+userID);
        exists = false;
        btnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(forumID).getValue() != null) {
                    exists = true;
                    addBtn.setText("Remove from my favorites");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    public void addForumToFavorites(){
        FirebaseDatabase.getInstance().getReference("userForums/"+userID+"/"+forumID).setValue(true);
    }

    private void removeForumFromFavorites(){
        FirebaseDatabase.getInstance().getReference("userForums/"+userID+"/"+forumID).setValue(null);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.addBtn:
                if(exists){
                    removeForumFromFavorites();
                    addBtn.setText("Add to my favorites");
                    exists = false;
                }else{
                    addForumToFavorites();
                    addBtn.setText("Remove from my favorites");
                    exists = true;
                }
                break;
        }
    }
}
