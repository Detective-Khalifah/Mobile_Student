package com.blogspot.thengnet.mobilestudent.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteProvider extends ContentProvider {

    private final static int NOTES = 5;
    private final static int NOTES_ID = 55;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.NOTES_PATH, NOTES);
        sUriMatcher.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.NOTES_PATH + "/#", NOTES_ID);
    }

    private NoteDbHelper mNoteDbHelper;

    /**
     * Initialize the {@link NoteProvider} and {@link NoteDbHelper} object.
     *
     * @return the status.
     */
    @Override
    public boolean onCreate () {
        mNoteDbHelper = new NoteDbHelper(getContext());
        return true;
    }

    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs,
                         String sortOrder) {
        Cursor table;
        SQLiteDatabase theDb = mNoteDbHelper.getReadableDatabase();

        int UriType = sUriMatcher.match(uri);
        switch (UriType) {
            case NOTES:
                table = theDb.query(NoteContract.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                // Register notifier on cursor object
                table.setNotificationUri(getContext().getContentResolver(), uri);

                return table;
            case NOTES_ID:
                selection = NoteContract.NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                table = theDb.query(NoteContract.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                // Register notifier on cursor object
                table.setNotificationUri(getContext().getContentResolver(), uri);

                return table;
            default:
                throw new IllegalArgumentException("Content URI is unknown!" + uri);
        }
    }

    public String getType (Uri uri) {
        return null;
    }

    public Uri insert (Uri uri, ContentValues values) {
        String noteTitle = values.getAsString(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
        String noteContent = values.getAsString(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT);
        String noteUpdate = values.getAsString(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE);

        if (noteTitle == null)
            throw new IllegalArgumentException("Note Title is missing!");
        if (noteContent == null)
            throw new IllegalArgumentException("Note content missing!");
        if (noteUpdate == null)
            throw new IllegalArgumentException("When was the note made/updated/taken?!");

        final int UriType = sUriMatcher.match(uri);
        switch (UriType) {
            case NOTES:
                return addNote(uri, values);
            default:
                throw new IllegalArgumentException("INSERT Content uri unknown!");
        }
    }

    @Override
    public int delete (Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        final int deleteType = sUriMatcher.match(uri);
        switch (deleteType) {
            case NOTES:
                rowsDeleted = mNoteDbHelper.getWritableDatabase().delete(NoteContract.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_ID:
                selection = NoteContract.NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = mNoteDbHelper.getWritableDatabase().delete(NoteContract.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("DELETE Content uri unknown!");
        }
        if (rowsDeleted > 0) {
            // Notify the cursor loader when the cursor data has changed due to a delete query
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update (Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int UriType = sUriMatcher.match(uri);
        switch (UriType) {
            case NOTES:
                return updateNote(uri, values, selection, selectionArgs);
            case NOTES_ID:
                selection = NoteContract.NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("UPDATE Content URI unknown!");
        }
    }

    private Uri addNote (Uri uri, ContentValues values) {
        // Notify the cursor loader when the cursor data has changed when a note is added
        getContext().getContentResolver().notifyChange(uri, null);

        long id = mNoteDbHelper.getWritableDatabase().insert(NoteContract.TABLE_NAME,
                null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateNote (Uri uri, ContentValues updateValues, String whereClause,
                            String[] whereArg) {
        if (updateValues.containsKey(NoteContract.NoteEntry.COLUMN_NOTE_TITLE)) {
            String noteTitle = updateValues.getAsString(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
            if (noteTitle == null || noteTitle.equals(""))
                throw new IllegalArgumentException("Note Title passed but not updated!");
        }
        if (updateValues.containsKey(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT)) {
            String noteContent = updateValues.getAsString(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT);
            if (noteContent == null || noteContent.equals(""))
                throw new IllegalArgumentException("Note Content passed but not updated!");
        }
        if (updateValues.containsKey(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE)) {
            String noteUpdate = updateValues.getAsString(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE);
            if (noteUpdate == null || noteUpdate.equals(""))
                throw new IllegalArgumentException("Note's \"Last Updated Time\" passed, but as a null value!");
        }

        int rowsUpdated = mNoteDbHelper.getWritableDatabase().update(NoteContract.TABLE_NAME,
                updateValues, whereClause, whereArg);
        if (rowsUpdated > 0) {
            // Notify the cursor loader when the cursor data has changed when a note is updated
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}