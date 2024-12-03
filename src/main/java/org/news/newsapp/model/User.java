package org.news.newsapp.model;

/**
 * Class: User
 * Author: Senuli Wickramage
 * Description:
 * The User class serves as the base class for all user types in the application (e.g., Admin, Reader).
 * It contains common properties such as name, email, password, and type, and provides getter and setter methods
 * for managing these properties.
 * This class is extended by specific user types like Admin and Reader, which add functionality tailored to each user type.
 */
public abstract class User {
    protected String name;
    protected String email;
    protected String password;
    protected String type; // The type of the user (e.g., ADMIN, READER)

    /**
     * Constructor to initialize a User object with the specified name, email, password, and type.
     *
     * @param name The name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param type The type of the user (e.g., ADMIN, READER).
     */
    public User(String name,  String email, String password, String type){
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    // getter and setter methods
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
