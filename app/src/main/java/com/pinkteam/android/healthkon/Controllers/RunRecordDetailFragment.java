package com.pinkteam.android.healthkon.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.HeightUtil;
import com.pinkteam.android.healthkon.database.JourneyUtil;
import com.pinkteam.android.healthkon.database.WeightUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RunRecordDetailFragment extends Fragment {
    JourneyUtil mJourneyUtil;
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("E, hh:mm dd/MM/yyyy");

    Button backButton;
    Button editButton;
    TextView dateTextview;
    TextView journeyNameTextview;
    TextView commentTextview;
    TextView distanceTextview;
    TextView timeTextview;
    RatingBar ratingBar;

    Journey mJourney;


    private int journeyID = 2;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_record_detail_layout, container, false);

        mJourneyUtil = new JourneyUtil(getContext());
        mJourney = mJourneyUtil.getJourneyByID(journeyID);

        dateTextview = (TextView) view.findViewById(R.id.date_textview);
        journeyNameTextview = (TextView) view.findViewById(R.id.journey_name_textview);
        commentTextview = (TextView) view.findViewById(R.id.comment_textview);
        distanceTextview = (TextView) view.findViewById(R.id.distance_textview);
        timeTextview = (TextView) view.findViewById(R.id.time_textview);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getContext(),"Vote Rating:"+rating,Toast.LENGTH_SHORT).show();
                mJourney.setmRating(rating);
                mJourneyUtil.update(mJourney);
                loadLayout();
            }
        });

        loadLayout();
        return view;
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
        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.fragment_custom_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set builder view
        builder.setView(view);
        // Create the AlertDialog object and return it
        AlertDialog dialog =  builder.create();
        journey_name_Edittext = (EditText) view.findViewById(R.id.journey_name_edt);
        comment_Edittext = (EditText) view.findViewById(R.id.comment_edt);
        cancel_Button = (Button) view.findViewById(R.id.cancel_btn);
        save_Button = (Button) view.findViewById((R.id.save_btn));
    }
}
