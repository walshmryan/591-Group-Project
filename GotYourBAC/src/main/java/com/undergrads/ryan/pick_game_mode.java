package com.undergrads.ryan;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/*

Game mode fragment picker, prompts user to pick which game they want to play

*/
public class pick_game_mode extends Fragment {

    Button startGame;
    Button signOut;
    Button checkGames;
    Button quickGame;
    Button help;
    Button leaderboard;
    TextView txtStroop;
    TextView txtTilt;

    public pick_game_mode() {
        // Required empty public constructor
    }

    public interface gameModeListener{
        void startMatchButton();
        void onCheckGamesClicked();
        void signOutOfGoogle();
        void loadSinglePlayerGame();
        void startHelpButton();
        void showLeaderboard();
    }
    gameModeListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pick_game_mode, container, false);

        // initialize values
        signOut = (Button)v.findViewById(R.id.sign_out_button);
        startGame = (Button) v.findViewById(R.id.startMatchButton);
        checkGames = (Button)v.findViewById(R.id.checkGamesButton);
        quickGame = (Button)v.findViewById(R.id.quickMatchButon);
        help = (Button)v.findViewById(R.id.helpButton);
        leaderboard = (Button)v.findViewById(R.id.btnLeaderBoard);
        txtStroop = (TextView)v.findViewById(R.id.txtStroop);
        txtTilt = (TextView) v.findViewById(R.id.txtTilt);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startMatchButton();
            }
        });
        checkGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheckGamesClicked();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startHelpButton();
            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showLeaderboard();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.signOutOfGoogle();
            }
        });
        quickGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.loadSinglePlayerGame();
            }
        });

        checkBaselines();

        return v;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (pick_game_mode.gameModeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void checkBaselines() {

        // gets baseline score from DB and sees if a baseline has been set yet
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("baseline-stroop")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Scores baseline = dataSnapshot.getValue(Scores.class);

                        // if there is no baseline then don't allow certain buttons to be hit
                        if(baseline == null) {
                            startGame.setEnabled(false);
                            startGame.setTextColor(Color.BLACK);
                            checkGames.setEnabled(false);
                            checkGames.setTextColor(Color.BLACK);
                        } else {
                            startGame.setEnabled(true);
                            startGame.setTextColor(Color.WHITE);
                            checkGames.setEnabled(true);
                            checkGames.setTextColor(Color.WHITE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

        // gets baseline score from DB and sees if a baseline has been set yet
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("baseline-tilt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Scores baseline = dataSnapshot.getValue(Scores.class);

                        // if there is no baseline then don't allow certain buttons to be hit
                        if(baseline == null) {
                            startGame.setEnabled(false);
                            startGame.setTextColor(Color.BLACK);
                            checkGames.setEnabled(false);
                            checkGames.setTextColor(Color.BLACK);
                        } else {
                            startGame.setEnabled(true);
                            startGame.setTextColor(Color.WHITE);
                            checkGames.setEnabled(true);
                            checkGames.setTextColor(Color.WHITE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

    }

    public void showTiltTxtView(View v){

        checkBaselines();

        TextView txtStroop=(TextView) getView().findViewById(R.id.txtStroop);
        TextView txtTilt=(TextView) getView().findViewById(R.id.txtTilt);

        txtStroop.setVisibility(View.INVISIBLE);
        txtTilt.setVisibility(View.VISIBLE);
    }

    public void showStroopTxtView(View v){

        checkBaselines();

        TextView txtStroop=(TextView) getView().findViewById(R.id.txtStroop);
        TextView txtTilt=(TextView) getView().findViewById(R.id.txtTilt);

        txtStroop.setVisibility(View.VISIBLE);
        txtTilt.setVisibility(View.INVISIBLE);
    }

    // get user id
    protected String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
