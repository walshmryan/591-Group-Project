package com.google.example.tbmpskeleton;

/**
 * Created by walsh on 4/6/17.
 */

import android.os.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;


//https://www.tutorialspoint.com/android/android_php_mysql.htm


public class LoginCheck extends AsyncTask<String,Void,Boolean> {

    private Context context;
    private Intent i;
    private MobileServiceClient mClient;



    public LoginCheck(Context context, Intent i, MobileServiceClient mClient) {
        this.context = context;
        this.i = i;
        this.mClient = mClient;
    }


    @Override
    protected Boolean doInBackground(String... arg) {

        MobileServiceTable<Users> mUser = mClient.getTable("Users", Users.class);

        String username = arg[0];
        String password = arg[1];

        try {
            final MobileServiceList<Users> result = mUser.where().field("username").eq(username).execute().get();

            if(result.size() > 0) {
                System.out.println("found a user matching username: " + username);

                System.out.println("username: " + result.get(0).username);
                System.out.println("password: " + result.get(0).password);

                if(result.get(0).password.equals(password)) {
                    System.out.println("password matched");
                    return true;
                }
            }

            return false;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean match) {

        if(match) {
            context.startActivity(i);
        }

    }
}
