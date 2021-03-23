package com.blogspot.thengnet.mobilestudent;

import android.content.ContentUris;
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

/**
 * A simple {@link Fragment} subclass for audio files.
 */
public class AudioFragment extends Fragment implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private static final int AUDIO_LOADER_ID = 3;

    private static Cursor audioCursor;
    private static MediaPlayer mAudioPlayer;
    private final String LOG_TAG = AudioFragment.class.getName();
    AudioAdapter audioAdapter;
    MediaControlsFragment controlsFragment;
    FragmentManager childrenManager;
    FragmentTransaction channel;
    private Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    public AudioFragment () {
        // Required empty public constructor
    }

    /**
     * A callback method that takes the @param parent {@link AdapterView} object,
     * the list item @param view,
     *
     * @param position of the list item view in the list view,
     *                 the int @param id of the view
     *                 that has been clicked on.
     */
    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        Log.v(LOG_TAG, "ContentUris method: " + ContentUris.withAppendedId(mAudioUri, id));
        playAudioFile(getContext(), Uri.parse(
                String.valueOf(ContentUris.withAppendedId(mAudioUri, id))
        ));

        // show the {@link MediaControlsFragment} Fragment
        showControlsFragment();

        // set title of #audioListener to audio item at current position
        if (controlsFragment == null)
            Log.v(LOG_TAG, "controlsFragment null!");
        if (controlsFragment.isResumed())
            controlsFragment.setAudioMetrics(audioCursor.getString(audioCursor.getColumnIndex(
                    MediaStore.Audio.Media.TITLE)), 0);
        else
            Log.v(LOG_TAG, "controlsFragment not resumed");
    }

    /**
     * A callback method to handle {@link Audio} file completions.
     * Takes the {@link MediaPlayer=mAudioPlayer} object as @param mp
     */
    @Override
    public void onCompletion (MediaPlayer mp) {
        // TODO: check if Fragment is visible to user first, then make this call to hide the controls
        //  fragment or postpone it till onResumed()
        getChildFragmentManager().beginTransaction().hide(controlsFragment).commit();
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create and initialise the ArrayAdapter<Audio> object
        // to parse the {@link Audio} list item views
        audioAdapter = new AudioAdapter(getContext(), null);

        // instantiate the {@link mAudioPlayer} object
        mAudioPlayer = new MediaPlayer();

        // register callback methods for the {@link mAudioPlayer} object
        mAudioPlayer.setOnCompletionListener(this);
        mAudioPlayer.setOnErrorListener(this);

        // instantiate #this Fragment
        controlsFragment = new MediaControlsFragment();

        getLoaderManager().initLoader(AUDIO_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View audioRoot = inflater.inflate(R.layout.fragment_audio, container, false);

        // Inflate the layout for this fragment
        return audioRoot;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the {@link audioAdapter} ArrayAdapter on the ListView
        ListView lvAudio = (ListView) view.findViewById(R.id.lv_audio);
        lvAudio.setAdapter(audioAdapter);

        lvAudio.setOnItemClickListener(this);

        getLoaderManager().restartLoader(AUDIO_LOADER_ID, null, this);
    }

    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {

        String[] mAudioTableColumns = new String[]{MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
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
        if (cursor != null && cursor.getCount() > 0) {
            Log.v(LOG_TAG, "audioCursor not empty!");
            audioCursor = cursor;
            audioAdapter.swapCursor(audioCursor);
            return;
        }

        Log.v(LOG_TAG, "audioCursor empty!");

    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        audioAdapter.swapCursor(null);
    }

    /**
     * Show the {@link MediaControlsFragment} child fragment when an audio file is played
     */
    private void showControlsFragment () {
        childrenManager = getChildFragmentManager();
        channel = childrenManager.beginTransaction();
        if (controlsFragment.isAdded())
            channel.show(controlsFragment);
        else
            channel.add(R.id.controls_container, controlsFragment);

        channel.commit();
    }
    // TODO: find a way - if any, besides LiveData + ViewModel - to handle click events on the
    //  child fragment's views.

    /**
     * Use the @param context of the current activity and @param path of the audio file
     * to play it.
     *
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

    @Override
    public boolean onError (MediaPlayer mp, int what, int extra) {
        return false;
    }

}