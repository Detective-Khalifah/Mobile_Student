package com.blogspot.thengnet.mobilestudent.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteProvider extends ContentProvider {

    private NoteDbHelper mNoteDbHelper;

    /**
     * Initialize the {@link NoteProvider} and {@link NoteDbHelper} object.
     * @return the status.
     */
    @Override
    public boolean onCreate () {
        mNoteDbHelper = new NoteDbHelper(getContext());
        return true;
    }

    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs,
                         String sortOrder) {
        SQLiteDatabase theDb = mNoteDbHelper.getReadableDatabase();
        Cursor table = theDb.query(NoteContract.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        return null;
    }

    public String getType (Uri uri) {
        return null;
    }

    public Uri insert (Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete ( Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update ( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}