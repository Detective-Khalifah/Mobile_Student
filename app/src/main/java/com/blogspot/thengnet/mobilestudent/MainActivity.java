package com.blogspot.thengnet.mobilestudent;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment mediaFrag, notesFrag, calcFrag;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new NotesFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
                Fragment currentFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.media_page:
                        menuItem.setChecked(true);
                        displayMediaFrag();
                        break;
                    case R.id.notes_page:
                        menuItem.setChecked(true);
                        displayNotesFrag();
                        break;
                    case R.id.calculator_page:
                        menuItem.setChecked(true);
                        displayCalculatorFrag();
                        break;
                }
                return false;
            }
        });
    }

    private void displayCalculatorFrag () {
        Fragment currentFragment;
        currentFragment = new SimpleCalculatorFragment();
        FragmentTransaction calculatorFragTransaction = getSupportFragmentManager().beginTransaction();
        calculatorFragTransaction.replace(R.id.fragment_frame, currentFragment);
        calculatorFragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        calculatorFragTransaction.commit();
    }

    private void displayNotesFrag () {
        Fragment currentFragment;
        currentFragment = new NotesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, currentFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void displayMediaFrag () {
        Fragment currentFragment;
        currentFragment = new MediaFragment();
        FragmentTransaction mediaTransact = getSupportFragmentManager()
                .beginTransaction();
        mediaTransact.replace(R.id.fragment_frame, currentFragment);
        mediaTransact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mediaTransact.commit();
    }

}