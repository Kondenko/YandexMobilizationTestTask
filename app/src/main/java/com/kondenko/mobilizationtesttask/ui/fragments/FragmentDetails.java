package com.kondenko.mobilizationtesttask.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.FragmentArtistDetailsBinding;
import com.kondenko.mobilizationtesttask.model.Artist;


public class FragmentDetails extends Fragment {

    private Artist mArtist = null;

    public FragmentDetails() {
    }

    public static FragmentDetails newInstance(Artist artist) {
        FragmentDetails fragment = new FragmentDetails();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_ARTIST_ID, artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtist = getArguments().getParcelable(Constants.ARG_ARTIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentArtistDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_details, container, false);
        binding.setArtist(mArtist);
        return binding.getRoot();
    }

}
