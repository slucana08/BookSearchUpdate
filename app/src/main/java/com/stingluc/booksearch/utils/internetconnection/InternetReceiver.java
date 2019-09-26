package com.stingluc.booksearch.utils.internetconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetReceiver extends BroadcastReceiver {

    private Connectivity connectivity = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context)) {
            connectivity.onChanged(true);
        } else {
            connectivity.onChanged(false);
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public interface Connectivity {
        void onChanged(boolean isConnected);
    }


}
