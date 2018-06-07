package com.creativitude.jeewa.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by naveen on 02/06/2018.
 */

public class CurrentDate {

    public static String getDate () {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ", Locale.getDefault());

        return mdformat.format(calendar.getTime());
    }
}
