package com.kondenko.mobilizationtesttask.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.utils.ArtistsAdapter;
import com.kondenko.mobilizationtesttask.utils.OnListFragmentInteractionListener;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;

import butterknife.BindString;


/**
 * This fragment grabs artists data from the JSON file and displays it as a list.
 */
public class FragmentArtists extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private View mRecyclerView; // A single RecyclerView

    public FragmentArtists() {
    }

    public static FragmentArtists newInstance() {
        return new FragmentArtists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = inflater.inflate(R.layout.fragment_artists, container, false);
        mListener.onFragmentSet(getContext().getString(R.string.title_artists));
        runJsonParsingTask(); // Download JSON, parse it and configure RecyclerView to show it
        return mRecyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Calls AsyncTask to download JSON from the url.
     */
    private void runJsonParsingTask() {
        JsonDownloaderTask jsonDownloader = new JsonDownloaderTask();
        jsonDownloader.execute(Constants.ARTISTS_JSON_URL);
    }

    /**
     * Parses JSON from an URL and creates an array of (@link Artist) objects
     */
    private void setupRecyclerView(View view, Artist[] data) {
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new ArtistsAdapter(data, mListener));
        }
    }

    /**
     * Downloads JSON file from specified URL.
     */
    private class JsonDownloaderTask extends AsyncTask<String, Void, Artist[]> {
        @Override
        protected Artist[] doInBackground(String... urls) {
            try {
                // We should be able to download only one json file here
                if (urls.length > 1) throw new IllegalArgumentException("Multiple parameters are not allowed here");
                String json = IOUtils.toString(new URL(urls[0]));
                Type array = new TypeToken<Artist[]>() {}.getType();
                return new Gson().fromJson(json, array);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            super.onPostExecute(artists);
            setupRecyclerView(mRecyclerView, artists);
        }
    }

}
