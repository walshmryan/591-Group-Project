package com.google.example.tbmpskeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.undergrads.ryan.R;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import java.net.MalformedURLException;

public class MainActivity extends FragmentActivity implements LoginActivity.LoginListener {

    //    Login loginFragment;
    String loginTag = "login screen";
    String createNewUser = "create new user";

    MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

//        String loginTag = "login screen";
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginActivity fragment = new LoginActivity();
        fragmentTransaction.add(R.id.fragment_login_container, fragment, loginTag);
        fragmentTransaction.commit();

//        loginFragment = (Login) findFragmentById(R.layout.fragment_login);
    }

    @Override
    public void goToNewUserFragment() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        CreateNewUser newFragment = new CreateNewUser();
//        String createNewUser = "create new user";
//        transaction.replace(R.id.fragment_container, newFragment, createNewUser);
//        transaction.addToBackStack(null);

        // Commit the transaction
//        transaction.commit();
    }

    public void goToLoginFragment(String username, String password) {

        try {
            mClient = new MobileServiceClient("https://flashcard-app.azurewebsites.net", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, SkeletonActivity.class);

        new LoginCheck(this, i, mClient).execute(username,password);

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
//
//    @Override
//    public void sendEmail(String e) {
//        CreateNewUser receivingFragment = (CreateNewUser) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        receivingFragment.setEmailFromLogin(e);
//    }
}