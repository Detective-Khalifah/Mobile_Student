package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;
import com.blogspot.thengnet.mobilestudent.data.NoteDbHelper;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private ArrayList<Note> mNotes;

    private NoteDbHelper mNoteDbHelper;

    private FloatingActionButton fabNewNote;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mNotes = new ArrayList<>();
//        mNotes.add(new Note("Second Note - from Activity",
//                "This is a test note from NotesActivity. 0... 1... 2... 3... 4... 5....",
//                new Date()));
//
//        mNotes.add(new Note("Third Note - from Activity",
//                "This is a test note from NotesActivity. 2... 1... 0...",
//                new Date()));

//        mNoteDbHelper = new NoteDbHelper(this);

        fabNewNote = (FloatingActionButton) findViewById(R.id.fab_new_note);
        fabNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(NotesActivity.this, NotesEditor.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void openMediaActivity (View view) {
        if (getRows()) {

//            NotesAdapter notesAdapter = new NotesAdapter(this, mNotes);
//
//            ListView notesList = (ListView) findViewById(R.id.notes_list);
//            notesList.setAdapter(notesAdapter);
        }
//        startActivity(new Intent(NotesActivity.this, MainActivity.class));
    }

    private boolean getRows () {

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
//                mNotes.add(new Note(title, content, new Date(noteDate)));
                existentNotes.moveToNext();
            }
            return true;
        } else {
            Snackbar.make(findViewById(R.id.notes_snackbar_frame), "Cursor empty!", Snackbar.LENGTH_SHORT).show();
            Log.v(NotesActivity.class.getName(), "Cursor empty!");
            return false;
        }
    }
}

//mNotes.add(new Note(
//        existentNotes.getString(existentNotes.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT)),
//        existentNotes.getString(existentNotes.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT)),
//        new Date(existentNotes.getString(existentNotes.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE))))
//        );