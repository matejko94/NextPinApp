package com.example.nextpin.myNextPinApp.AtBoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.nextpin.myNextPinApp.preference.SharedPrefUtils;

import net.nextpin.geolib.NextPin;


public class StartMyServiceAtBootReciever extends BroadcastReceiver {
    public static String PROP_TOKEN = "PREF_TOKEN";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i("net.nextpin.example","Start alarm");
            String nextPinToken = SharedPrefUtils.getString(context, PROP_TOKEN);

            if (nextPinToken != null && !nextPinToken.equals("")) {
            NextPin nextpin = NextPin.getNextPinInstance(context);
                try {
                    nextpin.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
             }
        }//if boot intent
    }//onReceive
}