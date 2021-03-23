package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the empty constructor to create an instance of this fragment.
 */
public class MediaControlsFragment extends Fragment {

    // track controller initialiser keys
    private static final String TRACK_TITLE_KEY = "track_title";
    private static final String TRACK_DURATION_KEY = "track_duration";

    private static String trackTitle;
    private static long trackDuration;

    private TextView tvTrackTitle;

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
    }

    public void setAudioMetrics (String title, long trackPosition) {
        Log.v(MediaControlsFragment.class.getName(), "titleReceived::" + title);
        tvTrackTitle.setText(title + " playing");
    }
}