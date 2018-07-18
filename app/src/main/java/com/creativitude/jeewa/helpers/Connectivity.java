package com.creativitude.jeewa.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.creativitude.jeewa.R;

/**
 * Created by naveen on 27/05/2018.
 */

public class Connectivity {

    private Context context;

    public Connectivity(Context context) {
        this.context = context;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void checkConnectionState (final View view) {

//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    setSnackBar(view,context.getString(R.string.back_online),1);
//                } else {
//                    setSnackBar(view,context.getString(R.string.device_offline),0);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });


    }

    private void setSnackBar (View view,String message,int state) {


        Snackbar snackbar = Snackbar.make(view,message,Snackbar.LENGTH_LONG);

        if (state == 0) {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(context,R.color.sb_red));
        } else {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(context,R.color.sb_green));
        }

        snackbar.show();


    }


}
