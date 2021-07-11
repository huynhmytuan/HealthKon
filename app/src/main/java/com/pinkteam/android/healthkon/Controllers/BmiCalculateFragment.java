package com.pinkteam.android.healthkon.Controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
import android.util.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pinkteam.android.healthkon.Models.Height;
import com.pinkteam.android.healthkon.Models.Weight;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.HeightUtil;
import com.pinkteam.android.healthkon.database.WeightUtil;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivities;


public class BmiCalculateFragment extends Fragment {

    HeightUtil mHU;
    WeightUtil mWU;

    TextView mViewHeight;
    TextView mViewWeight;
    TextView mViewBMIScore;
    TextView mViewBMIStatus;
    TextView mViewDate;
    LineChart mChart;

    private Button mWeightBtn;
    private Button mHeightBtn;
    private Button mCalendarBtn;

    private AlertDialog dialogAdd;
    FloatingActionButton mFloatButtonAdd;

    Pair<Long, Long> selectionDate;
    private static boolean isHeightView = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bmi_calculate_layout, container, false);

        mHU = new HeightUtil(this.getContext());
        mWU = new WeightUtil(this.getContext());

        mViewHeight = (TextView) view.findViewById(R.id.height_textview);
        mViewWeight = (TextView) view.findViewById(R.id.weight_textview);
        mViewBMIScore = (TextView) view.findViewById(R.id.scorebmi_textview);
        mViewBMIStatus = (TextView) view.findViewById(R.id.statusbmi_textview);
        mViewDate = (TextView) view.findViewById(R.id.date_range_textview);
        mWeightBtn = (Button) view.findViewById(R.id.view_weight_btn);
        mWeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHeightView = false;
                ShowWeightChart(null,null);
            }
        });

        mHeightBtn = (Button) view.findViewById(R.id.view_height_btn);
        mHeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHeightView = true;
                ShowHeightChart(null,null);
            }
        });


        mChart = view.findViewById(R.id.lineChart);

        mCalendarBtn = (Button) view.findViewById(R.id.view_cal_btn);



        mCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar();
            }
        });



        mFloatButtonAdd = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        mFloatButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogAdd(v);
            }
        });

        if(isHeightView){
            ShowHeightChart(null,null);
            RefreshLayout(view);
        }else {
            ShowWeightChart(null,null);
            RefreshLayout(view);
        }


        return view;
    }

    //Set Calendar
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ShowCalendar(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointBackward.now());

        builder.setCalendarConstraints(constraintsBuilder.build());

        // picker.setStyle(DialogFragment.STYLE_NORMAL);
        if(selectionDate !=null){
            builder.setSelection(selectionDate);
        }else{
            long start = 0;
            long end = 0;

            Date endDay = new Date();
            Log.d("Test_LOL:",endDay.toString());
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDay);
            cal.set(Calendar.DAY_OF_MONTH,1);
            Date startDay = cal.getTime();

            Log.d("Test_LOL:",startDay.toString());

            start = startDay.getTime();
            end = endDay.getTime();
            selectionDate = new Pair(start,end);

            //set day
            builder.setSelection(selectionDate);
        }
        MaterialDatePicker<Pair<Long,Long>> picker = builder.build();
        picker.show(getFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Date now = new Date();
                Calendar cal = Calendar.getInstance();


                long firstDateLong = selection.first;
                Date firstDate = new Date(firstDateLong);
                cal.setTime(firstDate);
                cal.set(Calendar.HOUR_OF_DAY,now.getHours());
                cal.set(Calendar.MINUTE,now.getMinutes());
                cal.set(Calendar.SECOND,now.getSeconds());
                firstDate = cal.getTime();

                long endDateLong = selection.second;
                Date endDate = new Date(endDateLong);
                cal.setTime(endDate);
                cal.set(Calendar.HOUR_OF_DAY,now.getHours());
                cal.set(Calendar.MINUTE,now.getMinutes());
                cal.set(Calendar.SECOND,now.getSeconds());
                endDate = cal.getTime();

                setDateRangeText(firstDate,endDate);
                selectionDate = selection;
                picker.dismiss();
                if(isHeightView){
                    ShowHeightChart(firstDate,endDate);
                }else {
                    ShowWeightChart(firstDate,endDate);
                }
            }
        });
    }

    //Set date range text
    private void setDateRangeText(Date StartDate, Date EndDate){
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //format dd/MM/yyyy
        String string_date = sdf2.format(StartDate);
        String string_date2 = sdf2.format(EndDate);


        String selectedDateStr = string_date + " - " + string_date2;
        mViewDate.setText(selectedDateStr);
    }

    //Calculate BMI index
    private void calculateBMI(int Weight, int Height){
        double bmiScore = Weight / Math.pow(Height, 2) * 10000;
        DecimalFormat df = new DecimalFormat("0.00");
        mViewBMIScore.setText(df.format(bmiScore)+"");
        if (bmiScore < 18)
            mViewBMIStatus.setText("Underweight");
        else if (18 <= bmiScore && bmiScore < 25)
            mViewBMIStatus.setText("Normal Weight");
        else if (25 <= bmiScore && bmiScore < 30)
            mViewBMIStatus.setText("Over Weight");
        else if (30 <= bmiScore && bmiScore < 35)
            mViewBMIStatus.setText("Obesity");
        else if (35 <= bmiScore)
            mViewBMIStatus.setText("Extremely Obesity");
    }

    //Set Button Weight Chart
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ShowWeightChart(Date StartDate, Date EndDate){
        //Get data from database:
        ArrayList<Entry> valuesWeight = new ArrayList<>();
        List<Weight> weightList;

        if(StartDate != null && EndDate != null){
            //get data from db by (StartDate to EndDate)
            weightList = mWU.getWeightInRange(StartDate,EndDate);

        }else {
            //Get today and first date of current month
            Date endDate = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            int days = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, days);
            Date startDate = cal.getTime();
            setDateRangeText(startDate,endDate);
            //Get data from db by first date of month and today
            weightList = mWU.getWeightInRange(startDate,endDate);
        }
        if(!weightList.isEmpty()){
            for (int i =0 ; i < weightList.size(); i++) {
                float value = (float) weightList.get(i).getmValue();
                Log.d("Test_W-",value+"_"+i);
                valuesWeight.add(new Entry((float)i, value));
            }
            CreateChart(valuesWeight, StartDate, EndDate);
        }else {
            CreateChart(null,StartDate,EndDate);
        }

    }

    //Set Button Height Chart
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ShowHeightChart(Date StartDate, Date EndDate){
        //Get data from database:
        ArrayList<Entry> valuesHeight = new ArrayList<>();
        List<Height> heightList;

        if(StartDate != null && EndDate != null){
            //get data from db by (StartDate to EndDate)
            heightList = mHU.getHeightInRange(StartDate,EndDate);

        }else {
            //Get today and first date of current month
            Date endDate = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            int days = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, days);
            Date startDate = cal.getTime();
            setDateRangeText(startDate,endDate);
            //Get data from db by first date of month and today
            heightList = mHU.getHeightInRange(startDate,endDate);
        }
        if(!heightList.isEmpty()){
            for (int i =0 ; i < heightList.size(); i++) {
                float value = (float) heightList.get(i).getmValue();
                Log.d("Test_H-",value+"_"+i);
                valuesHeight.add(new Entry((float)i, value));
            }
            CreateChart(valuesHeight, StartDate, EndDate);
        }else {
            CreateChart(null,StartDate,EndDate);
        }

    }

    //Chart
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void CreateChart(ArrayList<Entry> valuesSet, Date StartDate, Date EndDate){
        if(valuesSet != null){
            LineDataSet data;   //Line arguments
            //Graph background color
            mChart.setBackgroundColor(Color.BLACK);

            //x-axis setting
            XAxis xAxis = mChart.getXAxis();
            //Output x-axis diagonally
            xAxis.setLabelRotationAngle(0);
            //Set the date registered in the database to the x-axis
            xAxis.setValueFormatter(new IndexAxisValueFormatter(getDate(StartDate,EndDate)));

            //Make the x-axis a dashed line(Dashed Line)
            xAxis.enableGridDashedLine(102f, 10f, 0f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //Display
            xAxis.setLabelCount(getDate(StartDate,EndDate).size(),true);
            xAxis.setTextColor(Color.WHITE);

            //y-axis setting
            YAxis yAxis = mChart.getAxisLeft();

            //Make the y-axis a dashed line
            yAxis.setDrawZeroLine(false);


            //The scale on the right. False if not needed
            mChart.getAxisRight().setEnabled(false);
            yAxis.setEnabled(false);

            mChart.setScaleMinima(5f, 5f);
            mChart.fitScreen();


            //Make another set

            if(isHeightView){
                data = new LineDataSet(valuesSet,"Height");
                data.setColor(Color.RED);
                data.setCircleColor(Color.rgb(254,157,112));
                //Y-axis maximum / minimum setting
                yAxis.setAxisMaximum(230f);
                yAxis.setAxisMinimum(50f);
            }
            else {
                //Y-axis maximum / minimum setting
                yAxis.setAxisMaximum(200f);
                yAxis.setAxisMinimum(20f);
                data = new LineDataSet(valuesSet,"Weight");
                data.setColor(Color.BLUE);
                data.setCircleColor(Color.CYAN);
            }

            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(10f);
            data.setFillAlpha(65);
            data.setFillColor(Color.RED);
            data.setCircleColor(Color.WHITE);
            data.setCircleColorHole(Color.BLUE);
            data.setLineWidth(2f);
            data.setCircleSize(5f);
            data.setDrawValues(true);

            //set the line on the chart
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(data);
            LineData lineData = new LineData(dataSets);
            mChart.setData(lineData);
            mChart.getLegend().setEnabled(false);
            //Animate the data. millisecond.Larger numbers are slower
            mChart.animateX(1000);
        }else {
            mChart.invalidate();
            mChart.clear();
        }


    }

    //Refresh layout
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void RefreshLayout(View view){
        Height mHeight = mHU.getLastestHeight();
        String lastHeight = Integer.toString(mHeight.getmValue()) + " cm";
        mViewHeight.setText(lastHeight);

        Weight mWeight = mWU.getLastestWeight();
        String lastWeight = Integer.toString(mWeight.getmValue()) + " kg";
        mViewWeight.setText(lastWeight);

        calculateBMI(mWeight.getmValue(), mHeight.getmValue());
        if(isHeightView){
            ShowHeightChart(null,null);
        }
        else {
            ShowWeightChart(null,null);
        }
    }

    //Rewrite the data saved in String type to date type and SimpleDateFormat
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  ArrayList<String> getDate(Date StartDate, Date EndDate) {
        List<Weight> weightList;
        if(StartDate != null && EndDate != null){
            //get data from db by (StartDate to EndDate)
            weightList = mWU.getWeightInRange(StartDate,EndDate);
        }else {
            //Get today and first date of current month
            Date endDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            int days = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, days);
            Date startDate = cal.getTime();
            //Get data from db by first date of month and today
            weightList = mWU.getWeightInRange(startDate,endDate);
        }
        ArrayList<String> dateLabels = new ArrayList<>();

        for (Weight x : weightList) {
            //Correct the date to date
            String strDate = x.getmDate().toString();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM");
            String entryDate = dateFormat.format(x.getmDate());
            dateLabels.add(entryDate);
        }
        return dateLabels;
    }


    //Show dialog add information
    public void ShowDialogAdd(View view) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout_dialog_add = inflater.inflate(R.layout.fragment_custom_dialog,null);

        //------------------- build dialog add new ---------------------
        // Xây dựng cái view
        if (layout_dialog_add.getParent() != null) {// xóa các view ở lần bấm chuột trước
            ((ViewGroup) layout_dialog_add.getParent()).removeAllViews();
        }
        //layout_root should be the name of the "top-level" layout node in the dialog_layout.xml file.
        final EditText itemWeight = (EditText) layout_dialog_add.findViewById(R.id.itemWeight);  // editext này lấy ở file layout_custom_dialog
        final EditText itemHeight = (EditText) layout_dialog_add.findViewById(R.id.itemHeight);


        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout_dialog_add);
        builder.setTitle("Update information");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogAdd.dismiss();
                    }
                }
        );
        builder.setPositiveButton("Save",null);
        dialogAdd = builder.create();
        dialogAdd.show();
        dialogAdd.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                boolean error = false;
                String _Weight = itemWeight.getText().toString();
                String _Height = itemHeight.getText().toString();


                if(_Weight.isEmpty()){
                    itemWeight.setError("The value can't be empty");
                    error = true;
                }else{
                    if(Integer.parseInt(_Weight) < 20 || Integer.parseInt(_Weight) > 200) {
                        itemWeight.setError("The value must more than 20 and under 200.");
                        error = true;
                    }
                }

                if(_Height.isEmpty()) {
                    itemHeight.setError("The value can't be empty");
                    error = true;
                }else{
                    if(Integer.parseInt(_Height) <= 50 || Integer.parseInt(_Height) > 230) {
                        itemHeight.setError("The value more than 49 and under 230.");
                        error = true;
                    }
                }

                if(!error){
                    Date date = new Date();
                    mHU.add(Integer.parseInt(_Height),date);
                    mWU.add(Integer.parseInt(_Weight),date);
                    Toast.makeText(getContext(),"You enter: " + _Weight + "\n" + _Height, Toast.LENGTH_SHORT).show();
                    RefreshLayout(getView());
                    dialogAdd.dismiss(); // tắt dialog
                }
            }
        });
    }


}