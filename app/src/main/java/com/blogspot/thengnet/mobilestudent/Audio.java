package com.blogspot.thengnet.mobilestudent;

public class Audio {

    private String mAudioTitle;
    private String mAudioLength;
    private String mAudioDisplayName;
    private String mAudioPath;

    public Audio () {
    }

    public Audio (String mAudioTitle, String mAudioDisplayName, String mAudioLength/*, String mAudioPath*/) {
        this.mAudioTitle = mAudioTitle;
        this.mAudioLength = mAudioLength;
        this.mAudioDisplayName = mAudioDisplayName;
        this.mAudioPath = mAudioPath;
    }

    public String getAudioTitle () {
        return mAudioTitle;
    }

    public String getAudioLength () {
        return mAudioLength;
    }

    public String getAudioDisplayName () {
        return mAudioDisplayName;
    }

    public String getAudioPath () {
        return mAudioPath;
    }
}
