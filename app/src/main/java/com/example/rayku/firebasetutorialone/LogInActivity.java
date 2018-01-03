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

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText logEmail, logPwd;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logEmail = findViewById(R.id.logEmail);
        logPwd = findViewById(R.id.logPwd);
        progressBar2 = findViewById(R.id.progressBar2);
        findViewById(R.id.toSignUp).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        //createDataBase();
    }

    private void createDataBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("contactsList");

        HashMap<String, HashMap> mainDict = new HashMap<>();
        HashMap<String, ArrayList> contacts = new HashMap<>();
        ArrayList<Message> chat = new ArrayList<>();

        chat.add(new Message("Ke onda mi perro", true));
        chat.add(new Message("Ola k aze", false));
        chat.add(new Message("A lo bn?", false));
        chat.add(new Message("No ze lo puedo kreeeeer", true));
        chat.add(new Message("Chao ps", true));

        contacts.put("alroruni16@gmail%com", chat);
        contacts.put("ramruizni@gmail%com", chat);
        contacts.put("ramruizni@unal%edu%co", chat);

        mainDict.put("ramruizni@gmail%com", contacts);
        mainDict.put("ramruizni@unal%edu%co", contacts);
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
