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
import android.widget.EditText;
import android.widget.TextView;
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
    TextView txtName;
    EditText edtName;
    TextView txtPhone;
    EditText edtPhone;
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
        edtName = (EditText)v.findViewById(R.id.edtName);
        txtName = (TextView)v.findViewById(R.id.txtName);
        edtPhone = (EditText)v.findViewById(R.id.edtPhone);
        txtPhone = (TextView)v.findViewById(R.id.txtPhone);
        viewswitcherName = (ViewSwitcher) v.findViewById(R.id.viewswitcherName);
        viewswitcherPhone = (ViewSwitcher) v.findViewById(R.id.viewswitcherPhone);
        final ViewSwitcher viewSwitcherSave = (ViewSwitcher) v.findViewById(R.id.viewSwitcherSave);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();
                viewSwitcherSave.showNext();
                btnUpdate.setVisibility(View.INVISIBLE);
            }
        });
        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save to database
                txtName.setText(edtName.getText().toString());
                txtPhone.setText(edtPhone.getText().toString());
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();
                viewSwitcherSave.showNext();
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }

}

