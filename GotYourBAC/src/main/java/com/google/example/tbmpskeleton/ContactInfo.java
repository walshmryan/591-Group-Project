package com.google.example.tbmpskeleton;

import java.text.DateFormat;
import java.util.Date;

public class ContactInfo {
    private String userId;
    private String name;
    private String number;


    public ContactInfo(){};

    public ContactInfo(String userId, String name, String number) {
        this.userId = userId;
        this.name = name;
        this.number = number;
    };

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getUserId() { return userId; }
}