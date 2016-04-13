package com.kondenko.mobilizationtesttask.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentDetails;
import com.kondenko.mobilizationtesttask.utils.DetailsTransition;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentArtists.OnListFragmentInteractionListener {

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private FragmentArtists mFragmentArtists;
    private boolean mIsDetailsFragmentOpened;
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
        setFragment(mFragmentArtists, null, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setFragment(mFragmentArtists, null, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment, @Nullable ImageView sharedElement, boolean openDetailsFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mIsDetailsFragmentOpened = openDetailsFragment;
        // Display the up button if we open the details screen
        mActionBar.setDisplayHomeAsUpEnabled(openDetailsFragment);
        mActionBar.setHomeButtonEnabled(openDetailsFragment);

        // Animated transition between fragments
        if (openDetailsFragment && sharedElement != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            fragment.setExitTransition(new Fade());
            mFragmentArtists.setExitTransition(new Fade());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            transaction.addSharedElement(sharedElement, Constants.TRANSITION_ARTIST_PHOTO);
        }

        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onListItemClick(Artist artistItem, ImageView sharedElement) {
        setFragment(FragmentDetails.newInstance(artistItem), sharedElement, true);
    }

    @Override
    public void onOfflineModeEnabled() {
        mOfflineModeBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mIsDetailsFragmentOpened) setFragment(mFragmentArtists, null, false);
        else super.onBackPressed();
    }

}
