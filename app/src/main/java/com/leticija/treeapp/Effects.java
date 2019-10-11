package com.leticija.treeapp;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class Effects {

    public static void alterTextView (TextView textView, boolean visibility, String text, int color) {
        textView.setText(text);
        if (visibility) {
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
        }
        textView.setTextColor(ContextCompat.getColor(textView.getContext(),color));
    }

    public static void setRotateAnimation(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 1440, Animation.RELATIVE_TO_SELF,
                .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(8000);
        System.out.println("START: " + rotateAnimation.getStartOffset());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotateAnimation);
    }

    public static void fadeIn (final View view,int duration) {

        Animation fadeOut = new AlphaAnimation(0,1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        view.startAnimation(fadeOut);
    }

    public static void fadeOut (final View view,int duration) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        view.startAnimation(fadeOut);
    }
}
