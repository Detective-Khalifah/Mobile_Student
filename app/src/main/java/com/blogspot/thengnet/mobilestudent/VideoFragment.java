package com.blogspot.thengnet.mobilestudent;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for video files.
 */
public class VideoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int VIDEO_LOADER_ID = 5;
    private static Cursor mVideoCursor;
    private static MediaPlayer mVideoPlayer;
    private final String LOG_TAG = VideoFragment.class.getName();
    Fragment controlsFragment;
    FragmentManager childrenManager;
    FragmentTransaction channel;
    private ArrayList<Audio> mVideoFiles;
    private CursorAdapter videoAdapter;

    private String[] mVideoTableColumns;
    private String mVideoSortOrder = MediaStore.Audio.Media.TITLE + " ASC"; // TODO: present option in preferences
    private Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    public VideoFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoTableColumns = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
                MediaStore.Video.Media.HEIGHT, MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.RESOLUTION
        };

        getLoaderManager().initLoader(VIDEO_LOADER_ID, null, this);

        // instantiate the {@link mVideoPlayer} object
        mVideoPlayer = new MediaPlayer();

        // register callback methods for the {@link mVideoPlayer} object
//        mVideoPlayer.setOnCompletionListener(this);
//        mVideoPlayer.setOnErrorListener(this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View videoRoot = inflater.inflate(R.layout.fragment_video, container, false);

        // create and initialise the ArrayAdapter<Video> object
        // to parse the {@link Video} list item views
        videoAdapter = new AudioAdapter(getContext(), null);

        // Inflate the layout for this fragment
        return videoRoot;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the {@link audioAdapter} ArrayAdapter on the ListView
        ListView lvAudio = (ListView) view.findViewById(R.id.lv_video);
        lvAudio.setAdapter(videoAdapter);

        lvAudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Intent vid = new Intent(getContext(), VideoActivity.class);
                vid.setData(ContentUris.withAppendedId(mVideoUri, id));
                startActivity(vid);
            }
        });
    }

    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {
        return new CursorLoader(getContext(), mVideoUri, mVideoTableColumns, null,
                null, mVideoSortOrder);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor cursor) {
        mVideoCursor = cursor;
        if (mVideoCursor != null && mVideoCursor.getCount() > 0) {
            // release the device resources after the {@link mVideoCursor} object has been utilised
//            mVideoCursor.close();
            videoAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        videoAdapter.swapCursor(null);
    }
}