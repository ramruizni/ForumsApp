package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText logEmail, logPwd;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logEmail = findViewById(R.id.logEmail);
        logPwd = findViewById(R.id.logPwd);
        progressBar2 = findViewById(R.id.progressBar2);
        findViewById(R.id.toSignUp).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("contactsList");

        HashMap<String, ArrayList> mainDict = new HashMap<>();

        ArrayList<String> contacts = new ArrayList<>();

        contacts.add("alroruni16@gmail.com");
        contacts.add("ramruizni@gmail.com");
        contacts.add("ramruizni@unal.edu.co");

        mainDict.put("thisisTheKey1", contacts);
        mainDict.put("thisisAnotherKey", contacts);
        ref.setValue(mainDict);

    }

    private void userLogin(){

        progressBar2.setVisibility(View.VISIBLE);

        String email = logEmail.getText().toString().trim();
        String password = logPwd.getText().toString().trim();

        if(email.isEmpty()){
            logEmail.setError("Email is required");
            logEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            logEmail.setError("Enter a valid email");
            logEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            logPwd.setError("Password is required");
            logPwd.requestFocus();
            return;
        }

        if(password.length()<6){
            logPwd.setError("Minimum length of password should be 6");
            logPwd.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar2.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                } else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toSignUp:
                finish();
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                break;

            case(R.id.loginBtn):
                userLogin();
                break;
        }
    }
}
