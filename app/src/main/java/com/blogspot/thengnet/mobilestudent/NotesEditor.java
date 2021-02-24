package com.blogspot.thengnet.mobilestudent;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NotesEditor extends AppCompatActivity {

    TextInputEditText editTitle, editContent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        editTitle = (TextInputEditText) findViewById(R.id.edit_note_title);
        editContent = (TextInputEditText) findViewById(R.id.edit_note_content);

        editTitle.getText().toString();
        editContent.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}