package com.frimpong.hot_quakes;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetExplorer {
    public static final String EARTH_QUAKE_URL = "http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";
    public AfterEffect onComplete;
    String url;

    public InternetExplorer(String url, AfterEffect onComplete){
        this.url = url;
        this.onComplete = onComplete;
    }

    public void fetchDataInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;

                try {
                    URL urlObj = new URL(url);
                    urlConnection = (HttpURLConnection) urlObj.openConnection();
                    int code = urlConnection.getResponseCode();

                    if (code != 200) {
                        // Use your error interface in here
                        throw new IOException("Invalid response from server: " + code);
                    }

                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;

                    final StringBuilder sb = new StringBuilder();
                    while ((line = rd.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    onComplete.sendResponse(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }


}