package com.blogspot.thengnet.mobilestudent;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

public class NotesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ProgressBar pbLoadingNotes;

    private FloatingActionButton fabNewNote;

    private NotesCursorAdapter mNoteCursorAdapter;

    private static final int NOTES_ACTIVITY_LOADER = 105;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        pbLoadingNotes = (ProgressBar) findViewById(R.id.pb_loading_notes);

        ListView notesList = (ListView) findViewById(R.id.notes_list);

        mNoteCursorAdapter = new NotesCursorAdapter(this, null);
        notesList.setAdapter(mNoteCursorAdapter);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                showProgress();
                startActivity(new Intent(NotesActivity.this, NotesViewer.class)
                        .setData(ContentUris.withAppendedId(
                                NoteContract.NoteEntry.CONTENT_URI, id))); // content uri of the clicked #NoteEntry
            }
        });

        getSupportLoaderManager().initLoader(NOTES_ACTIVITY_LOADER, null, this);

        fabNewNote = (FloatingActionButton) findViewById(R.id.fab_new_note);
        fabNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(NotesActivity.this, NotesEditor.class));
            }
        });

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.media_page:
                        menuItem.setChecked(true);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.notes_page:
                        menuItem.setChecked(true);
                        startActivity(new Intent(getApplicationContext(), NotesActivity.class));
                        break;
                    case R.id.calculator_page:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {
        String[] projection = {
                NoteContract.NoteEntry._ID,
                NoteContract.NoteEntry.COLUMN_NOTE_TITLE,
                NoteContract.NoteEntry.COLUMN_NOTE_CONTENT,
                NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE
        };
        String whereClause = null;
        String[] whereArgs = null;

        showProgress();
        return new CursorLoader(this, NoteContract.NoteEntry.CONTENT_URI,
                projection, whereClause, whereArgs, null);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor cursor) {
        hideProgress();
        mNoteCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        mNoteCursorAdapter.swapCursor(null);
    }

    private void showProgress () {
        pbLoadingNotes.setVisibility(View.VISIBLE);
    }

    private void hideProgress () {
        pbLoadingNotes.setVisibility(View.GONE);
    }
}