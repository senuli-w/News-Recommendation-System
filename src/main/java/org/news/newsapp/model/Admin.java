package org.example.diagramnewsrecommendation.model;

import org.bson.Document;
import org.example.diagramnewsrecommendation.model.User;

public class Admin extends User implements Runnable{
    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
    }

    @Override
    public void run() {

    }

    public void addAccount(){}
    public void deleteAccount(){}
    public void addAdminAccount(){}
    public void deleteAdminAccount(){}

    public Document toDocument() {
        return new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password)
                .append("type", this.type);
    }
}
