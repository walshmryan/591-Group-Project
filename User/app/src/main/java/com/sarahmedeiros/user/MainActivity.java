package com.sarahmedeiros.user;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
/**
 * Created by sarahmedeiros on 3/31/17.
 */

public class MainActivity extends FragmentActivity implements Login.LoginListener {

//    Login loginFragment;
    String loginTag = "login screen";
    String createNewUser = "create new user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        String loginTag = "login screen";
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Login fragment = new Login();
        fragmentTransaction.add(R.id.fragment_container, fragment, loginTag);
        fragmentTransaction.commit();

//        loginFragment = (Login) findFragmentById(R.layout.fragment_login);
    }

    @Override
    public void goToNewUserFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CreateNewUser newFragment = new CreateNewUser();
//        String createNewUser = "create new user";
        transaction.replace(R.id.fragment_container, newFragment, createNewUser);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void goToLoginFragment() {
//        check user login information
//        then go to main activity page
//        String loginTag = "login screen";
//        String createNewUser = "create new user";
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment loginFragment = getSupportFragmentManager().findFragmentByTag(loginTag);
        if (loginFragment!= null){
            transaction.remove(loginFragment).commit();
        }
        Fragment newUserFragment = getSupportFragmentManager().findFragmentByTag(createNewUser);
        if (newUserFragment != null){
            transaction.remove(newUserFragment).commit();
        }
    }
//
//    @Override
//    public void sendEmail(String e) {
//        CreateNewUser receivingFragment = (CreateNewUser) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        receivingFragment.setEmailFromLogin(e);
//    }
}