package com.example.rayku.firebasetutorialone;

public class Message {

    public String content;
    public Boolean fromUser;

    Message(String content, boolean fromUser){
        this.content = content;
        this.fromUser = fromUser;
    }

    public String getContent(){ return content; }

}
