package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;
import com.blogspot.thengnet.mobilestudent.data.NoteDbHelper;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private ArrayList<Note> mNotes;

    private FloatingActionButton fabNewNote;

    private NotesCursorAdapter mNoteCursorAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ListView notesList = (ListView) findViewById(R.id.notes_list);

        mNoteCursorAdapter = new NotesCursorAdapter(this, getRows());
        notesList.setAdapter(mNoteCursorAdapter);

        fabNewNote = (FloatingActionButton) findViewById(R.id.fab_new_note);
        fabNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(NotesActivity.this, NotesEditor.class));
            }
        });

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.media_page:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        menuItem.setChecked(true);
                        break;
                    case R.id.notes_page:
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

    private Cursor getRows () {

        Cursor existentNotes = getContentResolver().query(NoteContract.NoteEntry.CONTENT_URI, null, null, null, null);
        int totalRows = -2;

        if (existentNotes != null && totalRows != -1) {
            Snackbar.make(findViewById(R.id.notes_snackbar_frame), "Cursor not empty!", Snackbar.LENGTH_SHORT).show();
            Log.v(NotesActivity.class.getName(), "Cursor not empty!");

            totalRows = existentNotes.getCount();
            existentNotes.moveToFirst();

            for (int i = 0; i < totalRows; i++) {
                int titleIndex = existentNotes.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
                int contentIndex = existentNotes.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT);
                int date = existentNotes.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE);

                String title = existentNotes.getString(titleIndex);
                String content = existentNotes.getString(contentIndex);
                String noteDate = existentNotes.getString(date);

                Log.v(NotesActivity.class.getName(), "Title: " + title + "\nContent: " + content + "Date: " + noteDate);
                existentNotes.moveToNext();
            }
            return existentNotes;
        } else {
            Snackbar.make(findViewById(R.id.notes_snackbar_frame), "Cursor empty!", Snackbar.LENGTH_SHORT).show();
            Log.v(NotesActivity.class.getName(), "Cursor empty!");
            return null;
        }
    }
}