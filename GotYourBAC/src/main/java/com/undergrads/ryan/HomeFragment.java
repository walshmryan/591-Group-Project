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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.undergrads.ryan.Manifest;
import com.undergrads.ryan.R;
import com.undergrads.ryan.Scores;
import com.undergrads.ryan.Weather;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private TextView temperature;
    private ImageView weatherIcon;
    private TextView weatherForCity;
    private String city;
    private TextView score1;
    private TextView score2;
    private TextView score3;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private LocationManager lm;
    private LocationListener ll;
    final static String MYTAG = "LOCATION";

    private static final String[] LOCATION_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int LOCATION_REQUEST = 1340;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        temperature = (TextView) v.findViewById(R.id.temperature);
        weatherIcon = (ImageView) v.findViewById(R.id.weatherIcon);
        weatherForCity = (TextView) v.findViewById(R.id.weatherForCity);
        score1 = (TextView) v.findViewById(R.id.score1);
        score2 = (TextView) v.findViewById(R.id.score2);
        score3 = (TextView) v.findViewById(R.id.score3);

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
         Fetches top scores from DB and displays them
         */
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String uId = mAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("scores").orderByChild("timestamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Scores score = snapshot.getValue(Scores.class);
                            if (score.getUserId().equals(uId)) {
                                postScore(score, count);
                                count++;
                            }
                        }
                        if(count == 0) {
                            score1.setText("No past games played");
                            score2.setText("");
                            score3.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error", "bad");
                    }
                });

        return v;
    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        lm.
//    }

    protected void postScore(Scores s, int index) {
        String score = s.getGameType() + ": " + s.getScore();
        if (index == 0) {
            score1.setText(score);
        } else if (index == 1) {
            score2.setText(score);
        } else if (index == 2) {
            score3.setText(score);
        }
    }

    protected void setCity(Location currLoc) {

        if (currLoc != null) {
            double lat = currLoc.getLatitude();
            double lon = currLoc.getLongitude();
            Log.i(MYTAG,  lat + ", " + lon);

            try {
                Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());

                List<Address> addresses = gcd.getFromLocation(lat, lon, 1);
                if (addresses.size() > 0)
                {
                    if(addresses.get(0).getLocality() != null) {
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea().replaceAll("\\s+","");
                        Log.i(MYTAG, "Current city is: " + addresses.get(0).getLocality() + ", " + state);

                        if(state != null) {
//                            new Weather(temperature, weatherIcon).execute("http://api.wunderground.com/api/fd527dc2ea48e15c/conditions/q/" + state + "/" + city + ".json");
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
            Log.i(MYTAG, "Location is null");
        }

    }

    private boolean canAccessLocation() {
        return(hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(getActivity(), perm));
    }


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