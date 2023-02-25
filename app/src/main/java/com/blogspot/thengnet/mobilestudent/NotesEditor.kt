package com.blogspot.thengnet.mobilestudent

import android.content.ContentValues
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.thengnet.mobilestudent.data.NoteContract
import com.blogspot.thengnet.mobilestudent.databinding.ActivityNotesEditorBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class NotesEditor : AppCompatActivity() {

    private lateinit var binding: ActivityNotesEditorBinding

    private lateinit var editNotify: Snackbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editNotify = Snackbar.make(binding.editorSnackbarFrame, "", Snackbar.LENGTH_SHORT)
        val currentNote = intent
        mNoteUri = currentNote.data

        // Fetch the saved note's details if its Uri was passed; do not fetch details of a note
        // that does not exist yet, otherwise!
        if (mNoteUri != null) {
            noteDetails
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                //                InputMethodManager inputMM = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMM.hideSoftInputFromInputMethod((IBinder) this, 0);
                if (mNoteUri != null) {
                    if (updateNote()) finish()
                }
                if (saveNote()) finish()
            }
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.editor_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }// TODO: Show a Snackbar only if one is not currently being shown.

    // TODO: Set a timer to close - #finish() - this activity after 30 seconds.
    // Fetch saved note title and set fetched text on the #editTitle
    // {@link TextInputEditText}

    // Fetch saved note content and set fetched text on the #editContent
    // {@link TextInputEditText}
    private val noteDetails: Unit
        private get() {
            var currentNote: Cursor? = null
            var notFound: Snackbar? = null
            try {
                currentNote = contentResolver.query(
                    mNoteUri!!, null, null, null, null
                )
                if (currentNote != null) {
                    currentNote.moveToFirst()

                    // Fetch saved note title and set fetched text on the #editTitle
                    // {@link TextInputEditText}
                    previousTitle = currentNote.getString(
                        currentNote.getColumnIndex(
                            NoteContract.NoteEntry.COLUMN_NOTE_TITLE
                        )
                    ).trim { it <= ' ' }
                    binding.editNoteTitle.setText(previousTitle)

                    // Fetch saved note content and set fetched text on the #editContent
                    // {@link TextInputEditText}
                    previousContent = currentNote.getString(
                        currentNote.getColumnIndex(
                            NoteContract.NoteEntry.COLUMN_NOTE_CONTENT
                        )
                    ).trim { it <= ' ' }
                    binding.editNoteContent.setText(previousContent)
                } else throw CursorIndexOutOfBoundsException("Could not find note!")
            } catch (cursorException: CursorIndexOutOfBoundsException) {
                // TODO: Show a Snackbar only if one is not currently being shown.
                if (notFound == null) {
                    notFound = Snackbar.make(
                        findViewById(R.id.editor_snackbar_frame),
                        (if (cursorException.message != null) cursorException.message else "")!!,
                        Snackbar.LENGTH_LONG
                    )
                    notFound.show()
                }

                // TODO: Set a timer to close - #finish() - this activity after 30 seconds.
            } finally {
                currentNote?.close()
            }
        }

    private fun updateNote(): Boolean {
        noteTitle = binding.editNoteTitle.text.toString().trim { it <= ' ' }
        noteContent = binding.editNoteContent.text.toString().trim { it <= ' ' }

        // Ascertain changes were made to the notes's title or content
        if (noteTitle == previousTitle && noteContent == previousContent) {
            editNotify.setText(getString(R.string.update_found_matching_note))
            editNotify.show()
            return false
        }
        if (checkIfFieldsEmpty()) return false
        val updateValues = ContentValues()
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, noteTitle)
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, noteContent)
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE, Date().toString())
        val notesUpdated = contentResolver
            .update(mNoteUri!!, updateValues, null, null)
        return if (notesUpdated == 1) {
            editNotify.setText(getString(R.string.note_editor_successful_update))
            editNotify.show()
            true
        } else {
            editNotify.setText(getString(R.string.note_editor_unsuccessful_update))
            editNotify.show()
            false
        }
    }

    private fun saveNote(): Boolean {
        noteTitle = binding.editNoteTitle.text.toString().trim { it <= ' ' }
        noteContent = binding.editNoteContent.text.toString()
        val matchArgs = arrayOf(noteTitle, noteContent)
        var matchFound: Cursor? = null
        if (checkIfFieldsEmpty()) return false
        try {
            matchFound = contentResolver.query(
                NoteContract.NoteEntry.CONTENT_URI, null,
                NoteContract.NoteEntry.COLUMN_NOTE_TITLE + "=? OR " +
                        NoteContract.NoteEntry.COLUMN_NOTE_CONTENT + "=?",
                matchArgs, null
            )
            if (matchFound != null && matchFound.count > 0) {
                editNotify.setText(getString(R.string.save_found_matching_note))
                editNotify.show()
                return false
            }
        } catch (npe: NullPointerException) {
            npe.message
        } finally {
            matchFound?.close()
        }
        val values = ContentValues()
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, noteTitle)
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, noteContent)
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE, Date().toString())
        val ins = contentResolver.insert(NoteContract.NoteEntry.CONTENT_URI, values)
        return if (ins != null) {
            editNotify.setText(getString(R.string.note_editor_successful_save))
            editNotify.show()
            true
        } else {
            editNotify.setText(getString(R.string.note_editor_unsuccessful_save))
            editNotify.show()
            false
        }
    }

    private fun checkIfFieldsEmpty(): Boolean {
        if (noteTitle == "" && noteContent == "") {
            editNotify.setText(getString(R.string.error_title_and_content_empty))
            editNotify.show()
            return true
        } else {
            if (noteTitle == "") {
                editNotify.setText(getString(R.string.error_title_empty))
                editNotify.show()
                return true
            }
            if (noteContent == "") {
                editNotify.setText(getString(R.string.error_content_empty))
                editNotify.show()
                return true
            }
        }
        return false
    }

    companion object {
        private var noteTitle: String? = null
        private var noteContent: String? = null
        private var previousTitle: String? = null
        private var previousContent: String? = null
        private var mNoteUri: Uri? = null
    }
}