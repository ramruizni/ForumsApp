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
import com.google.firebase.database.FirebaseDatabase;

public class ActivitySignup extends AppCompatActivity implements View.OnClickListener{

    private EditText emailTextView, pwdTextView;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailTextView = findViewById(R.id.emailTextView);
        pwdTextView = findViewById(R.id.pwdTextView);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.signUpBtn).setOnClickListener(this);
        findViewById(R.id.toLogInTextView).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.signUpBtn):
                registerUser();
                break;
            case(R.id.toLogInTextView):
                finish();
                startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                break;
        }
    }

    private void registerUser(){

        progressBar.setVisibility(View.VISIBLE);

        String email = emailTextView.getText().toString();
        String password = pwdTextView.getText().toString();

        if(email.isEmpty()){
            emailTextView.setError("Email is required");
            emailTextView.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTextView.setError("Enter a valid email");
            emailTextView.requestFocus();
            return;
        }

        if(password.isEmpty()){
            pwdTextView.setError("Password is required");
            pwdTextView.requestFocus();
            return;
        }

        if(password.length()<6){
            pwdTextView.setError("Minimum length of password should be 6");
            pwdTextView.requestFocus();
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
