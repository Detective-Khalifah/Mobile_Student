package com.blogspot.thengnet.mobilestudent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

import java.util.Date;

public class NotesEditor extends AppCompatActivity {

    private static Uri mNoteUri;

    TextInputEditText editTitle, editContent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        editTitle = (TextInputEditText) findViewById(R.id.edit_note_title);
        editContent = (TextInputEditText) findViewById(R.id.edit_note_content);

        Intent currentNote = getIntent();
        mNoteUri = currentNote.getData();

        // Fetch the saved note's details if its Uri was passed; do not fetch details of a note
        // that does not exist yet, otherwise!
        if (mNoteUri != null) {
            getNoteDetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveNote();
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getNoteDetails () {
        Cursor currentNote = null;
        Snackbar notFound = null;
        try {
            currentNote = getContentResolver().query(
                    mNoteUri, null, null, null, null);

            if (currentNote != null) {
                currentNote.moveToFirst();
                Log.v(NotesEditor.class.getName(), "Note URI: " + mNoteUri + "\n" + "CurrentNote: " + currentNote);

                editTitle.setText(currentNote.getString(currentNote.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE)));
                editContent.setText(currentNote.getString(currentNote.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT)));
            } else
                throw new CursorIndexOutOfBoundsException("Could not find note!");
        } catch (CursorIndexOutOfBoundsException cursorException) {
            // TODO: Show a Snackbar only if one is not currently being shown.
            if (notFound == null) {
                notFound = Snackbar.make(findViewById(R.id.editor_snackbar_frame),
                        cursorException.getMessage() != null ? cursorException.getMessage() : "",
                        Snackbar.LENGTH_LONG);
                notFound.show();
            }

            // TODO: Set a timer to close - #finish() - this activity after 30 seconds.
        } finally {
            if (currentNote != null)
                currentNote.close();
        }
    }

    private void saveNote () {
        String noteTitle = editTitle.getText().toString().trim();
        String noteContent = editContent.getText().toString();

        Snackbar editNotify = null;

        if (noteTitle.equals("") && noteContent.equals("")) {
            editNotify = Snackbar.make(findViewById(R.id.editor_snackbar_frame),
                    "Title & Content cannot be empty!",
                    Snackbar.LENGTH_SHORT);
            editNotify.show();
            return;
        } else {
            if (noteTitle.equals("")) {
                editNotify = Snackbar.make(findViewById(R.id.editor_snackbar_frame),
                        "Title cannot be empty!",
                        Snackbar.LENGTH_SHORT);
                editNotify.show();
                return;
            }

            if (noteContent.equals("")) {
                editNotify = Snackbar.make(findViewById(R.id.editor_snackbar_frame),
                        "Content cannot be empty!",
                        Snackbar.LENGTH_SHORT);
                editNotify.show();
                return;
            }
        }

        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, noteTitle);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, noteContent);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE, String.valueOf(new Date()));

        Uri ins = getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI, values);
        if (ins != null) {
            editNotify = Snackbar.make(findViewById(R.id.editor_snackbar_frame),
                    getString(R.string.note_editor_successful_save), Snackbar.LENGTH_SHORT);
        } else {
            editNotify = Snackbar.make(findViewById(R.id.editor_snackbar_frame),
                    getString(R.string.note_editor_unsuccessful_save), Snackbar.LENGTH_SHORT);
        }
        editNotify.show();

    }
}