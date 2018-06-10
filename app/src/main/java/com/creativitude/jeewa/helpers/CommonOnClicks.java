package com.creativitude.jeewa.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by naveen on 07/06/2018.
 */

public class CommonOnClicks {

    public static void fullCardOnClick(View view, String key, Activity activity, Context context, Class toClass) {

        Intent intent = new Intent(activity, toClass);
        intent.putExtra("POST_ID",key);

//        Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
//                view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
//
//        ActivityCompat.startActivity(AllRequests.this, intent, options);

        context.startActivity(intent);

    }
}
