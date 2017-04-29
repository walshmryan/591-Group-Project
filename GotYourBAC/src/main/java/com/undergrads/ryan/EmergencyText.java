package com.undergrads.ryan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sarahmedeiros on 4/27/17.
 */

public class EmergencyText extends BroadcastReceiver{
    int status;
    String iceName;
    String iceNumber;

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

    public static void sendSMStxt(String iceName, String number)
    {
//        http://stackoverflow.com/questions/4967448/send-sms-in-android
//        String phoneNumber="7749295480";
        String message;
//        if (canAccessLocation()) {
//      // TODO: 4/29/17  set location
//
//            message = "Hi " + iceName + ", I just wanted to let you know that their phone" +
//                    " battery is low. Their last location is ";
//        }
//        else{
            message = "Hi " + iceName + ",  I just wanted to let you know that their phone" +
                    " battery is low.";
//        }
//      this code sends the message without asking
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
//            Toast.makeText(getActivity().getApplicationContext(), "SMS Sent!",
//                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//            Toast.makeText(getActivity().getApplicationContext(),
//                    "SMS faild, please enable SMS in App Permissions.",
//                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //    get user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

//    private static boolean canAccessLocation() {
//        return(hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
//    }
//    private static boolean hasPermission(String perm) {
//        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(, perm));
//    }
}

