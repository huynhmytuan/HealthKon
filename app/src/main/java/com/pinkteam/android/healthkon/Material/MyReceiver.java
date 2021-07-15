package com.pinkteam.android.healthkon.Material;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pinkteam.android.healthkon.Controllers.LocationService;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {
        Log.d("mdp", "Low Battery Received");
        Intent intent = new Intent(context, LocationService.class);
        Bundle b = new Bundle();
        b.putBoolean("battery", true);
        intent.putExtras(b);
        context.startService(intent);
    }
}

