package com.google.example.tbmpskeleton;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.undergrads.ryan.R;


public class HomeFragment extends Fragment{

    private TextView temperature;
    private TextView weatherForCity;
    private String city;
    private TextView score1;
    private TextView score2;
    private TextView score3;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        temperature = (TextView) v.findViewById(R.id.temperature) ;
        weatherForCity = (TextView) v.findViewById(R.id.weatherForCity);
        score1 = (TextView) v.findViewById(R.id.score1);
        score2 = (TextView) v.findViewById(R.id.score1);
        score3 = (TextView) v.findViewById(R.id.score1);

        // get current location
        city = "Boston";

        weatherForCity.setText("Here's your weather for " + city + " today:");
        new Weather(temperature).execute("http://api.wunderground.com/api/fd527dc2ea48e15c/conditions/q/MA/Boston.json");


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uId = mAuth.getCurrentUser().getUid();


        Scores score = new Scores(uId, 90.0, "Stroop");
        Scores score1 = new Scores(uId, 80.0, "Stroop");

        DatabaseReference postsRef = mDatabase.child("scores");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(score);

        newPostRef = postsRef.push();
        newPostRef.setValue(score1);

        FirebaseDatabase.getInstance().getReference().child("scores").orderByChild("timestamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Scores score = snapshot.getValue(Scores.class);
                            postScore(score, count);
                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("error","bad");
                    }
                });


        return v;
    }

    protected void postScore(Scores s, int index) {
        String score = s.getGameType() + ": " + s.getScore();
        if(index == 0) {
            score1.setText(score);
        }
        else if(index == 1) {
            score2.setText(score);
        }
        else if(index == 2) {
            score3.setText(score);
        }

    }

}

