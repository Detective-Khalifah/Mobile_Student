package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
        tvLength.setText(timeConverter(cursor.getString(lengthIndex)));
    }

    private String timeConverter (String milliseconds) {
        SimpleDateFormat sdFormatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        long hour, minute, second, millisecond;
        millisecond = Long.parseLong(milliseconds);
        StringBuilder time = new StringBuilder();

        hour = TimeUnit.HOURS.convert(millisecond, TimeUnit.MILLISECONDS);
        minute = TimeUnit.MINUTES.convert(millisecond, TimeUnit.MILLISECONDS);
        second = TimeUnit.SECONDS.convert(millisecond, TimeUnit.MILLISECONDS);

        if (hour > 0)
            time.append(hour).append(":");
        if (minute > 0)
            time.append(minute).append(":");
        if (second > 0)
            time.append(second);

        return sdFormatter.format(new java.sql.Date(Long.parseLong(milliseconds))) + " -- " + time;
    }
}