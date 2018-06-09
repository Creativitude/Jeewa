package com.creativitude.jeewa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.creativitude.jeewa.activity.Drawer;

public class Dashboard extends Drawer {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.dashboard, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.home);

        getSupportActionBar().setTitle("");


    }

    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.home);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


}
