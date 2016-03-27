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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.model.Artist;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;


/**
 * This fragment grabs artists data from the JSON file and displays it as a list.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentArtists extends Fragment {

    private OnListFragmentInteractionListener mListener;

    public FragmentArtists() {
    }

    public static FragmentArtists newInstance() {
        return new FragmentArtists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_artists, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            recyclerView.setAdapter(new ArtistsAdapter(DummyContent.ITEMS, mListener));
        }

        getJson();

        return view;
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
    private void getJson() {
        JsonDownloaderTask jsonDownloader = new JsonDownloaderTask();
        jsonDownloader.execute(Constants.ARTISTS_JSON_URL);
    }

    /**
     * Parses JSON from an URL and creates an array of (@link Artist) objects
     */
    private void updateList(String json) {
        Gson gson = new Gson();
        Type array = new TypeToken<Artist[]>() {}.getType();
        Artist[] artists = gson.fromJson(json, array);
        Toast.makeText(getContext(), artists[0].getCover().getBig(), Toast.LENGTH_LONG).show();
    }

    /**
     * Downloads JSON file from specified URL.
     */
    private class JsonDownloaderTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                if (urls.length > 1) throw new IllegalArgumentException("Multiple parameters are not allowed here");
                return IOUtils.toString(new URL(urls[0]));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            updateList(json);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListItemClick(Artist artistItem);
    }
}
