package com.pixelworks.roam;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String firstName;
    private String lastName;
    private String description;
    private int id;
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

    public User(String userName, int id) {
        this.userName = userName;
        this.id = id;
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

    public int getId() { return id; }

    public Post[] getPosts() {
        return posts;
    }

    @Override
    public String toString() {
        if(firstName == null && lastName == null) {
            return userName;
        }
        else {
            return String.format("%s (%s %s)",
                    userName,
                    firstName,
                    lastName);
        }
    }
}
