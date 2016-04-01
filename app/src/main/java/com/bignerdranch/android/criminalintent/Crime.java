package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private int numberPicturesTaken = 0;

    public boolean isFirstPhoto() {
        return firstPhoto;
    }

    public void setFirstPhoto(boolean firstPhoto) {
        this.firstPhoto = firstPhoto;
    }

    private boolean firstPhoto = true;

    public int getNumberPicturesTaken() {
        return numberPicturesTaken;
    }

    public void setNumberPicturesTaken(int numberPicturesTaken) {
        this.numberPicturesTaken = numberPicturesTaken;
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename(int numPicToGet) {
        return "IMG_" + getId().toString() + "_"+numPicToGet+".jpg";
    }
}
