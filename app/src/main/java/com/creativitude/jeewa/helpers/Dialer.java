package com.creativitude.jeewa.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by naveen on 02/06/2018.
 */

public class Dialer {

    private Context context;

    public Dialer(Context context) {
        this.context = context;
    }

    public void dial(String number) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        context.startActivity(callIntent);

    }
}
