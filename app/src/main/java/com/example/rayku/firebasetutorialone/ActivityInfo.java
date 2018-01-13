package com.example.rayku.firebasetutorialone;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ActivityInfo extends AppCompatActivity {

    DatabaseReference dbRef;
    StorageReference stRef;
    String forumID, forumTitle, description;
    ImageView imageView;
    TextView titleView, descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        descView = findViewById(R.id.descView);

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
                //Toast.makeText(getApplicationContext(), forumTitle, Toast.LENGTH_SHORT).show();
                titleView.setText(forumTitle);
                descView.setText(description);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });



    }
}
