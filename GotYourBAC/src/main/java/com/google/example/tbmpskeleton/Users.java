package com.google.example.tbmpskeleton;

public class Users {
    public String username;
    public String firstName;
    public String lastName;
    public int weight;
    public String gender;

    public Users(String username, String first, String last, int weight, String gender) {
        this.username = username;
        this.firstName = first;
        this.lastName = last;
        this.weight = weight;
        this.gender = gender;
    }
}