package com.creativitude.jeewa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.creativitude.jeewa.helpers.Constants;
import com.creativitude.jeewa.helpers.Dialer;
import com.creativitude.jeewa.helpers.Transitions;
import com.creativitude.jeewa.https.PostMethod;
import com.creativitude.jeewa.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private DatabaseReference root;
    private FirebaseAuth auth;
    private String state;
    private boolean state_listener;
    private boolean rejectClicked;
    private boolean decrease;
    private boolean increase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_request_post, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.requests);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(RequestPost.this);
        }

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

        root = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    private void fetchData() {

        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);

        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postData = dataSnapshot.getValue(Post.class);
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

        checkStatus(); //check whether accepted or not
        request_state.setText(R.string.not_accepted);

        alert.hideAlert();

    }

    private void checkStatus() {

        state = "";

        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference userResponses = root.child("Users").child(user.getUid()).child("MyResponses");

        userResponses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot id: dataSnapshot.getChildren()) {
                        if (id.getValue().equals(postId)) {
                            Log.d("button","inside check status : accepted");
                            setRejectButton();
                            state_listener = true;
                        }
                        else {
                            Log.d("button","inside check status : not accepted");
                            setAcceptButton();
                            state_listener = false;

                        }
                    }
                } else {
                    state_listener = false;
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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

            case R.id.rp_btn_accept: {
                //state_listener = true means the person has accepted the request.
                // in the moment of button click the state listener should be false if the user is trying to accept

                if(!state_listener) {
                    Log.d("button","inside button onClick : accepted");
                    rejectClicked = false;
                    state_listener = true;
                    acceptRequest();

                } else {
                    Log.d("button","inside button onClick : not accepted");
                    rejectClicked = true;
                    state_listener = false;
                    rejectRequest();
                }

            }
        }

    }

    private void callNow() {

        Dialer dialer = new Dialer(this);
        dialer.dial(postData.getContactNumber());

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


    private void acceptRequest() {

        Log.d("button","inside acceptRequest");


        final Alert alert = new Alert(this);
        alert.showAlert();

        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        //add the post to users node under my responses
        DatabaseReference myResponses = root.child("Users").child(userId).child("MyResponses");
        myResponses.child(postId).setValue(postId);

        //add a response to posts node under responses node of the particular post
        DatabaseReference postRef = root.child("Posts").child(postId).child("Responses");
        postRef.child(userId).setValue(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    changeDrawable(true,alert);

                    decrease = false;
                    increase = true;
                    changeResponseNumber();

                    sendNotification("accepted");

                } else {
                    Snackbar.make(navigationView, R.string.accept_request_unsuccess,Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }

    private void sendNotification(String status) {

        JSONObject bodyObject = new JSONObject();



        try {
            bodyObject.put("postId",postId);
            bodyObject.put("status",status);
            bodyObject.put("acceptedById",auth.getCurrentUser().getUid());

            String bodyString = bodyObject.toString();

            PostMethod respond = new PostMethod();

            String result = respond.post(Constants.RESPONSE_FOR_REQUEST,bodyString);

            Log.i("Response_Report", "Response results: " + result);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void rejectRequest () {

        Log.d("button","inside reject Request");

        final Alert alert = new Alert(this);
        alert.showAlert();

        FirebaseUser user = auth.getCurrentUser();
        final String userId = user.getUid();

        //remove the post from users node under my responses
        final DatabaseReference myResponses = root.child("Users").child(userId).child("MyResponses");

        myResponses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && rejectClicked) {
                    for (DataSnapshot post: dataSnapshot.getChildren()) {
                        if (post.getValue().equals(postId)) {
                            myResponses.child(post.getKey()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //remove the response from posts node under responses node of the particular post
        final DatabaseReference postRef = root.child("Posts").child(postId).child("Responses");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && rejectClicked) {
                    for (DataSnapshot post: dataSnapshot.getChildren()) {
                        if (post.getValue().equals(userId)) {
                            postRef.child(post.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    decrease = true;
                                    increase = false;
                                    changeDrawable(false,alert);
                                    changeResponseNumber();
                                    sendNotification("rejected");

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void changeResponseNumber() {

        final DatabaseReference numberOfResponses = root.child("Posts").child(postId).child("number_of_responses");

        numberOfResponses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (increase) {
                        numberOfResponses.setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + 1);
                        increase = false;
                    } else if (decrease) {
                        numberOfResponses.setValue(Integer.parseInt(dataSnapshot.getValue().toString()) - 1);
                        decrease = false;
                    }

                } else {

                    if (increase) {
                        numberOfResponses.setValue(1);
                        increase = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void changeDrawable(boolean state, Alert alert) {

        // state = true for accept, false for reject
        Drawable img;

        alert.hideAlert();

        if (state) {

            Log.d("button","inside change Drawable : accepted");

//            img = this.getResources().getDrawable( R.drawable.request_reject);
            setRejectButton();
            Snackbar.make(navigationView, R.string.accept_request,Snackbar.LENGTH_LONG).show();

        } else {
//            img = this.getResources().getDrawable( R.drawable.request_accept);
            Log.d("button","inside change Drawable : not accepted");

            setAcceptButton();
            Snackbar.make(navigationView, R.string.reject_request,Snackbar.LENGTH_LONG).show();

        }
//        img.setBounds( 25, 25, 0, 0 );
//        accept.setCompoundDrawables( img, null, null, null );


    }

    private void setRejectButton() {
        accept.setText(R.string.btn_reject);
        request_state.setText(R.string.accepted);
        state = getString(R.string.accepted);
    }

    private void setAcceptButton() {
        accept.setText(R.string.accept);
        request_state.setText(R.string.not_accepted);
        state = getString(R.string.not_accepted);
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
