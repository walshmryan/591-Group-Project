package com.undergrads.ryan;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.google.example.tbmpskeleton.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.undergrads.ryan.R;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Calculate the blood alcohol content
 *  level of a user based on their weight and
 *  gender
 */
public class BacActivity extends Fragment{
    private ImageButton btnBeerMinus;
    private ImageButton btnHardAlcoholMinus;
    private ImageButton btnWineMinus;
    private ImageButton btnWinePlus;
    private ImageButton btnBeerPlus;
    private ImageButton btnHardAlcoholPlus;
    private TextView txtNumDrinks;
    private TextView txtBAC;
    private TextView txtBAClabel;

    private TextView wineCounter;
    private TextView beerCounter;
    private TextView hardCounter;

    private int totalHard;
    private int totalWine;
    private int totalBeer;
    private int total;
    private Stopwatch stopwatch;


    private double bac=0;
    private int gender;
    private int weight;
    private final int FEMALE = 1;
    private final int MALE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bac, container, false);

        // initialize values
        btnBeerMinus = (ImageButton) v.findViewById(R.id.btnBeerMinus);
        btnBeerPlus = (ImageButton) v.findViewById(R.id.btnBeerPlus);
        btnHardAlcoholMinus = (ImageButton) v.findViewById(R.id.btnHardAlcoholMinus);
        btnHardAlcoholPlus = (ImageButton) v.findViewById(R.id.btnHardAlcoholPlus);
        btnWineMinus = (ImageButton) v.findViewById(R.id.btnWineMinus);
        btnWinePlus = (ImageButton) v.findViewById(R.id.btnWinePlus);
        btnWineMinus = (ImageButton) v.findViewById(R.id.btnWineMinus);

        txtNumDrinks = (TextView) v.findViewById(R.id.txtNumDrinks);
        txtBAC = (TextView) v.findViewById(R.id.txtBAC);
        txtBAClabel = (TextView)v.findViewById(R.id.txtBAC_label);

        wineCounter = (TextView) v.findViewById(R.id.wineCounter);
        beerCounter = (TextView) v.findViewById(R.id.beerCounter);
        hardCounter = (TextView) v.findViewById(R.id.hardCounter);

        // initialize to zero on creation, check DB after if entries in there
        totalHard = 0;
        totalWine = 0;
        totalBeer = 0;
        total = 0;

        final String uId = getUid(); //get current user id

        // access the database and get the users sex and weight
        FirebaseDatabase.getInstance().getReference().child("users").child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // this value won't change so we are just going to listen for a single value event
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        Users user = dataSnapshot.getValue(Users.class);
                        // String currentUser = user.username;
                        weight = user.getWeight();
                        String g = user.getGender();
                        if (g.equals("Male")){
                            gender = 0;
                        }else{
                            gender = 1;
                        }


                        // once we got weight and gender, make another DB call
                        // get current values from the database in case user already entered info
                        FirebaseDatabase.getInstance().getReference().child("users").child(uId).child("drink-totals")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Drinks drinkInfo = dataSnapshot.getValue(Drinks.class);
                                        //check to see if there are any entries, if not go with defaults
                                        // only count drink info if within a 12 hour time span
                                        if(drinkInfo != null && !dateExpired(drinkInfo.getTimestamp(), 1)) {
                                            totalHard = drinkInfo.getTotalHard();
                                            totalWine = drinkInfo.getTotalWine();
                                            totalBeer = drinkInfo.getTotalBeer();
                                            setTotal();

                                            // set number of drinks here in case we loaded some from DB and set BAC
                                            txtNumDrinks.setText(String.valueOf(getTotal()));
                                            Log.i("BAC", weight + "");
                                            Log.i("BAC", gender + "");
                                            bac = calculateBAC(weight, gender);
                                            setBACtxtColor(bac);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("error","could not load drink info");
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("error","could not load user info");
                    }
                });


        // clicking + or - causes BAC and drink totals to be reset
        // if a categeory has 0 drinks pressing - will not do anything
        btnWineMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalWine - 1  >= 0){
                    totalWine--;
                    setTotal();
                    calculateBAC(weight,gender);
                }
        }});
        btnWinePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                totalWine += 1;
                setTotal();
                calculateBAC(weight,gender);
            }});

        btnHardAlcoholMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalHard - 1  >= 0){
                    totalHard--;
                    setTotal();
                    calculateBAC(weight,gender);
                }
            }});
        btnHardAlcoholPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                totalHard += 1;
                setTotal();
                calculateBAC(weight,gender);
            }});

        btnBeerMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalBeer - 1  >= 0){
                    totalBeer--;
                    setTotal();
                    calculateBAC(weight,gender);
                }
            }});
        btnBeerPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                totalBeer+=1;
                setTotal();
                calculateBAC(weight,gender);
            }});

        return v;
    }


    // returns the constant based on if person is male (0) or female (1)
    public double genderToGenderConstant(int gender){
        double mConstant = 0.73;
        double fConstant = 0.66;

        if (gender == FEMALE) {
            return fConstant;
        } else if (gender == MALE) {
            return mConstant;
        }
        return 0.0;
    }

    // percentage of alcohol * num ounces * num of glasses
    public double gOfWineConsumed(){return (5*.125*totalWine);}
    public double gOfHardAlcoholConsumed() {return (1.5*.4*totalHard);}
    public double gOfBeerConsumed(){
        return (12*.06*totalBeer);
    }

    public double calculateBAC(double weight, int gender){

        // updates drink totals to DB
        FirebaseCall fb = new FirebaseCall();
        fb.updateDrinkTotals(totalHard, totalWine, totalBeer);

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

        txtBAC.setText(dcmFormatter.format(bac) + "%");
        setBACtxtColor(bac);
        return (bac);

    }

    public int getTotal(){
        return total;
    }

    public double getTotalTimeInHrs(){
        double time = stopwatch.elapsedTime();
        return (time/3600);
    }
    public void setTotal(){
        if (total <= 1){
            // if there is one drink consumed start the stopwatch
            stopwatch = new Stopwatch();
        }

        // total number of drinks consumed
        total =  totalBeer + totalHard + totalWine;

        // set totals
        txtNumDrinks.setText(Integer.toString(total));
        wineCounter.setText(Integer.toString(totalWine));
        beerCounter.setText(Integer.toString(totalBeer));
        hardCounter.setText(Integer.toString(totalHard));


    }
    // gets current user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setBACtxtColor(double bac)
    {
        // warn users based on their bac how drunk they are
        if (bac <= .07){
            txtBAC.setTextColor(getResources().getColor(R.color.green));
        }else if (bac > .07 && bac <= .19){
            txtBAC.setTextColor(getResources().getColor(R.color.yellow));
        }else{
            txtBAC.setTextColor(getResources().getColor(R.color.red));
        }
    }

    // checks to see if the timestamp provided is over the hour thresholed proved
    public boolean dateExpired(Date timestamp, int hourThreshold) {

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
}