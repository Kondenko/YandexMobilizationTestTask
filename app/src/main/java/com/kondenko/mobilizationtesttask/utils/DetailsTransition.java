package com.kondenko.mobilizationtesttask.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.animation.DecelerateInterpolator;

import com.kondenko.mobilizationtesttask.Constants;

/**
 * Creates a TransitionSet that allows an image change its shape during animation
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DetailsTransition extends AutoTransition {
    public DetailsTransition() {
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
        setOrdering(ORDERING_TOGETHER);
        setDuration(Constants.TRANSITION_DURATION);
        setInterpolator(new DecelerateInterpolator());
    }
}