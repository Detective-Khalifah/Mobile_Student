package com.blogspot.thengnet.mobilestudent

import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.thengnet.mobilestudent.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    private var mVideoPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get path to video file from #Intent that launched this #Activity
        val videoToPlay = intent
        path = videoToPlay.data
        val mediaController = MediaController(this)
        //        mediaController.setMediaPlayer(mVideoView);
        binding.videoview.setMediaController(mediaController)
        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }
        startPlayback()

        // TODO: Implement {@link AudioFocusRequest, AudioAttributes}, whatever and learn Services
        //  as soon as possible; OnAudioFocusChangeListener & AudioManager worked poorly, unlike it
        //  did with {@link AudioFragment}.
    }

    override fun onStart() {
        super.onStart()
        initialisePlayer()
    }

    override fun onPause() {
        super.onPause()

        // Pause video if this activity is covered by another component on Android versions before
        // 7 (Nougat), and don't pause otherwise. This is due to features like "split screen" on
        // Android versions starting from Nougat; in such cases, #onPause() is called even though
        // Activity is active, in #onResume() lifecycle stage.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.videoview.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        stopPlayback()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save current position of the video being played when Activity goes off-screen.
        outState.putInt(PLAYBACK_TIME, binding.videoview.currentPosition)
    }

    /**
     * Set up the [VideoView] object with the path to video file
     */
    private fun initialisePlayer() {
        binding.videoview.setVideoURI(path)

        // if position greater than 0 millis - file had started playing before - seek
        // {@link VideoView object to the last position.
        if (mVideoPosition > 0) {
            binding.videoview.seekTo(mVideoPosition)
        } else {
            // Skipping to 1 shows the first frame of the video.
            binding.videoview.seekTo(1)
        }
    }

    /**
     * Start playback of the video if [VideoView] object is non-null and set
     * [MediaPlayer.OnCompletionListener], otherwise return without
     * doing anything.
     */
    private fun startPlayback() {
        if (binding.videoview != null) {
            binding.videoview.start()
        } else {
            return
        }

        // set {@link MediaPlayer.OnCompletionListener} on the {@link VideoView} object.
        binding.videoview.setOnCompletionListener {
            Toast.makeText(
                this@VideoActivity, "Playback completed!",
                Toast.LENGTH_SHORT
            ).show()
            binding.videoview.seekTo(1)
        }
    }

    /**
     * Stop the video -- reset to 0 seconds, if the #mVideoView object is non-null.
     */
    private fun stopPlayback() {
        binding.videoview?.stopPlayback()
    }

    companion object {
        private val LOG_TAG = VideoActivity::class.java.name
        private const val PLAYBACK_TIME = "play_time"
        private var path: Uri? = null
    }
}