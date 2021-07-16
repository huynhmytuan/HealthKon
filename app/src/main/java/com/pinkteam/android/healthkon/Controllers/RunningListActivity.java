package com.pinkteam.android.healthkon.Controllers;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pinkteam.android.healthkon.CustomClass.CustomDateRangeCalendar;
import com.pinkteam.android.healthkon.CustomClass.RecyclerAdapter;
import com.pinkteam.android.healthkon.CustomClass.SwipeDismissBaseActivity;
import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RunningListActivity extends SwipeDismissBaseActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<Journey> journeys;
    private JourneyUtil mJourneyUtil;
    private Button mBackButton;
    private LottieAnimationView mOpenCalendar;
    private TextView mDateRangeTextView;
    private Pair<Long, Long> selectionDate = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_running_layout);
        recyclerView = findViewById(R.id.recycle_view);
        mBackButton = (Button) findViewById(R.id.back_button);
        mOpenCalendar = findViewById(R.id.view_cal_btn);
        mDateRangeTextView = findViewById(R.id.date_range_textview);

        refreshLayout();;
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPostResume() {
        super.onPostResume();
        refreshLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshLayout(){
        if(selectionDate == null){
            selectionDate = CustomDateRangeCalendar.getMonthStartToNow();
        }
        journeys = new ArrayList<Journey>();
        mJourneyUtil = new JourneyUtil(getBaseContext());
        Pair<Date,Date> pair = CustomDateRangeCalendar.getDateFromSeclection(selectionDate);
        journeys = mJourneyUtil.getJourneysByDate(pair.first,pair.second);
        setDateRangeText(selectionDate);
        recyclerAdapter= new RecyclerAdapter(getApplicationContext(),journeys, "detail");
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ShowCalendar(){
        MaterialDatePicker<Pair<Long,Long>> picker = CustomDateRangeCalendar.ShowCalendar(selectionDate);
        picker.show(getSupportFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Pair<Date,Date> pair = CustomDateRangeCalendar.getDateFromSeclection(selection);
                //Set text and put to selectionDate
                selectionDate = selection;
                picker.dismiss();
                refreshLayout();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDateRangeText(Pair<Long,Long> selectionDate){
        Pair<Date, Date> pair = CustomDateRangeCalendar.getDateFromSeclection(selectionDate);
        Date startDate = pair.first;
        Date endDate = pair.second;
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //format dd/MM/yyyy
        String string_date = sdf2.format(startDate);
        String string_date2 = sdf2.format(endDate);
        String selectedDateStr ="Range Date: "+ string_date + " - " + string_date2;
        mDateRangeTextView.setText(selectedDateStr);
    }
}
