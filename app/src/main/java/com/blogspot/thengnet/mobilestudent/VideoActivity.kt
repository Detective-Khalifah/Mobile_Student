package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private static final String LOG_TAG = VideoActivity.class.getName();
    private static final String PLAYBACK_TIME = "play_time";
    private int mVideoPosition = 0;

    private static Uri path;
    private VideoView mVideoView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // get path to video file from #Intent that launched this #Activity
        Intent videoToPlay = getIntent();
        path = videoToPlay.getData();

        mVideoView = (VideoView) findViewById(R.id.videoview);

        MediaController mediaController = new MediaController(this);
//        mediaController.setMediaPlayer(mVideoView);

        mVideoView.setMediaController(mediaController);

        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        startPlayback();

        // TODO: Implement {@link AudioFocusRequest, AudioAttributes}, whatever and learn Services
        //  as soon as possible; OnAudioFocusChangeListener & AudioManager worked poorly, unlike it
        //  did with {@link AudioFragment}.
    }

    @Override
    protected void onStart () {
        super.onStart();

        initialisePlayer();
    }

    @Override
    protected void onPause () {
        super.onPause();

        // Pause video if this activity is covered by another component on Android versions before
        // 7 (Nougat), and don't pause otherwise. This is due to features like "split screen" on
        // Android versions starting from Nougat; in such cases, #onPause() is called even though
        // Activity is active, in #onResume() lifecycle stage.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop () {
        super.onStop();

        stopPlayback();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save current position of the video being played when Activity goes off-screen.
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    /**
     * Set up the {@link VideoView} object with the path to video file
     */
    private void initialisePlayer () {
        mVideoView.setVideoURI(path);

        // if position greater than 0 millis - file had started playing before - seek
        // {@link VideoView object to the last position.
        if (mVideoPosition > 0) {
            mVideoView.seekTo(mVideoPosition);
        } else {
            // Skipping to 1 shows the first frame of the video.
            mVideoView.seekTo(1);
        }
    }

    /**
     * Start playback of the video if {@link VideoView} object is non-null and set
     * {@link MediaPlayer.OnCompletionListener}, otherwise return without
     * doing anything.
     */
    private void startPlayback () {
        if (mVideoView != null) {
            mVideoView.start();
        } else {
            return;
        }

        // set {@link MediaPlayer.OnCompletionListener} on the {@link VideoView} object.
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion (MediaPlayer mp) {
                Toast.makeText(VideoActivity.this, "Playback completed!",
                        Toast.LENGTH_SHORT).show();
                mVideoView.seekTo(1);
            }
        });
    }

    /**
     * Stop the video -- reset to 0 seconds, if the #mVideoView object is non-null.
     */
    private void stopPlayback () {
        if (mVideoView != null)
            mVideoView.stopPlayback();
    }

}