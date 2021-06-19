package com.pinkteam.android.healthkon.Models;

import java.util.Date;

public class BMI {
    private int mId;
    private Date mDate;
    private int mCurbmi;
    private int mTarge_tweight;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public int getmCurbmi() {
        return mCurbmi;
    }

    public void setmCurbmi(int mCurbmi) {
        this.mCurbmi = mCurbmi;
    }

    public int getmTarge_tweight() {
        return mTarge_tweight;
    }

    public void setmTarge_tweight(int mTarge_tweight) {
        this.mTarge_tweight = mTarge_tweight;
    }

    public BMI(Date mDate, int mCurbmi, int mTarge_tweight) {
        this.mDate = mDate;
        this.mCurbmi = mCurbmi;
        this.mTarge_tweight = mTarge_tweight;
    }

    public BMI() {
        this.mId = mId;
        this.mDate = mDate;
        this.mCurbmi = mCurbmi;
        this.mTarge_tweight = mTarge_tweight;
    }

    public void setmDate(String date) {
    }
}
