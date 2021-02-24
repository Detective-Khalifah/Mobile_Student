package com.blogspot.thengnet.mobilestudent.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.thengnet.mobilestudent.data.NoteContract.NoteEntry;

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "student_notes.db";

    private static final String TABLE_CREATION_STATEMENT = "CREATE TABLE " + NoteContract.TABLE_NAME
            + " ( " + NoteEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            NoteEntry.COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
            NoteEntry.COLUMN_NOTE_CONTENT + " TEXT NOT NULL, " +
            NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE + " TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP " +
            ");";

    public NoteDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(TABLE_CREATION_STATEMENT);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteContract.TABLE_NAME);
        onCreate(db);
    }
}
