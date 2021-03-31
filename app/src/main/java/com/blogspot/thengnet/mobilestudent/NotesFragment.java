package com.blogspot.thengnet.mobilestudent;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTES_ACTIVITY_LOADER = 105;
    ProgressBar pbLoadingNotes;
    private FloatingActionButton fabNewNote;
    private NotesCursorAdapter mNoteCursorAdapter;

    public NotesFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNoteCursorAdapter = new NotesCursorAdapter(getContext(), null);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View notesView = inflater.inflate(R.layout.fragment_notes, container, false);

        return notesView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbLoadingNotes = (ProgressBar) view.findViewById(R.id.pb_loading_notes);

        fabNewNote = (FloatingActionButton) view.findViewById(R.id.fab_new_note);
        fabNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), NotesEditor.class));
            }
        });

        ListView notesList = (ListView) view.findViewById(R.id.notes_list);

        notesList.setAdapter(mNoteCursorAdapter);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                showProgress();
                startActivity(new Intent(getContext(), NotesViewer.class)
                        .setData(ContentUris.withAppendedId(
                                NoteContract.NoteEntry.CONTENT_URI, id))); // content uri of the clicked #NoteEntry
            }
        });

        // Set a #OnItemLongClickListener handler to start {@link NotesEditor} Activity when a Note
        // is long clicked.
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), NotesEditor.class)
                        .setData(ContentUris.withAppendedId(
                                NoteContract.NoteEntry.CONTENT_URI, id)));
                return true;
            }
        });

        getLoaderManager().initLoader(NOTES_ACTIVITY_LOADER, null, this);
    }

    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {
        String[] projection = {
                NoteContract.NoteEntry._ID,
                NoteContract.NoteEntry.COLUMN_NOTE_TITLE,
                NoteContract.NoteEntry.COLUMN_NOTE_CONTENT,
                NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE
        };
        String whereClause = null;
        String[] whereArgs = null;

        showProgress();
        return new CursorLoader(getContext(), NoteContract.NoteEntry.CONTENT_URI,
                projection, whereClause, whereArgs, null);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor cursor) {
        hideProgress();
        mNoteCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        mNoteCursorAdapter.swapCursor(null);
    }

    private void showProgress () {
        pbLoadingNotes.setVisibility(View.VISIBLE);
    }

    private void hideProgress () {
        pbLoadingNotes.setVisibility(View.GONE);
    }
}