package com.blogspot.thengnet.mobilestudent

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.blogspot.thengnet.mobilestudent.data.NoteContract
import com.blogspot.thengnet.mobilestudent.databinding.ActivityNotesViewerBinding

class NotesViewer : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var binding: ActivityNotesViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentNoteDeets = intent
        mNoteUri = currentNoteDeets.data
        supportLoaderManager.restartLoader(106, null, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.viewer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_edit) {
            // Start the {@link NotesEditor} Activity, passing mNoteUri to query in the database.
            startActivity(Intent(this, NotesEditor::class.java).setData(mNoteUri))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this, mNoteUri!!, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        cursor.moveToFirst()
        val titleIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE)
        val contentIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT)
        val title = cursor.getString(titleIndex)
        val content = cursor.getString(contentIndex)
        binding.notesViewerTitle.text = title
        binding.notesViewerContent.text = content
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        binding.notesViewerTitle.text = null
        binding.notesViewerContent.text = null
    }

    companion object {
        private var mNoteUri: Uri? = null
    }
}