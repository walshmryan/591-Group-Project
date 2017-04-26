//package com.google.example.tbmpskeleton;
//
///**
// * Created by sarahmedeiros on 4/11/17.
// */
//
//
////import android.graphics.Color;
////        import android.support.v7.app.AppCompatActivity;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.TextView;
//
////        import com.undergrads.ryan.R;
////
//        import java.util.Random;
//
//        import java.util.List;
//
//        import static android.view.View.VISIBLE;
//
//public class GameActivity extends AppCompatActivity {
//
//    Button btnStart;
//    Button btnYellow;
//    Button btnGreen;
//    Button btnRed;
//    Button btnBlue;
//    Button btnBlack;
//    TextView txtView0;
//    TextView txtView1;
//    TextView txtView2;
//    TextView txtView3;
//    TextView txtView4;
//    TextView comment;
//    int totalRight = 0;
//    int guesses = 0;
//    Random rand = new Random();
//    private Stopwatch stopwatch;
//    String[] colours = {"yellow", "green", "red", "blue", "black"};
//    Integer wordNumber = rand.nextInt(6);
//    Integer textNumber = rand.nextInt(6);
//    Integer colourNumber = rand.nextInt(6);
//    double time;
//
//    boolean started = false;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        btnGreen = (Button) (findViewById(R.id.btnGreen));
//        btnRed = (Button) (findViewById(R.id.btnRed));
//        btnBlue = (Button) (findViewById(R.id.btnBlue));
//        btnStart = (Button) (findViewById(R.id.btnStart));
//        btnYellow = (Button) (findViewById(R.id.btnYellow));
//        btnBlack = (Button) (findViewById(R.id.btnBlack));
//        txtView0 = (TextView)(findViewById(R.id.txtView0));
//        txtView1 = (TextView)(findViewById(R.id.txtView1));
//        txtView2 = (TextView)(findViewById(R.id.txtView2));
//        txtView3 = (TextView)(findViewById(R.id.txtView3));
//        txtView4 = (TextView)(findViewById(R.id.txtView4));
//        comment = (TextView)(findViewById(R.id.comment));
//
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableButtons();
//                started = true;
//                guesses = 0;
//                totalRight = 0;
//                stopwatch = new Stopwatch();
//                comment.setVisibility(View.INVISIBLE);
//                txtView0.setVisibility(View.INVISIBLE);
//                txtView1.setVisibility(View.INVISIBLE);
//                txtView2.setVisibility(View.INVISIBLE);
//                txtView3.setVisibility(View.INVISIBLE);
//                txtView4.setVisibility(View.INVISIBLE);
//                nextWord();
//            }
//        });
//
//        btnYellow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guesses += 1;
//                if (guesses <= 10) {
//                    checkCorrect(0);
//                } else {
//                    disableButtons();
//                    time = stopwatch.elapsedTime();
//                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
//                    comment.setVisibility(VISIBLE);
//                }
//            }
//        });
//
//        btnGreen.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guesses += 1;
//                if (guesses <= 10) {
//                    checkCorrect(1);
//                } else {
//                    disableButtons();
//                    time = stopwatch.elapsedTime();
//                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
//                    comment.setVisibility(VISIBLE);
//                }
//            }
//        }));
//
//        btnRed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guesses += 1;
//                if (guesses <= 10) {
//                    checkCorrect(2);
//                } else {
//                    disableButtons();
//                    time = stopwatch.elapsedTime();
//                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
//                    comment.setVisibility(VISIBLE);
//                }
//            }
//        });
//
//        btnBlue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guesses += 1;
//                if (guesses <= 10) {
//                    checkCorrect(3);
//                } else {
//                    disableButtons();
//                    time = stopwatch.elapsedTime();
//                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
//                    comment.setVisibility(VISIBLE);
//                }
//            }
//        });
//
//        btnBlack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guesses += 1;
//                if (guesses <= 10) {
//                    checkCorrect(4);
//                } else {
//                    disableButtons();
//                    time = stopwatch.elapsedTime();
//                    comment.setText("You scored " + totalRight + "/10 in " + time + " seconds");
//                    comment.setVisibility(VISIBLE);
//                }
//            }
//        });
//    }
//
//
//    private void textSet(TextView txtView, int wordNum, int colourNum){
//        txtView.setText(colours[wordNumber]);
//        if (colourNum == 0) {
//            txtView.setTextColor(getResources().getColor(R.color.yellow));
//            txtView.setVisibility(VISIBLE);
//        }
//        else if (colourNum == 1) {
//            txtView.setTextColor(getResources().getColor(R.color.green));
//            txtView.setVisibility(VISIBLE);
//        }
//        else if (colourNum == 2) {
//            txtView.setTextColor(getResources().getColor(R.color.red));
//            txtView.setVisibility(VISIBLE);
//        }
//        else if (colourNum == 3) {
//            txtView.setTextColor(getResources().getColor(R.color.blue));
//            txtView.setVisibility(VISIBLE);
//        }
//        else if (colourNum == 4) {
//            txtView.setTextColor(getResources().getColor(R.color.black));
//            txtView.setVisibility(VISIBLE);
//        }
//    }
//
//    private void disableButtons(){
//        btnYellow.setEnabled(false);
//        btnGreen.setEnabled(false);
//        btnBlack.setEnabled(false);
//        btnBlue.setEnabled(false);
//        btnRed.setEnabled(false);
//    }
//
//    private void enableButtons(){
//        btnYellow.setEnabled(true);
//        btnGreen.setEnabled(true);
//        btnBlack.setEnabled(true);
//        btnBlue.setEnabled(true);
//        btnRed.setEnabled(true);
//    }
//
//    private void nextWord() {
//        wordNumber = rand.nextInt(5);
//        textNumber = rand.nextInt(5);
//        colourNumber = rand.nextInt(5);
//
//        // TESTING
////        wordNumber = 3;
////        textNumber = 4;
////        colourNumber = 1;
//        //TESTING
//
//        if (textNumber == 0) {
//            textSet(txtView0, wordNumber, colourNumber);
//        }
//        else if (textNumber == 1) {
//            textSet(txtView1, wordNumber, colourNumber);
//        }
//        else if (textNumber == 2) {
//            textSet(txtView2, wordNumber, colourNumber);
//        }
//        else if (textNumber == 3) {
//            textSet(txtView3, wordNumber, colourNumber);
//        }
//        else if (textNumber == 4) {
//            textSet(txtView4, wordNumber, colourNumber);
//        }
//    }
//
//    private boolean checkCorrect(int c) {
//        if (colourNumber == c) {
//            //txtView0.setText("TEST");
//            //txtView0.setVisibility(VISIBLE);
//            totalRight += 1;
//            txtView0.setVisibility(View.INVISIBLE);
//            txtView1.setVisibility(View.INVISIBLE);
//            txtView2.setVisibility(View.INVISIBLE);
//            txtView3.setVisibility(View.INVISIBLE);
//            txtView4.setVisibility(View.INVISIBLE);
//            nextWord();
//            return true;
//        }
//
//        return false;
//    }
//}
//
//
//
