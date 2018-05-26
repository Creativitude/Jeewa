package com.creativitude.jeewa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.creativitude.jeewa.activity.Drawer;

public class MainActivity extends Drawer {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.home);
        super.onResume();
    }


}
