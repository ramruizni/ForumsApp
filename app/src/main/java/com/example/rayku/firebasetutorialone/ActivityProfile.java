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

public class ActivityProfile extends AppCompatActivity implements View.OnClickListener{

    private ImageView profileImage;
    private EditText displayName;
    private Button saveBtn, toChatsBtn;
    private ProgressBar progressBar3;

    private Uri uriProfileImage;
    private String profilePicUrl;

    private FirebaseAuth mAuth;

    private static final int CHOOSE_IMAGE = 101;

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

        profileImage = findViewById(R.id.profileImage);
        displayName = findViewById(R.id.displayName);
        saveBtn = findViewById(R.id.saveBtn);
        progressBar3 = findViewById(R.id.progressBar3);
        toChatsBtn = findViewById(R.id.toChatsBtn);
        profileImage.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        toChatsBtn.setOnClickListener(this);

        progressBar3.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        loadUserInformation();
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                GlideApp.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileImage);
            }
            if (user.getDisplayName() != null) {
                displayName.setText(user.getDisplayName());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.profileImage:
                showImageChooser();
                break;
            case R.id.saveBtn:
                saveUserInformation();
                break;
            case R.id.toChatsBtn:
                finish();
                startActivity(new Intent(getApplicationContext(), ActivityScrolling.class));
                break;
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    private void uploadImageToFirebaseStorage(){

        StorageReference profileImageRef = FirebaseStorage.getInstance()
                .getReference("profileImages/"+
                        mAuth.getCurrentUser()
                        .getEmail()
                        .replace(".", "%")
                        + ".jpg");

        if(uriProfileImage!=null){
            progressBar3.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar3.setVisibility(View.GONE);
                            profilePicUrl = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(getApplicationContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uriProfileImage = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                profileImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
