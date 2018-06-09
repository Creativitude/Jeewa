package com.creativitude.jeewa.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativitude.jeewa.R;

/**
 * Created by naveen on 09/06/2018.
 */

public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    private TextView message;
    private ImageView image;

    public NotificationsViewHolder(View itemView) {
        super(itemView);

        message = itemView.findViewById(R.id.tv_notification);
        image = itemView.findViewById(R.id.iv_not_icon);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setImage(){

    }
}
