package com.undergrads.ryan;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.example.tbmpskeleton.R;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.undergrads.ryan.R;

//import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.app.Fragment;
//import android.app.F
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity implements LoginActivity.LoginListener,
        CreateNewUser.newUserListener, CreateICEFragment.iceCreateListener, StroopBaselineFragment.stroopBaselineListener {

    //    Login loginFragment;
    String loginTag = "login screen";
    String createNewUser = "create new user";

    final String firebaseTag = "firebase";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginActivity fragment = new LoginActivity();
        fragmentTransaction.add(R.id.main_frame, fragment, loginTag);
        fragmentTransaction.commit();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(firebaseTag, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(firebaseTag, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, final String password, final String firstName, final String lastName, final int weight, final String gender) {
        Log.d(firebaseTag, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(firebaseTag, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            http://stackoverflow.com/questions/43396781/how-to-catch-android-firebase-signupwithemailandpassword-error-code
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                // show error toast ot user ,user already exist
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            } catch (FirebaseNetworkException e) {
                                //show error tost network exception
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                                if (e.getMessage().contains("WEAK")){
                                    Toast.makeText(MainActivity.this, "Password is too weak", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
//                            Exception msg = task.getException();
//                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            ContactInfo ice = new ContactInfo();
                            writeNewUser(task.getResult().getUser(), firstName, lastName, weight, gender,ice);
                            goToICE();
                        }

                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(firebaseTag, "signIn:" + email);
//        if (!validateForm()) {
//            return;
//        }
        try{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(firebaseTag, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(firebaseTag, "signInWithEmail:failed", task.getException());
                                Toast.makeText(MainActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                goToMain();
                            }
                        }
                    });
        }catch (Exception e){
            Toast.makeText(MainActivity.this, R.string.auth_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void writeNewUser(FirebaseUser fUser, String firstName, String lastName, int weight, String gender,ContactInfo ice) {

        Users user = new Users(fUser.getEmail(), firstName, lastName, weight, gender,ice);

        mDatabase.child("users").child(fUser.getUid()).setValue(user);
    }

    @Override
    public void goToNewUserFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        CreateNewUser newFragment = new CreateNewUser();
        String createNewUser = "create new user";
        transaction.replace(R.id.main_frame, newFragment, createNewUser);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToLoginFragment(String username, String password) {
        signIn(username, password);
    }


//    @Override
//    public void switchActivity() {
//        String base = "stroop baseline";
//        String tag  =getFragmentTag();
//        if (tag ==base){
//            Intent i = new Intent(this, MenuActivity.class);
//            startActivity(i);
//        }
//    }

    public void goToICE() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        CreateICEFragment newFragment = new CreateICEFragment();
        String createNewUser = "create new user";
        transaction.replace(R.id.main_frame, newFragment, createNewUser);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void goToStroopBaseline() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StroopBaselineFragment newFragment = new StroopBaselineFragment();
//        StroopGame newFragment = new StroopGame();
        String createNewFrag = "stroop baseline";
        transaction.replace(R.id.main_frame, newFragment, createNewFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void goToCreateNew(String email, String password, String firstName, String lastName, String weight, String gender) {
        try {
            int w = Integer.parseInt(weight);
            createAccount(email,password,firstName,lastName,w,gender);
        }catch (Exception e){
            Toast toast = Toast.makeText(this.getApplicationContext(),"Create New User Failed",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void goToRetrievePassword(String email){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ForgotPassword newFragment = new ForgotPassword();
        String forgotpw = "forgot password";
        transaction.replace(R.id.main_frame, newFragment, forgotpw);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public String getFragmentTag(){
        FragmentManager fragment = getFragmentManager();
        Fragment curFrag = fragment.findFragmentById(R.id.frame_layout);
        return (curFrag.getTag().toString());

    }

    @Override
    public void goToMain() {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
}