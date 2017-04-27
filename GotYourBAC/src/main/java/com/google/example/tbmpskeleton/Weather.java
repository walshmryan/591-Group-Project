package com.google.example.tbmpskeleton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private ImageView weatherIcon;
    private ProgressBar iconProgress;
    private static String weatherIconUrl;

    public Weather(TextView tempView, ImageView weatherIcon, ProgressBar iconProgress) {
        this.tempView = tempView;
        this.weatherIcon = weatherIcon;
        this.iconProgress = iconProgress;
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
                    weatherIconUrl = j.getString("icon_url");
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
        new ImageLoadTask(weatherIconUrl, weatherIcon, iconProgress).execute();
    }
}

class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    private ProgressBar iconProgress;

    public ImageLoadTask(String url, ImageView imageView, ProgressBar iconProgress) {
        this.url = url;
        this.imageView = imageView;
        this.iconProgress = iconProgress;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
        imageView.setVisibility(View.VISIBLE);
        iconProgress.setVisibility(View.INVISIBLE);
    }

}
