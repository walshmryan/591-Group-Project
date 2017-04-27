package com.undergrads.ryan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

//import com.google.example.tbmpskeleton.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.undergrads.ryan.R;

/**
 * Fragment class for emergency contact info
 */
public class FragmentICE extends Fragment {
    Button btnUpdate;
    Button btnSendText;
    Button btnSave;
    TextView txtName;
    EditText edtName;
    TextView txtPhone;
    EditText edtPhone;
    ViewSwitcher viewswitcherName;
    ViewSwitcher viewswitcherPhone;
    String name;
    String number;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public FragmentICE() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ice, container, false);
//      initialize values
        btnUpdate = (Button)v.findViewById(R.id.btnUpdate);
        btnSave = (Button)v.findViewById(R.id.btnSave);
        btnSendText = (Button)v.findViewById(R.id.btnSendText);
        edtName = (EditText)v.findViewById(R.id.edtName);
        txtName = (TextView)v.findViewById(R.id.txtName);
        edtPhone = (EditText)v.findViewById(R.id.edtPhone);
        txtPhone = (TextView)v.findViewById(R.id.txtPhone);
        viewswitcherName = (ViewSwitcher) v.findViewById(R.id.viewswitcherName);
        viewswitcherPhone = (ViewSwitcher) v.findViewById(R.id.viewswitcherPhone);
        final ViewSwitcher viewSwitcherSave = (ViewSwitcher) v.findViewById(R.id.viewSwitcherSave);


        String uId = getUid(); //get user id
        FirebaseDatabase.getInstance().getReference().child("users").child(uId).child("contact-info")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
//                        Users user = dataSnapshot.getValue(Users.class);
                        ContactInfo ice = dataSnapshot.getValue(ContactInfo.class);
                        //get current values from the database
                        if (ice != null) {
                            name = ice.getName();
                            number = ice.getNumber();
//
////                      set the text view and edit view values from the stored
////                       database values
                            txtName.setText(name);
                            txtPhone.setText(number);
                            edtName.setText(name);
                            edtPhone.setText(number);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });
//        name =
        txtName.setText(name);
        txtPhone.setText(number);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change views for update mode
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();

                viewSwitcherSave.showNext();
//                hide update button
                btnUpdate.setVisibility(View.INVISIBLE);
            }
        });

        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMStxt();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save values to database
                name = edtName.getText().toString();
                number = edtPhone.getText().toString();
                FirebaseCall fb = new FirebaseCall();
                fb.updateICEContactInfo(name, number);


                //set text views
                txtName.setText(name);
                txtPhone.setText(number);

                //switch views back to non edit mode
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();
                viewSwitcherSave.showNext();

                //make the update button visible
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }
    public void sendSMStxt()
    {
//        http://stackoverflow.com/questions/4967448/send-sms-in-android
//        String phoneNumber="7749295480";
        String message="This is a test";

//      this code opens a message in the message app
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
        intent.putExtra(name, message);
        startActivity(intent);

//      this code sends the message without asking
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);

    }

    //    get user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}

