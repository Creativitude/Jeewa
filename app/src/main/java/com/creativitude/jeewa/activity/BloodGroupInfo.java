package com.creativitude.jeewa.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.creativitude.jeewa.R;

public class BloodGroupInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group_info);

        ImageView imageView = findViewById(R.id.image_bg_info);

        Glide.with(this)
                .load(R.drawable.en_bgc)
                .into(imageView);
    }
}
