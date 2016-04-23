package com.kondenko.mobilizationtesttask.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * POJO to save data from JSON
 */

public class Artist implements Parcelable {

    public static class Cover implements Parcelable {
        public String small;
        public String big;

        protected Cover(Parcel in) {
            small = in.readString();
            big = in.readString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(small);
            parcel.writeString(big);
        }

        public static final Creator<Cover> CREATOR = new Creator<Cover>() {
            @Override
            public Cover createFromParcel(Parcel in) {
                return new Cover(in);
            }

            @Override
            public Cover[] newArray(int size) {
                return new Cover[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

    }

    public int id;
    public String name;
    public String[] genres;
    public int tracks;
    public int albums;
    public String link;
    public String description;
    public Cover cover;

    private static Callback mImageLoadCallback;

    /**
     * Returns a single string of genres separated with commas.
     *
     * @return list of genres
     */
    public String getGenresString() {
        if (genres.length == 1)
            return genres[0]; // No point doing anything else id there's only one genre.
        return TextUtils.join(", ", genres);
    }

    /**
     * Loads an image into an ImageView within a layout with Data Binding support
     *
     * @param view
     * @param imageUrl
     */
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .into(view, mImageLoadCallback);
    }

    public void setImageLoadCallback(Callback callback) {
        mImageLoadCallback = callback;
    }

    /** Parcelable stuff **/

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    protected Artist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        genres = in.createStringArray();
        tracks = in.readInt();
        albums = in.readInt();
        link = in.readString();
        description = in.readString();
        cover = in.readParcelable(Cover.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeStringArray(genres);
        parcel.writeInt(tracks);
        parcel.writeInt(albums);
        parcel.writeString(link);
        parcel.writeString(description);
        parcel.writeParcelable(cover, i);
    }

}
