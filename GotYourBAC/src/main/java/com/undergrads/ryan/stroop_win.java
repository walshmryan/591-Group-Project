package com.undergrads.ryan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class stroop_win extends Fragment {

    private stroopGameListener listener;
    private TextView txtScore;

    public interface stroopGameListener {
        // TODO: Update argument type and name
        public int getScore();
    }

    public stroop_win() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_stroop_win, container, false);
        txtScore = (TextView) v.findViewById(R.id.score_display);
        txtScore.setText("You scored " + listener.getScore()+ " out of 10" );

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

}
