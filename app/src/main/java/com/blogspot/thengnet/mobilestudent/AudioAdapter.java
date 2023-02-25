package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.cursoradapter.widget.CursorAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.thengnet.mobilestudent.data.TimeConverter;

public class AudioAdapter extends CursorAdapter {

    public AudioAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.media_list_item,
                viewGroup, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int lengthIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

        Log.v(AudioAdapter.class.getName(),
                String.format("title: %s; length: %d Path: %s",
                        cursor.getString(titleIndex),
                        cursor.getInt(lengthIndex),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                )
        );

        try {
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_media_title);
            tvTitle.setText(cursor.getString(titleIndex));

            TextView tvLength = (TextView) view.findViewById(R.id.tv_media_length);
            tvLength.setText(TimeConverter.convertTime(cursor.getString(lengthIndex)));
        } catch (NumberFormatException npe) {
            TextView tvLength = (TextView) view.findViewById(R.id.tv_media_length);
            tvLength.setText("0");
        }
    }


}