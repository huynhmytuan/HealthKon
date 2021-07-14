package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;
import com.pinkteam.android.healthkon.database.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    JourneyUtil mJourneyUtil;
    UserUtil mUserUtil;

    TextView mNameTextView;
    HorizontalBarChart mBarChart;

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

        mNameTextView = (TextView) view.findViewById(R.id.user_name_textview);


        return view;
    }

    private void loadLayout(){
        //Set hello user's last name
        String fullName = mUserUtil.getAllUser().get(0).getmName();
        String lastName = fullName.substring(fullName.lastIndexOf(" "));
        mNameTextView.setText(lastName);
        //

    }

    private List<String> getTitle(){
        List<String> resultTitle = new ArrayList<>();

        //Get list data sum last 5 week
        ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistance5Weeks();
        //Get start and end date of week
        for (HashMap x : sumDistanceList){
               String title = x.get("week_start")+ "-" + x.get("week_end");
               Log.d("TEST_WEEK",title);
               resultTitle.add(title);
        }

        //Get list 1 week
        return resultTitle;
    }
}
