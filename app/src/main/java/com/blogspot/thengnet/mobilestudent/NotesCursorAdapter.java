package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blogspot.thengnet.mobilestudent.data.NoteContract;

public class NotesCursorAdapter extends CursorAdapter {
    
    public NotesCursorAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater.from(context).inflate(R.layout.notes_list_item, parent, false);
        return null;
    }

    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        TextView tvNoteTitle = (TextView) view.findViewById(R.id.tv_note_title);
        TextView tvNoteContent = (TextView) view.findViewById(R.id.tv_note_summary);
        TextView tvNoteLastUpdate = (TextView) view.findViewById(R.id.tv_note_last_update);

        int noteTitleIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
        int noteContentIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT);
        int noteLastUpdate = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_PREVIOUS_UPDATE);

        String title = cursor.getString(noteTitleIndex);
        String content = cursor.getString(noteContentIndex);
        String lastUpdate = cursor.getString(noteLastUpdate);

        tvNoteTitle.setText(title);
        tvNoteContent.setText(content);
        tvNoteLastUpdate.setText(lastUpdate);
    }
}
