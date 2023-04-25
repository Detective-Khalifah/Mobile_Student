package com.blogspot.thengnet.mobilestudent

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.blogspot.thengnet.mobilestudent.data.NoteContract
import com.blogspot.thengnet.mobilestudent.databinding.FragmentNotesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class NotesFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentNotesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    var pbLoadingNotes: ProgressBar? = null
    private var fabNewNote: FloatingActionButton? = null
    private var mNoteCursorAdapter: NotesCursorAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNoteCursorAdapter = NotesCursorAdapter(context, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbLoadingNotes = view.findViewById<View>(R.id.pb_loading_notes) as ProgressBar
        fabNewNote = activity?.findViewById<FloatingActionButton>(R.id.fab)
        fabNewNote?.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity().applicationContext,
                    NotesEditor::class.java
                )
            )
        }
        val notesList = view.findViewById<View>(R.id.notes_list) as ListView
        notesList.adapter = mNoteCursorAdapter
        notesList.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            showProgress()
            startActivity(
                Intent(context, NotesViewer::class.java)
                    .setData(
                        ContentUris.withAppendedId(
                            NoteContract.NoteEntry.CONTENT_URI, id
                        )
                    )
            ) // content uri of the clicked #NoteEntry
        }

        // Set a #OnItemLongClickListener handler to start {@link NotesEditor} Activity when a Note
        // is long clicked.
        notesList.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            startActivity(
                Intent(context, NotesEditor::class.java)
                    .setData(
                        ContentUris.withAppendedId(
                            NoteContract.NoteEntry.CONTENT_URI, id
                        )
                    )
            )
            true
        }
        loaderManager.initLoader(NOTES_ACTIVITY_LOADER, null, this)
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            NoteContract.NoteEntry._ID,
            NoteContract.NoteEntry.COLUMN_NOTE_TITLE,
            NoteContract.NoteEntry.COLUMN_NOTE_CONTENT,
            NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE
        )
        val whereClause: String? = null
        val whereArgs: Array<String>? = null
        showProgress()
        return CursorLoader(
            requireContext(), NoteContract.NoteEntry.CONTENT_URI,
            projection, whereClause, whereArgs, null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        hideProgress()
        mNoteCursorAdapter!!.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mNoteCursorAdapter!!.swapCursor(null)
    }

    private fun showProgress() {
        pbLoadingNotes!!.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pbLoadingNotes!!.visibility = View.GONE
    }

    companion object {
        private const val NOTES_ACTIVITY_LOADER = 105
    }
}