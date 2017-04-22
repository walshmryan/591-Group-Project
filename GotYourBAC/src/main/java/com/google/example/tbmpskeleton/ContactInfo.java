package com.google.example.tbmpskeleton;

import java.text.DateFormat;
import java.util.Date;

public class ContactInfo {
    private String name;
    private String number;


    public ContactInfo(){};

    public ContactInfo(String name, String number) {
        this.name = name;
        this.number = number;
    };

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}