package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.pinkteam.android.healthkon.R;

public class OnboardFragment1 extends Fragment {
    Button nextButton;
    NonSwipeableViewPager viewPager;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_input_layout, container, false);

        viewPager = getActivity().findViewById(R.id.slide_viewpager);
        nextButton = view.findViewById(R.id.next_button2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEdt = (EditText) view.findViewById(R.id.name_text2);
                EditText emailEdt = (EditText) view.findViewById(R.id.email_text);
                EditText phoneEdt = (EditText) view.findViewById(R.id.phone_text);
                EditText ageEdt = (EditText) view.findViewById(R.id.age_text);
                boolean error = false;
                if(!ValidateHelper.validateName(nameEdt)){
                    nameEdt.setError("Please enter a valid name");
                    error = true;
                }
                if(!ValidateHelper.validatePhone(phoneEdt)) {
                    phoneEdt.setError("Please enter a valid phone number\n (i.e 0798469633)");
                    error = true;
                }
                if(!ValidateHelper.validateAge(ageEdt)){
                    ageEdt.setError("Please enter a valid age number ");
                    error = true;
                }
                if(!ValidateHelper.validateEmail(emailEdt)) {
                    emailEdt.setError("Please enter a valid individual email\n address (i.e name12@email.com). ");
                    error = true;
                }
                if(!error){
                    WelcomeActivity.user.setmName(nameEdt.getText().toString());
                    WelcomeActivity.user.setmEmail(emailEdt.getText().toString());
                    WelcomeActivity.user.setmPhone(phoneEdt.getText().toString());
                    WelcomeActivity.user.setmAge(Integer.parseInt(ageEdt.getText().toString()));
                    Log.d("Test user:", "name:" + WelcomeActivity.user.getmName()
                            + " - email:" + WelcomeActivity.user.getmEmail()
                            + " - phone:" + WelcomeActivity.user.getmPhone()
                            + " - Gender:" + WelcomeActivity.user.getmGender()
                            + " - Age:" + WelcomeActivity.user.getmAge());
                    viewPager.setCurrentItem(2);
                }
            }
        });
        return view;
    }

}
