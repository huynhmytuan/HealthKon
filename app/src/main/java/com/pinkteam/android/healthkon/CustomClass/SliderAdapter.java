package com.pinkteam.android.healthkon.CustomClass;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pinkteam.android.healthkon.Controllers.OnboardFragment0;
import com.pinkteam.android.healthkon.Controllers.OnboardFragment1;
import com.pinkteam.android.healthkon.Controllers.OnboardFragment2;
import com.pinkteam.android.healthkon.Controllers.OnboardFragment3;
import com.pinkteam.android.healthkon.Controllers.OnboardFragment4;

import org.jetbrains.annotations.NotNull;

public class SliderAdapter extends FragmentPagerAdapter {


    public SliderAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    public SliderAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnboardFragment0();
            case 1:
                return new OnboardFragment1();
            case 2:
                return new OnboardFragment2();
            case 3:
                return new OnboardFragment3();
            case 4:
                return new OnboardFragment4();
            default:
                return new OnboardFragment0();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
