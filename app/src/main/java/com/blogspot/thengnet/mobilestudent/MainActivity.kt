package com.blogspot.thengnet.mobilestudent

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var currentFragment: Fragment? = null

    // the current activity selected
    var mediaFrag: Fragment? = null
    var calcFrag: Fragment? = null
    var notesFrag: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // instantiate the 3 main component fragments of the app
        calcFrag = SimpleCalculatorFragment()
        mediaFrag = MediaFragment()
        notesFrag = NotesFragment()
        val bottomNavigationView =
            findViewById<View>(R.id.bottom_navigation_view) as BottomNavigationView

        // if a Fragment is already in view when #onStop got called, re-display it.
        if (savedInstanceState != null) {
            val fragmentTag = savedInstanceState.getString("current-fragment-tag")
            when (fragmentTag) {
                "calculator-frag" -> {
                    currentFragment = calcFrag
                    displayCalculatorFrag()
                }
                "media-frag" -> {
                    currentFragment = mediaFrag
                    displayMediaFrag()
                }
                "notes-frag" -> {
                    currentFragment = notesFrag
                    displayNotesFrag()
                }
            }
        } else {
            // Replace #FrameLayout content with {@link NotesFragment} at startup
            currentFragment = notesFrag
            displayNotesFrag()
            bottomNavigationView.menu.getItem(1).isChecked = true
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem -> // set the #currentFragment to {@link BottomNavigationView} item selected and display it
            when (menuItem.itemId) {
                R.id.media_page -> {
                    menuItem.isChecked = true
                    currentFragment = mediaFrag
                    displayMediaFrag()
                }
                R.id.notes_page -> {
                    menuItem.isChecked = true
                    currentFragment = notesFrag
                    displayNotesFrag()
                }
                R.id.calculator_page -> {
                    menuItem.isChecked = true
                    currentFragment = calcFrag
                    displayCalculatorFrag()
                }
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (currentFragment != null) outState.putString(
            "current-fragment-tag",
            currentFragment!!.tag
        )
    }

    /**
     * Use a [FragmentTransaction] to add/show the [SimpleCalculatorFragment] and hide
     * other 2 fragments -- [MediaFragment] & [NotesFragment].
     */
    private fun displayCalculatorFrag() {
        val calculatorFragTransaction = supportFragmentManager.beginTransaction()
        calculatorFragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        // Make the #SimpleCalculatorFragment visible
        if (currentFragment!!.isAdded) {
            calculatorFragTransaction.show(currentFragment!!)
        } else {
            calculatorFragTransaction.add(R.id.fragment_frame, currentFragment!!, "calculator-frag")
        }

        // Hide the Media and Notes Fragments
        if (mediaFrag!!.isAdded) calculatorFragTransaction.hide(mediaFrag!!)
        if (notesFrag!!.isAdded) calculatorFragTransaction.hide(notesFrag!!)
        calculatorFragTransaction.remove(notesFrag!!)

        // commit changes made on {@link FragmentTransaction} #calculatorFragTransaction
        calculatorFragTransaction.commit()
    }

    /**
     * Use a [FragmentTransaction] to add/show the [NotesFragment] and hide
     * other 2 fragments -- [SimpleCalculatorFragment] & [MediaFragment].
     */
    private fun displayNotesFrag() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        // Make the #NotesFragment visible
        if (currentFragment!!.isAdded) {
            ft.show(currentFragment!!)
        } else {
            ft.add(R.id.fragment_frame, currentFragment!!, "notes-frag")
        }

        // Hide the Simple Calculator and Media Fragments
        if (calcFrag!!.isAdded) ft.hide(calcFrag!!)
        ft.remove(calcFrag!!)
        if (mediaFrag!!.isAdded) ft.hide(mediaFrag!!)

        // commit changes made on {@link FragmentTransaction} #ft
        ft.commit()
    }

    /**
     * Use a [FragmentTransaction] to add/show the [MediaFragment] and hide
     * other 2 fragments -- [SimpleCalculatorFragment] & [NotesFragment].
     */
    private fun displayMediaFrag() {
        val mediaTransact = supportFragmentManager.beginTransaction()
        mediaTransact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        // Make the #MediaFragment visible
        if (currentFragment!!.isAdded) {
            mediaTransact.show(currentFragment!!)
        } else {
            mediaTransact.add(R.id.fragment_frame, currentFragment!!, "media-frag")
        }

        // Hide the Simple Calculator & Notes Fragments
        if (calcFrag!!.isAdded) mediaTransact.hide(calcFrag!!)
        if (notesFrag!!.isAdded) mediaTransact.hide(notesFrag!!)

        // commit changes made on {@link FragmentTransaction} #mediaTransact
        mediaTransact.commit()
    }
}