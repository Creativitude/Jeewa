package com.creativitude.jeewa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.creativitude.jeewa.activity.BeforeTransfusionInfo;
import com.creativitude.jeewa.activity.BloodGroupInfo;
import com.creativitude.jeewa.activity.DonorSelectionInfo;
import com.creativitude.jeewa.activity.Drawer;
import com.creativitude.jeewa.activity.RisksInfo;
import com.creativitude.jeewa.helpers.Connectivity;
import com.creativitude.jeewa.helpers.Transitions;

public class InfoList extends Drawer {

    private Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_info_list, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.important_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(InfoList.this);
        }

        connectivity = new Connectivity(this);


        TextView donorSelection = findViewById(R.id.tv_info_donorSelection);
        TextView beforeTrans = findViewById(R.id.tv_info_beforeTransfusion);
        TextView bloodGroup = findViewById(R.id.tv_info_bloodGroup);
        TextView risks = findViewById(R.id.tv_info_risks);

        donorSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoList.this, DonorSelectionInfo.class));
            }
        });

        beforeTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoList.this, BeforeTransfusionInfo.class));
            }
        });

        bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoList.this, BloodGroupInfo.class));
            }
        });

        risks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoList.this, RisksInfo.class));
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.important_info);
        connectivity.checkConnectionState(navigationView);
    }
}
