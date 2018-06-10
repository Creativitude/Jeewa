package com.creativitude.jeewa;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.creativitude.jeewa.activity.Drawer;
import com.creativitude.jeewa.activity.NearbyUsers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Dashboard extends Drawer implements View.OnClickListener {

    private static final String TAG = "Dashboard";
    private Button nearbyUsers;
    private static final int ERROR_DIALOG_REQUEST = 9001;



    @Override
    public void onClick(View view) {

        if(isServicesOk()) {
            startActivity(new Intent(Dashboard.this, NearbyUsers.class));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.dashboard, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.home);
        getSupportActionBar().setTitle("");


        init();



    }

    private void init() {

        nearbyUsers = findViewById(R.id.btn_dash_search);
        nearbyUsers.setOnClickListener(this);
    }

    public boolean isServicesOk() {

        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Dashboard.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOk: google play services is working");
            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOk: an error occurred but can be resolved");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Dashboard.this,available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }

        else {
            Snackbar.make(navigationView, R.string.google_map_error,Snackbar.LENGTH_LONG).show();
        }

        return false;
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
