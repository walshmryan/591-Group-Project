package com.google.example.tbmpskeleton;

import com.google.firebase.auth.FirebaseAuth;

public class Users {
    public String username;
    public String firstName;
    public String lastName;
    public int weight;
    public String gender;

    public Users(){};

    public Users(String username, String first, String last, int weight, String gender) {
        this.username = username;
        this.firstName = first;
        this.lastName = last;
        this.weight = weight;
        this.gender = gender;
    }

}