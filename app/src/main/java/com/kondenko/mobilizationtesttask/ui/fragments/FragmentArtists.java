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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.utils.ArtistsAdapter;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Shows a list of artists
 */
public class FragmentArtists extends Fragment {

    private OnListFragmentInteractionListener mListener;

    @Bind(R.id.progressbar_artists)
    protected ProgressBar mProgressBar;
    @Bind(R.id.recyclerview_artists)
    protected RecyclerView mRecyclerViewArtists;


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
        getActivity().setTitle(R.string.title_artists);
        runJsonParsingTask(); // Download JSON, parse it and configure RecyclerView to show it
        return rootView;
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
     * Sets up the RecyclerView and its adapter to show the list of artists
     */
    private void setupRecyclerView(Artist[] data) {
        mRecyclerViewArtists.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewArtists.setAdapter(new ArtistsAdapter(data, mListener));
    }


    /**
     * Saves a file with JSON code into internal storage
     *
     * @param contents json code
     * @throws IOException
     */
    private void cacheJson(String contents) throws IOException {
        FileOutputStream outputStream = getActivity().openFileOutput(Constants.CACHED_FILE_NAME, Context.MODE_PRIVATE);
        outputStream.write(contents.getBytes());
        outputStream.close();
    }

    /**
     * Read the file with JSON code from internal storage
     *
     * @return JSON code string
     * <p/>
     * The snippet is taken from <a href="http://www.stackoverflow.com/questions/14768191/how-do-i-read-the-file-content-from-the-internal-storage-android-app">here</a>
     */
    private String getCachedJson() throws IOException {
        FileInputStream inputStream = getActivity().openFileInput(Constants.CACHED_FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }


    /**
     * Downloads JSON file from specified URL.
     */
    private class JsonDownloaderTask extends AsyncTask<String, Void, Artist[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Artist[] doInBackground(String... urls) {

            // We should be able to download only one json file here
            if (urls.length > 1)
                throw new IllegalArgumentException("Multiple parameters are not allowed here");

            String json;
            try {
                // Try to use the cached file
                json = getCachedJson();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    // The file doesn't exist yet, we should create one
                    // Download the file from the url and save it
                    json = IOUtils.toString(new URL(urls[0]));
                    cacheJson(json);
                } catch (IOException e1) {
                    // Can't get any data
                    e1.printStackTrace();
                    return null;
                }
            }

            Type array = new TypeToken<Artist[]>() {}.getType();
            return new Gson().fromJson(json, array);
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            super.onPostExecute(artists);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (artists != null) {
                // JSON file have been successfully downloaded, go further
                setupRecyclerView(artists);
            } else {
                // No connection, no cached data - show an error
                Toast.makeText(getContext(), "Cannot load artists list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListItemClick(Artist artistItem);
    }
}
