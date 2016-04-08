package com.kondenko.mobilizationtesttask.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentDetails;

public class MainActivity extends AppCompatActivity implements FragmentArtists.OnListFragmentInteractionListener, FragmentDetails.OnArtistFragmentInteractionListener {

    private FragmentManager mFragmentManager;
    private FragmentArtists mFragmentArtists;

    private boolean mIsDetailsFragmentOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mFragmentArtists = FragmentArtists.newInstance();
        setFragment(mFragmentArtists);
    }

    private void setFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onFragmentSet(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void onReturnToList() {
        super.onBackPressed();
    }

    @Override
    public void onListItemClick(Artist artistItem) {
        setFragment(FragmentDetails.newInstance(artistItem));
        mIsDetailsFragmentOpen = true;
    }

    @Override
    public void onBackPressed() {
        if (mIsDetailsFragmentOpen) {
            mIsDetailsFragmentOpen = false;
            setFragment(mFragmentArtists);
        }
        else super.onBackPressed();
    }

}
