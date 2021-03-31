package com.blogspot.thengnet.mobilestudent;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView imgThumbnail = (ImageView) view.findViewById(R.id.img_media_thumbnail);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        try {
            // get thumbnail of video files
            Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                    context.getContentResolver(),
                    ContentUris.parseId(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)))),
                    MediaStore.Video.Thumbnails.MINI_KIND, options);

            // set the thumbnail on {@link ImageView} if one exists, otherwise use placeholder.
            if (thumbnail != null) {
                Log.v(AudioAdapter.class.getName(), "thumbnail not null!");
                imgThumbnail.setImageBitmap(thumbnail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method to convert time from source type
     *
     * @param milliseconds fetched time in same unit
     * @return converted time in seconds, minutes and hours.
     */
    private String timeConverter (String milliseconds) {
        long hour, minute, second, millisecond;
        millisecond = Long.parseLong(milliseconds);
        StringBuilder time = new StringBuilder();

        hour = TimeUnit.HOURS.convert(millisecond, TimeUnit.MILLISECONDS);
        minute = TimeUnit.MINUTES.convert(millisecond, TimeUnit.MILLISECONDS);
        second = TimeUnit.SECONDS.convert(millisecond, TimeUnit.MILLISECONDS);

        if (hour > 0)
            time.append(hour).append(":");

        if (minute > 9) {
            if (minute > 59) {
                long remainingMinutes = minute - ((minute / 60) * 60);
                if (remainingMinutes > 9)
                    time.append(remainingMinutes).append(":");
                else
                    time.append("0").append(remainingMinutes).append(":");
            } else
                time.append(minute).append(":");
        } else
            time.append("0").append(minute).append(":");

        if (second > 9) {
            if (second > 59) {
                long remainingSeconds = second - (second / 60) * 60;
                if (remainingSeconds > 9)
                    time.append(remainingSeconds);
                else
                    time.append("0").append(remainingSeconds);
            } else
                time.append(second);
        } else {
            time.append("0").append(second);
        }

        return String.valueOf(time);
    }
}