package com.creativitude.jeewa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.creativitude.jeewa.activity.BeforeTransfusionInfo;
import com.creativitude.jeewa.activity.BloodGroupInfo;
import com.creativitude.jeewa.activity.DonorSelectionInfo;
import com.creativitude.jeewa.activity.RisksInfo;

public class InfoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);


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
}
