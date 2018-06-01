package com.creativitude.jeewa.viewholders;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creativitude.jeewa.R;

/**
 * Created by naveen on 29/05/2018.
 */

public class AllRequestsHolder extends RecyclerView.ViewHolder{

    private final TextView bloodType;
    private final TextView optionalMessage;
    private final TextView district;
    private final TextView contactPerson;
    private final Button callNow;
    private final CardView priority;

    public AllRequestsHolder(View itemView) {
        super(itemView);

        bloodType = itemView.findViewById(R.id.tv_rc_bloodGroup);
        optionalMessage = itemView.findViewById(R.id.tv_rc_msg);
        district = itemView.findViewById(R.id.tv_rc_area);
        contactPerson = itemView.findViewById(R.id.tv_rc_relationsName);
        callNow = itemView.findViewById(R.id.btn_rc_call);
        priority = itemView.findViewById(R.id.card_rc_priority);
    }

    public void setBloodType(String bloodType) {
        this.bloodType.setText(bloodType);
    }

    public void setOptionalMessage(String message) {
        this.optionalMessage.setText(message);
    }
    public void setDistrict(String district) {
        this.district.setText(district);
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson.setText(contactPerson);
    }

    public void setCallNow (String number) {
        this.callNow.setTag(number);
    }

    public void setPriority (String priority) {

        switch (Integer.parseInt(priority)) {

            case 0: {
                this.priority.setCardBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            }

            case 1: {
                this.priority.setCardBackgroundColor(Color.parseColor("#FFC107"));
                break;
            }

            case 2: {
                this.priority.setCardBackgroundColor(Color.parseColor("#F44336"));
                break;
            }
        }
    }




}
