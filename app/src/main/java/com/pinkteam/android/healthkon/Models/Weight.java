package com.pinkteam.android.healthkon.Models;

import java.util.Date;

public class Weight {
    private int mId;
    private int mValue;
    private Date mDate;

    public Weight(int value, Date date) {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmValue() {
        return mValue;
    }

    public void setmValue(int mValue) {
        this.mValue = mValue;
    }

    public Date getmDate() {
        return mDate;
    }


    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public Weight() {
        this.mValue = mValue;
        this.mDate = mDate;
    }
}
