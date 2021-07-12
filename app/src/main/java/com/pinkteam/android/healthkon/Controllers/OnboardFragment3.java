package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
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
    MediaPlayer mp;
    NonSwipeableViewPager viewPager;
    NumberPicker weightNumPicker;
    Button nextButton;
    Button backButton;

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
            weightNumPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    mp = MediaPlayer.create(getActivity(),R.raw.effect_tick);
                    mp.setLooping(false);
                    if(mp.isPlaying()){
                        mp.stop();
                    }
                    mp.start();
                }
            });
        }
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            nextButton = getActivity().findViewById(R.id.next_button);
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
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setText("Next");

            backButton = getActivity().findViewById(R.id.back_button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(2);
                }
            });
            backButton.setVisibility(View.VISIBLE);
            backButton.setText("Back");
        }
    }
}
