package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Transitions;

public class Notifications extends Drawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_notifications, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.notifications);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(Notifications.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            finishAfterTransition();

        } else {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);

        }
    }
}
