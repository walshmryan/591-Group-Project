package com.undergrads.ryan;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class pick_game_mode extends Fragment {

    Button startGame;
    Button signOut;
    Button checkGames;
    Button quickGame;

    public pick_game_mode() {
        // Required empty public constructor
    }

    public interface gameModeListener{
        public void startMatchButton();
        public void onCheckGamesClicked();
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
}
