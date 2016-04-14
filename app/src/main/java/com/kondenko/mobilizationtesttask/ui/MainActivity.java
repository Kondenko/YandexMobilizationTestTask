package com.kondenko.mobilizationtesttask.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentArtists.ArtistsFragmentInteractionListener {

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
        setArtistsFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setArtistsFragment();
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
            DetailsTransition sharedTransition = new DetailsTransition();
            TransitionSet slideFadeTransition = new TransitionSet().addTransition(new Slide()).addTransition(new Fade()).setInterpolator(new DecelerateInterpolator());

//            mFragmentArtists.setExitTransition(slideFadeTransition);
//
//            fragment.setEnterTransition(slideFadeTransition);
//            fragment.setExitTransition(sharedTransition);

            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            transaction.addSharedElement(sharedElement, Constants.TRANSITION_ARTIST_PHOTO);
        }

        transaction.replace(R.id.container, fragment).commit();
    }

    private void setArtistsFragment() {
        setFragment(mFragmentArtists, null, false);
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
    public void onLoadingFail() {
        FragmentError fragmentError = FragmentError.newInstance();
        setFragment(fragmentError, null, false);
        fragmentError.setOnRetryListener(new FragmentError.OnRetryListener() {
            @Override
            public void onRetry() {
                setArtistsFragment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mIsDetailsFragmentOpened) setArtistsFragment();
        else super.onBackPressed();
    }
}
