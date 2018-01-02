package com.example.rayku.firebasetutorialone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();

        Map<String, String> values = new HashMap<>();

        values.put("name", "rayku");

        DBref.push().setValue(values, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if(databaseError==null){
                    Log.i("Info", "Save successful");
                } else Log.i("Info", "Save failed");
            }
        });

    }
}
