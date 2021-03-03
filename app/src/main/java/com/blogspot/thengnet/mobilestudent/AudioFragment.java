package com.blogspot.thengnet.mobilestudent;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for audio files.
 */
public class AudioFragment extends Fragment implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private final String LOG_TAG = AudioFragment.class.getName();

    AudioAdapter audioAdapter;
    private static Cursor audioCursor;
    private static MediaPlayer mAudioPlayer;

    Fragment controlsFragment;
    FragmentManager childrenManager;
    FragmentTransaction channel;

    private ArrayList<Audio> mAudioFiles;

    public AudioFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "This is onCreate()");

        // create and initialise the ArrayAdapter<Audio> object
        // to parse the {@link Audio} list item views
        audioAdapter = new AudioAdapter(getContext(), null);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View audioRoot = inflater.inflate(R.layout.fragment_audio, container, false);

        Log.v(LOG_TAG, "This is onCreateView");
        // Inflate the layout for this fragment
        return audioRoot;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(LOG_TAG, "This is onViewCreated()");

        // Set the {@link audioAdapter} ArrayAdapter on the ListView
        ListView lvAudio = (ListView) view.findViewById(R.id.lv_audio);
        lvAudio.setAdapter(audioAdapter);

        lvAudio.setOnItemClickListener(this);

        // instantiate the {@link mAudioPlayer} object
        mAudioPlayer = new MediaPlayer();

        // register callback methods for the {@link mAudioPlayer} object
        mAudioPlayer.setOnCompletionListener(this);
        mAudioPlayer.setOnErrorListener(this);

        getLoaderManager().initLoader(5, null, this);
    }

    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {
        Log.v(LOG_TAG, "This is onCreateLoader");

        Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] mAudioTableColumns = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA
        };
        String mAudioSelection = MediaStore.Audio.Media.IS_MUSIC + " OR " +
                MediaStore.Audio.Media.IS_PODCAST;
        // TODO: present option in preferences
        String mAudioSortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        return new CursorLoader(getActivity().getApplicationContext(), mAudioUri,
                mAudioTableColumns, mAudioSelection, null, mAudioSortOrder);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "This is onLoadFinished");
        if (cursor != null) {
            audioCursor = cursor;
            audioAdapter.swapCursor(audioCursor);
            return;
        }
        /**
         if (audioCursor != null && audioCursor.getCount() > 0) {
         Log.v(LOG_TAG, "audioCursor not empty!");

         // create the {@link ArrayList<Audio>} object
         mAudioFiles = new ArrayList<>();

         //
         for (; audioCursor.moveToNext(); ) {

         // get reference to column indices of interesting columns from the table
         int nTitle = audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
         int nPath = audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
         int nLength = audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

         // instantiate {@link Audio} objects and add them to the {@link ArrayList<Audio>}
         mAudioFiles.add(
         new Audio(
         audioCursor.getString(nTitle),
         audioCursor.getString(nPath),
         audioCursor.getString(nLength)
         )
         );
         }
         audioAdapter.swapCursor(cursor);
         return;

         //             release the device resources after the {@link audioCursor} object has been utilised
         //            audioCursor.close();
         } */

        Log.v(LOG_TAG, "audioCursor empty!");

    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        Log.v(LOG_TAG, "This is onLoaderReset()");
        audioAdapter.swapCursor(null);
    }

    /**
     * Show the {@link MediaControlsFragment} child fragment when an audio file is played
     */
    private void showControlsFragment () {
        controlsFragment = new MediaControlsFragment();
        childrenManager = getChildFragmentManager();
        channel = childrenManager.beginTransaction();
        channel.add(R.id.controls_container, controlsFragment)
                .addToBackStack("control_frag")
                .commit();
    }
    // TODO: find a way - if any, besides LiveData + ViewModel - to handle click events on the
    // child fragment's views.

    /**
     * Use the @param context of the current activity and @param path of the audio file
     * to play it.
     * @return true when mAudioPlayer has been initialised -- left the Idle State prior
     * otherwise false.
     */
    private boolean playAudioFile (Context context, Uri path) {
        if (mAudioPlayer != null) {
            // reset the {@link MediaPlayer} object
            mAudioPlayer.reset();
            try {
                // change data source to a different file at {@link path}
                mAudioPlayer.setDataSource(context, path);

                // transition to prepared state
                mAudioPlayer.prepare();

                // start playing the audio file
                mAudioPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * A callback method to handle {@link Audio} file completions.
     * Takes the {@link MediaPlayer=mAudioPlayer} object as @param mp
     */
    @Override
    public void onCompletion (MediaPlayer mp) {
        childrenManager.popBackStack();
    }

    @Override
    public boolean onError (MediaPlayer mp, int what, int extra) {
        return false;
    }

    /**
     * A callback method that takes the @param parent {@link AdapterView} object,
     * the list item @param view,
     * @param position of the list item view in the list view,
     * the int @param id of the view
     * that has been clicked on.
     */
    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        Audio currentAudio = mAudioFiles.get(position);
        playAudioFile(getContext(), Uri.parse(currentAudio.getAudioPath()));
        showControlsFragment();
    }

}