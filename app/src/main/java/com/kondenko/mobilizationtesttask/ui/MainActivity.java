package com.kondenko.mobilizationtesttask.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentDetails;

// TODO: Provide up navigation
public class MainActivity extends AppCompatActivity implements FragmentArtists.OnListFragmentInteractionListener {

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private FragmentArtists mFragmentArtists;
    private boolean mIsDetailsFragmentOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();
        mFragmentArtists = FragmentArtists.newInstance();
        setFragment(mFragmentArtists, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setFragment(mFragmentArtists, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment, boolean openDetailsFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mIsDetailsFragmentOpened = openDetailsFragment;
        if (openDetailsFragment) transaction.addToBackStack(null);
        mActionBar.setDisplayHomeAsUpEnabled(openDetailsFragment);
        mActionBar.setHomeButtonEnabled(openDetailsFragment);
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onListItemClick(Artist artistItem) {
        setFragment(FragmentDetails.newInstance(artistItem), true);
    }

    @Override
    public void onBackPressed() {
        if (mIsDetailsFragmentOpened) setFragment(mFragmentArtists, false);
        else super.onBackPressed();
    }

}
