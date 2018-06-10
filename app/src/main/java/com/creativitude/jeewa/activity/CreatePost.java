package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Connectivity;
import com.creativitude.jeewa.helpers.CurrentDate;
import com.creativitude.jeewa.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePost extends Drawer implements View.OnClickListener {

    private EditText name;
    private RadioButton male;
    private EditText age;
    private Spinner bg;
    private Spinner district;
    private AppCompatSeekBar priority;
    private EditText message;
    private Spinner relationship;
    private EditText contactPerson;
    private EditText contactNumber;
    private RadioGroup genderGroup;

    private FirebaseDatabase rootRef;
    private FirebaseAuth mAuth;

    private Post post;
    private int error;
    private Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_create_post, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.requests);

        initialize();

        Button addNewPost = findViewById(R.id.btn_cp_post);
        Button sharePostFb = findViewById(R.id.btn_cp_share);

        addNewPost.setOnClickListener(this);
        sharePostFb.setOnClickListener(this);
    }

    private void initialize() {

        name = findViewById(R.id.et_cp_name);
        male = findViewById(R.id.rb_cp_male);
        age = findViewById(R.id.et_cp_age);
        bg = findViewById(R.id.sp_cp_blood_groups);
        district = findViewById(R.id.sp_cp_districts);
        message = findViewById(R.id.et_cp_message);
        relationship = findViewById(R.id.sp_cp_relationships);
        contactPerson = findViewById(R.id.et_cp_postedName);
        contactNumber = findViewById(R.id.et_cp_postedContact);
        genderGroup = findViewById(R.id.gender_group);
        priority = findViewById(R.id.cp_seekbar);

        connectivity = new Connectivity(this);

        post = new Post();


        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.requests);
        connectivity.checkConnectionState(navigationView);

    }

    @Override
    public void onClick(View view) {

        int btnId = view.getId();
        error = 0;

        switch (btnId) {

            case R.id.btn_cp_post: {

                validateInput();
                if (error == 0) {
                    if (connectivity.isOnline()) {
                        storeInFirebaseDb();
                    } else {
                        Snackbar.make(navigationView,getString(R.string.device_offline),Snackbar.LENGTH_LONG).show();
                    }
                }

                break;
            }
            case R.id.btn_cp_share: {
                break;
            }
        }

    }

    private void storeInFirebaseDb() {

        DatabaseReference postRef = rootRef.getReference("Posts").push();

        post.setDate(CurrentDate.getDate());

        postRef.setValue(this.post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                clearFields();

                if (task.isSuccessful()) {
                    Snackbar.make(navigationView,String.valueOf(getString(R.string.post_success)),Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(navigationView,String.valueOf(getString(R.string.post_unsuccess)),Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //add the post id (key) to user's 'posts' node
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference userRef = rootRef.getReference("Users");
        userRef.child(firebaseUser.getUid()).child("Posts").child(postRef.getKey()).setValue(postRef.getKey());

        //start new response node
//        DatabaseReference responseRef = rootRef.getReference("Responses");
//        responseRef.push().setValue(postRef.getKey());


    }

    private void clearFields() {

        name.setText(null);
        age.setText(null);
        message.setText(null);
        contactPerson.setText(null);
        contactNumber.setText(null);
        priority.setProgress(0);
        genderGroup.clearCheck();
        district.setSelection(0);
        bg.setSelection(0);
        relationship.setSelection(0);
    }

    private void validateInput() {

        if(!TextUtils.isEmpty(name.getText().toString())) {
            post.setName(name.getText().toString().trim());
        } else {
            name.setError(getString(R.string.empty_field));
            error++;
        }

        if(!TextUtils.isEmpty(age.getText().toString())) {
            post.setAge(age.getText().toString().trim());
        } else {
            age.setError(getString(R.string.empty_field));
            error++;
        }

//        if(!TextUtils.isEmpty(message.getText().toString())) {
            post.setOptionalMessage(message.getText().toString().trim());
//        } else {
//            message.setError(getString(R.string.empty_field));
//            error++;
//        }

        if(!TextUtils.isEmpty(contactNumber.getText().toString())) {
            post.setContactNumber(contactNumber.getText().toString().trim());
        } else {
            contactNumber.setError(getString(R.string.empty_field));
            error++;
        }

        if(!TextUtils.isEmpty(contactPerson.getText().toString())) {
            post.setContactPerson(contactPerson.getText().toString().trim());
        } else {
            contactPerson.setError(getString(R.string.empty_field));
            error++;
        }

        post.setPriority(String.valueOf(priority.getProgress()));

        switch (genderGroup.getCheckedRadioButtonId()) {
            case R.id.rb_cp_male: {
                post.setGender("Male");
                break;
            }

            case R.id.rb_cp_female:{
                post.setGender("Female");
                break;
            }

            default:{
                male.setError(getString(R.string.empty_field));
                error++;
                break;
            }
        }

        if(district.getSelectedItemPosition() != 0) {
            post.setArea(district.getSelectedItem().toString());
        } else {
            error++;
            Snackbar.make(navigationView,getString(R.string.distric_required),Snackbar.LENGTH_SHORT).show();
        }

        if(bg.getSelectedItemPosition() != 0) {
            post.setBloodGroup(bg.getSelectedItem().toString());
        } else {
            error++;
            Snackbar.make(navigationView,getString(R.string.bg_required),Snackbar.LENGTH_SHORT).show();

        }

        if(relationship.getSelectedItemPosition() != 0) {
            post.setRelationship(relationship.getSelectedItem().toString());
        } else {
            error++;
            Snackbar.make(navigationView,getString(R.string.relationship_required),Snackbar.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

}
