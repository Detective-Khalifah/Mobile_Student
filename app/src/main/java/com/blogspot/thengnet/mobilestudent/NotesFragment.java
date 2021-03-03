package com.blogspot.thengnet.mobilestudent;

import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NOTES_ACTIVITY_LOADER = 105;
    ProgressBar pbLoadingNotes;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton fabNewNote;
    private NotesCursorAdapter mNoteCursorAdapter;

    public NotesFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance (String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getLoaderManager().initLoader(NOTES_ACTIVITY_LOADER, null, this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        try {
            // Inflate the layout for this fragment
            View notesView = inflater.inflate(R.layout.fragment_notes, container, false);

            pbLoadingNotes = (ProgressBar) notesView.findViewById(R.id.pb_loading_notes);

            fabNewNote = (FloatingActionButton) notesView.findViewById(R.id.fab_new_note);
            fabNewNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    //                startActivity(new Intent(MainActivity.this, NotesEditor.class));
                }
            });

            ListView notesList = (ListView) notesView.findViewById(R.id.notes_list);

            mNoteCursorAdapter = new NotesCursorAdapter(getContext(), null);
            notesList.setAdapter(mNoteCursorAdapter);

            notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    showProgress();
                    //                startActivity(new Intent(NotesFragment.this, NotesViewer.class)
                    //                        .setData(ContentUris.withAppendedId(
                    //                                NoteContract.NoteEntry.CONTENT_URI, id))); // content uri of the clicked #NoteEntry
                }
            });

            return notesView;
        } catch (InflateException e) {
            e.printStackTrace();
            Log.v(NotesFragment.class.getName(), "NotesFragment couldn't be inflated: " + e.getMessage());
        }
        return null;
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

//        showProgress();
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