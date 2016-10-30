package ru.alexandertsebenko.shoplist2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.net.Client;

public class NetworkChangeReceiver extends BroadcastReceiver {

    boolean mIsConnected;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        mIsConnected = (netInfo != null && netInfo.isConnected());
        if(mIsConnected) {
            Client c = new Client();
             c.getPpb("9139209220");
        } else {
            Toast.makeText(context, getClass().getSimpleName() + ": Disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}
