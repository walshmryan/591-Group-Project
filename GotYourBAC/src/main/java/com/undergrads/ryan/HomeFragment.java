package com.undergrads.ryan;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/*

Fragment for the home screen which displays weather, recent scores, and current BAC level

*/
public class HomeFragment extends Fragment {

    private TextView temperature;
    private ImageView weatherIcon;
    private TextView weatherForCity;
    private String city;
    private TextView score1;
    private TextView score2;
    private TextView score3;
    private ProgressBar iconProgress;
    private ArrayList<Scores> scoreList;
    private TextView BACLevel;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private LocationManager lm;
    private LocationListener ll;
    final static String MYTAG = "LOCATION";

    private int weight;
    private int gender;
    private double bac = 0.0;
    DecimalFormat dcmFormatter = new DecimalFormat("0.##");

    // list of permissions we want
    private static final String[] LOCATION_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int LOCATION_REQUEST = 1340;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate frag
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // initialize values
        temperature = (TextView) v.findViewById(R.id.temperature);
        weatherIcon = (ImageView) v.findViewById(R.id.weatherIcon);
        weatherForCity = (TextView) v.findViewById(R.id.weatherForCity);
        score1 = (TextView) v.findViewById(R.id.score1);
        score2 = (TextView) v.findViewById(R.id.score2);
        score3 = (TextView) v.findViewById(R.id.score3);
        iconProgress = (ProgressBar) v.findViewById(R.id.iconProgress);
        BACLevel = (TextView)v.findViewById(R.id.BACLevel);

        // make everything empty till DB returns
        temperature.setText("");
        score1.setText("");
        score2.setText("");
        score3.setText("");

        // make sure to get permissions if not already requested
        if (!canAccessLocation()) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }

        // set listeners
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        ll = new MyLocationListener();

        // try requesting location from Network and GPS
        // if GPS fails try to use Network
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0.0f, ll);
            Location lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            setCity(lastLocation);

        } catch (Exception e) {
            Log.i(MYTAG, "Could not connect to Network");

            try {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 0.0f, ll);
                Location lastLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                setCity(lastLocation);
            } catch (Exception ex) {
                Log.i(MYTAG, "Could not connect to GPS");
            }
        }

        /*
         Fetches top scores from DB and display them
         */
        mAuth = FirebaseAuth.getInstance();
        final String uId = mAuth.getCurrentUser().getUid();
        scoreList = new ArrayList<Scores>();
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
                                        if(drinkInfo != null && !CalculateBAC.dateExpired(drinkInfo.getTimestamp(), 1)) {
                                            CalculateBAC.totalHard = drinkInfo.getTotalHard();
                                            CalculateBAC.totalWine = drinkInfo.getTotalWine();
                                            CalculateBAC.totalBeer = drinkInfo.getTotalBeer();
                                            CalculateBAC.setTotal();
                                            bac = CalculateBAC.calculateBAC(weight, gender);
                                            BACLevel.setText(dcmFormatter.format(bac) + "%");
                                            //setViews();


                                            // set number of drinks here in case we loaded some from DB and set BAC
                                            Log.i("BAC", weight + "");
                                            Log.i("BAC", gender + "");
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference().child("scores").orderByValue()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // add scores to list if conditions met
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Scores score = snapshot.getValue(Scores.class);
                            // only post this score if the userId matches
                            if (score.getUserId().equals(uId)) {
                                scoreList.add(score);
                            }
                        }
                        // if no scores found set to be empty
                        if(scoreList.size() == 0) {
                            score1.setText("No past games played");
                            score2.setText("");
                            score3.setText("");
                        } else {
                            // otherwise send scores to UI
                            postScores();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error", "bad");
                    }
                });

        return v;
    }

    @Override
    public void onStop(){
        super.onStop();
        lm.removeUpdates(ll);
    }

    // sets text views with a given score based on index
    protected void postScores() {

        // we start at end of list to get most recent scores first
        for(int i = scoreList.size()-1; i >=0; i--) {
            Scores currScore = scoreList.get(i);
            String score = currScore.getGameType() + ": " + currScore.getScore();

            if (i == scoreList.size()-1) {
                score1.setText(score);
            } else if (i == scoreList.size()-2) {
                score2.setText(score);
            } else if (i == scoreList.size()-3) {
                score3.setText(score);
                break;
            }
        }
    }

    // given a location, fetch correct weather temp and icon
    protected void setCity(Location currLoc) {

        if (currLoc != null) {
            double lat = currLoc.getLatitude();
            double lon = currLoc.getLongitude();
            Log.i(MYTAG,  lat + ", " + lon);

            try {
                // initialize geocoder
                Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());

                // get list of addresses from lat/lon
                List<Address> addresses = gcd.getFromLocation(lat, lon, 1);
                if (addresses.size() > 0)
                {
                    // if we have an address and we can get locality
                    if(addresses.get(0).getLocality() != null) {
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea().replaceAll("\\s+","_");
                        Log.i(MYTAG, "Current city is: " + addresses.get(0).getLocality() + ", " + state);

                        // if we have state then we can make request to Wunderground API
                        if(state != null) {
                            // pass in widgets that will be populated with returned results from API
                            new Weather(temperature, weatherIcon, iconProgress).execute("http://api.wunderground.com/api/fd527dc2ea48e15c/conditions/q/" + state + "/" + city + ".json");
                        }

                        weatherForCity.setText("Here's the current weather for " + city + ":");

                    } else {
                        Log.i(MYTAG, "Could not determine city from: " + addresses.get(0));
                    }
                }
                else
                {
                    Log.i(MYTAG, "Address is empty");
                }

            } catch (Exception e) {
                Log.e(MYTAG, "Error with Geocoder");

            }
        } else {
            // if location is not found display error
            Log.i(MYTAG, "Location is null");
            weatherForCity.setText("Can't determine location");
            temperature.setText("Error");
        }

    }

    // checks for permissions
    private boolean canAccessLocation() {
        return(hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(getActivity(), perm));
    }

    // inner class for location listener
    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            setCity(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(MYTAG, "Location Provider Status Has Changed. " + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.i(MYTAG, "Location Provider Has been DISabled. " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(MYTAG, "Location Provider Has been ENabled. " + provider);
        }
    }

}