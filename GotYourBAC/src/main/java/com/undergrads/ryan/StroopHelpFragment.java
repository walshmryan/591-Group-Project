package com.undergrads.ryan;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.google.example.tbmpskeleton.R;
//import com.undergrads.ryan.R;

/**
 * Calculate the blood alcohol content
 *  level of a user based on their weight and
 *  gender
 */
public class StroopHelpFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_stroop_help, container, false);

        return v;
    }


}