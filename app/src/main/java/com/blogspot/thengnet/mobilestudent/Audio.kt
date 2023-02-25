package com.blogspot.thengnet.mobilestudent;

public class Audio {

    private String mAudioTitle;
    private String mAudioPath;
    private String mAudioLength;

    public Audio () {
    }

    public Audio (String theAudioTitle, String theAudioPath, String theAudioLength) {
        this.mAudioTitle = theAudioTitle;
        this.mAudioPath = theAudioPath;
        this.mAudioLength = theAudioLength;
    }

    public String getAudioTitle () {
        return mAudioTitle;
    }

    public String getAudioLength () {
        return mAudioLength;
    }

    public String getAudioPath () {
        return mAudioPath;
    }
}