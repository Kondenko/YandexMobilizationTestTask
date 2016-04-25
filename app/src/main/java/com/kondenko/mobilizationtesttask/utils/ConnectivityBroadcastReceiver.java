package com.kondenko.mobilizationtesttask.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Checks if there's internet connection and sends the result outside the receiver
 */
public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    private ConnectivityListener mListener;

    private boolean mLastCheckResult;

    public void setConnectivityListener(ConnectivityListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        checkConnection(context);
    }

    private void checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean available = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        mLastCheckResult = available;
        mListener.onConnectionChecked(available);
    }

    public boolean isConnectionAvailable() {
        return mLastCheckResult;
    }

    public interface ConnectivityListener {
        void onConnectionChecked(boolean available);
    }

}
