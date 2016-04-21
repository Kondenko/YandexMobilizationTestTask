package com.kondenko.mobilizationtesttask.ui.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ActivityArtistDetailsBinding;
import com.kondenko.mobilizationtesttask.model.Artist;


/**
 * Provides detailed info about an artist
 */
public class ActivityDetails extends AppCompatActivity {

    private Artist mArtist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        // Get an artist object from extras
        Intent incoming = getIntent();
        mArtist = incoming.getExtras().getParcelable(Constants.ARG_ARTIST);
        setTitle(mArtist.name);

        // Capitalize the first letter of the description
        mArtist.description = mArtist.description.substring(0, 1).toUpperCase() + mArtist.description.substring(1);

        // Setup binding
        ActivityArtistDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_details);
        binding.setArtist(mArtist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) supportFinishAfterTransition();
        return super.onOptionsItemSelected(item);
    }
}
