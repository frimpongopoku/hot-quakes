package com.frimpong.hot_quakes;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetExplorer extends AsyncTask {
    public AfterEffect onComplete;
    public InternetExplorer(){
        this.onComplete = onComplete;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml");
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();
            if (code !=  200) {
                // Use your error interface in here
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;

            final StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o== null) onComplete.sendResponse(null);
        else onComplete.sendResponse(String.valueOf(o));
    }
}