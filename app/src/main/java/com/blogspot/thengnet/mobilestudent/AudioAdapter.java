package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AudioAdapter extends ArrayAdapter<Audio> {

    public AudioAdapter (Context context, ArrayList<Audio> resource) {
        super(context, 0, resource);
    }

    public View getView (int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.media_list_item,
                    parent, false);
        }

        Audio currentAudio = getItem(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_media_title);
        tvTitle.setText(currentAudio.getAudioTitle());

        TextView tvLength = (TextView) convertView.findViewById(R.id.tv_media_length);
        tvLength.setText(currentAudio.getAudioLength());

        return convertView;
    }
}
