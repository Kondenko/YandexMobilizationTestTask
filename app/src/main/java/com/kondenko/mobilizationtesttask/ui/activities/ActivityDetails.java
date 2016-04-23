package com.kondenko.mobilizationtesttask.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ActivityArtistDetailsBinding;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.squareup.picasso.Callback;


/**
 * Provides detailed info about an artist
 */
public class ActivityDetails extends AppCompatActivity {

    private Artist mArtist;

    private CustomTabsSession mCustomTabsSession;

    private FloatingActionButton mFab = null;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup binding
        final ActivityArtistDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_details);

        // Setup toolbar
        setSupportActionBar(binding.toolbarDetails);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Get an artist object from extras
        Intent incoming = getIntent();
        mArtist = incoming.getExtras().getParcelable(Constants.EXTRA_ARTIST);
        // Postpone shared element transition until artist's photo is loaded
        supportPostponeEnterTransition();
        mArtist.setImageLoadCallback(new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                // No connection: don't show the image, collapse the app bar
                // hiding the FAB which opens artist's website out of sight
                supportStartPostponedEnterTransition();
                binding.appBarLayout.setExpanded(false);
                Snackbar.make(binding.getRoot(), getString(R.string.message_no_connection), Snackbar.LENGTH_LONG).show();
            }
        });
        binding.setArtist(mArtist);

        // Capitalize the first letter of the description
        mArtist.description = mArtist.description.substring(0, 1).toUpperCase() + mArtist.description.substring(1);

        // Perform additional stuff
        setTitle(mArtist.name);

        mFab = binding.fabOpenInBrowser;

        if (mArtist.link != null) {

            // Setup a FAB to open artist's website if there is one
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(mArtist.link);
                }
            });

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        if (savedInstanceState == null) mFab.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        animateFab(mFab, true);
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
            } else {
                mFab.setVisibility(View.INVISIBLE);
                animateFab(mFab, true);
            }
        } else {
            // Don't show the button if there's no link provided
            mFab.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Open an url with Chrome Custom Tabs
     *
     * @param url artist website url
     */
    private void openLink(String url) {
        CustomTabsServiceConnection mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                customTabsClient.warmup(0L);
                mCustomTabsSession = customTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsSession = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(this, Constants.CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setShowTitle(true)
                .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                .setSecondaryToolbarColor(getResources().getColor(R.color.colorPrimaryDark))
                .build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    /**
     * Runs a scale animation from the center of a floating action button
     *
     * @param fab the button to animate
     */
    private void animateFab(FloatingActionButton fab, boolean in) {
        int from = in ? 0 : 1;
        int to = in ? 1 : 0;
        ScaleAnimation scale = new ScaleAnimation(from, to, from, to, 50f, 50f);
        scale.setDuration(Constants.TRANSITION_DURATION_FAB_DETAILS);
        scale.setInterpolator(new DecelerateInterpolator());
        fab.startAnimation(scale);
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) supportFinishAfterTransition();
        return super.onOptionsItemSelected(item);
    }

}
