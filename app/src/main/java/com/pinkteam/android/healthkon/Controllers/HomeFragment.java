package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment {
    JourneyUtil mJourneyUtil;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        mJourneyUtil = new JourneyUtil(getContext());
        List<String> test = getWeekTitle();
        return view;
    }
    private List<String> getWeekTitle(){
        //Get list data sum last 5 week
        ArrayList<HashMap<String,Object>> sumDistanceList = mJourneyUtil.getTotalDistance5Weeks();
        List<String> resultTitle = new ArrayList<>();
        //Get start and end date of week
        for (HashMap x : sumDistanceList){
               String title = x.get("week_order")+" : "+ x.get("week_start")+ "-" + x.get("week_end");
               Log.d("TEST_WEEK",title);
               resultTitle.add(title);
        }
        return resultTitle;
    }
}
