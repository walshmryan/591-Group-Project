package com.google.example.tbmpskeleton;

/**
 * Created by walsh on 3/30/17.
 */

public class Users {
    public String Id;
    public String username;
    public String password;
    public String firstname;
    public String lastname;

    public Users(String username, String password, String first, String last) {
        this.username = username;
        this.password = password;
        this.firstname = first;
        this.lastname = last;

    }

    public String getUsername() {
        return username;
    }
}