package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the empty constructor to create an instance of this fragment.
 */
public class MediaControlsFragment extends Fragment implements View.OnClickListener,
        View.OnLongClickListener {

    // track controller initialiser keys
    private static final String TRACK_TITLE_KEY = "track_title";
    private static final String TRACK_DURATION_KEY = "track_duration";

    private static String trackTitle;
    private static long trackDuration;

    private ImageView imgPlay, imgPrev, imgNext, imgRewind, imgFastForward;
    private TextView tvTrackTitle;

    private static AudioFragment audioFrag;

    public MediaControlsFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title    Title of the audio file.
     * @param duration Duration of the audio file.
     * @return A new instance of fragment MediaControlsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaControlsFragment newInstance (String title, Long duration) {
        MediaControlsFragment mediaControlsFragment = new MediaControlsFragment();
        Bundle mediaMetrics = new Bundle();
        mediaMetrics.putString(TRACK_TITLE_KEY, title);
        mediaMetrics.putLong(TRACK_DURATION_KEY, duration);
        mediaControlsFragment.setArguments(mediaMetrics);
        return mediaControlsFragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getArguments() != null) {
            trackTitle = getArguments().getString(TRACK_TITLE_KEY);
            trackDuration = getArguments().getLong(TRACK_DURATION_KEY);
        }
        audioFrag = (AudioFragment) getParentFragment();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_controls, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTrackTitle = (TextView) view.findViewById(R.id.tv_track_title);
        tvTrackTitle.setText(trackTitle);

        // ImageView lookup
        imgPlay = (ImageView) view.findViewById(R.id.btn_play_pause);
        imgPrev = (ImageView) view.findViewById(R.id.btn_prev_track);
        imgNext = (ImageView) view.findViewById(R.id.btn_next_track);
        imgRewind = (ImageView) view.findViewById(R.id.btn_rewind);
        imgFastForward = (ImageView) view.findViewById(R.id.btn_fastForward);

        // attach event-handler - #View.OnClickListener - to the controllers.
        imgPlay.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgRewind.setOnClickListener(this);
        imgFastForward.setOnClickListener(this);

        // attach event-handler - #View.OnClickListener - to the controllers.
        imgPlay.setOnLongClickListener(this);
        imgPrev.setOnLongClickListener(this);
        imgNext.setOnLongClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn_play_pause:
                pausePlayBack();
                break;
            case R.id.btn_prev_track:
                audioFrag.previousTrack();
                break;
            case R.id.btn_next_track:
                audioFrag.nextTrack();
                break;
            case R.id.btn_rewind:
                audioFrag.rewind();
            case R.id.btn_fastForward:
                audioFrag.fastForward();
            default:
        }
    }

    @Override
    public boolean onLongClick (View v) {
        switch (v.getId()) {
            case R.id.btn_play_pause:
                audioFrag.stopPlayback();
                return true;
            case R.id.btn_prev_track:

            case R.id.btn_next_track:


            default:
        }
        return false;
    }

    protected void setAudioMetrics (String title, long position) {
        // TODO: reset old/new title on TextView and position SeekBar accordingly
    }

    // TODO: Use clickListener & LongClickListener for the "next track" and fast-forward features
    //  respectively

    private void pausePlayBack () {
        if (audioFrag.isPlaying()) {
            audioFrag.pausePlayback();
            imgPlay.setImageResource(R.drawable.baseline_play_arrow_black_48);
        } else {
            audioFrag.playAudioFile();
            imgPlay.setImageResource(R.drawable.baseline_pause_black_48);
        }
    }

}