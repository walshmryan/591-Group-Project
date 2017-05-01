package com.undergrads.ryan;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * A simple {@link Fragment} subclass.
 */
public class google_mainmenu extends Fragment implements View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;


    public google_mainmenu() {
        // Required empty public constructor
    }

    public interface gMainListener{
        public void goToGamePickerFrag();
    }

    gMainListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_google_mainmenu, container, false);

        v.findViewById(R.id.sign_in_button).setOnClickListener(this);
        showSpinner(v);

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            listener.goToGamePickerFrag();
            dismissSpinner(v);
        }

        if (v.findViewById(R.id.spinner).getVisibility() != (View.GONE)){
            dismissSpinner(v);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MenuActivity activity = context instanceof MenuActivity ? (MenuActivity) context : null;
        listener = (gMainListener) context;
        mGoogleApiClient = activity.getGoogleApiClient();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (google_mainmenu.gMainListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                listener.goToGamePickerFrag();
                // TODO: 4/25/17 switch fragments
                break;
        }
    }

    public void showSpinner(View v) {
        v.findViewById(R.id.spinner).setVisibility(View.VISIBLE);
    }
    public void dismissSpinner(View v) {
        v.findViewById(R.id.spinner).setVisibility(View.GONE);
    }
}
