package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    Fragment currentFragment, // the current activity selected
            mediaFrag, calcFrag, notesFrag;

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
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame, currentFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
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
        calculatorFragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (currentFragment.isAdded()) {
            calculatorFragTransaction.show(currentFragment);
        } else {
            calculatorFragTransaction.add(R.id.fragment_frame, currentFragment);
        }

        if (mediaFrag.isAdded())
            calculatorFragTransaction.hide(mediaFrag);
        if (notesFrag.isAdded())
            calculatorFragTransaction.hide(notesFrag);

        // commit changes made on {@link FragmentTransaction} #calculatorFragTransaction
        calculatorFragTransaction.commit();
    }

    private void displayNotesFrag () {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
        } else {
            ft.add(R.id.fragment_frame, currentFragment);
        }

        if (calcFrag.isAdded())
            ft.hide(calcFrag);
        if (mediaFrag.isAdded())
            ft.hide(mediaFrag);

        // commit changes made on {@link FragmentTransaction} #ft
        ft.commit();
    }

    private void displayMediaFrag () {
        FragmentTransaction mediaTransact = getSupportFragmentManager().beginTransaction();
        mediaTransact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (currentFragment.isAdded()) {
            mediaTransact.show(currentFragment);
        } else {
            mediaTransact.add(R.id.fragment_frame, currentFragment);
        }

        if (calcFrag.isAdded())
            mediaTransact.hide(calcFrag);
        if (notesFrag.isAdded())
            mediaTransact.hide(notesFrag);

        // commit changes made on {@link FragmentTransaction} #mediaTransact
        mediaTransact.commit();
    }

}