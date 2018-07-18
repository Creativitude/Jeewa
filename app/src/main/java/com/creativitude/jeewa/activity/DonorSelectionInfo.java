package com.creativitude.jeewa.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.creativitude.jeewa.R;

public class DonorSelectionInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_selection_info);

        ImageView imageView = findViewById(R.id.image_donor_selection);

        Glide.with(this)
                .load(R.drawable.donor_selection_criteria_en)
                .into(imageView);
    }
}
