package com.undergrads.ryan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.undergrads.ryan.R;

//import com.google.example.tbmpskeleton.R;


public class LoginActivity extends Fragment {
    Button btnSignIn;
    Button btnRegister;
    EditText edtPassword;
    AutoCompleteTextView txtEmail;
    EditText edtFirstName;
    TextView txtForgotPassword;

    public LoginActivity() {
        // Required empty public constructor
    }

    public interface LoginListener{
        public void goToNewUserFragment();
        public void goToLoginFragment(String username, String password);
        public  void goToRetrievePassword(String email);
    }

    LoginListener loginlistener;

    //    //onAttach gets called when fragment attaches to Main Activity.  This is the right time to instantiate
//    //our ControlFragmentListener, why?  Because we know the Main Activity was successfully created and hooked.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginlistener = (LoginListener) context;  //context is a handle to the main activity, let's bind it to our interface.
        //set number of stars
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        btnSignIn = (Button) v.findViewById(R.id.btnSignIn);
        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        txtEmail = (AutoCompleteTextView) v.findViewById(R.id.txtEmail);
        edtPassword = (EditText) v.findViewById(R.id.edtPassword);
        txtForgotPassword = (TextView)v.findViewById(R.id.txtForgotPW);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginlistener.goToNewUserFragment();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                String username = "sarahpmedeiros@gmail.com";
                String password = "password123";
//// TODO: 4/27/17 put back when testing is done
//                String username = txtEmail.getText().toString();
//                String password = edtPassword.getText().toString();

                loginlistener.goToLoginFragment(username, password);
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtEmail.getText().toString();
                loginlistener.goToRetrievePassword(username);
            }
        });
        return v;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            loginlistener = (LoginActivity.LoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }



}
