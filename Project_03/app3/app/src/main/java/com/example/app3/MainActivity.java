package com.example.app3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.app.ActionBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.app3.VacationList_fragment.ListSelectionListener;
//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
public class MainActivity
        extends FragmentActivity
        implements ListSelectionListener {

    public static String[] mTitleArray;
    private static final String TOAST_INTENT = "edu.uic.cs478.BroadcastReceiver3.showToast";
    private static final String PERMISSION = "edu.uic.cs478.f20.kaboom";

    public static int[] mImageArray = {R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4, R.drawable.i5
            , R.drawable.i6};


    private final ImageFragment mQuoteFragment = new ImageFragment();

    // UB 2/24/2019 -- Android Pie twist: Original FragmentManager class is now deprecated.
    private androidx.fragment.app.FragmentManager mFragmentManager;

    private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, getClass().getSimpleName() + ": entered onCreate()");

        super.onCreate(savedInstanceState);

        //Action bar
        android.app.ActionBar actionbar =getActionBar();
        actionbar.setTitle("  Vacation Spots");
        actionbar.setIcon(R.drawable.logo);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);


        // Get the string arrays with the titles
        mTitleArray = getResources().getStringArray(R.array.Titles);


        setContentView(R.layout.activity_main);

        // Get references to the TitleFragment and to the QuotesFragment
        mTitleFrameLayout = (FrameLayout) findViewById(R.id.title_fragment_container);
        mQuotesFrameLayout = (FrameLayout) findViewById(R.id.quote_fragment_container);


        // Get reference to the SupportFragmentManager instead of original FragmentManager
        mFragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction with backward compatibility
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();

        // Add the TitlesFragment to the layout
        // UB: 10/2/2016 Changed add() to replace() to avoid overlapping fragments
        if (savedInstanceState == null) {
            fragmentTransaction.replace(
                    R.id.title_fragment_container,
                    new VacationList_fragment());

            // Commit the FragmentTransaction
            fragmentTransaction.commit();
        }
        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        // This can happen either (1) because the user made a selection before the
        // quotes fragment was shown or (2) because the user moved back after the quotes
        // fragment was shown.  These two cases are handled separately in setLayout() below.
        mFragmentManager.addOnBackStackChangedListener(
                // UB 2/24/2019 -- Use support version of Listener

                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });
    }
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("IMG_FRAME", (Parcelable) mQuoteFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onListSelection(VacationList_fragment.position);
    }
    private void setLayout() {
        boolean landscape_flag = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if(!landscape_flag) {
            if (!mQuoteFragment.isAdded()) {

                // Make the TitleFragment occupy the entire layout
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));
            }
            else {
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        0, MATCH_PARENT));
                mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        MATCH_PARENT));
            }
        }
        else {
            // Determine whether the QuoteFragment has been added
            if (!mQuoteFragment.isAdded()) {

                // Make the TitleFragment occupy the entire layout
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));
            } else {

                // Make the TitleLayout take 1/3 of the layout's width
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));

                // Make the QuoteLayout take 2/3's of the layout's width
                mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
        }


    }



    // Implement Java interface ListSelectionListener defined in TitlesFragment
    // Called by TitlesFragment when the user selects an item in the TitlesFragment
    @Override
    public void onListSelection(int index) {

        // If the QuoteFragment has not been added, add it now
        if (!mQuoteFragment.isAdded()) {

            // Start a new FragmentTransaction
            // UB 2/24/2019 -- Now must use compatible version of FragmentTransaction
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the QuoteFragment to the layout
            fragmentTransaction.add(R.id.quote_fragment_container, mQuoteFragment);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        if (mQuoteFragment.getShownIndex() != index) {

            // Tell the QuoteFragment to show the quote string at position index
            mQuoteFragment.showQuoteAtIndex(index);

        }
    }



    @Override
    protected void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.appbar,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "Launching A1 and A2", Toast.LENGTH_SHORT).show();
                permissions();
                break;
            case R.id.item2:
                Toast.makeText(this, "Exiting A3", Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void permissions() {
        int pos = VacationList_fragment.position;
        String link = null;
        if (pos >= 0) {
            link = getResources().getStringArray(R.array.Links)[pos];

            Intent intent = new Intent(TOAST_INTENT);
            intent.putExtra("POSITION", pos);
            intent.putExtra("PLACENAME", mTitleArray[pos]);
            intent.putExtra("URL", link);

            sendOrderedBroadcast(intent, PERMISSION);
        }
        else{
            Toast.makeText(this,"No Item selected", Toast.LENGTH_SHORT).show();
        }
    }

}