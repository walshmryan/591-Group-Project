package com.undergrads.ryan;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Date;

/*

Class that takes in drink totals and calculates BAC for the BAC Activity

*/
public class CalculateBAC
{
    public static int totalHard = 0;
    public static int totalWine = 0;
    public static int totalBeer = 0;
    public static int total = 0;
    public static int weight;
    public static final int FEMALE = 1;
    public static final int MALE = 0;
    public static Stopwatch stopwatch;


    // checks to see if the given time is passed the allotted time expiration based on threshold proved
    public static boolean dateExpired(Date timestamp, int hourThreshold) {

        // adds additional time to timestamp
        long additionMilli = 1000 * 60 * 60 * hourThreshold;
        Date expiredDate = new Date(timestamp.getTime() + additionMilli);

        Log.i("DATE", timestamp.toString());
        Log.i("DATE", expiredDate.toString());
        Log.i("DATE", new Date().toString());

        // if current time is before the expired time
        if (new Date().after(expiredDate)) {
            Log.i("DATE", "current time is after expired time");
            return true;
        } else {
            return false;
        }
    }

    // sets drink totals
    public static void setTotal(){
        if (total <= 1){
            // if there is one drink consumed start the stopwatch
            stopwatch = new Stopwatch();
        }

        // total number of drinks consumed
        total =  totalBeer + totalHard + totalWine;

    }

    // gets drink totals
    public static int getTotal(){
        return total;
    }

    // calculates BAC level based on weight and gender
    public static double calculateBAC(double weight, int gender){

        // % BAC = (A x 5.14 / W x r) – .015 x H
        // http://www.teamdui.com/bac-widmarks-formula/
        double liqWeight = 5.14;
        double avgAlcoholEliminationRate = .015;
        DecimalFormat dcmFormatter = new DecimalFormat("0.##");
        double num = (gOfWineConsumed() + gOfHardAlcoholConsumed() + gOfBeerConsumed())*liqWeight;
        double den = weight*genderToGenderConstant(gender);
        double bac = 0;
        double hrsElapsed = (int)getTotalTimeInHrs();

        if (den != 0){
            bac = (num/den) - (avgAlcoholEliminationRate*hrsElapsed);
        }

        return bac;
    }

    public static double gOfWineConsumed(){return (5*.125*totalWine);}

    public static double gOfHardAlcoholConsumed() {return (1.5*.4*totalHard);}

    public static double gOfBeerConsumed(){
        return (12*.06*totalBeer);
    }

    // gets the correct gender contant based on gender provided from user
    public static double genderToGenderConstant(int gender){
        double mConstant = 0.73;
        double fConstant = 0.66;

        if (gender == FEMALE) {
            return fConstant;
        } else if (gender == MALE) {
            return mConstant;
        }
        return 0.0;
    }

    // gets the total time of the stopwatch currently
    public static double getTotalTimeInHrs(){
        if(stopwatch == null){
            stopwatch = new Stopwatch();
        }
        double time = stopwatch.elapsedTime();
        return (time/3600);
    }
}
