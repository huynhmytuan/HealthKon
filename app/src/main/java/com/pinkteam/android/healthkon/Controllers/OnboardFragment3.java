package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
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

import com.pinkteam.android.healthkon.R;

import java.util.Date;

public class OnboardFragment3 extends Fragment {
    NonSwipeableViewPager viewPager;
    NumberPicker weightNumPicker;
    Button nextButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_input_layout, container, false);

        viewPager = getActivity().findViewById(R.id.slide_viewpager);

        weightNumPicker = (NumberPicker) view.findViewById(R.id.weight_numberpicker);
        if (weightNumPicker != null) {
            weightNumPicker.setMinValue(1);
            weightNumPicker.setMaxValue(200);
            weightNumPicker.setValue(40);
            weightNumPicker.setWrapSelectorWheel(true);
        }
        nextButton = view.findViewById(R.id.next_button2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
                WelcomeActivity.weight.setmDate(new Date());
                WelcomeActivity.weight.setmValue(weightNumPicker.getValue());
                Log.d("Test weight:", "Weight: " +WelcomeActivity.weight.getmValue()
                        + " - Date: " +WelcomeActivity.weight.getmDate());
            }
        });
        return view;
    }
}
