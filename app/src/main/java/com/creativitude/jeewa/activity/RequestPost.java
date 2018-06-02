package com.creativitude.jeewa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.Connectivity;
import com.creativitude.jeewa.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestPost extends Drawer implements View.OnClickListener {

    private Connectivity connectivity;
    private Alert alert;
    private String postId;
    private Post postData;

    private TextView bg;
    private TextView message;
    private TextView name;
    private TextView age;
    private TextView district;
    private TextView contact_person;
    private TextView relationship;
    private TextView priority;
    private TextView date;
    private TextView gender;
    private TextView request_state;

    private Button call_now;
    private Button accept;

    private CardView priority_color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_request_post, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.requests);

        alert = new Alert(this);
        alert.showAlert();


        Intent intent = getIntent();
        postId = intent.getStringExtra("POST_ID");
        postData = new Post();

        init();
        fetchData();

        connectivity = new Connectivity(this);

        call_now.setOnClickListener(this);
        accept.setOnClickListener(this);
    }

    private void init() {

        bg = findViewById(R.id.rp_tv_blood_group);
        age = findViewById(R.id.rp_tv_age);
        district = findViewById(R.id.rp_tv_district);
        name = findViewById(R.id.rp_tv_name);
        contact_person = findViewById(R.id.rp_tv_contact_name);
        gender = findViewById(R.id.rp_tv_gender);
        date = findViewById(R.id.rp_tv_date);
        relationship = findViewById(R.id.rp_tv_relationship);
        priority = findViewById(R.id.rp_tv_priority);
        request_state = findViewById(R.id.rp_tv_request_state);
        message = findViewById(R.id.rp_tv_message);

        accept = findViewById(R.id.rp_btn_accept);
        call_now = findViewById(R.id.rp_btn_call_now);

    }

    private void fetchData() {

        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);

        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postData = dataSnapshot.getValue(Post.class);

                Log.d("Firebase", String.valueOf(postData));
                setFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setFields() {

        bg.setText(postData.getBloodGroup());
        message.setText(postData.getOptionalMessage());
        name.setText(postData.getName());
        age.setText(postData.getAge());
        district.setText(postData.getArea());
        contact_person.setText(postData.getContactPerson());
        gender.setText(postData.getGender());
        date.setText(postData.getDate());


        switch (postData.getPriority()) {
            case "0": {
                priority.setText(getString(R.string.blood_not_urgent));
                bg.setBackgroundColor(Color.parseColor("#4CAF50"));

                break;
            }

            case "1": {
                priority.setText(getString(R.string.blood_somewhat_urgent));
                bg.setBackgroundColor(Color.parseColor("#FFC107"));

                break;
            }

            case "2": {
                priority.setText(getString(R.string.blood_urgent));
                bg.setBackgroundColor(Color.parseColor("#F44336"));

                break;
            }
        }

        relationship.setText(postData.getRelationship());
        request_state.setText("Pending"); // should be changed according to the user selection

        alert.hideAlert();

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.requests);
        connectivity.checkConnectionState(navigationView);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rp_btn_call_now: {
                if(isPermissionGranted()){
                    callNow();
                }
                break;
            }
        }

    }

    private void callNow() {

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + postData.getContactNumber()));
            startActivity(callIntent);
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    callNow();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
