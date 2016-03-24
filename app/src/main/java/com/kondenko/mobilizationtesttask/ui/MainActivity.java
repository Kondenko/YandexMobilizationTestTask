package com.kondenko.mobilizationtesttask.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kondenko.mobilizationtesttask.Artist;
import com.kondenko.mobilizationtesttask.FragmentArtists.OnListFragmentInteractionListener;
import com.kondenko.mobilizationtesttask.R;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
    }

    private void setFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }


    @Override
    public void onListItemClick(Artist artistItem) {
//        setFragment(new FragmentArtists().newInstance(artistItem.getId()));
    }

}
