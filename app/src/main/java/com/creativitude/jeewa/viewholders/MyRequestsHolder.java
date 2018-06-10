package com.creativitude.jeewa.viewholders;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.creativitude.jeewa.R;

/**
 * Created by naveen on 07/06/2018.
 */

public class MyRequestsHolder extends RecyclerView.ViewHolder {

    private final TextView bloodType;
    private final CardView priority;
    private final TextView numberOfResponses;
    private final TextView date;



    public MyRequestsHolder(View itemView) {
        super(itemView);

        this.bloodType = itemView.findViewById(R.id.tv_mrc_bloodGroup);
        this.numberOfResponses = itemView.findViewById(R.id.tv_mrc_noOfResponses);
        this.priority = itemView.findViewById(R.id.card_mrc_priority);
        this.date = itemView.findViewById(R.id.tv_mrc_datePosted);
    }


    public void setBloodType(String bloodType) {
        this.bloodType.setText(bloodType);
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

    public void setNumberOfResponses(String responses) {
        this.numberOfResponses.setText(responses);
    }

    public void setDate(String date) {
        this.date.setText(date);
    }


}
