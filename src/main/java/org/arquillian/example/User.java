package org.arquillian.example;

/**
 * Created by hemani on 12/1/16.
 */
public class User {
    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}