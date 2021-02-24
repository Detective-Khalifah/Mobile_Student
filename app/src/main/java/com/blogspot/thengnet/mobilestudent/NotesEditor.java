package com.blogspot.thengnet.mobilestudent;

import android.content.ContentValues;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

import java.util.Date;

public class NotesEditor extends AppCompatActivity {

    TextInputEditText editTitle, editContent;
    String title, content;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        editTitle = (TextInputEditText) findViewById(R.id.edit_note_title);
        editContent = (TextInputEditText) findViewById(R.id.edit_note_content);

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

    private void saveNote() {

        title = editTitle.getText().toString();
        content = editContent.getText().toString();

        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, title);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, content);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE, String.valueOf(new Date()));

        Uri ins = getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI, values);
        if (ins != null)
            Snackbar.make(findViewById(R.id.notes_snackbar_frame), "Data successfully entered!", Snackbar.LENGTH_SHORT).show();
        else
            Snackbar.make(findViewById(R.id.notes_snackbar_frame), "Data not entered!", Snackbar.LENGTH_SHORT).show();

    }
}