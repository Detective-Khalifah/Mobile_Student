package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AudioAdapter extends CursorAdapter {

    public AudioAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.media_list_item,
                viewGroup, false);
    }

    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int lengthIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_media_title);
        tvTitle.setText(cursor.getString(titleIndex));

        TextView tvLength = (TextView) view.findViewById(R.id.tv_media_length);
        tvLength.setText(cursor.getString(lengthIndex));
    }
}
