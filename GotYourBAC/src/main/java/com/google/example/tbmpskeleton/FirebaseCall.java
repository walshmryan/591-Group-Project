package com.google.example.tbmpskeleton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class FirebaseCall {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String currentUser;

    public FirebaseCall() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser().getUid();
    }

    public void updateICEContactInfo(String name, String number) {

        ContactInfo contact = new ContactInfo(name, number);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("contact-info", contact);

        usersRef.updateChildren(userUpdate);
    }
    public void updateUserInfo(String username, String first, String last, int weight, String gender) {

        Users user = new Users(username,first,last,weight,gender);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("users", user);

        usersRef.updateChildren(userUpdate);
    }

    public void updateGameBaseline(double score, String gameType) {

        Scores s = new Scores(currentUser, score, gameType);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("baseline-" + gameType.toLowerCase(), s);

        usersRef.updateChildren(userUpdate);
    }

    public void postScore(double scoreNum, String gameType) {
        Scores score = new Scores(currentUser, scoreNum, gameType);

        DatabaseReference postsRef = mDatabase.child("scores");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(score);
    }
}
