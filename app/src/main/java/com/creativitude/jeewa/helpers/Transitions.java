package com.creativitude.jeewa.helpers;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Slide;
import android.view.Gravity;

import com.creativitude.jeewa.R;

/**
 * Created by naveen on 06/06/2018.
 */

public class Transitions {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void init(Activity activity) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.excludeTarget(R.id.appBar, true);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        slide.setDuration(500);
        activity.getWindow().setEnterTransition(slide);
        activity.getWindow().setExitTransition(slide);
    }


}
