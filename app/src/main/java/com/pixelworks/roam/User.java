package com.pixelworks.roam;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String firstName;
    private String lastName;
    private String description;
    private Post[] posts;

    public User(String userName, String firstName, String lastName, String description) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    public User(String userName, String firstName, String lastName, String description, Post[] posts) {
        this(userName, firstName, lastName, description);
        this.posts = posts;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s (%s %s)",
                        userName,
                        firstName,
                        lastName);
    }
}
