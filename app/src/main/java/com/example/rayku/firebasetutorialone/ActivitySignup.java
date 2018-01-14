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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class ActivitySignup extends AppCompatActivity implements View.OnClickListener{

    private EditText signEmail, signPwd;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signEmail = findViewById(R.id.signEmail);
        signPwd = findViewById(R.id.signPwd);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.signUpBtn).setOnClickListener(this);
        findViewById(R.id.toLogIn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.signUpBtn):
                registerUser();
                break;
            case(R.id.toLogIn):
                finish();
                startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                break;
        }
    }

    private void registerUser(){

        progressBar.setVisibility(View.VISIBLE);

        String email = signEmail.getText().toString();
        String password = signPwd.getText().toString();

        if(email.isEmpty()){
            signEmail.setError("Email is required");
            signEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signEmail.setError("Enter a valid email");
            signEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signPwd.setError("Password is required");
            signPwd.requestFocus();
            return;
        }

        if(password.length()<6){
            signPwd.setError("Minimum length of password should be 6");
            signPwd.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
