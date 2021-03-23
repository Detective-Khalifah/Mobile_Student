package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaFragment extends Fragment {

    MediaCategoryAdapter pageAdapter;

    public MediaFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageAdapter = new MediaCategoryAdapter(getFragmentManager(), getContext());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View mediaView = inflater.inflate(R.layout.fragment_media, container, false);

        // Inflate the layout for this fragment
        return mediaView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager mediaPager = (ViewPager) view.findViewById(R.id.media_pager);
        mediaPager.setAdapter(pageAdapter);

        TabLayout mediaTabs = (TabLayout) view.findViewById(R.id.tab_layout);
        mediaTabs.setupWithViewPager(mediaPager);
    }
}