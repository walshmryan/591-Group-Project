package com.google.example.tbmpskeleton;

public class Users {
    private String username;
    private String firstName;
    private String lastName;
    private int weight;
    private String gender;
    private ContactInfo ice;

    public Users(){};

    public Users(String username, String first, String last, int weight, String gender) {
        this.username = username;
        this.firstName = first;
        this.lastName = last;
        this.weight = weight;
        this.gender = gender;
    }

    public String getUsername() { return username; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public int getWeight() { return weight; }

    public String getGender() { return gender; }

    public ContactInfo getICE() { return ice; }


}