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

    // arbitrary code value to match permission request code
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 12;

    MediaCategoryAdapter pageAdapter;
    private Button btnStorageAccess;
    private TextView tvStorageAccessExplanation;

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
    public void onViewCreated (final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean hasStorageAccess = checkStorageAccess(view.getContext());

        // find the views explaining reason for, and enabling permission grant
        btnStorageAccess = view.findViewById(R.id.btn_external_storage_access);
        tvStorageAccessExplanation = view.findViewById(R.id.tv_external_storage_access);

        if (hasStorageAccess) {
            hideRationale();
            setupMediaFragments(view);
        } else {
            showRationale();

            btnStorageAccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    requestStorageAccess(view.getContext());
                }
            });
        }
    }

    private void showRationale () {
        // make the views available if app has storage access permission denied -- not granted
        btnStorageAccess.setVisibility(View.VISIBLE);
        tvStorageAccessExplanation.setVisibility(View.VISIBLE);
    }

    private void setupMediaFragments (View rootView) {
        ViewPager mediaPager = (ViewPager) rootView.findViewById(R.id.media_pager);
        mediaPager.setAdapter(pageAdapter);

        TabLayout mediaTabs = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mediaTabs.setupWithViewPager(mediaPager);
    }

    private void hideRationale() {
        // make the views unavailable if app has storage access permission granted
        btnStorageAccess.setVisibility(View.GONE);
        tvStorageAccessExplanation.setVisibility(View.GONE);
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

    private void requestStorageAccess (Context appContext) {
        String[] permissionSet = {Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissionSet, EXTERNAL_STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Use a SnackBar to notify permission grant
                    hideRationale();
                    setupMediaFragments(getView());
                } else {
                    showRationale();
                }
            }
        } else {
            // TODO: Check if user 'denied' permission more than once, and use #Intent to get user
            //  to Settings screen to enable permission manually.
            return;
        }
    }
}