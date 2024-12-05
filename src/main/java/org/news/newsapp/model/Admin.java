package org.news.newsapp.model;

import org.bson.Document;

/**
 * @Class: Admin
 * @Author: Senuli Wickramage
 * @Description:
 * The Admin class represents an administrator user in the application. It extends the User class, inheriting all
 * properties and behaviors of a user, with the added role of "ADMIN". This class provides a method to convert
 * the Admin object into a MongoDB-compatible Document for storage.
 */
public class Admin extends User {

    /**
     * Constructor to initialize an Admin object with name, email, password, and type set to "ADMIN".
     *
     * @param name The name of the admin.
     * @param email The email of the admin.
     * @param password The password for the admin.
     */
    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
    }

    /**
     * Converts the Admin object into a MongoDB-compatible Document.
     *
     * @return A Document representation of the Admin object, containing name, email, password, and type.
     */
    @Override
    public Document toDocument() {
        return new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password)
                .append("type", this.type);
    }
}
