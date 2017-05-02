package com.undergrads.ryan;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

/*

Class that handles all Firebase updates to DB, like scores, contact info, drink totals

*/
public class FirebaseCall {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String currentUser;

    // gets firebase instance
    public FirebaseCall() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser().getUid();
    }

    // updates emergency contact info
    public void updateICEContactInfo(String name, String number) {

        ContactInfo contact = new ContactInfo(name, number);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("contact-info", contact);

        usersRef.updateChildren(userUpdate);
    }

    // updates user account info
    public void updateUserInfo(String username, String first, String last, int weight, String gender) {

        Users user = new Users(username,first,last,weight,gender);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("users", user);

        usersRef.updateChildren(userUpdate);
    }

    // updates the users baseline score
    public void updateGameBaseline(double score, String gameType) {

        Scores s = new Scores(currentUser, score, gameType);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("baseline-" + gameType.toLowerCase(), s);

        usersRef.updateChildren(userUpdate);
    }

    // updates the drink totals of a user
    public void updateDrinkTotals(int totalHard, int totalWine, int totalBeer) {

        // FYI resets time stamp for drinks
        Drinks d = new Drinks(totalHard, totalWine, totalBeer);

        DatabaseReference usersRef = mDatabase.child("users").child(currentUser);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("drink-totals", d);

        usersRef.updateChildren(userUpdate);
    }

    // posts a score object to the DB
    public void postScore(double scoreNum, String gameType) {
        Scores score = new Scores(currentUser, scoreNum, gameType);

        DatabaseReference postsRef = mDatabase.child("scores");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(score);
    }
}
