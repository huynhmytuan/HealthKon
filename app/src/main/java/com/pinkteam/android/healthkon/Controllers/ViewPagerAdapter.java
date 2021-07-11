package com.pinkteam.android.healthkon.Controllers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new RunRecordFragment();
            case 3:
                return new Fragment4();
            case 4:
                return new Fragment5();
            default:
                return new Fragment1();

        }

    }
    @Override
    public int getCount() {
        return 5;
    }
}
