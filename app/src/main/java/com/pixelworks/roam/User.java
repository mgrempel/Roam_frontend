package com.pixelworks.roam;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String description;

    public User(String userName, String firstName, String lastName, String description) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
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
