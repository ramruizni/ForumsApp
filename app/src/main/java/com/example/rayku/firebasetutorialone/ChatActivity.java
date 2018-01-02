package com.example.rayku.firebasetutorialone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView messages = findViewById(R.id.messages);

        ArrayList<String> arrayOfMessages = new ArrayList<>();
        arrayOfMessages.add("Ke onda mi perro");
        arrayOfMessages.add("Ola k aze");
        arrayOfMessages.add("A lo bn?");
        arrayOfMessages.add("No ze lo puedo kreeeeer");
        arrayOfMessages.add("Chao ps");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayOfMessages);
        messages.setAdapter(adapter);

    }
}
