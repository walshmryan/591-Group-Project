package com.undergrads.ryan;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.undergrads.ryan.R;

//import com.google.example.tbmpskeleton.R;

import java.util.Random;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayGame extends Fragment {

    Button btnStart;
    Button btnYellow;
    Button btnGreen;
    Button btnRed;
    Button btnBlue;
    Button btnBlack;
    TextView txtWord0;
    TextView txtWord1;
    TextView txtWord2;
    TextView txtWord3;
    TextView txtWord4;
    TextView comment;
    int totalRight = 0;
    int guesses = 0;
    Random rand = new Random();
    private Stopwatch stopwatch;
    String[] colours = {"yellow", "green", "red", "blue", "black"};
    Integer wordNumber = rand.nextInt(6);
    Integer textNumber = rand.nextInt(6);
    Integer colourNumber = rand.nextInt(6);
    double time;

    boolean started = false;
    public PlayGame() {
        // Required empty public constructor
    }
    public interface PlayGameListener{
        public void startGame();
        public int btnYellowClicked();
        public int btnBlackClicked();
        public int btnBlueClicked();
        public int btnRedClicked();
        public int btnGreenClicked();
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
        View v = inflater.inflate(R.layout.fragment_play_game, container, false);


        btnGreen = (Button) (v.findViewById(R.id.btnGreen));
        btnRed = (Button) (v.findViewById(R.id.btnRed));
        btnBlue = (Button) (v.findViewById(R.id.btnBlue));
        btnStart = (Button) (v.findViewById(R.id.btnStart));
        btnYellow = (Button) (v.findViewById(R.id.btnYellow));
        btnBlack = (Button) (v.findViewById(R.id.btnBlack));
        txtWord0 = (TextView)(v.findViewById(R.id.txtWord0));
        txtWord1 = (TextView)(v.findViewById(R.id.txtWord1));
        txtWord2 = (TextView)(v.findViewById(R.id.txtWord2));
        txtWord3 = (TextView)(v.findViewById(R.id.txtWord3));
        txtWord4 = (TextView)(v.findViewById(R.id.txtWord4));
        comment = (TextView)(v.findViewById(R.id.comment));

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                gameListener.startGame();
            }
        });

        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(0);
                } else {
                    disableButtons();
                    time = stopwatch.elapsedTime();
                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
                    comment.setVisibility(VISIBLE);
                }
                gameListener.btnYellowClicked();

            }
        });

        btnGreen.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(1);
                } else {
                    disableButtons();
                    time = stopwatch.elapsedTime();
                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
                    comment.setVisibility(VISIBLE);
                }
                gameListener.btnGreenClicked();
            }
        }));

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(2);
                } else {
                    disableButtons();
                    time = stopwatch.elapsedTime();
                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
                    comment.setVisibility(VISIBLE);
                }
                gameListener.btnRedClicked();

            }
        });

        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(3);
                } else {
                    disableButtons();
                    time = stopwatch.elapsedTime();
                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
                    comment.setVisibility(VISIBLE);
                }
                gameListener.btnBlueClicked();

            }
        });

        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses += 1;
                if (guesses <= 10) {
                    checkCorrect(4);
                } else {
                    disableButtons();
                    time = stopwatch.elapsedTime();
                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
                    comment.setVisibility(VISIBLE);
                }

                gameListener.btnBlackClicked();
            }
        });


        return v;
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
        }

        return false;

    }

}
