package com.example.rayku.firebasetutorialone;

public class Forum {

    public String title;
    public String description;

    Forum(String title){
        this.title=title;
        this.description = null;
    }

    Forum(String title, String description){
        this.title = title;
        this.description = description;
    }
}
