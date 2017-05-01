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
import com.undergrads.ryan.CalculateBAC;

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

    private int gender;
    private int weight;
    private double bac = 0.0;
    DecimalFormat dcmFormatter = new DecimalFormat("0.##");

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

        final String uId = getUid(); //get current user id
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
                                        if(drinkInfo != null && !CalculateBAC.dateExpired(drinkInfo.getTimestamp(), 12)) {
                                            CalculateBAC.totalHard = drinkInfo.getTotalHard();
                                            CalculateBAC.totalWine = drinkInfo.getTotalWine();
                                            CalculateBAC.totalBeer = drinkInfo.getTotalBeer();
                                            CalculateBAC.setTotal();
                                            setViews();

                                            // set number of drinks here in case we loaded some from DB and set BAC
                                            Log.i("BAC", weight + "");
                                            Log.i("BAC", gender + "");
                                            bac = CalculateBAC.calculateBAC(weight, gender);
                                            setBACtxtColor(bac);
                                        } else {
                                            CalculateBAC.totalHard = 0;
                                            CalculateBAC.totalWine = 0;
                                            CalculateBAC.totalBeer = 0;
                                            CalculateBAC.setTotal();
                                            setViews();
                                            bac = CalculateBAC.calculateBAC(weight, gender);
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
                if (CalculateBAC.totalWine - 1  >= 0){
                    CalculateBAC.totalWine--;
                    updateDrinkData();
                    CalculateBAC.setTotal();
                    setViews();
                    bac = CalculateBAC.calculateBAC(weight,gender);
                    txtBAC.setText(dcmFormatter.format(bac) + "%");
                    setBACtxtColor(bac);
                }
        }});
        btnWinePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateBAC.totalWine += 1;
                updateDrinkData();
                CalculateBAC.setTotal();
                setViews();
                bac = CalculateBAC.calculateBAC(weight,gender);
                txtBAC.setText(dcmFormatter.format(bac) + "%");
                setBACtxtColor(bac);
            }});

        btnHardAlcoholMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CalculateBAC.totalHard - 1  >= 0){
                    CalculateBAC.totalHard--;
                    updateDrinkData();
                    CalculateBAC.setTotal();
                    setViews();
                    bac = CalculateBAC.calculateBAC(weight,gender);
                    txtBAC.setText(dcmFormatter.format(bac) + "%");
                    setBACtxtColor(bac);
                }
            }});
        btnHardAlcoholPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateBAC.totalHard += 1;
                updateDrinkData();
                CalculateBAC.setTotal();
                setViews();
                bac = CalculateBAC.calculateBAC(weight,gender);
                txtBAC.setText(dcmFormatter.format(bac) + "%");
                setBACtxtColor(bac);
            }});

        btnBeerMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CalculateBAC.totalBeer - 1  >= 0){
                    CalculateBAC.totalBeer--;
                    updateDrinkData();
                    CalculateBAC.setTotal();
                    setViews();
                    bac = CalculateBAC.calculateBAC(weight,gender);
                    txtBAC.setText(dcmFormatter.format(bac) + "%");
                    setBACtxtColor(bac);
                }
            }});
        btnBeerPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateBAC.totalBeer+=1;
                updateDrinkData();
                CalculateBAC.setTotal();
                setViews();
                bac = CalculateBAC.calculateBAC(weight,gender);
                txtBAC.setText(dcmFormatter.format(bac) + "%");
                setBACtxtColor(bac);
            }});
        setViews();
        txtNumDrinks.setText(String.valueOf(CalculateBAC.getTotal()));

        return v;
    }

    public void setViews(){
        txtNumDrinks.setText(Integer.toString(CalculateBAC.total));
        wineCounter.setText(Integer.toString(CalculateBAC.totalWine));
        beerCounter.setText(Integer.toString(CalculateBAC.totalBeer));
        hardCounter.setText(Integer.toString(CalculateBAC.totalHard));
        bac = CalculateBAC.calculateBAC(weight, gender);
        txtBAC.setText(dcmFormatter.format(bac) + "%");
        txtNumDrinks.setText(String.valueOf(CalculateBAC.getTotal()));
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

    public void updateDrinkData(){
        FirebaseCall fb = new FirebaseCall();
        fb.updateDrinkTotals(CalculateBAC.totalHard, CalculateBAC.totalWine, CalculateBAC.totalBeer);
    }

    public  String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}