package com.blogspot.thengnet.mobilestudent;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private static final String PLAYBACK_TIME = "play_time";
    private static Uri path;
    private int mVideoPosition = 0;
    private VideoView mVideoView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        Intent videoToPlay = getIntent();
        path = videoToPlay.getData();

        mVideoView = (VideoView) findViewById(R.id.videoview);

        MediaController mediaController = new MediaController(this);
//        mediaController.setMediaPlayer(mVideoView);

        mVideoView.setMediaController(mediaController);

        initialisePlayer(path);
    }

    private void initialisePlayer (Uri path) {

        mVideoView.setVideoURI(path);

        if (mVideoPosition > 0) {
            mVideoView.seekTo(mVideoPosition);
        } else {
            // Skipping to 1 shows the first frame of the video.
            mVideoView.seekTo(1);
        }

        mVideoView.start();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion (MediaPlayer mp) {
                Toast.makeText(VideoActivity.this, "Playback completed!",
                        Toast.LENGTH_SHORT).show();
                mVideoView.seekTo(1);
            }
        });
    }

    private void releasePlayer () {
        mVideoView.stopPlayback();
    }

    @Override
    protected void onStart () {
        super.onStart();

        initialisePlayer(path);
    }

    @Override
    protected void onPause () {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop () {
        super.onStop();

        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }
}