package com.creativitude.jeewa.helpers;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

/**
 * Created by naveen on 01/06/2018.
 */

public class AnimateSwipe {

    public static void animateLeftSwipe(View view) {

        SpringAnimation animation = new SpringAnimation(view,DynamicAnimation.TRANSLATION_X,0);
        animation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                .setStiffness(SpringForce.STIFFNESS_HIGH);


        animation.start();
    }
}
