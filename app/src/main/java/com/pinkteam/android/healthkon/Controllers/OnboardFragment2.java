package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.pinkteam.android.healthkon.R;

public class OnboardFragment2 extends Fragment {
    NonSwipeableViewPager viewPager;
    Button nextButton;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_input_layout, container, false);

        viewPager = getActivity().findViewById(R.id.slide_viewpager);
        nextButton = view.findViewById(R.id.next_button2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton maleCheck = (RadioButton) view.findViewById(R.id.male_radiobtn);
                RadioButton femaleCheck = (RadioButton) view.findViewById(R.id.female_radiobtn);
                if(!maleCheck.isChecked() && !femaleCheck.isChecked()){
                    Toast.makeText(getContext(),"Please choose your gender.",Toast.LENGTH_SHORT).show();
                }
                if(maleCheck.isChecked()){
                    WelcomeActivity.user.setmGender("Male");
                    viewPager.setCurrentItem(3);
                }else {
                    if(femaleCheck.isChecked()){
                        WelcomeActivity.user.setmGender("Female");
                        viewPager.setCurrentItem(3);
                    }
                }
                Log.d("Test user:", "name:"+WelcomeActivity.user.getmName()
                        + " - email:"+WelcomeActivity.user.getmEmail()
                        + " - phone:"+WelcomeActivity.user.getmPhone()
                        + " - Gender:"+WelcomeActivity.user.getmGender()
                        + " - Age:"+WelcomeActivity.user.getmAge());
            }
        });
        return view;
    }
}
