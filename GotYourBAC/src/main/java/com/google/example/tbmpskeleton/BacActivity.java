package com.google.example.tbmpskeleton;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.undergrads.ryan.R;

import java.text.DecimalFormat;


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
    private int totalHard = 0;
    private int totalWine = 0;
    private int totalBeer = 0;
    private int total = 0;
    private Stopwatch stopwatch;
    private DatabaseReference mUserReference;
    private DatabaseReference mUserKey;
    private int gender;
    private int weight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bac, container, false);
//        initialize values
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

        // disable minus buttons for start
        btnBeerMinus.setEnabled(false);
        btnHardAlcoholMinus.setEnabled(false);
        btnWineMinus.setEnabled(false);


        String uId = getUid(); //get current user id

//        access the database and get the users sex and weight
        FirebaseDatabase.getInstance().getReference().child("users").child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    this value won't change so we are just going to listen for a single value event
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        Users user = dataSnapshot.getValue(Users.class);
                        weight = user.weight;
                        String g = user.gender;
                        if (g=="Male"){ //set the gender to 0 if its a man else 1
                            gender = 0;
                        }else{
                            gender = 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

//      when a user adds a drink calculate the total
//        number of drinks and new bac and enable
//        the corresponding minus buttons
//        when a user minuses a drink check to see if you need to
//        disable the minus button and calculate the total and
//        the BAC
        btnWineMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                totalWine -= 1;
                if (totalWine == 0){
                    btnWineMinus.setEnabled(false);
                }
                setTotal();
                txtNumDrinks.setText(String.valueOf(getTotal()));
                calculateBAC(weight,gender);

        }});
        btnWinePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalWine == 0){
                    btnWineMinus.setEnabled(true);
                }
                totalWine += 1;
                setTotal();
                txtNumDrinks.setText(String.valueOf(getTotal()));
                calculateBAC(weight,gender);

            }});

        btnHardAlcoholMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                totalHard-= 1;
                if (totalHard == 0){
                    btnHardAlcoholMinus.setEnabled(false);
                }
                setTotal();
                txtNumDrinks.setText(String.valueOf(getTotal()));
                calculateBAC(weight,gender);

            }});
        btnHardAlcoholPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalHard==0){
                    btnHardAlcoholMinus.setEnabled(true);
                }
                totalHard += 1;
                setTotal();
                txtNumDrinks.setText(String.valueOf(getTotal()));
                calculateBAC(weight,gender);
            }});

        btnBeerMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                totalBeer-=1;
                if (totalBeer==0){
                    btnBeerMinus.setEnabled(false);
                }
                setTotal();
                txtNumDrinks.setText(String.valueOf(getTotal()));
                calculateBAC(weight,gender);
            }});
        btnBeerPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalBeer==0){
                    btnBeerMinus.setEnabled(true);
                }
                totalBeer+=1;
                setTotal();
                txtNumDrinks.setText(String.valueOf(getTotal()));
                calculateBAC(weight,gender);
            }});
        return v;
    }


    public double genderToGenderConstant(int gender){
        double mConstant = 0.73;
        double fConstant = 0.66;
        int female = 1;
        int male = 0;

        if (gender == female){
            return fConstant;
        }
        if (gender == male){
            return mConstant;
        }
        return 0.0;
    }

//        percentage of alcohol * num ounces * num of glasses
    public double gOfWineConsumed(){return (5*.125*totalWine);}
    public double gOfHardAlcoholConsumed() {return (1.5*.4*totalHard);}
    public double gOfBeerConsumed(){
        return (12*.06*totalBeer);
    }

    public void calculateBAC(double weight, int gender){
//        % BAC = (A x 5.14 / W x r) – .015 x H
//          http://www.teamdui.com/bac-widmarks-formula/
        double liqWeight = 5.14;
        double avgAlcoholEliminationRate = .015;
        DecimalFormat dcmFormatter = new DecimalFormat("0.##");
        double num = (gOfWineConsumed() + gOfHardAlcoholConsumed() + gOfBeerConsumed())*liqWeight;
        double den = weight*genderToGenderConstant(gender);
        double bac = 0;
        double hrsElapsed = (int)getTotalTimeInHrs();
//        Log.i("stopwatch", "calculateBAC: " + stopwatch.elapsedTime());

        if (den != 0){
            bac = (num/den) - (avgAlcoholEliminationRate*hrsElapsed);
        }

        txtBAC.setText(dcmFormatter.format(bac) + "%");
        setBACtxtColor(bac);

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
//            if there is one drink consumed start the stopwatch
            stopwatch = new Stopwatch();
        }
//        total number of drinks consumed
        total =  totalBeer + totalHard + totalWine;
    }
//    gets current user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setBACtxtColor(double bac)
    {
//        warn users based on their bac how drunk they are
        if (bac > .02 && bac <= .07){
            txtBAC.setTextColor(getResources().getColor(R.color.green));
        }else if (bac > .07 && bac <= .19){
            txtBAC.setTextColor(getResources().getColor(R.color.yellow));
        }else{
            txtBAC.setTextColor(getResources().getColor(R.color.red));
        }
    }
}
//Subtract approximately 0.01 every 40 minutes after drinking.
