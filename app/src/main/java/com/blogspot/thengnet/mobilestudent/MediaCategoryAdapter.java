package com.blogspot.thengnet.mobilestudent;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MediaCategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MediaCategoryAdapter (FragmentManager fm, Context theContext) {
        super(fm);
        mContext = theContext;
    }

    @Override
    public Fragment getItem (int pos) {
        switch (pos) {
            case 0:
                return new AudioFragment();
            case 1:
                return new VideoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount () {
        return 2;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.audio_tab_label);
            case 1:
                return mContext.getString(R.string.video_tab_label);
            default:
                return null;
        }
    }
}
