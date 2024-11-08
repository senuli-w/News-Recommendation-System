package org.news.newsapp.model;

import org.bson.Document;

public class User {
    private String name;
    private String email;
    private String password;

    public User(){}

    public User(String name,String email, String password) {
        setName(name);
        setEmail(email);
        this.password = password;
    }

    public Document toDocument() {
        return new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password);
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

    public boolean setEmail(String email) {
        if (email.trim().isEmpty()) {return false;}
        if (email.contains(" ")) {return false;}
        if (email.split("@").length != 2){return false;}
        if (!email.split("@")[1].contains(".")){return false;}
        this.email = email;
        return true;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        if (password.trim().isEmpty()){return false;}
        this.password = password;
        return true;
    }
}
