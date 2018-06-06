package com.creativitude.jeewa.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Transitions;

public class MyActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(MyActivities.this);
        }
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
}
