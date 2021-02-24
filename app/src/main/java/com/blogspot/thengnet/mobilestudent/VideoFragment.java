package com.blogspot.thengnet.mobilestudent;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for video files.
 */
public class VideoFragment extends Fragment {

    private final String LOG_TAG = VideoFragment.class.getName();
    private ArrayList<Audio> mVideoFiles;

    private static Cursor mVideoCursor;
    private static MediaPlayer mVideoPlayer;
    Fragment controlsFragment;
    FragmentManager childrenManager;
    FragmentTransaction channel;

    private String[] mVideoTableColumns;
    private String mVideoSortOrder = MediaStore.Audio.Media.TITLE + " ASC"; // TODO: present option in preferences
    private Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    public VideoFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View videoRoot = inflater.inflate(R.layout.fragment_video, container, false);

        mVideoTableColumns = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
                MediaStore.Video.Media.HEIGHT, MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.RESOLUTION
        };

        mVideoCursor = getContext().getContentResolver().query(mVideoUri,
                mVideoTableColumns, null, null, mVideoSortOrder);

        if (mVideoCursor != null && mVideoCursor.getCount() > 0) {

            // create the {@link ArrayList<Video>} object
            mVideoFiles = new ArrayList<>();

            //
            for (; mVideoCursor.moveToNext(); ) {

                // get reference to column indices of interesting columns from the table
                int nTitle = mVideoCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int nPath = mVideoCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int nLength = mVideoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                // instantiate {@link Video} objects and add them to the {@link ArrayList<Video>}
                mVideoFiles.add(
                        new Audio(
                                mVideoCursor.getString(nTitle),
                                mVideoCursor.getString(nPath),
                                mVideoCursor.getString(nLength)
                        )
                );
            }
            // release the device resources after the {@link mVideoCursor} object has been utilised
            mVideoCursor.close();
        }

        // create and initialise the ArrayAdapter<Video> object
        // to parse the {@link Video} list item views
        ArrayAdapter<Audio> videoAdapter = new AudioAdapter(getContext(), mVideoFiles);

        // Set the {@link audioAdapter} ArrayAdapter on the ListView
        ListView lvAudio = (ListView) videoRoot.findViewById(R.id.lv_video);
        lvAudio.setAdapter(videoAdapter);

        lvAudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

            }
        });

        // instantiate the {@link mVideoPlayer} object
        mVideoPlayer = new MediaPlayer();

        // register callback methods for the {@link mVideoPlayer} object
//        mVideoPlayer.setOnCompletionListener(this);
//        mVideoPlayer.setOnErrorListener(this);

        // Inflate the layout for this fragment
        return videoRoot;
    }
}