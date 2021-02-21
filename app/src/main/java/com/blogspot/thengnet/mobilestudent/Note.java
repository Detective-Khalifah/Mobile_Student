package com.blogspot.thengnet.mobilestudent;

import java.util.Date;

public class Note {
    private String mNoteTitle;
    private String mNoteBody;
    private Date mDateUpdated;

    public Note() {
    }

    public Note (String theNoteTitle, String theNoteBody, Date theDate) {
        this.mNoteTitle = theNoteTitle;
        this.mNoteBody = theNoteBody;
        this.mDateUpdated = theDate;
    }

    public String getNoteTitle () {
        return mNoteTitle;
    }

    public String getNoteBody () {
        return mNoteBody;
    }

    public Date getDateUpdated () {
        return mDateUpdated;
    }
}
