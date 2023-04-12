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

    // A simple class that accepts a URL and presents it's response as a string via the AfterEffect callback interface
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
                        // Response failed somehow, so notify the callback with a null value
                        onComplete.sendResponse(null);
                    }

                    // Here we stream the response from the request per line
                    // Into one bundled string, and transfer it via the callback interface(i.e. AfterEffect)
                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;

                    final StringBuilder sb = new StringBuilder();
                    while ((line = rd.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    // We pass on the bundled string here
                    onComplete.sendResponse(sb.toString());
                } catch (Exception e) {
                    onComplete.sendResponse(null);
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