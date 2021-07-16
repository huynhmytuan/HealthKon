package com.pinkteam.android.healthkon.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinkteam.android.healthkon.Controllers.RecyclerAdapter;
import com.pinkteam.android.healthkon.Material.SwipeDismissBaseActivity;
import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.JourneyUtil;

import java.util.ArrayList;
import java.util.List;

public class RunningListActivity extends SwipeDismissBaseActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<Journey> journeys;
    private JourneyUtil mJourneyUtil;
    private Button mBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_running_layout);
        recyclerView = findViewById(R.id.recycle_view);
        mBackButton = (Button) findViewById(R.id.back_button);
        refreshLayout();;
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshLayout();
    }

    private void refreshLayout(){
        journeys = new ArrayList<Journey>();
        mJourneyUtil = new JourneyUtil(getBaseContext());
        journeys = mJourneyUtil.getAllJourney();
        recyclerAdapter= new RecyclerAdapter(getApplicationContext(),journeys, "detail");
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
