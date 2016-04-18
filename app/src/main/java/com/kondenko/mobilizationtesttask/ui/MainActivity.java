package com.kondenko.mobilizationtesttask.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentDetails;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentError;
import com.kondenko.mobilizationtesttask.utils.DetailsTransition;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentArtists.ArtistsFragmentInteractionListener {

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private FragmentArtists mFragmentArtists;
    private boolean mIsDetailsFragmentOpened = false;
    @Bind(R.id.textview_offline_mode)
    protected TextView mOfflineModeBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActionBar = getSupportActionBar();
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

    public void updateUi(boolean openDetailsFragment) {
        mIsDetailsFragmentOpened = openDetailsFragment;
        // Display the up button if we open the details screen
        mActionBar.setDisplayHomeAsUpEnabled(openDetailsFragment);
        mActionBar.setHomeButtonEnabled(openDetailsFragment);
    }

    @Override
    public void onListItemClick(Artist artist, ImageView sharedElement) {
        updateUi(true);
        FragmentDetails fragment = FragmentDetails.newInstance(artist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Setup transitions
            Transition sharedElementTransition = new DetailsTransition();
            Transition slideTransition = new Slide().setDuration(Constants.TRANSITION_DURATION).setInterpolator(new DecelerateInterpolator());
            Transition fadeTransition = new Fade().setDuration(Constants.TRANSITION_DURATION).setInterpolator(new DecelerateInterpolator());

            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setSharedElementReturnTransition(sharedElementTransition);
            fragment.setEnterTransition(slideTransition);
            fragment.setExitTransition(fadeTransition);
        }

        mFragmentManager.beginTransaction()
                .addSharedElement(sharedElement, getString(R.string.transition_artist_photo))
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
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

    @Override
    public void onBackPressed() {
        if (mIsDetailsFragmentOpened) {
            updateUi(false);
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
