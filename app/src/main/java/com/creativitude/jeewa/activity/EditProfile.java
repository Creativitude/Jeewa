package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.Connectivity;
import com.creativitude.jeewa.helpers.Transitions;
import com.creativitude.jeewa.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends Drawer {


    private EditText name;
    private EditText age;
    private EditText contact;
    private EditText town;

    private Spinner districts;

    private TextView bg;
    private TextView email;

    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    private Connectivity connectivity;

    DatabaseReference databaseReference;
    DatabaseReference userRef;
    FirebaseAuth mAuth;

    Alert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_edit_profile, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(EditProfile.this);
        }

        connectivity = new Connectivity(this);

        init();

        fetchData();

    }


    private void init() {

        alert = new Alert(this);
        name = findViewById(R.id.et_profile_name);
        age = findViewById(R.id.et_profile_age);
        contact = findViewById(R.id.et_profile_contact);
        town = findViewById(R.id.et_profile_town);
        districts = findViewById(R.id.sp_profile_districts);
        bg = findViewById(R.id.tv_profile_bg);
        email = findViewById(R.id.tv_profile_email);
        gender = findViewById(R.id.gender_group_profile);
        male = findViewById(R.id.radio_profile_male);
        female = findViewById(R.id.radio_profile_female);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userRef = databaseReference.child("Users").child(mAuth.getCurrentUser().getUid());

    }

    private void fetchData() {

        alert.showAlert();


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    setData(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setData(User user) {

        name.setText(user.getName());
        age.setText(user.getAge());
        contact.setText(user.getContact_no());
//        town.setText(user.get()); //add nearest town name to db when registers
        email.setText(user.getEmail());
        bg.setText(user.getBg());
//        districts.setSe //add district position to db when registers

        if(user.getGender().equals("Male")) {
            male.setChecked(true);
            female.setChecked(false);
        } else {
            male.setChecked(false);
            female.setChecked(true);
        }

        alert.hideAlert();


    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.profile);
        connectivity.checkConnectionState(navigationView);

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

    public void save(View view) {
    }
}
