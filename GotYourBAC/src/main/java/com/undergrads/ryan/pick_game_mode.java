package com.undergrads.ryan;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
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
        public void startMatchButton();
        public void onCheckGamesClicked();
        public void signOutOfGoogle();
        public void loadSinglePlayerGame();
        public void startHelpButton();
        public void showLeaderboard();
    }
    gameModeListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pick_game_mode, container, false);

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
    public void showTiltTxtView(View v){
        TextView txtStroop=(TextView) getView().findViewById(R.id.txtStroop);
        TextView txtTilt=(TextView) getView().findViewById(R.id.txtTilt);

        txtStroop.setVisibility(View.INVISIBLE);
        txtTilt.setVisibility(View.VISIBLE);
    }
    public void showStroopTxtView(View v){
//        txtStroop = (TextView)v.findViewById(R.id.txtStroop);
//        txtTilt = (TextView) v.findViewById(R.id.txtTilt);
        TextView txtStroop=(TextView) getView().findViewById(R.id.txtStroop);
        TextView txtTilt=(TextView) getView().findViewById(R.id.txtTilt);

        txtStroop.setVisibility(View.VISIBLE);
        txtTilt.setVisibility(View.INVISIBLE);
    }
}
