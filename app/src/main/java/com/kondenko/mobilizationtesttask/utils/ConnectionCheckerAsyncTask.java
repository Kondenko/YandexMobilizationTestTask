package com.kondenko.mobilizationtesttask.utils;

import android.os.AsyncTask;

import com.kondenko.mobilizationtesttask.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Checks if a device has a working internet connection
 *
 * stackoverflow.com/questions/6493517/detect-if-android-device-has-internet-connection
 */
public class ConnectionCheckerAsyncTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Constants.ARTISTS_JSON_URL).openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
