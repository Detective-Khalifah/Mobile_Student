package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blogspot.thengnet.mobilestudent.R;
import com.blogspot.thengnet.mobilestudent.data.NoteContract;

public class NotesViewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static Uri noteUri;
    private TextView tvNoteTitle, tvNoteContent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);

        Intent currentNoteDeets = getIntent();
        noteUri = currentNoteDeets.getData();

        tvNoteTitle = (TextView) findViewById(R.id.notes_viewer_title);
        tvNoteContent = (TextView) findViewById(R.id.notes_viewer_content);

        getSupportLoaderManager().restartLoader(106, null, this);
    }

    public Loader<Cursor> onCreateLoader (int id, Bundle bundle) {
        return new CursorLoader(this, noteUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();

        int titleIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
        int contentIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT);

        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);
        Log.v(NotesViewer.class.getSimpleName(), "Title: " + title);
        Log.v(NotesViewer.class.getSimpleName(), "Content: " + content);

        tvNoteTitle.setText(title);
        tvNoteContent.setText(content);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        tvNoteTitle.setText(null);
        tvNoteContent.setText(null);
    }
}