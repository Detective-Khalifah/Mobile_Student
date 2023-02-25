package com.blogspot.thengnet.mobilestudent

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.blogspot.thengnet.mobilestudent.databinding.FragmentAudioBinding
import java.io.IOException

/**
 * A simple [Fragment] subclass for playing audio files.
 */
class AudioFragment : Fragment(), OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor?> {

    private var _binding: FragmentAudioBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var audioAdapter: AudioAdapter? = null
    private var controlsFragment: MediaControlsFragment? = null
    private var childrenManager: FragmentManager? = null
    private var channel: FragmentTransaction? = null
    private var audioFocus: OnAudioFocusChangeListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // context of the app
        mAppContext = context
    }

    override fun onResume() {
        super.onResume()
        requireActivity().volumeControlStream = AudioManager.STREAM_MUSIC
        if (mAudioPlayer != null && mCurrentAudioUri != null && controlsFragment != null) {
            Log.v(LOG_TAG, "onResume found not null")
            showControlsFragment()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mCurrentAudioUri != null) {
            outState.putString("PREVIOUS-PLAYBACK-FILE", mCurrentAudioUri.toString())
            outState.putInt("PREVIOUS-POSITION", mAudioPlayer!!.currentPosition)
        }
    }

    // TODO: Find a way to keep playback data and continue playback when blocker is away!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioPlayManager = mAppContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // create and initialise the ArrayAdapter<Audio> object
        // to parse the {@link Audio} list item views
        audioAdapter = AudioAdapter(context, null)
        if (savedInstanceState != null) {
            mCurrentAudioUri = Uri.parse(
                savedInstanceState.getString(
                    "PREVIOUS-PLAYBACK-FILE", mCurrentAudioUri.toString()
                )
            )
            mPosition = savedInstanceState.getInt("PREVIOUS-POSITION")

            // If an audio file wasn't playing, reset {@link MediaPlayer} object, nullify and
            // initialise, otherwise continue playing.
            if (mCurrentAudioUri != null && mPosition > 0) {
                Log.v(LOG_TAG, "mCurrentAudioUri not null in @onCreate")
                resumePlayback()
            } else {
                mAudioPlayer!!.reset()
                mAudioPlayer = null
                initialisePlayer()
            }
        } else Log.v(LOG_TAG, "wasn't playing")
        loaderManager.initLoader(AUDIO_LOADER_ID, null, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the {@link audioAdapter} ArrayAdapter on the ListView
        val lvAudio = view.findViewById<View>(R.id.lv_audio) as ListView
        lvAudio.adapter = audioAdapter
        lvAudio.onItemClickListener = this

        // definition of #OnAudioFocusChangeListener for the audio player
        audioFocus = object : OnAudioFocusChangeListener {
            override fun onAudioFocusChange(focusChange: Int) {
                when (focusChange) {
                    AudioManager.AUDIOFOCUS_GAIN, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> {
                        // Set the volume to "normal level" and start playback
                        Log.v(LOG_TAG, "ContentUris method: " + mCurrentAudioUri)
                        if (mAudioPlayer == null) initialisePlayer() else mAudioPlayer!!.setVolume(
                            1f,
                            1f
                        )
                        playAudioFile()
                    }
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> {
                        // Pause playback -- Lowering volume at this stage could be detrimental to
                        // information inculcation; I'm working with podcasts eventually.
                        Log.v(LOG_TAG, "ContentUris method: " + mCurrentAudioUri)
                        if (isPlaying) pausePlayback()
                    }
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->                         // Pause playback.
                        if (isPlaying) pausePlayback()
                    AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE -> {
                        // do not play -- abandon audio focus
                        Log.v(LOG_TAG, "FOCUS LOSS")
                        stopPlayback()
                    }
                    else -> {}
                }
            }
        }
        loaderManager.restartLoader(AUDIO_LOADER_ID, null, this)
    }

    /**
     * A callback method that takes the @param parent [AdapterView] object,
     * the list item @param view,
     *
     * @param position of the list item view in the list view,
     * the int @param id of the view
     * that has been clicked on.
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        // TODO: Implement #requestAudioFocus(AudioFocusRequest) for SDK >= Android OREO later
        //  "Note that the return value is never {@link #AUDIOFOCUS_REQUEST_DELAYED} when focus
        //     *     is requested without building the {@link AudioFocusRequest} with
        //     *     {@link AudioFocusRequest.Builder#setAcceptsDelayedFocusGain(boolean)} set to
        //     *     {@code true}." -- Documentation
        val focus = audioPlayManager!!.requestAudioFocus(
            audioFocus, AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        when (focus) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                mCurrentAudioUri = ContentUris.withAppendedId(mAudioUri, id)
                initialisePlayer()
                playAudioFile()
            }
            AudioManager.AUDIOFOCUS_REQUEST_FAILED -> stopPlayback()
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor?> {
        val mAudioTableColumns = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        val mAudioSelection = MediaStore.Audio.Media.IS_MUSIC + " OR " +
                MediaStore.Audio.Media.IS_PODCAST
        // TODO: present option in preferences
        val mAudioSortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        return CursorLoader(
            requireActivity().applicationContext, mAudioUri,
            mAudioTableColumns, mAudioSelection, null, mAudioSortOrder
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor?>, cursor: Cursor?) {
        if (cursor != null && cursor.count > 0) {
            mAudioCursor = cursor
            audioAdapter!!.swapCursor(mAudioCursor)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        audioAdapter!!.swapCursor(null)
    }

    /**
     * Show the [MediaControlsFragment] child fragment when an audio file is played
     */
    private fun showControlsFragment() {
        if (this.isResumed) {
            childrenManager = childFragmentManager
            channel = childrenManager!!.beginTransaction()
            if (controlsFragment!!.isAdded) channel!!.show(controlsFragment!!) else channel!!.add(
                R.id.controls_container,
                controlsFragment!!
            )
            channel!!.commit()
        }
    }

    /**
     * Utility method to remove the [MediaControlsFragment] child fragment.
     */
    private fun hideControlsFragment() {
        channel = childrenManager!!.beginTransaction()
        channel!!.remove(controlsFragment!!)
        controlsFragment = null
        if (this.isVisible) channel!!.commit()
    }
    // TODO: Implement LiveData + ViewModel - to handle click events on the
    //  child fragment's views.
    //  Also use it to handle playback across Fragments.
    /**
     * A method to initialise the #mAudioPlayer object if it is not already instantiated
     */
    protected fun initialisePlayer() {
        if (mAudioPlayer != null) {
            Log.v(LOG_TAG, "init not null")
            // reset the {@link MediaPlayer} object
            mAudioPlayer!!.reset()
            try {
                // change data source to a different file at {@link path}
                mAudioPlayer!!.setDataSource(mAppContext!!, mCurrentAudioUri!!)

                // transition to prepared state
                mAudioPlayer!!.prepare()
            } catch (ioE: IOException) {
                ioE.printStackTrace()
            }
            return
        }
        Log.v(LOG_TAG, "init null")
        // instantiate the {@link mAudioPlayer} object
        mAudioPlayer = MediaPlayer()

        // register callback methods for the {@link mAudioPlayer} object
        mAudioPlayer!!.setOnCompletionListener { // TODO: check if Fragment is visible to user first, then make this call to hide the
            //  controls fragment or postpone it till onResumed()

            // release resources, hide {@link MediaControlsFragment} if this {@link Fragment}
            // is in the #onResumed state.
            Log.v(LOG_TAG, "onCompletion.")
            stopPlayback()
        }
        mAudioPlayer!!.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
            when (what) {
                MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.v(LOG_TAG, "Media Error Unknown")
                MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.v(LOG_TAG, "Media Error Server Died")
                else -> Log.v(LOG_TAG, "Media Error what: $what")
            }
            when (extra) {
                MediaPlayer.MEDIA_ERROR_IO -> Log.v(LOG_TAG, "Media Error IO")
                MediaPlayer.MEDIA_ERROR_MALFORMED -> Log.v(LOG_TAG, "Media Error Malformed")
                MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> Log.v(LOG_TAG, "Media Error Unsupported")
                MediaPlayer.MEDIA_ERROR_TIMED_OUT -> Log.v(LOG_TAG, "Media Error Timed Out")
                else -> Log.v(LOG_TAG, "Media Error extra: $extra")
            }
            if (mp != null) {
                Log.v(LOG_TAG, "onError")
                mp.reset()
                hideControlsFragment()

                // Successfully handled error
                return@OnErrorListener true
            }

            // Unsuccessfully handled error
            mp?.reset()
            Log.v(LOG_TAG, "Error handled unsuccessfully.")
            false
        })
    }

    /**
     * Utility method to start playback of the #mAudioPlayer object, if one exits (non-null)
     * If #controlsFragment is not null, call #hideControlsFragment to remove it, then create a new
     * instance and start playing.
     */
    fun playAudioFile() {
        if (mAudioPlayer != null) {
            Log.v(LOG_TAG, "mAudioPlayer not null in #playAudioFile.")
            if (controlsFragment != null) {
                if (mCurrentAudioUri != null) {
                    showControlsFragment()
                } else hideControlsFragment()
            } else {
                // get a new instance of {@link MediaControlsFragment}, setting title and duration of the
                //  audio item to the object
                controlsFragment = MediaControlsFragment.newInstance(
                    mAudioCursor!!.getString(
                        mAudioCursor!!.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    ), mAudioCursor!!.getString(
                        mAudioCursor!!.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    ).toLong()
                )

                // show the {@link MediaControlsFragment} Fragment
                showControlsFragment()
            }
            // start playing the audio file
            mAudioPlayer!!.start()
        }
    }

    /**
     * Helper method to check playback status
     *
     * @return #mAudioPlayer state
     */
    val isPlaying: Boolean
        get() {
            if (mAudioPlayer != null) {
                if (mAudioPlayer!!.isPlaying) return true
            }
            return false
        }
    // TODO: Use ternary operator for short, non-complex lines.
    /**
     * A method to pause playback of the #mAudioPlayer object.
     */
    fun pausePlayback() {
        if (mAudioPlayer != null) {
            mPosition = mAudioPlayer!!.currentPosition
            mAudioPlayer!!.pause()
        }
    }

    /**
     * A method to resume playback of the #mAudioPlayer object.
     */
    private fun resumePlayback() {
        if (mAudioPlayer != null && mPosition > 0) {
            mAudioPlayer!!.seekTo(mPosition)
            mAudioPlayer!!.start()
        } else {
//            initialisePlayer();
//            resumePlayback();
        }
    }

    /**
     * Stop playback and release resources hoarded by #mAudioPlayer.
     */
    fun stopPlayback() {
        if (mAudioPlayer != null) {
            Log.v(LOG_TAG, "Not NULL!")
            mAudioPlayer!!.reset()
            mAudioPlayer!!.release()
            mAudioPlayer = null
            audioPlayManager!!.abandonAudioFocus(audioFocus)
        }
        // TODO: Consider calling #hideControlsFragment here.
        if (this.isVisible && this.isResumed && controlsFragment != null) childFragmentManager.beginTransaction()
            .hide(
                controlsFragment!!
            ).commit()
    }

    fun fastForward() {
        if (isPlaying) {
            mAudioPlayer!!.seekTo(mAudioPlayer!!.currentPosition + 10000)
        }
    }

    fun rewind() {
        if (isPlaying) {
            mAudioPlayer!!.seekTo(mAudioPlayer!!.currentPosition - 10000)
        }
    }

    fun previousTrack() {
        if (mAudioPlayer != null) {
            try {
                // Move to previous audio file in generated cursor/table, if there's one before
                // current file/track.
                val previousExists = mAudioCursor!!.moveToPrevious()
                if (previousExists) {
                    // Set #mCurrentAudioUri to the current row of #mAudioCursor
                    mCurrentAudioUri = ContentUris.withAppendedId(
                        mAudioUri,
                        ContentUris.parseId(
                            Uri.parse(
                                mAudioCursor!!.getString(
                                    mAudioCursor!!.getColumnIndex(MediaStore.Audio.Media._ID)
                                )
                            )
                        )
                    )
                } else  // Otherwise return to track originally being played.
                    mAudioCursor!!.moveToNext()
            } catch (cursorException: CursorIndexOutOfBoundsException) {
                // TODO: Display a Toast/Snackbar if there's such error.
            }

            // Re-initialise the {@link MediaPlayer} object #mAudioPlayer, and start playing previous
            // audio file/track.
            initialisePlayer()
            playAudioFile()
        }
    }

    fun nextTrack() {
        if (mAudioPlayer != null) {
            try {
                // Move to next audio file in generated cursor/table, if there's one after current
                // file/track.
                val nextExists = mAudioCursor!!.moveToNext()
                if (nextExists) {
                    // Set #mCurrentAudioUri to the current row of #mAudioCursor
                    mCurrentAudioUri = ContentUris.withAppendedId(
                        mAudioUri,
                        ContentUris.parseId(
                            Uri.parse(
                                mAudioCursor!!.getString(
                                    mAudioCursor!!.getColumnIndex(MediaStore.Audio.Media._ID)
                                )
                            )
                        )
                    )
                } else  // Otherwise return to track originally being played.
                    mAudioCursor!!.moveToPrevious()
            } catch (cursorException: CursorIndexOutOfBoundsException) {
                // TODO: Display a Toast/Snackbar if there's such error.
            }

            // Re-initialise the {@link MediaPlayer} object #mAudioPlayer, and start playing next
            // audio file/track.
            initialisePlayer()
            playAudioFile()
        }
    }

    companion object {
        private const val AUDIO_LOADER_ID = 3
        private val LOG_TAG = AudioFragment::class.java.name
        private val mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        private var mCurrentAudioUri: Uri? = null
        private var mAppContext: Context? = null
        private var mAudioCursor: Cursor? = null
        private var mAudioPlayer: MediaPlayer? = null
        private var mPosition = 0
        private var audioPlayManager: AudioManager? = null
    }
}