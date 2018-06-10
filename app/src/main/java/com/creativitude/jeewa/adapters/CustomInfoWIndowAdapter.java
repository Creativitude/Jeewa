package com.creativitude.jeewa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Dialer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by naveen on 10/06/2018.
 */

public class CustomInfoWIndowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    @SuppressLint("InflateParams")
    public CustomInfoWIndowAdapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window,null);
    }

    private void rendowWindowText (Marker marker, View view) {
        String title = marker.getTitle();
        TextView tv_title = view.findViewById(R.id.map_title);
        Button call_btn = view.findViewById(R.id.map_call_btn);

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialer dialer = new Dialer(view.getContext());
                dialer.dial(view.getTag().toString());
            }
        });

        call_btn.setTag(marker.getSnippet());

        if(!title.equals("")) {
            tv_title.setText(title);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker,mWindow);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker,mWindow);
        return null;
    }
}
