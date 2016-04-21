package com.kondenko.mobilizationtesttask.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.ActivityDetails;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentArtists.ArtistsFragmentInteractionListener {

    private FragmentManager mFragmentManager;
    private FragmentArtists mFragmentArtists;

    @Bind(R.id.textview_offline_mode)
    protected TextView mOfflineModeBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentArtists = FragmentArtists.newInstance();
        setFragment(mFragmentArtists);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onListItemClick(Artist artist, ImageView sharedElement) {
        Intent detailsActivity = new Intent(this, ActivityDetails.class);
        detailsActivity.putExtra(Constants.ARG_ARTIST, artist);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, sharedElement, Constants.TRANSITION_ARTIST_PHOTO);
            startActivity(detailsActivity, options.toBundle());
        } else {
            startActivity(detailsActivity);
        }
    }

    @Override
    public void onOfflineModeEnabled() {
        mOfflineModeBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFail() {
        FragmentError fragmentError = FragmentError.newInstance();
        setFragment(fragmentError);
        fragmentError.setOnRetryListener(new FragmentError.OnRetryListener() {
            @Override
            public void onRetry() {
                setFragment(mFragmentArtists);
            }
        });
    }

}
