package com.undergrads.ryan;

import android.content.Context;
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


public class stroop_game_done extends Fragment {

    private stroopGameListener listener;
    private TextView txtScore;
    private TextView baselineScore;
    private TextView baselineComparison;
    private Button btnDone;

    public interface stroopGameListener {
        // TODO: Update argument type and name
        double getScore();
        void goToMainGameScreen();
    }

    public stroop_game_done() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_stroop_done, container, false);
        txtScore = (TextView) v.findViewById(R.id.score_display);
        baselineScore = (TextView) v.findViewById(R.id.baselineScore);
        btnDone = (Button)v.findViewById(R.id.win_ok_button);
        baselineComparison = (TextView) v.findViewById(R.id.baselineComparison);

        // gets baseline score from DB
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("baseline-stroop")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Scores baseline = dataSnapshot.getValue(Scores.class);

                        if(baseline != null) {
                            baselineScore.setText("Baseline: " + baseline.getScore());
                            compareScores(baseline.getScore(), listener.getScore());
                        } else {
                            baselineComparison.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });

        txtScore.setText("Time: " + listener.getScore());

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToMainGameScreen();
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof stroopGameListener) {
            listener = (stroopGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //    get user id
    protected String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    protected void compareScores(double baselineScore, double currentScore) {
        //compare their scores to their baseline to show them how they did 
        String message = "";

        if(baselineScore > currentScore) {
            message = getString(R.string.sobriety_level_0);
        } else {
            double diff = Math.abs(baselineScore - currentScore);

            if(diff < 3) {
                message = getString(R.string.sobriety_level_1);
            } else if(diff < 7) {
                message = getString(R.string.sobriety_level_2);
            } else {
                message = getString(R.string.sobriety_level_3);
            }
        }

        baselineComparison.setText(message);

    }

}
