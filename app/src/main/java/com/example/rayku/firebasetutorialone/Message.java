package com.example.rayku.firebasetutorialone;

public class Message {

    public String sender;
    public String content;
    public String senderDisplayName;

    Message(String sender, String content){
        this.sender = sender;
        this.content = content;
        senderDisplayName = null;
    }
    Message(String sender, String content, String senderDisplayName){
        this.sender = sender;
        this.content = content;
        this.senderDisplayName = senderDisplayName;
    }

}
