package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

public class NotesViewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static Uri mNoteUri;
    private TextView tvNoteTitle, tvNoteContent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);

        Intent currentNoteDeets = getIntent();
        mNoteUri = currentNoteDeets.getData();

        tvNoteTitle = (TextView) findViewById(R.id.notes_viewer_title);
        tvNoteContent = (TextView) findViewById(R.id.notes_viewer_content);

        getSupportLoaderManager().restartLoader(106, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.viewer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.menu_edit) {
            // Start the {@link NotesEditor} Activity, passing mNoteUri to query in the database.
            startActivity(new Intent(this, NotesEditor.class).setData(mNoteUri));
        }
        return super.onOptionsItemSelected(item);
    }

    public Loader<Cursor> onCreateLoader (int id, Bundle bundle) {
        return new CursorLoader(this, mNoteUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();

        int titleIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
        int contentIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT);

        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);

        tvNoteTitle.setText(title);
        tvNoteContent.setText(content);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        tvNoteTitle.setText(null);
        tvNoteContent.setText(null);
    }
}