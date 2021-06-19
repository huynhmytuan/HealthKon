package com.pinkteam.android.healthkon.Models;

import java.util.Date;

public class Top {
    private int mId;
    private String mUsername;
    private int mDistance;
    private Date mStart_day;
    private Date mEnd_day;
    private String mTop;

    public Top(String mUsername, int mDistance, Date mStart_day, Date mEnd_day, String mTop) {
        this.mUsername = mUsername;
        this.mDistance = mDistance;
        this.mStart_day = mStart_day;
        this.mEnd_day = mEnd_day;
        this.mTop = mTop;
    }

    public Top() {
        this.mId = mId;
        this.mUsername = mUsername;
        this.mDistance = mDistance;
        this.mStart_day = mStart_day;
        this.mEnd_day = mEnd_day;
        this.mTop = mTop;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public int getmDistance() {
        return mDistance;
    }

    public void setmDistance(int mDistance) {
        this.mDistance = mDistance;
    }

    public Date getmStart_day() {
        return mStart_day;
    }

    public void setmStart_day(Date mStart_day) {
        this.mStart_day = mStart_day;
    }

    public Date getmEnd_day() {
        return mEnd_day;
    }

    public void setmEnd_day(Date mEnd_day) {
        this.mEnd_day = mEnd_day;
    }

    public String getmTop() {
        return mTop;
    }

    public void setmTop(String mTop) {
        this.mTop = mTop;
    }

}
