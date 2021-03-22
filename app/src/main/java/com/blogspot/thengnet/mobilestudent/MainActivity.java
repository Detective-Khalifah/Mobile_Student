package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment, // the current activity selected
            mediaFrag, notesFrag, calcFrag;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate the 3 main component fragments of the app
        calcFrag = new SimpleCalculatorFragment();
        mediaFrag = new MediaFragment();
        notesFrag = new NotesFragment();

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        // Replace #FrameLayout content with {@link NotesFragment} at startup
        currentFragment = notesFrag;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, currentFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
                Fragment currentFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.media_page:
                        menuItem.setChecked(true);
                        currentFragment = mediaFrag;
                        displayMediaFrag();
                        break;
                    case R.id.notes_page:
                        menuItem.setChecked(true);
                        currentFragment = notesFrag;
                        displayNotesFrag();
                        break;
                    case R.id.calculator_page:
                        menuItem.setChecked(true);
                        currentFragment = calcFrag;
                        displayCalculatorFrag();
                        break;
                }
                return false;
            }
        });
    }

    private void displayCalculatorFrag () {
        FragmentTransaction calculatorFragTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment.isAdded()) {
            calculatorFragTransaction.show(currentFragment);
            calculatorFragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        } else {
            calculatorFragTransaction.add(R.id.fragment_frame, currentFragment);
        }

        calculatorFragTransaction.commit();
    }

    private void displayNotesFrag () {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        } else {
            ft.add(R.id.fragment_frame, currentFragment);
        }

        ft.commit();
    }

    private void displayMediaFrag () {
        FragmentTransaction mediaTransact = getSupportFragmentManager().beginTransaction();
        if (currentFragment.isAdded()) {
            mediaTransact.show(currentFragment);
        } else {
            mediaTransact.add(R.id.fragment_frame, currentFragment);
            mediaTransact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        mediaTransact.commit();
    }

}