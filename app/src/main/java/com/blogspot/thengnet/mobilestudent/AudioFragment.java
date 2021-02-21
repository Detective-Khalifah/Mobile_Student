package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends Fragment {

    public AudioFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View audioRoot = inflater.inflate(R.layout.fragment_audio, container, false);

        // Inflate the layout for this fragment
        return audioRoot;
    }

}