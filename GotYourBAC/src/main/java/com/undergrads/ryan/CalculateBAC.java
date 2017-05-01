package com.undergrads.ryan;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Fionnan on 01/05/2017.
 */

public class CalculateBAC
{
    public static int totalHard = 0;
    public static int totalWine = 0;
    public static int totalBeer = 0;
    public static int total = 0;
    public static Stopwatch stopwatch;

    public static int gender;
    public static int weight;
    public static final int FEMALE = 1;
    public static final int MALE = 0;

//    final static String uId = getUid(); //get current user id
//
//    public static int[] getWeightAndGender(){
//        FirebaseDatabase.getInstance().getReference().child("users").child(uId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    // this value won't change so we are just going to listen for a single value event
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user information
//                        Users user = dataSnapshot.getValue(Users.class);
//                        // String currentUser = user.username;
//                        weight = user.getWeight();
//                        String g = user.getGender();
//                        if (g.equals("Male")){
//                            gender = 0;
//                        }else{
//                            gender = 1;
//                        }
//
//
//                        // once we got weight and gender, make another DB call
//                        // get current values from the database in case user already entered info
//                        FirebaseDatabase.getInstance().getReference().child("users").child(uId).child("drink-totals")
//                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                        Drinks drinkInfo = dataSnapshot.getValue(Drinks.class);
//                                        //check to see if there are any entries, if not go with defaults
//                                        // only count drink info if within a 12 hour time span
//                                        if(drinkInfo != null && !dateExpired(drinkInfo.getTimestamp(), 1)) {
//                                            totalHard = drinkInfo.getTotalHard();
//                                            totalWine = drinkInfo.getTotalWine();
//                                            totalBeer = drinkInfo.getTotalBeer();
//                                            setTotal();
//
//                                            // set number of drinks here in case we loaded some from DB and set BAC
//                                            Log.i("BAC", weight + "");
//                                            Log.i("BAC", gender + "");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                        Log.e("error","could not load drink info");
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e("error","could not load user info");
//                    }
//                });
//        int[] result = {weight, gender};
//        return result;
//    }

//    public static String getUid() {
//        return FirebaseAuth.getInstance().getCurrentUser().getUid();
//    }

    public static boolean dateExpired(Date timestamp, int hourThreshold) {

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

    public static void setTotal(){
        if (total <= 1){
            // if there is one drink consumed start the stopwatch
            stopwatch = new Stopwatch();
        }

        // total number of drinks consumed
        total =  totalBeer + totalHard + totalWine;

    }
    public static int getTotal(){
        return total;
    }

    public static double calculateBAC(double weight, int gender){

        // updates drink totals to DB
//        FirebaseCall fb = new FirebaseCall();
//        fb.updateDrinkTotals(totalHard, totalWine, totalBeer);

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

    public static double getTotalTimeInHrs(){
        if(stopwatch == null){
            stopwatch = new Stopwatch();
        }
        double time = stopwatch.elapsedTime();
        return (time/3600);
    }
}
