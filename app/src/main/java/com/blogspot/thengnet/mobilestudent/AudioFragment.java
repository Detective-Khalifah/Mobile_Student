package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for audio files.
 */
public class AudioFragment extends Fragment {

    private static Cursor audioCursor;
    private static MediaPlayer mAudioPlayer;
    private String LOG_TAG = AudioFragment.class.getName();
    private ArrayList<Audio> mAudioFiles;
    private String[] mAudioTableColumns;
    private String mAudioSelection = MediaStore.Audio.Media.IS_MUSIC + " OR " +
            MediaStore.Audio.Media.IS_PODCAST;
    private String mAudioSortOrder = MediaStore.Audio.Media.TITLE + " ASC"; // TODO: present option in preferences
    private Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

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

        mAudioTableColumns = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA
        };

        audioCursor = getContext().getContentResolver().query(mAudioUri,
                mAudioTableColumns, mAudioSelection, null, mAudioSortOrder);

        if (audioCursor != null) {
            mAudioFiles = new ArrayList<>();
            audioCursor.moveToFirst();

            do {
                int nTitle = audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int nPath = audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int nLength = audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                mAudioFiles.add(
                        new Audio(
                                audioCursor.getString(nTitle),
                                audioCursor.getString(nPath),
                                audioCursor.getString(nLength)
                        )
                );
            } while (audioCursor.moveToNext());
            audioCursor.close();
        }

        ArrayAdapter<Audio> audioAdapter = new AudioAdapter(getContext(), mAudioFiles);

        ListView lvAudio = (ListView) audioRoot.findViewById(R.id.lv_audio);
        lvAudio.setAdapter(audioAdapter);

        lvAudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Audio currentAudio = mAudioFiles.get(position);
                playAudioFile(getContext(), Uri.parse(currentAudio.getAudioPath()));
            }
        });

        // Inflate the layout for this fragment
        return audioRoot;
    }

    private boolean playAudioFile (Context context, Uri path) {
        if (mAudioPlayer != null) {
            Log.v(LOG_TAG, "mAudioPlayer!=null");
            mAudioPlayer.reset();
            try {
                mAudioPlayer.setDataSource(context, path);
                Log.v(LOG_TAG, "setDatasource");
                mAudioPlayer.prepare();
                Log.v(LOG_TAG, "prepare");
                mAudioPlayer.start();
                Toast.makeText(getContext(), "Started: " + path.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        Log.v(LOG_TAG, "mediaPlayer = null");
        mAudioPlayer = MediaPlayer.create(context, path);
        Log.v(LOG_TAG, "create");
        mAudioPlayer.start();
        Toast.makeText(getContext(), "Started: " + path.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }
}