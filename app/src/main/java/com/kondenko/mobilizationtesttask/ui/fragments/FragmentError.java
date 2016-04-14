package com.kondenko.mobilizationtesttask.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kondenko.mobilizationtesttask.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentError extends Fragment {

    private OnRetryListener mListener;

    public FragmentError() {
    }

    public static FragmentError newInstance() {
        return new FragmentError();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.error_screen_artists_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void setOnRetryListener(OnRetryListener listener) {
        mListener = listener;
    }

    @OnClick(R.id.button_retry)
    public void retry() {
        mListener.onRetry();
    }

    public interface OnRetryListener {
        void onRetry();
    }
}
