package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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