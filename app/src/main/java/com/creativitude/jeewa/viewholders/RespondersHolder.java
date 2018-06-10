package com.creativitude.jeewa.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Dialer;

/**
 * Created by naveen on 07/06/2018.
 */

public class RespondersHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView bloodType;
    private TextView district;
    private ImageButton call;

    public RespondersHolder(View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.tv_res_name);
        this.bloodType = itemView.findViewById(R.id.tv_res_bloodGroup);
        this.district = itemView.findViewById(R.id.tv_res_district);
        this.call = itemView.findViewById(R.id.btn_res_call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialer dialer = new Dialer(view.getContext());
                dialer.dial(view.getTag().toString());
            }
        });
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setBloodType(String bloodType) {
        this.bloodType.setText(bloodType);
    }

    public void setDistrict(String district) {
        this.district.setText(district);
    }

    public void setCall(String number) {
        this.call.setTag(number);
    }
}
