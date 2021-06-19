package com.pinkteam.android.healthkon.Models;

public class Account {
    private int mId;
    private String mUsername;
    private String mPassword;

    public Account(String mUsername, String mPassword) {
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }

    public Account() {
        this.mId = mId;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
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

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
