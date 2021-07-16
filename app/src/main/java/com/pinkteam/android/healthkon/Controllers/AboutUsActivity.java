package com.pinkteam.android.healthkon.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pinkteam.android.healthkon.Material.SwipeDismissBaseActivity;
import com.pinkteam.android.healthkon.R;

public class AboutUsActivity extends SwipeDismissBaseActivity {
    private Button mBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_layout);
        mBackButton = findViewById(R.id.back_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
