package com.kondenko.mobilizationtesttask.model;

/**
 * POJO to save data from JSON
 */

public class Artist {

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

    public Artist() {
    }

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

}
