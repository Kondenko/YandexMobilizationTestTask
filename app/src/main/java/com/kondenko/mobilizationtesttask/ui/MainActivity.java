package com.kondenko.mobilizationtesttask.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentDetails;

public class MainActivity extends AppCompatActivity implements FragmentArtists.OnListFragmentInteractionListener {

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();
        setFragment(FragmentArtists.newInstance(), false, getTitle().toString());
    }

    private void setFragment(Fragment fragment, boolean addToBackStack, String title) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (addToBackStack) transaction.addToBackStack(null);
        if (mActionBar != null) mActionBar.setTitle(title);
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onListItemClick(Artist artistItem) {
        setFragment(FragmentDetails.newInstance(artistItem), true, artistItem.name);
    }

}
