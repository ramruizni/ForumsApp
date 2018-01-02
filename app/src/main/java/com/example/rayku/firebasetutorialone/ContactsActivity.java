package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsActivity extends AppCompatActivity {

    ListView contactsList;
    ArrayList<String> contacts;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsList = findViewById(R.id.contactsList);

        contacts = new ArrayList<>();
        contacts.add("alroruni16@gmail.com");
        contacts.add("ramruizni@gmail.com");
        contacts.add("ramruizni@unal.edu.co");

        contactsList.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, contacts));
        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("contactsList");

        HashMap<String, ArrayList> mainDict = new HashMap<>();

        mainDict.put("thisisTheKey1", contacts);
        mainDict.put("thisisAnotherKey", contacts);
        ref.setValue(mainDict);


    }


}
