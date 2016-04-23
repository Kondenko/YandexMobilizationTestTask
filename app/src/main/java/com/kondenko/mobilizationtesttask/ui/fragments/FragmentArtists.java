package com.kondenko.mobilizationtesttask.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.utils.ArtistsAdapter;
import com.kondenko.mobilizationtesttask.utils.ConnectionCheckerAsyncTask;
import com.kondenko.mobilizationtesttask.utils.JsonCacheHelper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Shows a list of artists
 */
public class FragmentArtists extends Fragment {

    private Activity mActivity;
    private ArtistsFragmentInteractionListener mListener;

    @Bind(R.id.progressbar_artists)
    protected ProgressBar mProgressBar;
    @Bind(R.id.recyclerview_artists)
    protected RecyclerView mRecyclerViewArtists;
    private ArtistsAdapter mAdapter;

    private List<Artist> mData;

    private boolean mIsConnectionAvailable = false;

    public FragmentArtists() {
    }

    public static FragmentArtists newInstance() {
        return new FragmentArtists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, rootView);

        mActivity = getActivity();
        mActivity.setTitle(R.string.title_artists);

        try {
            mIsConnectionAvailable = new ConnectionCheckerAsyncTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        runJsonParsingTask(); // Download JSON, parse it and configure RecyclerView to show it
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ArtistsFragmentInteractionListener) {
            mListener = (ArtistsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
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
    public void runJsonParsingTask() {
        JsonDownloaderTask jsonDownloader = new JsonDownloaderTask();
        jsonDownloader.execute(Constants.ARTISTS_JSON_URL);
    }

    /**
     * Sets up the RecyclerView and its adapter to show the list of artists
     */
    private void setupRecyclerView(List<Artist> data) {
        mData = data;
        mAdapter = new ArtistsAdapter(data, mListener);
        mRecyclerViewArtists.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewArtists.setAdapter(mAdapter);
    }

    /**
     * Updates the RecyclerView to show search results
     *
     * @param query text to look for
     */
    public void searchInList(@Nullable String query) {
        if (query == null) {
            // Return the list to it's original state
            mAdapter.update(mData);
        } else {
            // Show filtered items
            List<Artist> filteredList = filter(mData, query);
            if (filteredList.isEmpty())
                Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
            mAdapter.update(filteredList);
        }
        mRecyclerViewArtists.scrollToPosition(0);
    }

    /**
     * Goes through the given list and filters it according to the given query.
     *
     * @param artists list given as search sample
     * @param query to be searched
     * @return new filtered list
     *
     * @link blog.lovelyhq.com/implementing-a-live-list-search-in-android-action-bar
     */
    private List<Artist> filter(List<Artist> artists, String query) {
        // First we split the query so that we're able
        // to search word by word (in lower case).
        String[] queryByWords = query.toLowerCase().split("\\s+");
        // Empty list to fill with matches.
        List<Artist> moviesFiltered = new ArrayList<>();
        // Go through initial releases and perform search.
        for (Artist artist : artists) {
            // Content to search through (in lower case).
            String content = artist.name.toLowerCase();
            for (String word : queryByWords) {
                // There is a match only if all of the words are contained.
                int numberOfMatches = queryByWords.length;
                // All query words have to be contained,
                // otherwise the release is filtered out.
                if (content.contains(word)) {
                    numberOfMatches--;
                } else {
                    break;
                }
                // They all match.
                if (numberOfMatches == 0) {
                    moviesFiltered.add(artist);
                }
            }
        }
        return moviesFiltered;
    }

    /**
     * Downloads JSON file from specified URL.
     */
    private class JsonDownloaderTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Artist> doInBackground(String... urls) {
            // We should be able to download only one json file here
            if (urls.length > 1)
                throw new IllegalArgumentException("Multiple parameters are not allowed here");

            String json;
            boolean usingCachedFile = false;

            try {
                // Try to use the cached file
                json = JsonCacheHelper.getCachedJson(getContext());
                usingCachedFile = true;
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    // The file doesn't exist yet, we should create one
                    // Download the file from the url and save it
                    json = IOUtils.toString(new URL(urls[0]));
                    JsonCacheHelper.cacheJson(getContext(), json);
                } catch (IOException e1) {
                    // Can't get any data
                    e1.printStackTrace();
                    return null;
                }
            }

            // Enable offline mode if there's no connection but some data is available offline
            if (usingCachedFile && !mIsConnectionAvailable) mListener.onOfflineModeEnabled();

            Type array = new TypeToken<List<Artist>>() {
            }.getType();
            return new Gson().fromJson(json, array);
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (artists != null) {
                // JSON file have been successfully downloaded, go further
                setupRecyclerView(artists);
            } else {
                // No connection, no cached data - show an error
                if (mListener != null) mListener.onLoadingFail();
            }
        }
    }

    public interface ArtistsFragmentInteractionListener {
        void onListItemClick(Artist artistItem, ImageView sharedElement);

        void onOfflineModeEnabled();

        void onLoadingFail();
    }
}
