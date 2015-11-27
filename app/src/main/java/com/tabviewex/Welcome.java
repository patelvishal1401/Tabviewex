package com.tabviewex;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public  class Welcome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.welcome, container, false);
        TextView myView = (TextView) rootView.findViewById(R.id.textView);
        myView.setText("welcome");



        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(myView, "alpha",  1f, .3f);
        fadeOut.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", .3f, 1f);
        fadeIn.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

//        mAnimationSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mAnimationSet.start();
//            }
//        });
        mAnimationSet.start();

        ImageView imView = (ImageView) rootView.findViewById(R.id.imageView);

        Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);


        ObjectAnimator up = ObjectAnimator.ofFloat(imView, "y", 500, 0);
        up.setDuration(1000);

        ObjectAnimator down = ObjectAnimator.ofFloat(imView, "y",0,500);
        down.setDuration(1000);

        final AnimatorSet imAnimationSet = new AnimatorSet();

        imAnimationSet.play(up).after(down);

        imAnimationSet.start();





        return rootView;
    }
}