package com.kondenko.mobilizationtesttask.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtistDetails;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists.OnListFragmentInteractionListener;
import com.kondenko.mobilizationtesttask.R;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private Toolbar mToolbar;

    private FragmentManager mFragmentManager;
    private FragmentArtists mFragmentArtists;

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
    public void onListItemClick(Artist artistItem) {
        setFragment(FragmentArtistDetails.newInstance(artistItem.getId()));
    }

}
