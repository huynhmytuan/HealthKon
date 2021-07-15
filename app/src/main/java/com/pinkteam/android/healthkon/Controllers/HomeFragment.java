package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;
import com.pinkteam.android.healthkon.database.UserUtil;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private boolean _hasLoadedOnce= false; // your boolean fiel

    JourneyUtil mJourneyUtil;
    UserUtil mUserUtil;

    TextView mNameTextView;
    TextView mTotalDistanceTextView;
    TextView mTotalTimeTextView;
    RadioButton mWeekRadioButton;
    RadioButton mMonthRadioButton;
    RadioButton mYearRadioButton;
    RadioGroup mRadioGroup;
    BarChart mBarChart;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);

        mJourneyUtil = new JourneyUtil(getContext());
        mUserUtil = new UserUtil(getContext());

        //Referent to layout
        mNameTextView = (TextView) view.findViewById(R.id.user_name_textview);
        mTotalDistanceTextView = (TextView) view.findViewById(R.id.distance_textview);
        mTotalTimeTextView = (TextView) view.findViewById(R.id.time_textview);
        mBarChart = (BarChart) view.findViewById(R.id.barChart);
        mWeekRadioButton = (RadioButton) view.findViewById(R.id.week_radiobutton);
        mMonthRadioButton = (RadioButton) view.findViewById(R.id.month_radiobutton);
        mYearRadioButton = (RadioButton) view.findViewById(R.id.year_radiobutton);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.time_radiogroup);


        CreateBarChart("Week");
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mWeekRadioButton.isChecked()){
                    CreateBarChart("Week");
                }
                if(mMonthRadioButton.isChecked()){
                    CreateBarChart("Month");
                }
                if(mYearRadioButton.isChecked()){
                    CreateBarChart("Year");
                }
            }
        });
        loadLayout();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                loadLayout();
                _hasLoadedOnce = true;
            }
        }
    }

    private void loadLayout(){
        //Set hello user's last name
        String fullName = mUserUtil.getAllUser().get(0).getmName();
        String[] words = fullName.split("\\s+");
        if(words.length != 1){
            String lastName = fullName.substring(fullName.lastIndexOf(" "));
            mNameTextView.setText(lastName);
        }else {
            mNameTextView.setText(fullName);
        }
        //internalize Chart
        internalizeBarChart();
    }

    private void internalizeBarChart(){
        //Set Chart Style
        mBarChart.animateY(5000);
        mBarChart.setScaleEnabled(false);
        mBarChart.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.cod_gray));
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        //x-axis setting
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Display
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);

        //y-axis setting
        YAxis yAxis = mBarChart.getAxisLeft();
        yAxis.setTextColor(getResources().getColor(R.color.green_malachite));
        yAxis.setTextSize(13f);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(true);
        yAxis.setAxisMinValue(0f);
        //Set Legent
        mBarChart.getLegend().setTextColor(getResources().getColor(R.color.white));
        mBarChart.getLegend().setTextSize(10f);

    }
    private void CreateBarChart(String chartType){
        //x-axis setting
        XAxis xAxis = mBarChart.getXAxis();

        //setting String values in Xaxis
        ArrayList titleList = getTitle(chartType);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(titleList));
        xAxis.setLabelCount(titleList.size());

        //Adding data Value
        ArrayList dataValues = getDataValues(chartType);
        BarDataSet dataSet = new BarDataSet(dataValues,"Distances");
        dataSet.setColor(getResources().getColor(R.color.green_malachite));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);
        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(12f);

        mBarChart.setData(barData);
        mBarChart.animateY(1500);
        mBarChart.getAxisLeft().setAxisMaxValue(findMaxEntryValue(dataValues)+2f);
        mBarChart.invalidate();
        updateTotalValue(chartType);
    }
    private void updateTotalValue(String chartType){
        float totalDistance = 0;
        long totalDuration = 0;
        if(chartType.equals("Week")){
            ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistanceDaily();

            for (HashMap x : sumDistanceList){
                totalDistance += (float) x.get("total_distance");
                totalDuration += (long) x.get("total_duration");
            }

        }
        if(chartType.equals("Month")){
            ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistance5Weeks();
            for (HashMap x : sumDistanceList){
                totalDistance += (float) x.get("total_distance");
                totalDuration += (long) x.get("total_duration");
            }

        }
        if(chartType.equals("Week")){
            ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistanceMonthly();
            for (HashMap x : sumDistanceList){
               totalDistance += (float) x.get("total_distance");
               totalDuration += (long) x.get("total_duration");
            }
        }

        long hours = totalDuration / 3600;
        long minutes = (totalDuration % 3600) / 60;

        mTotalTimeTextView.setText(String.format("%.2f km", totalDistance));
        mTotalTimeTextView.setText(String.format("%02dh%02dm", hours, minutes));
    }
    private ArrayList<BarEntry> getDataValues(String chartType){
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        if(chartType.equals("Week")){
            ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistanceDaily();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Calendar cal = Calendar.getInstance();
            for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
                cal.set(Calendar.DAY_OF_WEEK, i);
                String dateStr = sdf.format(cal.getTime());
                boolean isAdded = false;
                //Get date from week number
                for (HashMap x : sumDistanceList){
                    if (dateStr.equals(x.get("day_of_week"))) {
                        float totalDistance = (float) x.get("total_distance");
                        dataValues.add(new BarEntry(i-1,totalDistance));
                        isAdded = true;
                    }
                }
                if(!isAdded){
                        dataValues.add(new BarEntry(i-1,0f));
                }
            }
        }
        if(chartType.equals("Month")){
            //Get list data sum last 5 week
            ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistance5Weeks();
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            calendar.setTime(now);
            //Get Current Week Number
            int weekNumer = calendar.get(Calendar.WEEK_OF_YEAR);
            for(int i = 0; i <5; i++){
                boolean isAdded = false;
                //Get date from week number
                for (HashMap x : sumDistanceList){
                    int week = (int) x.get("week_order");
                    if (weekNumer == week){
                        float totalDistance = (float) x.get("total_distance");
                        dataValues.add(new BarEntry(i,totalDistance));
                        isAdded = true;
                    }
                }
                if(!isAdded){
                    dataValues.add(new BarEntry(i,0f));
                }
                //Get date from week number
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.WEEK_OF_YEAR, weekNumer);
                //Get First Date of Week
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date startDate = cal.getTime();
                //Get Last Week number
                cal.setTime(startDate);
                cal.add(Calendar.DATE, -7);
                weekNumer = cal.get(Calendar.WEEK_OF_YEAR);
            }
        }
        if(chartType.equals("Year")){
            ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistanceMonthly();
            for(int i = 0; i < 12; i++){
                boolean isAdded = false;
                for (HashMap x : sumDistanceList){
                    int monthNumber = (int) x.get("month_number");
                    if(monthNumber-1 == i){
                        float totalDistance = (float) x.get("total_distance");
                        dataValues.add(new BarEntry(i,totalDistance));
                        isAdded = true;
                    }
                }
                if(!isAdded){
                    dataValues.add(new BarEntry(i,0f));
                }
            }
        }
        return dataValues;
    }
    private ArrayList<String> getTitle(String chartType){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        ArrayList<String> resultTitle = new ArrayList<>();
        //Get list last 5 week
        if(chartType.equals("Month")){
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            calendar.setTime(now);
            //Get Current Week Number
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            for(int i = 0; i <5; i++){
                //Get date from week number
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.WEEK_OF_YEAR, week);
                //Get First Date and Last Date of Week
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date startDate = cal.getTime();
                cal.add(Calendar.DATE, 6);
                Date endDate = cal.getTime();
                String title = dateFormat.format(startDate)+"-"+dateFormat.format(endDate);
                resultTitle.add(title);

                //Get Last Week number
                cal.setTime(startDate);
                cal.add(Calendar.DATE, -7);
                week = cal.get(Calendar.WEEK_OF_YEAR);
            }
        }
        if(chartType.equals("Year")){
            String[] list = new DateFormatSymbols().getShortMonths();
            for(int i = 0; i< list.length; i++){
                resultTitle.add(list[i]);
            }
        }
        if(chartType.equals("Week")){
            String[] list = new DateFormatSymbols().getShortWeekdays();
            for(int i = 1; i<list.length; i++){
                resultTitle.add(list[i]);
            }
        }
        //return
        return resultTitle;
    }
    private float findMaxEntryValue(ArrayList<BarEntry> valuesList){
        float value = valuesList.get(0).getY();
        for(Entry x : valuesList){
            //if function is find max value
                if(x.getY() > value){
                    value = x.getY();
                }
        }
        return  value;
    }
}
