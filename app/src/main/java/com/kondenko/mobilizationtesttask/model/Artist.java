package com.kondenko.mobilizationtesttask.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * POJO to save data from JSON
 */

public class Artist implements Parcelable {

    public static class Cover {
        public String small;
        public String big;

        public Cover() {
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

    /**
     * Returns a single string of genres separated with commas.
     *
     * @return list of genres
     */
    public String getGenresString() {
        if (genres.length == 1) return genres[0]; // No point doing anything else since there's only one genre.
        String genresSeparated = "";
        for (int i = 0; i < genres.length; i++) {
            // We don't want to add a comma in the end of the string
            genresSeparated += i < genres.length - 1 ? genres[i] + ", " : genres[i];
        }
        return genresSeparated;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .into(view);
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
        cover.small = in.readString();
        cover.big = in.readString();
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
        parcel.writeString(cover.small);
        parcel.writeString(cover.big);
    }

}
