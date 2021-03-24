package com.blogspot.thengnet.mobilestudent;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaFragment extends Fragment {
    private Button btnStorageAccess;
    private TextView tvStorageAccessExplanation;

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

        boolean hasStorageAccess = checkStorageAccess(view.getContext());

        // find the views explaining reason for, and enabling permission grant
        btnStorageAccess = view.findViewById(R.id.btn_external_storage_access);
        tvStorageAccessExplanation = view.findViewById(R.id.tv_external_storage_access);

        if (hasStorageAccess) {
            // make the views unavailable if app has storage access permission granted
            btnStorageAccess.setVisibility(View.GONE);
            tvStorageAccessExplanation.setVisibility(View.GONE);

            ViewPager mediaPager = (ViewPager) view.findViewById(R.id.media_pager);
            mediaPager.setAdapter(pageAdapter);

            TabLayout mediaTabs = (TabLayout) view.findViewById(R.id.tab_layout);
            mediaTabs.setupWithViewPager(mediaPager);
        }
    }

    private boolean checkStorageAccess (Context appContext) {
        int permissionStatus = ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        switch (permissionStatus) {
            case PackageManager.PERMISSION_GRANTED:
                return true;
            case PackageManager.PERMISSION_DENIED:
            default:
                return false;
        }
    }
}