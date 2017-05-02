package com.undergrads.ryan;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;

/*

Fragment for the home screen of the tilt game

*/
public class fragment_tilt_home extends Fragment{
    Button btnPlay;
    Button btnLearn;

    OnTiltHomeListener mCallback;

    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    // Container Activity must implement this interface
    public interface OnTiltHomeListener  {
        void onPlaySelect(boolean selected);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTiltHomeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTiltHomeListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tilt_home, container, false);
        btnPlay = (Button) v.findViewById(R.id.btnPlay);
        btnLearn = (Button) v.findViewById(R.id.btnLearn);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPlaySelect(true);
            }
        });

        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPlaySelect(false);
            }
        });

        return  v;

    }

}
