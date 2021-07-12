package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.pinkteam.android.healthkon.Models.Height;
import com.pinkteam.android.healthkon.Models.User;
import com.pinkteam.android.healthkon.Models.Weight;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.HeightUtil;
import com.pinkteam.android.healthkon.database.UserUtil;
import com.pinkteam.android.healthkon.database.WeightUtil;

import java.util.Date;

public class OnboardFragment4 extends Fragment {
    NonSwipeableViewPager viewPager;
    Button backButton;
    Button finishButton;
    NumberPicker heightNumPicker;
    UserUtil mUU;
    HeightUtil mHU;
    WeightUtil mWU;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.four_input_layout, container, false);
        viewPager = getActivity().findViewById(R.id.slide_viewpager);
        mUU = new UserUtil(this.getContext());
        mHU = new HeightUtil(this.getContext());
        mWU = new WeightUtil(this.getContext());
        heightNumPicker = (NumberPicker) view.findViewById(R.id.height_numberpicker);
        if (heightNumPicker != null) {
            heightNumPicker.setMinValue(1);
            heightNumPicker.setMaxValue(220);
            heightNumPicker.setValue(150);
            heightNumPicker.setWrapSelectorWheel(true);
        }
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            finishButton = getActivity().findViewById(R.id.next_button);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    Intent i = new Intent(getActivity(),MainActivity.class);
                    startActivity(i);
                    WelcomeActivity.height.setmDate(new Date());
                    WelcomeActivity.height.setmValue(heightNumPicker.getValue());
                    Log.d("Test height:", "Height: " +WelcomeActivity.height.getmValue()
                            + " - Date: " +WelcomeActivity.height.getmDate());
                    mUU.add(WelcomeActivity.user.getmName(),WelcomeActivity.user.getmAge(),WelcomeActivity.user.getmGender(),WelcomeActivity.user.getmEmail(),WelcomeActivity.user.getmPhone());
                    mWU.add(WelcomeActivity.weight.getmValue(),WelcomeActivity.weight.getmDate());
                    mHU.add(WelcomeActivity.height.getmValue(),WelcomeActivity.height.getmDate());
                }
            });
            finishButton.setVisibility(View.VISIBLE);
            finishButton.setText("Done");

            backButton = getActivity().findViewById(R.id.back_button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(3);
                }
            });
            backButton.setVisibility(View.VISIBLE);
        }
    }
}
