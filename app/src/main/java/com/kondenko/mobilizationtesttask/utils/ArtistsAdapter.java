
package com.kondenko.mobilizationtesttask.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ListItemArtistBinding;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {


    private final Artist[] mArtists;
    private final OnListFragmentInteractionListener mListener;

    public ArtistsAdapter(Artist[] artists, OnListFragmentInteractionListener listener) {
        mArtists = artists;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Artist artist = mArtists[position];
        holder.binding.setArtist(artist);
        loadImage(holder.imageView, artist.cover.small);
    }


    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String v) {
        Context context = imageView.getContext();
        int size = (int) context.getResources().getDimension(R.dimen.list_item_artist_photo_size);
        Picasso.with(context).load(v).resize(size, size).centerCrop().into(imageView);
    }


    @Override
    public int getItemCount() {
        return mArtists.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ListItemArtistBinding binding;
        @Bind(R.id.imageview_artist)
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
            binding = DataBindingUtil.bind(view);
        }
    }
}

