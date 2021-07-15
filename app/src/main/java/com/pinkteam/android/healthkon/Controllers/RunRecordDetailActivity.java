package com.pinkteam.android.healthkon.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.Models.Location;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.HeightUtil;
import com.pinkteam.android.healthkon.database.JourneyUtil;
import com.pinkteam.android.healthkon.database.LocationUtil;
import com.pinkteam.android.healthkon.database.UserUtil;
import com.pinkteam.android.healthkon.database.WeightUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RunRecordDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    JourneyUtil mJourneyUtil;
    LocationUtil mLocationUtil;
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("E, hh:mm dd/MM/yyyy");
    GoogleMap mMap;

    Button backButton;
    Button editButton;
    TextView dateTextview;
    TextView journeyNameTextview;
    TextView commentTextview;
    TextView distanceTextview;
    TextView timeTextview;
    RatingBar ratingBar;

    Journey mJourney;

    private int journeyID;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_record_detail_layout);

        mJourneyUtil = new JourneyUtil(getBaseContext());
        mLocationUtil = new LocationUtil(getBaseContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        journeyID = bundle.getInt("journeyID");

        mJourney = mJourneyUtil.getJourneyByID(journeyID);

        dateTextview = (TextView) findViewById(R.id.date_textview);
        journeyNameTextview = (TextView) findViewById(R.id.journey_name_textview);
        commentTextview = (TextView) findViewById(R.id.comment_textview);
        distanceTextview = (TextView) findViewById(R.id.distance_textview);
        timeTextview = (TextView) findViewById(R.id.time_textview);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editButton = (Button) findViewById(R.id.edit_btn);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialogueShow();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getBaseContext(),"Vote Rating:"+rating,Toast.LENGTH_SHORT).show();
                mJourney.setmRating(rating);
                mJourneyUtil.update(mJourney);
                loadLayout();
            }
        });
        loadLayout();
    }

    private void loadLayout(){
        mJourney = mJourneyUtil.getJourneyByID(journeyID);
        //get data by journey Id
        String journeyDate = dateFormat.format(mJourney.getmDate());
        String journeyName = mJourney.getmName();
        String comment = mJourney.getmComment();
        float rating = mJourney.getmRating();
        final long hours = mJourney.getmDuration() / 3600;
        final long minutes = (mJourney.getmDuration() % 3600) / 60;
        final long seconds = mJourney.getmDuration() % 60;

        float distance = mJourney.getmDistance();
        dateTextview.setText(journeyDate);
        journeyNameTextview.setText(journeyName);
        commentTextview.setText(comment);
        ratingBar.setRating(rating);
        distanceTextview.setText(String.format("%.2f", distance));
        timeTextview.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
    //Show dialog add information
    public void AddDialogueShow() {
        EditText journey_name_Edittext;
        EditText comment_Edittext;
        Button cancel_Button;
        Button save_Button;
        LayoutInflater i = this.getLayoutInflater();
        View v = i.inflate(R.layout.run_record_edit_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        journey_name_Edittext = (EditText) v.findViewById(R.id.journey_name_edt);
        comment_Edittext = (EditText) v.findViewById(R.id.comment_edt);
        cancel_Button = (Button) v.findViewById(R.id.cancel_btn);
        save_Button = (Button) v.findViewById((R.id.save_btn));
        //set builder view
        builder.setView(v);
        //Set text view text display name and journey comment
        journey_name_Edittext.setText(mJourney.getmName());
        comment_Edittext.setText(mJourney.getmComment());
        // Create the AlertDialog object and return it
        AlertDialog dialog =  builder.create();
        //Set action for save button
        save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJourney.setmName(journey_name_Edittext.getText().toString());
                mJourney.setmComment(comment_Edittext.getText().toString());
                mJourneyUtil.update(mJourney);
                dialog.dismiss();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        loadLayout();
                    }
                }, 2000);
            }
        });
        //Set Action for cancel button
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //show dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;

        // draw polyline

        List<Location> locations = mLocationUtil.getLocationsByJourney(journeyID);

        PolylineOptions line = new PolylineOptions().clickable(false);
        LatLng firstLoc = null;
        LatLng lastLoc = null;
        if(!locations.isEmpty()){
            for(int i = 1; i < locations.size()-1; i++){
                LatLng loc = new LatLng(locations.get(i).getmLatitude(), locations.get(i).getmLongitude());
                line.add(loc);
            }
            firstLoc = new LatLng(locations.get(0).getmLatitude(), locations.get(0).getmLongitude());
            lastLoc = new LatLng(locations.get(locations.size()-1).getmLatitude(), locations.get(locations.size()-1).getmLongitude());
        }

        float zoom = 15.0f;
        if(lastLoc != null && firstLoc != null) {
            mMap.addMarker(new MarkerOptions().position(firstLoc).title("Start"));
            mMap.addMarker(new MarkerOptions().position(lastLoc).title("End"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, zoom));
        }
        mMap.addPolyline(line);

    }
}
