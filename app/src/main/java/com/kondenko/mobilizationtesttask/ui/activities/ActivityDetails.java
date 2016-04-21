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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ActivityArtistDetailsBinding;
import com.kondenko.mobilizationtesttask.model.Artist;


/**
 * Provides detailed info about an artist
 */
public class ActivityDetails extends AppCompatActivity {

    private final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";


    private Artist mArtist;

    private CustomTabsSession mCustomTabsSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup binding
        ActivityArtistDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_details);

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
        binding.setArtist(mArtist);

        // Capitalize the first letter of the description
        mArtist.description = mArtist.description.substring(0, 1).toUpperCase() + mArtist.description.substring(1);

        // Perform additional stuff
        setTitle(mArtist.name);
        if (mArtist.link != null) {
            // Setup opening links
            binding.fabOpenLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(mArtist.link);
                }
            });
        } else {
            // Hide button if there's no link to open
            binding.fabOpenLink.setVisibility(View.GONE);
        }
    }

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
        CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setShowTitle(true)
                .build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) supportFinishAfterTransition();
        return super.onOptionsItemSelected(item);
    }

}
