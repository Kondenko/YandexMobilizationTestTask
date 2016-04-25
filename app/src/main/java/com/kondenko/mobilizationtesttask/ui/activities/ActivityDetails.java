package com.kondenko.mobilizationtesttask.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ActivityArtistDetailsBinding;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.utils.ConnectivityBroadcastReceiver;
import com.squareup.picasso.Callback;


/**
 * Provides detailed info about an artist
 */
public class ActivityDetails extends AppCompatActivity {

    private ConnectivityBroadcastReceiver mReceiver;
    private CustomTabsSession mCustomTabsSession;
    private ActivityArtistDetailsBinding mBinding;

    private Artist mArtist;

    private TextView mOfflineModeBanner;

    private boolean mIsConnectionAvailable = false;
    private boolean mIsImageAvailable = false;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist_details);

        // Setup toolbar
        setSupportActionBar(mBinding.toolbarDetails);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Get extras
        Intent incoming = getIntent();
        mArtist = incoming.getExtras().getParcelable(Constants.EXTRA_ARTIST);
        // This variable is needed because we cant check if user is online within onCreate block.
        // So we suppose the connection remains the same as it was when user was on the previous screen.
        // I know this is not a very good practice though.
        mIsConnectionAvailable = incoming.getBooleanExtra(Constants.EXTRA_CONNECTION, false);

        if (mIsConnectionAvailable) {
            // Postpone shared element transition until the artist's photo is loaded
            supportPostponeEnterTransition();
        }

        mArtist.setImageLoadCallback(new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
                mIsImageAvailable = true;
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
                mIsImageAvailable = false;
            }
        });

        mBinding.setArtist(mArtist);

        // Capitalize the first letter of the description
        mArtist.description = mArtist.description.substring(0, 1).toUpperCase() + mArtist.description.substring(1);

        // Perform additional stuff
        setTitle(mArtist.name);

        mOfflineModeBanner = (TextView) findViewById(R.id.textview_offline_mode_details);

        if (mArtist.link != null) {
            // Setup the FAB to open artist's website if there is one
            mBinding.fabOpenInBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(mArtist.link);
                }
            });

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Start FAB animation after the transition is finished
                getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        if (savedInstanceState == null)
                            mBinding.fabOpenInBrowser.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        if (mIsConnectionAvailable) animateFab(true);
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
                mBinding.fabOpenInBrowser.setVisibility(View.INVISIBLE);
                if (mIsConnectionAvailable) animateFab(true);
            }
        } else {
            // Don't show the button if there's no link provided
            mBinding.fabOpenInBrowser.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new ConnectivityBroadcastReceiver();
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mReceiver.setConnectivityListener(new ConnectivityBroadcastReceiver.ConnectivityListener() {
            @Override
            public void onConnectionChecked(boolean available) {
                useOfflineMode(!available);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
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
     */
    private void animateFab(boolean in) {
        int from = in ? 0 : 1;
        int to = in ? 1 : 0;
        ScaleAnimation scale = new ScaleAnimation(from, to, from, to, 50f, 50f);
        scale.setDuration(Constants.TRANSITION_DURATION_FAB_DETAILS);
        scale.setInterpolator(new DecelerateInterpolator());
        mBinding.fabOpenInBrowser.startAnimation(scale);
        mBinding.fabOpenInBrowser.setVisibility(View.VISIBLE);
    }

    /**
     * Changes the layout appearance to show and hide Views that are
     * not necessary without Internet connection.
     *
     * @param use true if all the views should be hidden, false otherwise
     */
    public void useOfflineMode(boolean use) {
        // Show a banner which indicates that the app works without connection
        mOfflineModeBanner.setVisibility(use ? View.VISIBLE : View.GONE);
        if (!mIsImageAvailable) {
            if (mReceiver.isConnectionAvailable()) {
                // Load and show artist's cover photo if user goes online
                Artist.loadImage(mBinding.imageviewArtistDetailsPhoto, mArtist.cover.big);
                mBinding.appBarLayout.setExpanded(true, true);
                if (!mBinding.fabOpenInBrowser.isShown()) animateFab(true);
            } else {
                // Artist photo couldn't be downloaded.
                // Collapse AppBar containing empty ImageView
                // so it won't take space.
                mBinding.appBarLayout.setExpanded(false, false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) supportFinishAfterTransition();
        return super.onOptionsItemSelected(item);
    }

}
