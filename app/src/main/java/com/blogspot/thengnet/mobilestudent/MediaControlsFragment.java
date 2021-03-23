package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the empty constructor to create an instance of this fragment.
 */
public class MediaControlsFragment extends Fragment {

    private TextView tvTrackTitle;

    public MediaControlsFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        tvTrackTitle = view.findViewById(R.id.tv_track_title);
    }

    protected void setAudioMetrics (String title, long trackPosition) {
        tvTrackTitle.setText(title);
    }
}