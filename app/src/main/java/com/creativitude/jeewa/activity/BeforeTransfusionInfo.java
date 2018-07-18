package com.creativitude.jeewa.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.creativitude.jeewa.R;

public class BeforeTransfusionInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_transfusion_info);
        ImageView imageView = findViewById(R.id.image_before_transfusion);

        Glide.with(this)
                .load(R.drawable.before_transfusion_en)
                .into(imageView);
    }
}
