package com.pixelworks.roam;

import java.io.Serializable;

public class Post implements Serializable {
    private String title, description;

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", title, description);
    }
}
