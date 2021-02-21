package com.blogspot.thengnet.mobilestudent;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for audio files.
 */
public class AudioFragment extends Fragment {

    private ArrayList<Audio> mAudioFiles;
    private String[] mProjection;

    public AudioFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View audioRoot = inflater.inflate(R.layout.fragment_audio, container, false);

        mProjection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DISPLAY_NAME
                /*MediaStore.Audio.Media.RELATIVE_PATH, MediaStore.Audio.Media.OWNER_PACKAGE_NAME*/};

        Cursor audioCursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mProjection, null, null, null);

        if (audioCursor != null) {
            mAudioFiles = new ArrayList<>();
            audioCursor.moveToFirst();

            do {
                int nTitle = audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int nDisp = audioCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int nLength = audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
//                int nDir = audioCursor.getColumnIndex(MediaStore.Audio.Media.RELATIVE_PATH);,
////                            audioCursor.getString(nDir)

                mAudioFiles.add(
                    new Audio(
                            audioCursor.getString(nTitle),
                            audioCursor.getString(nDisp),
                            audioCursor.getString(nLength)
                    )
                );
            } while (audioCursor.moveToNext());
            audioCursor.close();
        }

        ArrayAdapter<Audio> audioAdapter = new AudioAdapter(getContext(), mAudioFiles);

        ListView lvAudio = (ListView) audioRoot.findViewById(R.id.lv_audio);
        lvAudio.setAdapter(audioAdapter);

        // Inflate the layout for this fragment
        return audioRoot;
    }

}