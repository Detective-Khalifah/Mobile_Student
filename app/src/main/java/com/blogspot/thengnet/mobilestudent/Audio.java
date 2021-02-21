package com.blogspot.thengnet.mobilestudent;

public class Audio {

    private String mAudioTitle;
    private String mAudioLength;

    public Audio () {
    }

    public Audio (String mAudioTitle, String mAudioLength) {
        this.mAudioTitle = mAudioTitle;
        this.mAudioLength = mAudioLength;
    }

    public String getAudioTitle () {
        return mAudioTitle;
    }

    public String getAudioLength () {
        return mAudioLength;
    }

}