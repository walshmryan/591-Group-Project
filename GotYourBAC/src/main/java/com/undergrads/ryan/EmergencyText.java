package com.undergrads.ryan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class EmergencyText extends BroadcastReceiver{
    int status;
    String iceName;
    String iceNumber;

    Context activity;
    private LocationManager lm;
    private LocationListener ll;
    final static String MYTAG = "LOCATION";

    public EmergencyText(Context c) {
        this.activity = c;
    }

    String uId = getUid(); //get user id
    @Override
    public void onReceive(Context context, Intent intent) {
//        https://developer.android.com/training/monitoring-device-state/battery-monitoring.html#CurrentLevel
        status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        FirebaseDatabase.getInstance().getReference().child("users").child(uId).child("contact-info")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        ContactInfo ice = dataSnapshot.getValue(ContactInfo.class);
                        if (ice != null) {
                            iceName = ice.getName();
                            iceNumber = ice.getNumber();
                            sendSMStxt(iceName,iceNumber);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

    }

    public void sendSMStxt(String iceName, String number)
    {
//        http://stackoverflow.com/questions/4967448/send-sms-in-android

        String message = "Hi " + iceName + ",  I just wanted to let you know that my phone battery is low.";
        if(canAccessLocation()) {

            // set listeners
            lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            ll = new TextLocationListener();

            Location lastLocation = null;

            // try requesting location from Network and GPS
            // if GPS fails try to use Network
            try {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0.0f, ll);
                lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            } catch (Exception e) {
                Log.i(MYTAG, "Could not connect to Network");

                try {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 0.0f, ll);
                    lastLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                } catch (Exception ex) {
                    Log.i(MYTAG, "Could not connect to GPS");
                }
            }

            if(lastLocation != null) {
                message = "Hi " + iceName + ", I just wanted to let you know that my phone battery is" +
                        " low. My last known coordinates are " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude();
            }
        }

        Log.i(MYTAG, message);

        // sends the message without asking
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(activity.getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(),
                    "SMS faild, please enable SMS in App Permissions.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // get user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private boolean canAccessLocation() {
        return(hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(activity, perm));
    }

}

class TextLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.i(MYTAG, "Location Provider Status Has Changed. " + provider);
    }

    public void onProviderEnabled(String provider) {
//        Log.i(MYTAG, "Location Provider Has been DISabled. " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Log.i(MYTAG, "Location Provider Has been ENabled. " + provider);
    }
}

