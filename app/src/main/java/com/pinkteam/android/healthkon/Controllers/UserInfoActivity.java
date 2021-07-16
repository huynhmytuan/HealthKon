package com.pinkteam.android.healthkon.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;

import com.pinkteam.android.healthkon.CustomClass.SwipeDismissBaseActivity;
import com.pinkteam.android.healthkon.CustomClass.ValidateHelper;
import com.pinkteam.android.healthkon.Models.User;
import com.pinkteam.android.healthkon.R;
import com.pinkteam.android.healthkon.database.UserUtil;


public class UserInfoActivity extends SwipeDismissBaseActivity {
    private UserUtil mUserUtil;
    private EditText mNameTextview, mGenderTextview, mEmailTextview, mPhoneTextview, mAgeTextview;
    private Button mEditButton, mBackButton;
    private boolean isEditable = false;
    Drawable editableBg;
    Drawable unEditableBg;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        editableBg = getResources().getDrawable(R.drawable.edit_text_stoke_green);
        unEditableBg = getResources().getDrawable(android.R.color.transparent);

        mUserUtil = new UserUtil(getApplicationContext());
        user = mUserUtil.getAllUser().get(0);

        mNameTextview = findViewById(R.id.name_textview);
        mGenderTextview = findViewById(R.id.gender_textview);
        mEmailTextview = findViewById(R.id.email_textview);
        mPhoneTextview = findViewById(R.id.phone_textview);
        mAgeTextview = findViewById(R.id.age_textview);
        //Set disable edit view
        mNameTextview.setFocusable(isEditable);
        mPhoneTextview.setFocusable(isEditable);
        mEmailTextview.setFocusable(isEditable);
        mGenderTextview.setFocusable(isEditable);
        mAgeTextview.setFocusable(isEditable);

        mEditButton = findViewById(R.id.edit_button);
        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClicked();
            }
        });
        //load
        loadLayout();
    }
    private void loadLayout(){
        mNameTextview.setText(user.getmName()+"");
        mEmailTextview.setText(user.getmEmail());
        mGenderTextview.setText(user.getmGender());
        mPhoneTextview.setText(user.getmPhone());
        mAgeTextview.setText(user.getmAge()+"");
    }
    public void onEditClicked(){
        //Set for name
        if(isEditable){
            if(checkInput()){
                ShowConfirmDialog();
            }
        }else{
            setViewEditButton();
        }
    }
    private void setViewEditButton(){
        if(isEditable){
            isEditable = false;
            mEditButton.setText("Edit");
            mNameTextview.setFocusable(isEditable);
            mNameTextview.setBackground(unEditableBg);

            mPhoneTextview.setFocusable(isEditable);
            mPhoneTextview.setBackground(unEditableBg);

            mEmailTextview.setFocusable(isEditable);
            mEmailTextview.setBackground(unEditableBg);

            mAgeTextview.setFocusable(isEditable);
            mAgeTextview.setBackground(unEditableBg);
            hideKeyboardFrom(UserInfoActivity.this, findViewById(android.R.id.content).getRootView());
        }else{
            isEditable = true;
            mEditButton.setText("Save");
            mNameTextview.setFocusableInTouchMode(isEditable);
            mNameTextview.setBackground(editableBg);

            mPhoneTextview.setFocusableInTouchMode(isEditable);
            mPhoneTextview.setBackground(editableBg);

            mEmailTextview.setFocusableInTouchMode(isEditable);
            mEmailTextview.setBackground(editableBg);

            mAgeTextview.setFocusableInTouchMode(isEditable);
            mAgeTextview.setBackground(editableBg);
        }
    }
    private boolean checkInput(){
        boolean valid = true;
        if(mNameTextview.getText().toString().isEmpty()){
            mNameTextview.setError("Please enter your name here.");
            valid = false;
        }else {
            if(!ValidateHelper.validateName(mNameTextview)){
                mNameTextview.setError("Name can't be contains number.");
                valid = false;
            }
        }
        if(mPhoneTextview.getText().toString().isEmpty()){
            mPhoneTextview.setError("Please enter your phone number.");
            valid = false;
        }else {
            if(!ValidateHelper.validatePhone(mPhoneTextview)) {
                mPhoneTextview.setError("Please enter a valid Viet Nam \nphone number (i.e 0798469633)");
                valid = false;
            }
        }
        if(mAgeTextview.getText().toString().isEmpty()){
           mAgeTextview.setError("Please enter your age.");
            valid = false;
        }else {
            if(!ValidateHelper.validateAge(mAgeTextview)){
                mAgeTextview.setError("Please enter a valid age number.");
                valid = false;
            }
        }
        if(mEmailTextview.getText().toString().isEmpty()){
            mEmailTextview.setError("Please enter your email address.");
            valid = false;
        }else {
            if(!ValidateHelper.validateEmail(mEmailTextview)) {
                mEmailTextview.setError("Please enter a valid individual email\n address (i.e name12@email.com). ");
                valid = false;
            }
        }
        return valid;
    }

    private void ShowConfirmDialog(){
        Button mPositiveButton, mNegativeButton;
        TextView mDialogTitle, mDialogMessage;
        LayoutInflater i = UserInfoActivity.this.getLayoutInflater();
        View view = i.inflate(R.layout.confrim_custom_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        //set builder view
        builder.setView(view);
        mDialogTitle = view.findViewById(R.id.dialog_title);
        mDialogMessage = view.findViewById(R.id.dialog_message);
        mPositiveButton = view.findViewById(R.id.positive_button);
        mNegativeButton = view.findViewById(R.id.negative_button);
        mDialogTitle.setText("CONFIRM");
        mDialogMessage.setText("Update your information?");

        Dialog dialog = builder.create();
        mPositiveButton.setText("Update");
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setmName(mNameTextview.getText().toString());
                user.setmEmail(mEmailTextview.getText().toString());
                user.setmPhone(mPhoneTextview.getText().toString());
                user.setmAge(Integer.parseInt(mAgeTextview.getText().toString()));
                user = mUserUtil.update(user);
                setViewEditButton();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                loadLayout();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mNegativeButton.setText("Cancel");
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewEditButton();
                loadLayout();
                dialog.dismiss();}
        });
        // Create the AlertDialog object and return it
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
