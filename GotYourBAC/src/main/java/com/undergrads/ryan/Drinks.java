package com.undergrads.ryan;

import java.util.Date;

/*

Class that keeps track of total drink values and the time they were recorded

*/
public class Drinks {

    private int totalHard = 0;
    private int totalWine = 0;
    private int totalBeer = 0;
    private Date timestamp;

    public Drinks(){};

    public Drinks(int totalHard, int totalWine, int totalBeer) {
        this.totalHard = totalHard;
        this.totalWine = totalWine;
        this.totalBeer = totalBeer;
        this.timestamp = new Date();
    }
    public int getTotalHard() { return totalHard; }

    public int getTotalWine() { return totalWine; }

    public int getTotalBeer() { return totalBeer; }

    public Date getTimestamp() { return timestamp; }

    
}