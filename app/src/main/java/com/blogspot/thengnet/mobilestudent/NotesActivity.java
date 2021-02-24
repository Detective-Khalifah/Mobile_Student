package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.blogspot.thengnet.mobilestudent.data.NoteDbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private ArrayList<Note> mNotes;

    private NoteDbHelper mNoteDbHelper;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNotes = new ArrayList<>();
        mNotes.add(new Note("Second Note - from Activity",
                "This is a test note from NotesActivity. 0... 1... 2...",
                new Date()));

        mNotes.add(new Note("Third Note - from Activity",
                "This is a test note from NotesActivity. 2... 1... 0...",
                new Date()));

        NotesAdapter notesAdapter = new NotesAdapter(this, mNotes);

        ListView notesList = (ListView) findViewById(R.id.notes_list);
        notesList.setAdapter(notesAdapter);

        mNoteDbHelper = new NoteDbHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void openMediaActivity(View view) {
        startActivity(new Intent(NotesActivity.this, MainActivity.class));
    }
}