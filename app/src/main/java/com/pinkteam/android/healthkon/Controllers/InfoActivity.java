package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pinkteam.android.healthkon.R;

public class InfoActivity extends Fragment {
    private LinearLayout mUserButton;
    private LinearLayout mAboutButton;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_layout, container, false);
        mUserButton = view.findViewById(R.id.user_button);
        mAboutButton = view.findViewById(R.id.about_button);

        mUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(i);
            }
        });
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
