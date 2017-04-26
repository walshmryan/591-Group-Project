package com.undergrads.ryan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Weather extends AsyncTask<String,Void,String> {

    private TextView tempView;


    public Weather(TextView tempView) {
        this.tempView = tempView;

    }


    @Override
    protected String doInBackground(String... arg) {

        int i;
        char c;
        try {

            URL url = new URL(arg[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String s = "";
                while((i = in.read())!=-1) {
                    c = (char)i;
                    s += c;
                }
                try {
                    JSONObject json = new JSONObject(s);
                    JSONObject j = (JSONObject) json.get("current_observation");
                    return j.getString("temperature_string");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } finally {
                urlConnection.disconnect();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "no data";
    }

    @Override
    protected void onPostExecute(String temp) {
        tempView.setText(temp);
    }
}
