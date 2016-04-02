package com.kondenko.mobilizationtesttask.utils;

import com.kondenko.mobilizationtesttask.model.Artist;

/**
 * Created by Kondenko on 01.04.2016.
 */
public interface OnListFragmentInteractionListener {
    void onFragmentSet(String title); // Sets the title of toolbar
    void onListItemClick(Artist artistItem);
}
