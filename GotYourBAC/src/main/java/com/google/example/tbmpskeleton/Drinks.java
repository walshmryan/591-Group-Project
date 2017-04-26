package com.google.example.tbmpskeleton;

public class Drinks {

    private int totalHard = 0;
    private int totalWine = 0;
    private int totalBeer = 0;

    public Drinks(){};

    public Drinks(int totalHard, int totalWine, int totalBeer) {
        this.totalHard = totalHard;
        this.totalWine = totalWine;
    }
    public int getTotalHard() { return totalHard; }

    public int getTotalWine() { return totalWine; }

    public int getTotalBeer() { return totalBeer; }

    
}