package com.google.example.tbmpskeleton;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.undergrads.ryan.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainContent extends Activity {

    TextView temp;
    TextView weatherFor;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
//
//        temp = (TextView) findViewById(R.id.textView) ;
//        weatherFor = (TextView) findViewById(R.id.textView1);
//
//        // get current location
//        city = "Boston";
//
//        weatherFor.setText("Here's your weather for " + city);
//        new Weather(this, temp).execute("http://api.wunderground.com/api/fd527dc2ea48e15c/conditions/q/MA/Boston.json");

    }

}


