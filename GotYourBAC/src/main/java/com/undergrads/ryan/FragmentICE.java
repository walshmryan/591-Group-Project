package com.undergrads.ryan;


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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*

Fragment for emergency contact info updating and sending of emergency texts

*/
public class FragmentICE extends Fragment {

    private Button btnUpdate;
    private Button btnSendText;
    private Button btnSave;
    private TextView txtName;
    private EditText edtName;
    private TextView txtPhone;
    private EditText edtPhone;
    private ViewSwitcher viewswitcherName;
    private ViewSwitcher viewswitcherPhone;
    private String iceName;
    private String number;

    public FragmentICE() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ice, container, false);

        // initialize values
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
                        // Get user ICE information
                        ContactInfo ice = dataSnapshot.getValue(ContactInfo.class);

                        if (ice != null) {
                            iceName = ice.getName();
                            number = formatPhone(ice.getNumber());

                            // set the text view and edit view values from the stored database values
                            txtName.setText(iceName);
                            txtPhone.setText(number);
                            edtName.setText(iceName);
                            edtPhone.setText(number);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

        txtName.setText(iceName);
        txtPhone.setText(number);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change views for update mode
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();

                viewSwitcherSave.showNext();
                // hide update button
                btnUpdate.setVisibility(View.INVISIBLE);
            }
        });

        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmergencyText et = new EmergencyText(getActivity());
                et.sendSMStxt(iceName,number);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save values to database
                iceName = edtName.getText().toString();
                number = edtPhone.getText().toString();
                FirebaseCall fb = new FirebaseCall();
                fb.updateICEContactInfo(iceName, number);
                number = formatPhone(number);


                //set text views
                txtName.setText(iceName);
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

    // get user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // formats the phone number to appropriate format
    public String formatPhone(String number){
        int phoneNumLen = 10;
        if (number.length() == phoneNumLen){
            return (String.format("%s-%s-%s",number.substring(0,3),number.substring(3,6),number.substring(6,10)));
        }
        return number; //just return the string because they didn't input enough numbers
    }
}

