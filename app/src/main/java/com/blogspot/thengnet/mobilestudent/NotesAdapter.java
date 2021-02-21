package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {

    public NotesAdapter (Context context, ArrayList<Note> theNotes) {
        super(context, 0, theNotes);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notes_list_item,
                    parent, false);

        Note currentNote = getItem(position);

        TextView tvNoteTitle = (TextView) convertView.findViewById(R.id.tv_note_title);
        tvNoteTitle.setText(currentNote.getNoteTitle());

        TextView tvNoteSummary = (TextView) convertView.findViewById(R.id.tv_note_summary);
        tvNoteSummary.setText(currentNote.getNoteBody());

        TextView tvDateUpdated = (TextView) convertView.findViewById(R.id.tv_last_update);
        tvDateUpdated.append(" " + currentNote.getDateUpdated());

        return convertView;
//        return super.getView(position, convertView, parent);
    }
}
