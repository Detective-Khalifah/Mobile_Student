package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

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

        // if a Fragment is already in view when #onStop got called, re-display it.
        if (savedInstanceState != null) {
            String fragmentTag = savedInstanceState.getString("current-fragment-tag");
            switch (fragmentTag) {
                case "calculator-frag":
                    currentFragment = calcFrag;
                    displayCalculatorFrag();
                    break;
                case "media-frag":
                    currentFragment = mediaFrag;
                    displayMediaFrag();
                    break;
                case "notes-frag":
                    currentFragment = notesFrag;
                    displayNotesFrag();
                    break;
            }
        } else {
            // Replace #FrameLayout content with {@link NotesFragment} at startup
            currentFragment = notesFrag;
            displayNotesFrag();
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
                // set the #currentFragment to {@link BottomNavigationView} item selected and display it

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

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentFragment != null)
            outState.putString("current-fragment-tag", currentFragment.getTag());
    }

    /**
     * Use a {@link FragmentTransaction} to add/show the {@link SimpleCalculatorFragment} and hide
     * other 2 fragments -- {@link MediaFragment} & {@link NotesFragment}.
     */
    private void displayCalculatorFrag () {
        FragmentTransaction calculatorFragTransaction = getSupportFragmentManager().beginTransaction();
        calculatorFragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        // Make the #SimpleCalculatorFragment visible
        if (currentFragment.isAdded()) {
            calculatorFragTransaction.show(currentFragment);
        } else {
            calculatorFragTransaction.add(R.id.fragment_frame, currentFragment, "calculator-frag");
        }

        // Hide the Media and Notes Fragments
        if (mediaFrag.isAdded())
            calculatorFragTransaction.hide(mediaFrag);
        if (notesFrag.isAdded())
            calculatorFragTransaction.hide(notesFrag);
        calculatorFragTransaction.remove(notesFrag);

        // commit changes made on {@link FragmentTransaction} #calculatorFragTransaction
        calculatorFragTransaction.commit();
    }

    /**
     * Use a {@link FragmentTransaction} to add/show the {@link NotesFragment} and hide
     * other 2 fragments -- {@link SimpleCalculatorFragment} & {@link MediaFragment}.
     */
    private void displayNotesFrag () {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        // Make the #NotesFragment visible
        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
        } else {
            ft.add(R.id.fragment_frame, currentFragment, "notes-frag");
        }

        // Hide the Simple Calculator and Media Fragments
        if (calcFrag.isAdded())
            ft.hide(calcFrag);
        ft.remove(calcFrag);
        if (mediaFrag.isAdded())
            ft.hide(mediaFrag);

        // commit changes made on {@link FragmentTransaction} #ft
        ft.commit();
    }

    /**
     * Use a {@link FragmentTransaction} to add/show the {@link MediaFragment} and hide
     * other 2 fragments -- {@link SimpleCalculatorFragment} & {@link NotesFragment}.
     */
    private void displayMediaFrag () {
        FragmentTransaction mediaTransact = getSupportFragmentManager().beginTransaction();
        mediaTransact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        // Make the #MediaFragment visible
        if (currentFragment.isAdded()) {
            mediaTransact.show(currentFragment);
        } else {
            mediaTransact.add(R.id.fragment_frame, currentFragment, "media-frag");
        }

        // Hide the Simple Calculator & Notes Fragments
        if (calcFrag.isAdded())
            mediaTransact.hide(calcFrag);
        if (notesFrag.isAdded())
            mediaTransact.hide(notesFrag);

        // commit changes made on {@link FragmentTransaction} #mediaTransact
        mediaTransact.commit();
    }

}