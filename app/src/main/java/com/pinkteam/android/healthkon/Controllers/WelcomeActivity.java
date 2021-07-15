package com.pinkteam.android.healthkon.Controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pinkteam.android.healthkon.Material.NonSwipeableViewPager;
import com.pinkteam.android.healthkon.Models.Height;
import com.pinkteam.android.healthkon.Models.User;
import com.pinkteam.android.healthkon.Models.Weight;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    NonSwipeableViewPager viewPager;
    UserUtil mUU;
    public static User user = new User();
    public static Weight weight = new Weight();
    public static Height height = new Height();
    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_layout);
        mUU = new UserUtil(getBaseContext());
        users = mUU.getAllUser();
        if (users.isEmpty()){
            viewPager = (NonSwipeableViewPager) findViewById(R.id.slide_viewpager);
            SliderAdapter adapter = new SliderAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
        }else {
            this.finish();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }
}

