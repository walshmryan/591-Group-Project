package com.undergrads.ryan;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*

Fragment for the tilt game that handles all logic and score calculation

*/
public class fragment_tilt extends Fragment {
    private String[] directions = {"FORWARD", "BACK", "LEFT", "RIGHT"};
    private Queue<Integer> sequence = new LinkedList<Integer>();
    private SensorManager mSensorManager=null;
    private Button btnNextRound;
    private Button btnDone;
    private TextView txtCountdown;
    private TextView txtDirection;
    private ProgressBarAnimation anim1;
    private ProgressBarAnimation anim2;
    private ProgressBarAnimation anim3;
    private ProgressBarAnimation anim4;
    private ProgressBarAnimation anim5;
    private ProgressBar pBarOne;
    private ProgressBar pBarTwo;
    private ProgressBar pBarThree;
    private ProgressBar pBarFour;
    private ProgressBar pBarFive;
    private ProgressBar pCurrentBar;
    private ImageView imgDirection;
    private Integer rounds = 2;
    private Integer roundCountDown = rounds;
    private Boolean checkGuesses = false;
    private Boolean firstTick = true;
    private Boolean lastGuessCorrect;
    private Integer totalCorrect  = 0;
    private Integer tick = 1;
    private Integer delay = 2; //+2 for remember the following pattern
    private Integer ready = 3; //+3 for ready?
    private Integer generateSequenceTime = 5;//time to generate things per second
    private Integer timePerGuess = 3; //how much time per guess
    private Integer lastDirection = -1; //init so it won't crash
    private Integer guesses = generateSequenceTime;

