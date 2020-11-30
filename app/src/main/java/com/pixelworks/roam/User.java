package com.pixelworks.roam;

import java.io.Serializable;

//Class represents attributes and behaviours of a User
public class User implements Serializable {
    //Attributes
    private String userName;
    private String firstName;
    private String lastName;
    private String description;
    private int id;
    private Post[] posts;

    //initializes attributes
    public User(String userName, String firstName, String lastName, String description) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    public User(String userName, String firstName, String lastName, String description, int id, Post[] posts) {
        this(userName, firstName, lastName, description);
        this.id = id;
        this.posts = posts;
    }

    public User(String userName, int id) {
        this.userName = userName;
        this.id = id;
    }

    //Retrieves username
    public String getUserName() {
        return userName;
    }

    //Retrieves first name
    public String getFirstName() {
        return firstName;
    }

    //Retrieves last name
    public String getLastName() {
        return lastName;
    }

    //Retrieves description
    public String getDescription() {
        return description;
    }

    //Retrieves ID
    public int getId() { return id; }

    //Retrieves posts
    public Post[] getPosts() {
        return posts;
    }

    //Handles toString behaviour.
    @Override
    public String toString() {
        //Some cases may have null first and last names, adjust output based on available attributes.
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
