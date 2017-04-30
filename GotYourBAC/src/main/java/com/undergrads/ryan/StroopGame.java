package com.undergrads.ryan;


import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.undergrads.ryan.R;

//import com.google.example.tbmpskeleton.R;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.games.event.Events;

import java.util.Random;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StroopGame extends Fragment {

    Button btnStart;
    Button btnYellow;
    Button btnGreen;
    Button btnRed;
    Button btnBlue;
    Button btnBlack;
    Button btnSkip;
    Button btnEnd;

    TextView stroopDescription;
    TextView txtWord0;
    TextView txtWord1;
    TextView txtWord2;
    TextView txtWord3;
    TextView txtWord4;
    TextView comment;

    int totalRight = 0;
    int totalWrong = 0;
    int guesses = 0;
    Random rand;
    private Stopwatch stopwatch;
    String[] colours = {"yellow", "green", "red", "blue", "black"};
    Integer wordNumber;
    Integer textNumber;
    Integer colourNumber;
    double time;
    String base = "stroop baseline",quick = "stroop quick", mp = "stroop mp";
    private GoogleApiClient googleApiClient;

    boolean started = false;
    public StroopGame() {
        // Required empty public constructor
    }
    public interface PlayGameListener{
        public void gameDone(double num, String gameType);
        public void gameAborted();
    }


    PlayGameListener gameListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        gameListener = (PlayGameListener) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stroop_game, container, false);

        totalRight = 0;
        totalWrong = 0;
        guesses = 0;
        rand = new Random();
        wordNumber = rand.nextInt(6);
        textNumber = rand.nextInt(6);
        colourNumber = rand.nextInt(6);

        btnGreen = (Button) (v.findViewById(R.id.btnGreen));
        btnRed = (Button) (v.findViewById(R.id.btnRed));
        btnBlue = (Button) (v.findViewById(R.id.btnBlue));
        btnStart = (Button) (v.findViewById(R.id.btnStart));
        btnYellow = (Button) (v.findViewById(R.id.btnYellow));
        btnBlack = (Button) (v.findViewById(R.id.btnBlack));
        btnSkip = (Button)v.findViewById(R.id.btnSkip);
        btnEnd = (Button)v.findViewById(R.id.btnEnd);
        txtWord0 = (TextView)(v.findViewById(R.id.txtView0));
        txtWord1 = (TextView)(v.findViewById(R.id.txtView1));
        txtWord2 = (TextView)(v.findViewById(R.id.txtView2));
        txtWord3 = (TextView)(v.findViewById(R.id.txtView3));
        txtWord4 = (TextView)(v.findViewById(R.id.txtView4));
        stroopDescription = (TextView)(v.findViewById(R.id.stroopDescription));
        comment = (TextView)(v.findViewById(R.id.comment));

        btnSkip.setVisibility(View.GONE);
        stroopDescription.setVisibility(View.INVISIBLE);
        btnEnd.setVisibility(View.VISIBLE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (totalRight < 10) {
                    checkCorrect(0);
                } else {
                    done();
                }
            }
        });

        btnGreen.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (totalRight < 10) {
                    checkCorrect(1);
                } else {
                    done();
                }
            }
        }));

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (totalRight < 10) {
                    checkCorrect(2);
                } else {
                    done();
                }
            }
        });

        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (totalRight < 10) {
                    checkCorrect(3);
                } else {
                    done();
                }
            }
        });

        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (totalRight < 10) {
                    checkCorrect(4);
                } else {
                    done();
                }
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameListener.gameAborted();
            }
        });

        Log.i ("t",getFragmentTag());
        return v;
    }

    private void done() {
        double finalScore = stopwatch.elapsedTime() + (totalWrong * 2);
        String tag = getFragmentTag();
        gameListener.gameDone(finalScore, "Stroop");
    }

    public void textSet(TextView txtView, int wordNum, int colourNum){
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

    public void disableButtons(){
        btnYellow.setEnabled(false);
        btnGreen.setEnabled(false);
        btnBlack.setEnabled(false);
        btnBlue.setEnabled(false);
        btnRed.setEnabled(false);
    }

    public void enableButtons(){
        btnYellow.setEnabled(true);
        btnGreen.setEnabled(true);
        btnBlack.setEnabled(true);
        btnBlue.setEnabled(true);
        btnRed.setEnabled(true);
    }

    public void nextWord() {
        wordNumber = rand.nextInt(5);
        textNumber = rand.nextInt(5);
        colourNumber = rand.nextInt(5);

        // TESTING
//        wordNumber = 3;
//        textNumber = 4;
//        colourNumber = 1;
        //TESTING

        if (textNumber == 0) {
            textSet(txtWord0, wordNumber, colourNumber);
        }
        else if (textNumber == 1) {
            textSet(txtWord1, wordNumber, colourNumber);
        }
        else if (textNumber == 2) {
            textSet(txtWord2, wordNumber, colourNumber);
        }
        else if (textNumber == 3) {
            textSet(txtWord3, wordNumber, colourNumber);
        }
        else if (textNumber == 4) {
            textSet(txtWord4, wordNumber, colourNumber);
        }
    }

    public boolean checkCorrect(int c) {
        if (colourNumber == c) {
            //txtView0.setText("TEST");
            //txtView0.setVisibility(VISIBLE);
            totalRight += 1;
            txtWord0.setVisibility(View.INVISIBLE);
            txtWord1.setVisibility(View.INVISIBLE);
            txtWord2.setVisibility(View.INVISIBLE);
            txtWord3.setVisibility(View.INVISIBLE);
            txtWord4.setVisibility(View.INVISIBLE);
            nextWord();
            return true;
        } else {
            totalWrong += 1;
        }

        return false;

    }
    public void start(){
        enableButtons();
        started = true;
        guesses = 0;
        totalRight = 0;
        stopwatch = new Stopwatch();
        comment.setVisibility(View.INVISIBLE);
        txtWord0.setVisibility(View.INVISIBLE);
        txtWord1.setVisibility(View.INVISIBLE);
        txtWord2.setVisibility(View.INVISIBLE);
        txtWord3.setVisibility(View.INVISIBLE);
        txtWord4.setVisibility(View.INVISIBLE);
        nextWord();
    }
    public String getFragmentTag(){
        FragmentManager fragment = getFragmentManager();
        Fragment curFrag = fragment.findFragmentById(R.id.frame_layout);
        return (curFrag.getTag().toString());

    }

}
