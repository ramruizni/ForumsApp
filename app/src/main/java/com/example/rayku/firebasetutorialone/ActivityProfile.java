package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ActivityProfile extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private EditText nameTextView;
    private Button saveBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private static final int CHOOSE_IMAGE = 101;
    private FloatingActionButton camBtn;
    private Toolbar toolbar;
    private String userID;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        saveBtn = findViewById(R.id.saveBtn);
        progressBar = findViewById(R.id.progressBar);
        camBtn = findViewById(R.id.camBtn);
        saveBtn.setOnClickListener(this);
        camBtn.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        loadUserInformation();
    }

    private void loadUserInformation() {

        StorageReference imageRef = storage.getReference("profileImages/"+userID+".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .into(imageView);
            }
        });

        DatabaseReference displayNameRef = database.getReference("users/"+userID);
        displayNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameTextView.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
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
            Uri imageUri = data.getData();
            try{
                GlideApp.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(imageView);

                StorageReference ref = FirebaseStorage.getInstance().getReference("profileImages/"+mAuth.getUid()+".jpg");

                if(imageUri!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    ref.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void saveUserInformation(){

        String theDisplayName = nameTextView.getText().toString();

        if(theDisplayName.isEmpty()){
            nameTextView.setError("Name required");
            nameTextView.requestFocus();
            return;
        }

        DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference("users/"+userID);
        nameRef.setValue(theDisplayName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Information updated!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.camBtn:
                showImageChooser();
                break;
            case R.id.saveBtn:
                saveUserInformation();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
