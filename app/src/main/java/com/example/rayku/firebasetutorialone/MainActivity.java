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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    EditText logEmail, logPwd;

    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logEmail = findViewById(R.id.logEmail);
        logPwd = findViewById(R.id.logPwd);

        findViewById(R.id.toSignUp).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        progressBar2 = findViewById(R.id.progressBar2);


        mAuth = FirebaseAuth.getInstance();
    }

    private void userLogin(){
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

        progressBar2.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar2.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case(R.id.loginBtn):
                userLogin();
                break;
        }
    }
}
