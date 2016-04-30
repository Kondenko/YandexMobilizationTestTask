
package com.kondenko.mobilizationtesttask.utils;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kondenko.mobilizationtesttask.Constants;
import com.kondenko.mobilizationtesttask.R;
import com.kondenko.mobilizationtesttask.databinding.ListItemArtistBinding;
import com.kondenko.mobilizationtesttask.model.Artist;
import com.kondenko.mobilizationtesttask.ui.fragments.FragmentArtists;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private List<Artist> mArtists;
    private final FragmentArtists.ArtistsFragmentInteractionListener mListener;

    public ArtistsAdapter(List<Artist> artists, FragmentArtists.ArtistsFragmentInteractionListener listener) {
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
        Artist artist = mArtists.get(position);
        holder.binding.setArtist(artist);
        loadImage(holder.imageView, artist.cover.small);
        ViewCompat.setTransitionName(holder.imageView, String.valueOf(position) + Constants.TRANSITION_ARTIST_NAME_POSTFIX);
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        RequestCreator requestCreator = Picasso.with(imageView.getContext()).load(imageUrl);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Use a vector drawable
            requestCreator.placeholder(R.drawable.ic_photo_placeholder_24dp_vector);
        } else {
            // Use a PNG drawable
            Drawable placeholderDrawable = ResourcesCompat.getDrawable(imageView.getContext().getResources(), R.drawable.ic_photo_placeholder_24dp, null);
            if (placeholderDrawable != null) {
                placeholderDrawable.setAlpha(20); // Reduce opacity
                requestCreator.placeholder(placeholderDrawable);
            }
        }
        requestCreator.into(imageView);
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    public void update(List<Artist> newData) {
        mArtists = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ListItemArtistBinding binding;
        @Bind(R.id.imageview_artist)
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onListItemClick(mArtists.get(getAdapterPosition()), imageView);
        }
    }

}

