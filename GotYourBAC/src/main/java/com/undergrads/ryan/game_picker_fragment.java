package com.undergrads.ryan;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class game_picker_fragment extends Fragment {


    Button btnStroop;
    Button btnTilt;

    public game_picker_fragment() {
        // Required empty public constructor
    }

    public interface gamePickerListener{
        public void goToStroop(View view);
        public void goToTilt();
    }
    gamePickerListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_picker, container, false);
        btnStroop = (Button)v.findViewById(R.id.btnStroop);
        btnTilt = (Button)v.findViewById(R.id.btnTilt);
        btnStroop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToStroop(getView());
            }
        });
        btnTilt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToTilt();
            }
        });

        return v;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (game_picker_fragment.gamePickerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
