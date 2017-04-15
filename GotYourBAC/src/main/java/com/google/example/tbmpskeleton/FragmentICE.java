package com.google.example.tbmpskeleton;

/**
 * Created by sarahmedeiros on 4/14/17.
 */
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.undergrads.ryan.R;

//import com.undergrads.ryan.R;


    /**
     * A simple {@link Fragment} subclass.
     */
public class FragmentICE extends Fragment {
    Button btnUpdate;
    Button btnSendText;
    Button btnSave;
    ViewSwitcher viewswitcherName;
    ViewSwitcher viewswitcherPhone;

    public FragmentICE() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ice, container, false);
        btnUpdate = (Button)v.findViewById(R.id.btnUpdate);
        btnSave = (Button)v.findViewById(R.id.btnSave);
        btnSendText = (Button)v.findViewById(R.id.btnSendText);
        viewswitcherName = (ViewSwitcher) v.findViewById(R.id.viewswitcherName);
        viewswitcherPhone = (ViewSwitcher) v.findViewById(R.id.viewswitcherPhone);
        final ViewSwitcher viewSwitcherSave = (ViewSwitcher) v.findViewById(R.id.viewSwitcherSave);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();
            }
        });
        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcherSave.showNext();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcherSave.showNext();
            }
        });

        return v;
    }

}

