package com.google.example.tbmpskeleton;

/**
 * Created by sarahmedeiros on 4/14/17.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.undergrads.ryan.R;

/**
 * Fragment class for emergency contact info
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
//      initialize values
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
                //change views for update mode
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();
                viewSwitcherSave.showNext();
//                hide update button
                btnUpdate.setVisibility(View.INVISIBLE);
            }
        });
        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMStxt();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save values to database

                //set text views
                txtName.setText(edtName.getText().toString());
                txtPhone.setText(edtPhone.getText().toString());

                //switch views back to non edit mode
                viewswitcherName.showNext();
                viewswitcherPhone.showNext();
                viewSwitcherSave.showNext();

                //make the update button visible
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }
    public void sendSMStxt()
    {
//        http://stackoverflow.com/questions/4967448/send-sms-in-android
        String phoneNumber="7749295480";
        String message="This is a test";

//      this code opens a message in the message app
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", message);
        startActivity(intent);

//      this code sends the message without asking
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);

    }


}

