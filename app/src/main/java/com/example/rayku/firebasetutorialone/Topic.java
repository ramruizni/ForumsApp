package com.example.rayku.firebasetutorialone;

public class Topic {

    public String title;
    public String lastMessage;
    public int rating;
    public String ID;

    Topic(String title, String lastMessage, int rating, String ID){
        this.title = title;
        this.lastMessage = lastMessage;
        this.rating = rating;
        this.ID = ID;
    }

}
