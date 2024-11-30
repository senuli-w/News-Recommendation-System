package org.example.diagramnewsrecommendation.model;

import org.bson.Document;

public abstract class User {
    protected String name;
    protected String email;
    protected String password;
    protected String type;

    public User(String name,  String email, String password, String type){
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

}
