package com.blogspot.thengnet.mobilestudent;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MediaCategoryAdapter pageAdapter = new MediaCategoryAdapter(getSupportFragmentManager(), this);
//
//        ViewPager mediaPager = (ViewPager) findViewById(R.id.media_pager);
//        mediaPager.setAdapter(pageAdapter);
//
//        TabLayout mediaTabs = (TabLayout) findViewById(R.id.tab_layout);
//        mediaTabs.setupWithViewPager(mediaPager);
    }
}