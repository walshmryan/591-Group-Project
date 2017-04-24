package com.google.example.tbmpskeleton;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IntegerRes;
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
import com.undergrads.ryan.R;


/**
 * Fragment to edit existing user
 */
public class EditUser extends Fragment {

    TextView txtFirstName;
    TextView txtLastName;
    TextView txtWeight;
    TextView txtEmail;

    EditText edtFirstName;
    EditText edtLastName;
    EditText edtWeight;
    EditText edtEmail;

    Button btnUpdateUser;
    Button btnSave;

    ViewSwitcher viewSwitcherBtn;
    ViewSwitcher viewSwitcherFirst;
    ViewSwitcher viewSwitcherLast;
    ViewSwitcher viewSwitcherEmail;
    ViewSwitcher viewSwitcherWeight;

    String first;
    String last;
    String email;
    String gender;

    public EditUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_user, container, false);
//    initialize variables
        txtFirstName = (TextView)v.findViewById(R.id.txtFirstName);
        txtLastName = (TextView)v.findViewById(R.id.txtLastName);
        txtWeight = (TextView)v.findViewById(R.id.txtWeight);
        txtEmail = (TextView)v.findViewById(R.id.txtUsername);
        edtFirstName = (EditText)v.findViewById(R.id.edtFirstName);
        edtLastName = (EditText)v.findViewById(R.id.edtLastName);
        edtWeight = (EditText)v.findViewById(R.id.edtWeight);
        edtEmail = (EditText)v.findViewById(R.id.edtUsername);
        btnUpdateUser = (Button)v.findViewById(R.id.btnUpdateUser);
        btnSave = (Button)v.findViewById(R.id.btnSaveUser);
        viewSwitcherBtn = (ViewSwitcher)v.findViewById(R.id.viewSwitcherBtn);
        viewSwitcherEmail = (ViewSwitcher)v.findViewById(R.id.viewSwitcherEmail);
        viewSwitcherFirst = (ViewSwitcher)v.findViewById(R.id.viewSwitcherFirst);
        viewSwitcherLast = (ViewSwitcher)v.findViewById(R.id.viewSwitcherLast);
        viewSwitcherWeight = (ViewSwitcher)v.findViewById(R.id.viewSwitcherWeight);

        String uId = getUid(); //get user id
        FirebaseDatabase.getInstance().getReference().child("users").child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        Users user = dataSnapshot.getValue(Users.class);


                        //get current user values from the database
                        gender = user.getGender();
                        first = user.getFirstName();
                        last = user.getLastName();
                        email = user.getUsername();
                        int userWeight = user.getWeight();

//                      set the text view and edit view values from the stored
//                       database values
                        txtFirstName.setText(first);
                        txtLastName.setText(last);
                        txtEmail.setText(email);
                        txtWeight.setText(userWeight+"");
                        edtFirstName.setText(first);
                        edtLastName.setText(last);
                        edtEmail.setText(email);
                        edtWeight.setText(userWeight+"");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                user wants to update the values so switch to edit text views
                viewSwitcherFirst.showNext();
                viewSwitcherLast.showNext();
                viewSwitcherEmail.showNext();
                viewSwitcherBtn.showNext();
                viewSwitcherWeight.showNext();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save values to database
//                get the weight from the edit box -string
//                udate the database - int
//                change txtweight
                String w = edtWeight.getText().toString();
                int intW = Integer.parseInt(w);



//                writeNewData(edtEmail.getText().toString(),edtFirstName.getText().toString(),edtLastName.getText().toString(),intW,gender);



//                update text views

                txtFirstName.setText(edtFirstName.getText().toString());
                txtLastName.setText(edtLastName.getText().toString());
                txtEmail.setText(edtEmail.getText().toString());
                txtWeight.setText(w+"");
//              now that you saved the values switch back to text views
                viewSwitcherFirst.showNext();
                viewSwitcherLast.showNext();
                viewSwitcherEmail.showNext();
                viewSwitcherBtn.showNext();
                viewSwitcherWeight.showNext();
            }
        });
//
        return v;
    }
//    get user id
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
//    public void writeNewData(String username, String first, String last, int weight, String gender) {
//        Users user = new Users(username,first,last,weight,gender);
//        String uId = getUid();
//        FirebaseDatabase.getInstance().getReference().child("users").child(uId).setValue(user);
//    }

}
