package com.example.rayku.firebasetutorialone;

public class Forum {

    public String ID;
    public String title;
    public String description;

    Forum(String title){
        this.title=title;
        this.ID = null;
        this.description = null;
    }

    Forum(String title, String description){
        this.title = title;
        this.description = description;
        this.ID = null;
    }

    Forum(String ID, String title, String description){
        this.ID = ID;
        this.title = title;
        this.description = description;
    }
}
