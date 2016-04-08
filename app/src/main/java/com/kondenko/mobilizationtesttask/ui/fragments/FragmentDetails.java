package com.kondenko.mobilizationtesttask.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.FragmentArtistDetailsBinding;
import com.kondenko.mobilizationtesttask.model.Artist;


public class FragmentDetails extends Fragment {

    private Artist mArtist = null;

    private OnArtistFragmentInteractionListener mListener;

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
            if (mArtist != null) mListener.onFragmentSet(mArtist.name);
            else Toast.makeText(getContext(), "Could't load artist info", Toast.LENGTH_LONG).show(); // TODO: Show error screen
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout = R.layout.fragment_artist_details;
        View view = inflater.inflate(layout, container, false);

        FragmentArtistDetailsBinding binding = DataBindingUtil.inflate(inflater, layout, container, false);
        binding.setArtist(mArtist);

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArtistFragmentInteractionListener) {
            mListener = (OnArtistFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnArtistFragmentInteractionListener {
        void onFragmentSet(String title); // Sets the title of toolbar
        void onReturnToList();
    }

}
