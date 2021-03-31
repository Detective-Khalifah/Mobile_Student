package com.blogspot.thengnet.mobilestudent;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
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
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int AUDIO_LOADER_ID = 3;
    private static final String LOG_TAG = AudioFragment.class.getName();

    private static final Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static Uri mCurrentAudioUri;

    private static Context mAppContext;
    private static Cursor mAudioCursor;
    private static MediaPlayer mAudioPlayer;
    private AudioAdapter audioAdapter;
    private MediaControlsFragment controlsFragment;
    private FragmentManager childrenManager;
    private FragmentTransaction channel;

    private static AudioManager audioPlayManager;
    private AudioManager.OnAudioFocusChangeListener audioFocus;

    public AudioFragment () {
        // Required empty public constructor
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);

        // context of the app
        mAppContext = context;
    }

    @Override
    public void onResume () {
        super.onResume();
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAudioPlayer != null) {
            outState.putString("previous-playback-audio", String.valueOf(mCurrentAudioUri));
        }
    }

    /**
     * Call #stopPlayback if screen is covered by components like another Activity.
     */
    // TODO: Find a way to keep playback data and continue playback when blocker is away!
    @Override
    public void onPause () {
        super.onPause();
        stopPlayback();
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioPlayManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);

        // create and initialise the ArrayAdapter<Audio> object
        // to parse the {@link Audio} list item views
        audioAdapter = new AudioAdapter(getContext(), null);

        initialisePlayer();

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

        // definition of #OnAudioFocusChangeListener for the audio player
        audioFocus = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange (int focusChange) {
                switch (focusChange) {

                    // Focus gain, with expectation previous holder stops playback
                    // AUDIOFOCUS_REQUEST_GRANTED OR AUDIOFOCUS_REQUEST_DELAYED
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // Focus gain, with expectation of releasing focus momentarily
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        // Set the volume to "normal level" and start playback
                        Log.v(LOG_TAG, "ContentUris method: " + mCurrentAudioUri);
                        if (mAudioPlayer == null)
                            initialisePlayer();
                        else
                            mAudioPlayer.setVolume(1f, 1f);
                        playAudioFile();
                        break;

                    // Focus gain, with expectation of releasing focus momentarily, allowing others
                    // to 'duck'
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        // Pause playback -- Lowering volume at this stage could be detrimental to
                        // information inculcation; I'm working with podcasts eventually.
                        Log.v(LOG_TAG, "ContentUris method: " + mCurrentAudioUri);
                        if (isPlaying())
                            pausePlayback();
                        break;

                    // Focus loss, with expectation of re-gaining momentarily and ducking respectively
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Pause playback.
                        if (isPlaying())
                            pausePlayback();
                        break;

                    // Focus loss, without expectation of getting soon or ever
                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Stop playback and abandon audio focus
                        // Focus gain, with expectation of releasing focus momentarily, disabling all other playback (system & app)
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                        // do not play -- abandon audio focus
                        stopPlayback();
                        break;
                    default:
                }
            }
        };

        getLoaderManager().restartLoader(AUDIO_LOADER_ID, null, this);
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
        // TODO: Implement #requestAudioFocus(AudioFocusRequest) for SDK >= Android OREO later
        //  "Note that the return value is never {@link #AUDIOFOCUS_REQUEST_DELAYED} when focus
        //     *     is requested without building the {@link AudioFocusRequest} with
        //     *     {@link AudioFocusRequest.Builder#setAcceptsDelayedFocusGain(boolean)} set to
        //     *     {@code true}." -- Documentation
        int focus = audioPlayManager.requestAudioFocus(audioFocus, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        switch (focus) {
            // start playback if audio focus is granted
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                mCurrentAudioUri = ContentUris.withAppendedId(mAudioUri, id);
                initialisePlayer();
                playAudioFile();
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                stopPlayback();
                break;
        }


        // get a new instance of {@link MediaControlsFragment}, setting title and duration of the
        //  audio item to the object
        controlsFragment = MediaControlsFragment.newInstance(mAudioCursor.getString(
                mAudioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                Long.parseLong(mAudioCursor.getString(
                        mAudioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));

        // show the {@link MediaControlsFragment} Fragment
        showControlsFragment();
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
            mAudioCursor = cursor;
            audioAdapter.swapCursor(mAudioCursor);
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
    // TODO: Implement LiveData + ViewModel - to handle click events on the
    //  child fragment's views.
    //  Also use it to handle playback across Fragments.

    /**
     * A method to initialise the #mAudioPlayer object if it is not already instantiated
     */
    protected void initialisePlayer () {
        if (mAudioPlayer != null) {
            // reset the {@link MediaPlayer} object
            mAudioPlayer.reset();
            try {
                // change data source to a different file at {@link path}
                mAudioPlayer.setDataSource(mAppContext, mCurrentAudioUri);

                // transition to prepared state
                mAudioPlayer.prepare();
            } catch (IOException ioE) {
                ioE.printStackTrace();
            }
            return;
        }

        // instantiate the {@link mAudioPlayer} object
        mAudioPlayer = new MediaPlayer();

        // register callback methods for the {@link mAudioPlayer} object

        mAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            /**
             * A callback method to handle {@link Audio} file completions.
             * Takes the {@link MediaPlayer=mAudioPlayer} object as @param mp
             */
            @Override
            public void onCompletion (MediaPlayer mp) {
                // TODO: check if Fragment is visible to user first, then make this call to hide the
                //  controls fragment or postpone it till onResumed()

                // release resources, hide {@link MediaControlsFragment} if this {@link Fragment}
                // is in the #onResumed state.
                stopPlayback();
            }
        });

        mAudioPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError (MediaPlayer mp, int what, int extra) {
                if (mp != null) {
                    stopPlayback();

                    // Successfully handled error
                    return true;
                }

                // Unsuccessfully handled error
                mp.reset();
                return false;
            }
        });
    }

    /**
     * Start playback of the #mAudioPlayer object
     *
     * @return true when mAudioPlayer has been initialised -- left the Idle State prior
     * otherwise false.
     */
    protected void playAudioFile () {
        if (mAudioPlayer != null)
            // start playing the audio file
            mAudioPlayer.start();
    }

    /**
     * Helper method to check playback status
     *
     * @return #mAudioPlayer state
     */
    protected boolean isPlaying () {
        if (mAudioPlayer != null) {
            if (mAudioPlayer.isPlaying())
                return true;
        }
        return false;
    }

    // TODO: Use ternary operator for short, non-complex lines.

    /**
     * A method to pause playback of the #mAudioPlayer object.
     */
    protected void pausePlayback () {
        if (mAudioPlayer != null) {
            mAudioPlayer.pause();
        }
    }

    /**
     * Stop playback and release resources hoarded by #mAudioPlayer.
     */
    protected void stopPlayback () {
        if (mAudioPlayer != null) {
            mAudioPlayer.release();
            mAudioPlayer = null;
            audioPlayManager.abandonAudioFocus(audioFocus);
        }
        if (this.isVisible())
            getChildFragmentManager().beginTransaction().hide(controlsFragment).commit();
    }

    protected void fastForward () {
        if (isPlaying()) {
            mAudioPlayer.seekTo(mAudioPlayer.getCurrentPosition() + 10000);
        }
    }

    protected void rewind () {
        if (isPlaying()) {
            mAudioPlayer.seekTo(mAudioPlayer.getCurrentPosition() - 10000);
        }
    }
}