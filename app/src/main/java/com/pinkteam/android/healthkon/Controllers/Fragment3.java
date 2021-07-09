package com.pinkteam.android.healthkon.Controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;

import org.w3c.dom.Text;

public class Fragment3 extends Fragment {
    private LocationService.LocationServiceBinder mLocationService;

    private TextView mDistanceText;
    private TextView mDurationText;
    private TextView mAvgSpeed;

    private Button mStartButton;
    private Button mStopButton;
    private static final int PERMISSION_GPS_CODE = 1;

    // will poll the location service for distance and duration
    private Handler postBack = new Handler(Looper.getMainLooper());

    private ServiceConnection lsc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mLocationService = (LocationService.LocationServiceBinder) iBinder;

            // if currently tracking then enable stopButton and disable startButton
            initButtons();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mLocationService != null) {
                        // get the distance and duration from the surface
                        float d = (float) mLocationService.getDuration();
                        long duration = (long) d;  // in seconds
                        float distance = mLocationService.getDistance();

                        long hours = duration / 3600;
                        long minutes = (duration % 3600) / 60;
                        long seconds = duration % 60;

                        float avgSpeed = 0;
                        if(d != 0) {
                            avgSpeed = distance / (d / 3600);
                        }

                        final String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        final String dist = String.format("%.2f Km", distance);
                        final String avgs = String.format("%.2f Km/h", avgSpeed);

                        postBack.post(new Runnable() {
                            @Override
                            public void run() {
                                // post back changes to UI thread
                                mDurationText.setText(time);
                                mAvgSpeed.setText(avgs);
                                mDistanceText.setText(dist);
                            }
                        });

                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mLocationService = null;
        }
    };

    private void initButtons() {
        // no permissions means no buttons
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mStopButton.setEnabled(false);
            mStartButton.setEnabled(false);
            return;
        }
        // if currently tracking then enable stopButton and disable startButton
        if(mLocationService != null && mLocationService.currentlyTracking()) {
            mStopButton.setEnabled(true);
            mStopButton.setVisibility(View.VISIBLE);
            mStartButton.setEnabled(false);
            mStartButton.setVisibility(View.GONE);
        } else {
            mStopButton.setEnabled(false);
            mStopButton.setVisibility(View.GONE);
            mStartButton.setEnabled(true);
            mStartButton.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_record, container, false);

        mDistanceText  = (TextView) view.findViewById(R.id.total_distance_text);
        mDurationText = (TextView) view.findViewById(R.id.duration_text);
        mAvgSpeed = (TextView) view.findViewById(R.id.speed_text);

        mStartButton = (Button) view.findViewById(R.id.start_button);
        mStopButton = (Button) view.findViewById(R.id.stop_button);

        mStartButton.setEnabled(false);
        mStopButton.setEnabled(false);

        // register broadcast receiver to receive low battery broadcasts
        try {
            MyReceiver receiver = new MyReceiver();
            requireActivity().registerReceiver(receiver, new IntentFilter(
                    Intent.ACTION_BATTERY_LOW));
        } catch(IllegalArgumentException  e) {
        }


        handlePermissions();

        // start the service so that it persists outside of the lifetime of this activity
        // and also bind to it to gain control over the service
        getActivity().startService(new Intent(getActivity(),LocationService.class));
        getActivity().bindService(new Intent(getActivity(), LocationService.class), lsc, Context.BIND_AUTO_CREATE);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickPlay(v);
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStop(v);
            }
        });
        return view;
    }


    public void onClickPlay(View view) {
        // start the timer and tracking GPS locations
        mLocationService.playJourney();
        mStartButton.setEnabled(false);
        mStartButton.setVisibility(View.GONE);
        mStopButton.setEnabled(true);
        mStopButton.setVisibility(View.VISIBLE);
    }

    public void onClickStop(View view) {
        // save the current journey to the database
        float distance = mLocationService.getDistance();
        int journeyID = mLocationService.saveJourney();

        mStartButton.setEnabled(true);
        mStartButton.setVisibility(View.VISIBLE);
        mStopButton.setEnabled(false);
        mStopButton.setVisibility(View.GONE);


        DialogFragment modal = FinishedTrackingDialogue.newInstance(journeyID);
        modal.show(getParentFragmentManager(), "Finished");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // un-register this receiver since we only need it while recording GPS
        try {
            MyReceiver receiver = new MyReceiver();
            getActivity().unregisterReceiver(receiver);
        } catch(IllegalArgumentException  e) {
        }

        // unbind to the service (if we are the only binding activity then the service will be destroyed)
        if(lsc != null) {
            getActivity().unbindService(lsc);
            lsc = null;
        }
    }


    public static class FinishedTrackingDialogue extends DialogFragment {
        public static  FinishedTrackingDialogue newInstance(int JourneyID) {
            Bundle savedInstanceState = new Bundle();
            savedInstanceState.putInt("JourneyID", JourneyID);
            FinishedTrackingDialogue frag = new FinishedTrackingDialogue();
            frag.setArguments(savedInstanceState);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater i = getActivity().getLayoutInflater();
            View view = i.inflate(R.layout.fragment_4,null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            TextView distance = (TextView) view.findViewById(R.id.distance_textview);
            TextView duration = (TextView) view.findViewById(R.id.duration_textview);
            TextView avgSpeed = (TextView) view.findViewById(R.id.avgspeed_textview);
            Button detail = (Button) view.findViewById(R.id.detail_button);
            Button again = (Button) view.findViewById(R.id.run_again_button);
            again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });


            //get data by journey id
            JourneyUtil mJU = new JourneyUtil(getContext());
            Journey journey = mJU.getJourneyByID(getArguments().getInt("JourneyID"));
            final long hours = journey.getmDuration() / 3600;
            final long minutes = (journey.getmDuration() % 3600) / 60;
            final long seconds = journey.getmDuration() % 60;
            float avg = journey.getmDistance() / ((float)journey.getmDuration() / 3600);

            distance.setText(String.format("%.2f Km", journey.getmDistance()));
            duration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            avgSpeed.setText(String.format("%.2f Km/h", avg));

            //set builder view
            builder.setView(view);

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    // PERMISSION THINGS
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] permissions, int[] results) {
        switch(reqCode) {
            case PERMISSION_GPS_CODE:
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED){
                    // permission granted
                    initButtons();
                    if(mLocationService != null) {
                        mLocationService.notifyGPSEnabled();
                    }
                } else {
                    // permission denied, disable GPS tracking buttons
                    mStopButton.setEnabled(false);
                    mStartButton.setEnabled(false);
                }
                return;
        }
    }

    public static class NoPermissionDialogue extends DialogFragment {
        public static  NoPermissionDialogue newInstance() {
            NoPermissionDialogue frag = new NoPermissionDialogue();
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("GPS is required to track your journey!")
                    .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // user agreed to enable GPS
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GPS_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // dialogue was cancelled
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private void handlePermissions() {
        // if don't have GPS permissions then request this permission from the user.
        // if not granted the permission disable the start button
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // the user has already declined request to allow GPS
                // give them a pop up explaining why its needed and re-ask
                DialogFragment modal = NoPermissionDialogue.newInstance();
                modal.show(getParentFragmentManager(), "Permissions");
            } else {
                // request the permission
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GPS_CODE);
            }
        }
    }
}
