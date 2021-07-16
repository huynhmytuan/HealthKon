package com.pinkteam.android.healthkon.Controllers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;
import com.pinkteam.android.healthkon.database.LocationUtil;

public class LocationService extends Service {

    private LocationManager mLocationManager;
    private MyLocationListener mMyLocationListener;
    private final IBinder binder = new LocationServiceBinder();
    private JourneyUtil mJourneyUtil;
    private LocationUtil mLocationUtil;

    private final String CHANNEL_ID = "100";
    private final int NOTIFICATION_ID = 001;
    private long startTime = 0;
    private long stopTime = 0;

    final int TIME_INTERVAL = 3;
    final int DIST_INTERVAL = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mdp", "Location Service created");

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMyLocationListener = new MyLocationListener();
        mMyLocationListener.mRecordLocation = false;


        try {
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, TIME_INTERVAL, DIST_INTERVAL, mMyLocationListener);
        } catch(SecurityException e) {
            // don't have the permission to access GPS
            Log.d("mdp", "No Permissions for GPS");
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,  flags, startId);

        //Get Db Utils to read and write from DBset
        mLocationUtil = new LocationUtil(this);
        mJourneyUtil = new JourneyUtil(this);

        // broadcast receiver may send message about low battery in which bundle containing battery will exist
        if(intent != null) {
            Bundle b = intent.getExtras();
            if(b != null && b.getBoolean("battery")) {
                // slow down GPS request frequency
                changeGPSRequestFrequency(TIME_INTERVAL * 3, DIST_INTERVAL * 3);
            }
        }

        return START_NOT_STICKY;
    }


    private void addNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tracking Journey";
            String description = "Keep Running!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                    importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Tracking Journey")
                .setContentText("Keep Running!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // user has closed the application so cancel the current journey and stop tracking GPS
        mLocationManager.removeUpdates(mMyLocationListener);
        mMyLocationListener = null;
        mLocationManager = null;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        Log.d("mdp", "Location Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    protected float getDistance() {
        return mMyLocationListener.getDistanceOfJourney();
    }

    /* Display notification and start recording GPS locations for a new, also start timer */
    protected void playJourney() {
        addNotification();
        mMyLocationListener.newJourney();
        mMyLocationListener.mRecordLocation = true;
        startTime = SystemClock.elapsedRealtime();
        stopTime = 0;
    }

    /* Get the duration of the current journey */
    protected double getDuration() {
        if(startTime == 0) {
            return 0.0;
        }

        long endTime = SystemClock.elapsedRealtime();

        if(stopTime != 0) {
            // saveJourney has been called, until playJourney is called again display constant time
            endTime = stopTime;
        }

        long elapsedMilliSeconds = endTime - startTime;
        return elapsedMilliSeconds / 1000.0;
    }

    protected boolean currentlyTracking() {
        return startTime != 0;
    }

    /* Save journey to the database and stop saving GPS locations, also removes the notification */
    protected int saveJourney() {
        //Set default name of journey
        String pattern = "HH:mm:ss MM/dd/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        String time = df.format(getDateTime());
        String journeyName = "Running "+ time;
        // save journey to database
        mJourneyUtil.add((long)getDuration(),getDistance(),getDateTime(), journeyName,1,"(Default Comment) Running is good!","");
        int journeyID = Integer.parseInt(mJourneyUtil.getLastestRowID());

        // for each location belonging to this journey save it to the location table linked to this journey
        for(Location location : mMyLocationListener.getLocations()) {
            ContentValues locationData = new ContentValues();
            mLocationUtil.add(journeyID,location.getAltitude(),location.getLongitude(),location.getLatitude());
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        // reset state by clearing locations, stop recording, reset startTime
        mMyLocationListener.mRecordLocation = false;
        stopTime = SystemClock.elapsedRealtime();
        startTime = 0;
        mMyLocationListener.newJourney();

        Log.d("mdp", "Journey saved with id = " + journeyID);
        return journeyID;
    }

    protected void changeGPSRequestFrequency(int time, int dist) {
        // can be used ot change GPS request frequency for battery conservation
        try {
            mLocationManager.removeUpdates(mMyLocationListener);
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, time, dist, mMyLocationListener);
            Log.d("mdp", "New min time = " + time + ", min dist = " + dist);
        } catch(SecurityException e) {
            // don't have the permission to access GPS
            Log.d("mdp", "No Permissions for GPS");
        }
    }


    protected void notifyGPSEnabled() {
        try {
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 3, 3, mMyLocationListener);
        } catch(SecurityException e) {
            // don't have the permission to access GPS
            Log.d("mdp", "No Permissions for GPS");
        }
    }

    private Date getDateTime() {
        Date date = new Date();
        return date;
    }

    public class LocationServiceBinder extends Binder {
        // would like to get the distance in km for activity
        // the activity will keep track of the duration using chronometer
        public float getDistance() {
            return LocationService.this.getDistance();
        }


        public double getDuration() {
            // get duration in seconds
            return LocationService.this.getDuration();
        }

        public boolean currentlyTracking() {return LocationService.this.currentlyTracking();}

        public void playJourney() {
            LocationService.this.playJourney();
        }

        public int saveJourney() {
            return LocationService.this.saveJourney();
        }

        public void notifyGPSEnabled() { LocationService.this.notifyGPSEnabled();}

        public void changeGPSRequestFrequency(int time, int dist) {LocationService.this.changeGPSRequestFrequency(time, dist);}
    }
}



