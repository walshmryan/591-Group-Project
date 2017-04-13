package com.google.example.tbmpskeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.undergrads.ryan.R;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.net.MalformedURLException;

public class MainActivity extends FragmentActivity implements LoginActivity.LoginListener {

    //    Login loginFragment;
    String loginTag = "login screen";
    String createNewUser = "create new user";

    final String firebaseTag = "firebase";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseApp.initializeApp(MainActivity.this);

//        String loginTag = "login screen";
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginActivity fragment = new LoginActivity();
        fragmentTransaction.add(R.id.main_frame, fragment, loginTag);
        fragmentTransaction.commit();

//        loginFragment = (Login) findFragmentById(R.layout.fragment_login);


        mAuth = FirebaseAuth.getInstance();

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

    private void createAccount(String email, String password) {
        Log.d(firebaseTag, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(firebaseTag, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            switchActivity();
                        }

                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(firebaseTag, "signIn:" + email);
//        if (!validateForm()) {
//            return;
//        }

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
                            switchActivity();
                        }

                    }
                });
    }


    private void signOut() {
        mAuth.signOut();
    }


    @Override
    public void goToNewUserFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CreateNewUser newFragment = new CreateNewUser();
        String createNewUser = "create new user";
        transaction.replace(R.id.main_frame, newFragment, createNewUser);
        transaction.addToBackStack(null);

//         Commit the transaction
        transaction.commit();
    }

    public void goToLoginFragment(String username, String password) {

        signIn(username, password);

//        new LoginCheck(this, i, mClient).execute(username,password);

//        check user login information
//        then go to main activity page
//        String loginTag = "login screen";
//        String createNewUser = "create new user";
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        Fragment loginFragment = getSupportFragmentManager().findFragmentByTag(loginTag);
//        if (loginFragment!= null){
//            transaction.remove(loginFragment).commit();
//        }
//        Fragment newUserFragment = getSupportFragmentManager().findFragmentByTag(createNewUser);
//        if (newUserFragment != null){
//            transaction.remove(newUserFragment).commit();
//        }
    }

    public void switchActivity() {
        Intent i = new Intent(this, MenuActivity.class);
        System.out.println("=================");
        System.out.println("switching activity");
        System.out.println("switching activity");
        startActivity(i);
    }
}