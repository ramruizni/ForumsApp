package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView profilePic;
    EditText displayName;
    Button saveBtn;

    Uri uriProfileImage;

    ProgressBar progressBar3;

    String profilePicUrl;

    FirebaseAuth mAuth;

    private static final int CHOOSE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profilePic);
        displayName = findViewById(R.id.displayName);
        saveBtn = findViewById(R.id.saveBtn);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.GONE);

        profilePic.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

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
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                profilePic.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage(){

        StorageReference profileImageRef = FirebaseStorage.getInstance()
                .getReference("profilepics/"+System.currentTimeMillis() + ".jpg");

        if(uriProfileImage!=null){
            progressBar3.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar3.setVisibility(View.GONE);
                    profilePicUrl = taskSnapshot.getDownloadUrl().toString();
                    Toast.makeText(getApplicationContext(), "UPLOADED IMAGE BRO", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar3.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void saveUserInformation(){
        String theDisplayName = displayName.getText().toString();

        if(theDisplayName.isEmpty()){
            displayName.setError("Name required");
            displayName.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null && profilePicUrl!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(theDisplayName)
                    .setPhotoUri(Uri.parse(profilePicUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.profilePic:
                showImageChooser();
                break;
            case R.id.saveBtn:
                saveUserInformation();
                break;
        }
    }
}
