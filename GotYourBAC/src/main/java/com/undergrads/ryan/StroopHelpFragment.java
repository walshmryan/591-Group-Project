package com.undergrads.ryan;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*

Fragment for the stroop help screen

*/
public class StroopHelpFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate stroop help fragment
        View v = inflater.inflate(R.layout.fragment_stroop_help, container, false);

        return v;
    }


}