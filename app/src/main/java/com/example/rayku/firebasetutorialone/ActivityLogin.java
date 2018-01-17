package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText logEmail, logPwd;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this, ActivityScrolling.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logEmail = findViewById(R.id.logEmail);
        logPwd = findViewById(R.id.logPwd);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.toSignUp).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void userLogin(){

        progressBar.setVisibility(View.VISIBLE);

        String email = logEmail.getText().toString();
        String password = logPwd.getText().toString();

        if(email.isEmpty()){
            logEmail.setError("Email is required");
            logEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            logEmail.setError("Enter a valid email");
            logEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(password.isEmpty()){
            logPwd.setError("Password is required");
            logPwd.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(password.length()<6){
            logPwd.setError("Minimum length of password should be 6");
            logPwd.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), ActivityScrolling.class));
                } else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toSignUp:
                finish();
                startActivity(new Intent(getApplicationContext(), ActivitySignup.class));
                break;
            case(R.id.loginBtn):
                userLogin();
                break;
        }
    }
}
