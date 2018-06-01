package com.creativitude.jeewa.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.creativitude.jeewa.R;

/**
 * Created by naveen on 27/05/2018.
 */

public class Alert {

    private Activity activity;
    private AlertDialog alertDialog;

    public Alert(Activity activity) {
        this.activity = activity;
    }

    public void showAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.progress_bar_layout, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void hideAlert() {
        alertDialog.dismiss();
    }


}
