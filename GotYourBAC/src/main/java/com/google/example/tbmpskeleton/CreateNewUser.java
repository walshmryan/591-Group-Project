package com.google.example.tbmpskeleton;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.undergrads.ryan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewUser extends Fragment {

    EditText edtEmail;
    public CreateNewUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_new_user, container, false);
        edtEmail = (EditText) v.findViewById(R.id.edtEmail);

        return v;
    }
//    public void setEmailFromLogin(String email){
////        edtEmail.setText(email);
////    };


}
