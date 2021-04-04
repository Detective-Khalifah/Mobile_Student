package com.blogspot.thengnet.mobilestudent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

import java.util.Date;

public class NotesEditor extends AppCompatActivity {

    private static String noteTitle, noteContent, previousTitle, previousContent;
    private static Uri mNoteUri;

    private Snackbar editNotify;
    private TextInputEditText editTitle, editContent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        editTitle = (TextInputEditText) findViewById(R.id.edit_note_title);
        editContent = (TextInputEditText) findViewById(R.id.edit_note_content);
        editNotify = Snackbar.make(findViewById(R.id.editor_snackbar_frame), "",
                Snackbar.LENGTH_SHORT);

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
                if (mNoteUri != null) {
                    if (updateNote())
                        finish();
                    break;
                }

                if (saveNote())
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

                // Fetch saved note title and set fetched text on the #editTitle
                // {@link TextInputEditText}
                previousTitle = currentNote.getString(currentNote.getColumnIndex(
                        NoteContract.NoteEntry.COLUMN_NOTE_TITLE)).trim();
                editTitle.setText(previousTitle);

                // Fetch saved note content and set fetched text on the #editContent
                // {@link TextInputEditText}
                previousContent = currentNote.getString(currentNote.getColumnIndex(
                        NoteContract.NoteEntry.COLUMN_NOTE_CONTENT)).trim();
                editContent.setText(previousContent);
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

    private boolean updateNote () {
        noteTitle = editTitle.getText().toString().trim();
        noteContent = editContent.getText().toString().trim();

        // Ascertain changes were made to the notes's title or content
        if (noteTitle.equals(previousTitle) && noteContent.equals(previousContent)) {
            editNotify.setText(getString(R.string.update_found_matching_note));
            editNotify.show();
            return false;
        }

        if (checkIfFieldsEmpty()) return false;

        ContentValues updateValues = new ContentValues();
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, noteTitle);
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, noteContent);
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE, String.valueOf(new Date()));

        int notesUpdated = getContentResolver()
                .update(mNoteUri, updateValues, null, null);
        if (notesUpdated == 1) {
            editNotify.setText(getString(R.string.note_editor_successful_update));
            editNotify.show();
            return true;
        } else {
            editNotify.setText(getString(R.string.note_editor_unsuccessful_update));
            editNotify.show();
            return false;
        }
    }

    private boolean saveNote () {
        noteTitle = editTitle.getText().toString().trim();
        noteContent = editContent.getText().toString();
        String[] matchArgs = new String[]{noteTitle, noteContent};

        Cursor matchFound = null;

        if (checkIfFieldsEmpty()) return false;

        try {
            matchFound = getContentResolver().query(NoteContract.NoteEntry.CONTENT_URI, null,
                    NoteContract.NoteEntry.COLUMN_NOTE_TITLE + "=? OR " +
                            NoteContract.NoteEntry.COLUMN_NOTE_CONTENT + "=?",
                    matchArgs, null);

            if (matchFound != null && matchFound.getCount() > 0) {
                editNotify.setText(getString(R.string.save_found_matching_note));
                editNotify.show();
                return false;
            }
        } catch (NullPointerException npe) {
            npe.getMessage();
        } finally {
            if (matchFound != null)
                matchFound.close();
        }

        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, noteTitle);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, noteContent);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE, String.valueOf(new Date()));

        Uri ins = getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI, values);
        if (ins != null) {
            editNotify.setText(getString(R.string.note_editor_successful_save));
            editNotify.show();
            return true;
        } else {
            editNotify.setText(getString(R.string.note_editor_unsuccessful_save));
            editNotify.show();
            return false;
        }

    }

    private boolean checkIfFieldsEmpty () {
        if (noteTitle.equals("") && noteContent.equals("")) {
            editNotify.setText(getString(R.string.error_title_and_content_empty));
            editNotify.show();
            return true;
        } else {
            if (noteTitle.equals("")) {
                editNotify.setText(getString(R.string.error_title_empty));
                editNotify.show();
                return true;
            }

            if (noteContent.equals("")) {
                editNotify.setText(getString(R.string.error_content_empty));
                editNotify.show();
                return true;
            }
        }
        return false;
    }
}