    private Integer extraSec = 2;
    private Integer guessingTime = generateSequenceTime*timePerGuess;//time to guess
    private Integer roundLength =  guessingTime + generateSequenceTime + delay + ready + extraSec;
                                         //first num - generateSequenceTime = time to guess
    float pitch = 0;
    float roll = 0;
    private MyCountDownTimer myCountDownTimer;
    private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms
    private WindowManager mWindowManager;
    private int mLastAccuracy;
    public interface tiltGameListener {
        public void goToMainGameScreen();
    }
    tiltGameListener tiltListener;

    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fragment_tag","TILT");
    }
    ////////////////////////////////////////////////////////////
    /*SET UP THE SENSOR LISTENER*/
    SensorEventListener listener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent e) {
            if (e.sensor.getType()== Sensor.TYPE_ROTATION_VECTOR) {
                if ((checkGuesses == true) && (sequence.peek() != null)){
                    getPitchRoll(e.values);
                }
            }
            if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                return;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            if (mLastAccuracy != accuracy) {
                mLastAccuracy = accuracy;
            }
        }

    };
    ////////////////////////////////////////////////////////////
    /*Create View*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tilt, container, false);

        imgDirection = (ImageView) v.findViewById(R.id.imgDirection);
        btnNextRound = (Button) v.findViewById(R.id.btnNextRound);
        btnDone = (Button) v.findViewById(R.id.btnDone1);
        txtCountdown = (TextView) v.findViewById(R.id.txtCountdown);
        txtDirection = (TextView) v.findViewById(R.id.txtDirection);

        //set up animations
        pBarOne = (ProgressBar) v.findViewById(R.id.pBarOne);
        anim1 = new ProgressBarAnimation(pBarOne, 0, 100);
        anim1.setDuration(3000);

        pBarTwo = (ProgressBar) v.findViewById(R.id.pBarTwo);
        anim2 = new ProgressBarAnimation(pBarTwo, 0, 100);
        anim2.setDuration(3000);

        pBarThree = (ProgressBar) v.findViewById(R.id.pBarThree);
        anim3 = new ProgressBarAnimation(pBarThree, 0, 100);
        anim3.setDuration(3000);

        pBarFour = (ProgressBar) v.findViewById(R.id.pBarFour);
        anim4 = new ProgressBarAnimation(pBarFour, 0, 100);
        anim4.setDuration(3000);

        pBarFive = (ProgressBar) v.findViewById(R.id.pBarFive);
        anim5 = new ProgressBarAnimation(pBarFive, 0, 100);
        anim5.setDuration(3000);

        mWindowManager = getActivity().getWindow().getWindowManager();
        mSensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);

        mSensorManager.registerListener(listener,mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI);

        //try this shit out
        btnNextRound.setVisibility(View.INVISIBLE);
        progressVisible(View.INVISIBLE);
        imgDirection.setVisibility(View.INVISIBLE);

        myCountDownTimer = new MyCountDownTimer(roundLength*1000, tick*1000);
        myCountDownTimer.start();

        btnNextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guesses = generateSequenceTime;
                txtCountdown.setVisibility(View.INVISIBLE);
                btnNextRound.setVisibility(View.INVISIBLE);
                myCountDownTimer = new MyCountDownTimer(roundLength*1000, tick*1000);//
                myCountDownTimer.start();
                roundCountDown -= 1;
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tiltListener.goToMainGameScreen();
            }
        });
        return v;
    }

    public void progressVisible(Integer visible) {
        pBarOne.setVisibility(visible);
        pBarTwo.setVisibility(visible);
        pBarThree.setVisibility(visible);
        pBarFour.setVisibility(visible);
        pBarFive.setVisibility(visible);

    }

    public void progressReset() {
        pBarOne.setProgress(0);
        pBarOne.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        pBarTwo.setProgress(0);
        pBarTwo.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        pBarThree.setProgress(0);
        pBarThree.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        pBarFour.setProgress(0);
        pBarFour.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        pBarFive.setProgress(0);
        pBarFive.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));

    }

    ////////////////////////////////////////////////////////////
    /*TIMER CLASS*/
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int seconds = (int) (millisUntilFinished / 1000);

            //Start of the game printing out remember the pattern
            if (seconds > (roundLength - delay)) {
                txtDirection.setText("Remember the pattern..");

            //Generates queue of directions to remember and repeat
            } else if (seconds > (roundLength - delay - generateSequenceTime)) {

                //choose a random direction
                Random ran = new Random();

                //don't repeat directions
                int directionIndex;
                do {
                    directionIndex = ran.nextInt(directions.length);
                } while (directionIndex == lastDirection);
                lastDirection = directionIndex;

                //add direction to queue
                sequence.add(directionIndex);
                txtDirection.setText("");
                txtDirection.setText(directions[directionIndex]);
                txtCountdown.setText(String.format(""));
                imgDirection.setVisibility(View.VISIBLE);

                if (isAdded()) {
                    if (directions[directionIndex].equals("LEFT")) {
                        imgDirection.setImageDrawable(getResources().getDrawable(R.drawable.arrowleft));
                    } else if (directions[directionIndex].equals("RIGHT")) {
                        imgDirection.setImageDrawable(getResources().getDrawable(R.drawable.arrowright));

                    } else if (directions[directionIndex].equals("FORWARD")) {
                        imgDirection.setImageDrawable(getResources().getDrawable(R.drawable.arrowforward));

                    } else if (directions[directionIndex].equals("BACK")) {
                        imgDirection.setImageDrawable(getResources().getDrawable(R.drawable.arrowback));
                    }
                }

            //Give a few second leeway before they have the recollect
            } else if (seconds > (roundLength - delay - generateSequenceTime - ready)) {
                imgDirection.setVisibility(View.INVISIBLE);
                txtDirection.setText("Got it?...");
                progressVisible(View.VISIBLE);
                progressReset();

            //Time to have them repeat the sequence and check their answers
            } else {
                checkGuesses = true;

                if (((seconds % timePerGuess) == (extraSec%timePerGuess))) {
                    txtDirection.setText("Repeat!");

                    if (!firstTick) {

                        //check correctness of guesses
                        Integer currentSequenceElement = sequence.remove();
                        if (directions[currentSequenceElement] == "LEFT") {
                            if (roll > 0) {
                                txtDirection.setText("CORRECT");
                                lastGuessCorrect = true;
                                totalCorrect += 1;
                            } else {
                                txtDirection.setText("WRONG - LEFT");
                                lastGuessCorrect = false;
                            }

                        } else if (directions[currentSequenceElement] == "RIGHT") {
                            if (roll < 0) {
                                txtDirection.setText("CORRECT");
                                lastGuessCorrect = true;
                                totalCorrect += 1;
                            } else {
                                txtDirection.setText("WRONG - RIGHT");
                                lastGuessCorrect = false;
                            }

                        } else if (directions[currentSequenceElement] == "FORWARD") {
                            if (pitch > 0) {
                                txtDirection.setText("CORRECT");
                                lastGuessCorrect = true;
                                totalCorrect += 1;
                            } else {
                                txtDirection.setText("WRONG - FORWARD");
                                lastGuessCorrect = false;
                            }

                        } else if (directions[currentSequenceElement] == "BACK") {
                            if (pitch < 0) {
                                txtDirection.setText("CORRECT");
                                lastGuessCorrect = true;
                                totalCorrect += 1;
                            } else {
                                txtDirection.setText("WRONG - BACK");
                                lastGuessCorrect = false;
                            }

                        }


                        //set the prog bar that just finished to green or red depending if they got
                        // it right
                        if (isAdded()) {
                            if (lastGuessCorrect) {
                                pCurrentBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgreen)));
                            } else {
                                pCurrentBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkred)));

                            }
                        }
                    }

                    //start animation of the progress bar
                    if (guesses == 1) {
                        pBarOne.startAnimation(anim1);
                        pCurrentBar = pBarOne;
                        Log.d("TEST", "pBarSet Fifth");
                    } else if (guesses == 2) {
                        pBarTwo.startAnimation(anim2);
                        pCurrentBar = pBarTwo;
                        Log.d("TEST", "pBarSet Fourth");
                    } else if (guesses == 3) {
                        pBarThree.startAnimation(anim3);
                        pCurrentBar = pBarThree;
                        Log.d("TEST", "pBarSet Third");
                    } else if (guesses == 4) {
                        pBarFour.startAnimation(anim4);
                        pCurrentBar = pBarFour;
                        Log.d("TEST", "pBarSet Second");
                    } else if (guesses == 5) {
                        pBarFive.startAnimation(anim5);
                        pCurrentBar = pBarFive;
                        Log.d("TEST", "pBarSet First");
                    }
                    guesses -= 1;


                }
                firstTick = false;

            }
        }
            @Override
            public void onFinish () {
                //once the round is done check if there are more to go
                roundCountDown -=1;
                if (roundCountDown >= 0) {
                    progressVisible(View.INVISIBLE);
                    checkGuesses = false;
                    firstTick = true;
                    txtCountdown.setText(String.format("ROUND OVER"));
                    txtDirection.setText("");
                    btnNextRound.setVisibility(View.VISIBLE);
                }else {
                    progressVisible(View.INVISIBLE);
                    btnDone.setVisibility(View.VISIBLE);

                    Log.i("DEBUG", totalCorrect + "");
                    Log.i("DEBUG", generateSequenceTime + "");
                    Log.i("DEBUG", rounds + "");

                    double score = ((double)totalCorrect/(double)(generateSequenceTime*rounds)) * 100;
                    txtDirection.setText(score + "%");
                    txtCountdown.setVisibility(View.VISIBLE);
                    txtCountdown.setText(String.format("COMPLETE"));

                    String tag = getFragmentTag();

                    if(tag.equals("TILTBaseline")) {
                        new FirebaseCall().updateGameBaseline(score, "Tilt");
                    } else {
                        new FirebaseCall().postScore(score, "Tilt");
                    }
                }

            }

    }

    ////////////////////////////////////////////////////////////
    /*HELPER CLASS TO GET ORIENTATION OF THE SCREEEN*/
    private void getPitchRoll(float[] rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        // Remap the axes as if the device screen was the instrument panel,
        // and adjust the rotation matrix for the device orientation.

        worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
        worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;

        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix);

        // Transform rotation matrix into azimuth(discard)/pitch(forward/back)/roll(left/right)
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // Convert radians to degrees
        pitch = orientation[1] * -57;
        roll = orientation[2] * -57;
    }

    ////////////////////////////////////////////////////////////
    /*PROGRESS BAR CLASS TO IMPROVE LOOK AND FEEL*/
    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            tiltListener = (tiltGameListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public String getFragmentTag(){
        FragmentManager fragment = getFragmentManager();
        Fragment curFrag = fragment.findFragmentById(R.id.frame_layout);

        if(curFrag == null) {
            curFrag = fragment.findFragmentById(R.id.main_frame);
        }
        return (curFrag.getTag().toString());
    }


    ////////////////////////////////////////////////////////////
    /*END*/

}



