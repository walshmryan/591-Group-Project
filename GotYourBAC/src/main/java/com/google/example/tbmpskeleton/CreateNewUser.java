package com.google.example.tbmpskeleton;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.undergrads.ryan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewUser extends Fragment {

    EditText edtEmail;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText weight;
    Spinner gender;
    String strGender;
    Button createUser;
    public CreateNewUser() {
        // Required empty public constructor
    }

    public interface newUserListener{
        public void goToCreateNew(String email,
              String password,String firstName,
              String lastName, String weight,String gender);

    }

    newUserListener userListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_new_user, container, false);
        edtEmail = (EditText) v.findViewById(R.id.edtEmail);
        createUser = (Button)v.findViewById(R.id.btnCreateUser);
        password = (EditText)v.findViewById(R.id.edtPassword);
        firstName = (EditText)v.findViewById(R.id.edtFirstName);
        lastName =(EditText)v.findViewById(R.id.edtLastName);
        weight = (EditText)v.findViewById(R.id.edtWeight);
        gender = (Spinner)v.findViewById(R.id.sexSpinner);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = gender.getSelectedItem().toString();
                userListener.goToCreateNew(edtEmail.getText().toString(),password.getText().toString(),firstName.getText().toString(),lastName.getText().toString(),
                        weight.getText().toString(),strGender);
            }
        });
        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            userListener = (newUserListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
