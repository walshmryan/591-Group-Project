package com.undergrads.ryan;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.google.example.tbmpskeleton.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
//import com.undergrads.ryan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;

import static android.view.View.VISIBLE;


public class StroopBaselineFragment extends Fragment {

    Button btnStart;
    Button btnSkip;
    Button btnYellow;
    Button btnGreen;
    Button btnRed;
    Button btnBlue;
    Button btnBlack;

    TextView txtView0;
    TextView txtView1;
    TextView txtView2;
    TextView txtView3;
    TextView txtView4;
    TextView comment;

    int totalRight = 0;
    int guesses = 0;
    boolean started = false;
    double time;
    Random rand = new Random();

    private Stopwatch stopwatch;
    String[] colours = {"yellow", "green", "red", "blue", "black"};
    Integer wordNumber = rand.nextInt(6);
    Integer textNumber = rand.nextInt(6);
    Integer colourNumber = rand.nextInt(6);
    public String data = "";
    public int turnCounter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static final String TAG = "EBTurn";

    public interface stroopBaselineListener{
        public void goToMain();

    }

    stroopBaselineListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_stroop_game, container, false);

        btnGreen = (Button) (v.findViewById(R.id.btnGreen));
        btnRed = (Button) (v.findViewById(R.id.btnRed));
        btnBlue = (Button) (v.findViewById(R.id.btnBlue));
        btnStart = (Button) (v.findViewById(R.id.btnStart));
        btnSkip = (Button) (v.findViewById(R.id.btnSkip));
        btnYellow = (Button) (v.findViewById(R.id.btnYellow));
        btnBlack = (Button) (v.findViewById(R.id.btnBlack));
        txtView0 = (TextView)(v.findViewById(R.id.txtView0));
        txtView1 = (TextView)(v.findViewById(R.id.txtView1));
        txtView2 = (TextView)(v.findViewById(R.id.txtView2));
        txtView3 = (TextView)(v.findViewById(R.id.txtView3));
        txtView4 = (TextView)(v.findViewById(R.id.txtView4));
        comment = (TextView)(v.findViewById(R.id.comment));

        btnSkip.setVisibility(View.VISIBLE);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableButtons();
                started = true;
                guesses = 0;
                totalRight = 0;
                stopwatch = new Stopwatch();
                comment.setVisibility(View.INVISIBLE);
                txtView0.setVisibility(View.INVISIBLE);
                txtView1.setVisibility(View.INVISIBLE);
                txtView2.setVisibility(View.INVISIBLE);
                txtView3.setVisibility(View.INVISIBLE);
                txtView4.setVisibility(View.INVISIBLE);

                nextWord();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToMain();
            }
        });

        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(0);
                } else {
                    gameDone();
                }
            }
        });

        btnGreen.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(1);
                } else {
                    gameDone();
                }
            }
        }));

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(2);
                } else {
                    gameDone();
                }
            }
        });

        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(3);
                } else {
                    gameDone();
                }
            }
        });

        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(4);
                } else {
                    gameDone();
                }
            }
        });

        return v;
    }

    private void gameDone() {

        time = stopwatch.elapsedTime();

        FirebaseCall fb = new FirebaseCall();
        fb.updateGameBaseline(time, "Stroop");

        disableButtons();
        comment.setText("Your completion time was " + time + " seconds");
        comment.setVisibility(VISIBLE);
        listener.goToMain();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            listener = (StroopBaselineFragment.stroopBaselineListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (StroopBaselineFragment.stroopBaselineListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    private void textSet(TextView txtView, int wordNum, int colourNum){
        txtView.setText(colours[wordNumber]);
        if (colourNum == 0) {
            txtView.setTextColor(getResources().getColor(R.color.yellow));
            txtView.setVisibility(VISIBLE);
        }
        else if (colourNum == 1) {
            txtView.setTextColor(getResources().getColor(R.color.green));
            txtView.setVisibility(VISIBLE);
        }
        else if (colourNum == 2) {
            txtView.setTextColor(getResources().getColor(R.color.red));
            txtView.setVisibility(VISIBLE);
        }
        else if (colourNum == 3) {
            txtView.setTextColor(getResources().getColor(R.color.blue));
            txtView.setVisibility(VISIBLE);
        }
        else if (colourNum == 4) {
            txtView.setTextColor(getResources().getColor(R.color.black));
            txtView.setVisibility(VISIBLE);
        }
    }

    private void disableButtons(){
        btnYellow.setEnabled(false);
        btnGreen.setEnabled(false);
        btnBlack.setEnabled(false);
        btnBlue.setEnabled(false);
        btnRed.setEnabled(false);
    }

    private void enableButtons(){
        btnYellow.setEnabled(true);
        btnGreen.setEnabled(true);
        btnBlack.setEnabled(true);
        btnBlue.setEnabled(true);
        btnRed.setEnabled(true);
    }

    private void nextWord() {
        wordNumber = rand.nextInt(5);
        textNumber = rand.nextInt(5);
        colourNumber = rand.nextInt(5);

        if (textNumber == 0) {
            textSet(txtView0, wordNumber, colourNumber);
        }
        else if (textNumber == 1) {
            textSet(txtView1, wordNumber, colourNumber);
        }
        else if (textNumber == 2) {
            textSet(txtView2, wordNumber, colourNumber);
        }
        else if (textNumber == 3) {
            textSet(txtView3, wordNumber, colourNumber);
        }
        else if (textNumber == 4) {
            textSet(txtView4, wordNumber, colourNumber);
        }
    }

    private boolean checkCorrect(int c) {
        if (colourNumber == c) {
            totalRight += 1;
            txtView0.setVisibility(View.INVISIBLE);
            txtView1.setVisibility(View.INVISIBLE);
            txtView2.setVisibility(View.INVISIBLE);
            txtView3.setVisibility(View.INVISIBLE);
            txtView4.setVisibility(View.INVISIBLE);
            nextWord();
            return true;
        }

        return false;
    }
//    public byte[] persist() {
//        JSONObject retVal = new JSONObject();
//
//        try {
//            retVal.put("data", data);
//            retVal.put("turnCounter", turnCounter);
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        String st = retVal.toString();
//
//        Log.d(TAG, "==== PERSISTING\n" + st);
//
//        return st.getBytes(Charset.forName("UTF-8"));
//    }
//    // Creates a new instance of StroopTurn.
//    static public StroopBaselineFragment unpersist(byte[] byteArray) {
//
//        if (byteArray == null) {
//            Log.d(TAG, "Empty array---possible bug.");
//            return new StroopBaselineFragment();
//        }
//
//        String st = null;
//        try {
//            st = new String(byteArray, "UTF-8");
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//            return null;
//        }
//
//        Log.d(TAG, "====UNPERSIST \n" + st);
//
//        StroopBaselineFragment retVal = new StroopBaselineFragment();
//
//        try {
//            JSONObject obj = new JSONObject(st);
//
//            if (obj.has("data")) {
//                retVal.data = obj.getString("data");
//            }
//            if (obj.has("turnCounter")) {
//                retVal.turnCounter = obj.getInt("turnCounter");
//            }
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return retVal;
//    }

}

