
package com.kondenko.mobilizationtesttask.utils;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ListItemArtistBinding;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {


    private final Artist[] mArtists;
    private final FragmentArtists.OnListFragmentInteractionListener mListener;

    public ArtistsAdapter(Artist[] artists, FragmentArtists.OnListFragmentInteractionListener listener) {
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
        ViewCompat.setTransitionName(holder.imageView, String.valueOf(position) + "_image");
    }

    public static void loadImage(ImageView imageView, String imageUrl) {
        Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }


    @Override
    public int getItemCount() {
        return mArtists.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public ListItemArtistBinding binding;
        @Bind(R.id.imageview_artist)
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mView = view;
            binding = DataBindingUtil.bind(view);
        }

        @Override
        public void onClick(View view) {
            mListener.onListItemClick(mArtists[getAdapterPosition()], imageView);
        }
    }
}

