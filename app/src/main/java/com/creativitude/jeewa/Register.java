package com.creativitude.jeewa;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.Constants;
import com.creativitude.jeewa.https.PostMethod;
import com.creativitude.jeewa.models.Topic;
import com.creativitude.jeewa.models.User;
import com.creativitude.jeewa.services.MyFirebaseInstanceIDService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private static final String TAG = "RegisterActivity";
    private EditText name;
    private EditText age;
    private EditText contact_no;
    private EditText email;
    private EditText password;
    private EditText confirm_password;

    private EditText nearest_town;
    private Button register;


    private Spinner district;
    private Spinner bg;

    private Double ht_lat;
    private Double ht_long;

    private RadioGroup gender;
    private RadioButton male;

    private CheckBox donor;
    private CheckBox send_notifications;
    private CheckBox terms;

    private User user;
    private int error;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private String get_password;
    private Alert alert;

    private ArrayList<String> token;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {
        name = findViewById(R.id.et_reg_name);
        age = findViewById(R.id.et_reg_age);
        contact_no = findViewById(R.id.et_reg_contact);
        district = findViewById(R.id.sp_districts);
        bg = findViewById(R.id.sp_blood_groups);
        gender = findViewById(R.id.gender_group);
        male = findViewById(R.id.male);
        email = findViewById(R.id.et_reg_email);
        password = findViewById(R.id.et_reg_password);
        confirm_password = findViewById(R.id.et_reg_confirm_password);
        donor = findViewById(R.id.cb_reg_donor);
        send_notifications = findViewById(R.id.cb_reg_send_notifications);
        terms = findViewById(R.id.cb_reg_terms_and_conditions);
        register = findViewById(R.id.btn_reg_register);
        nearest_town = findViewById(R.id.et_reg_nearest_town);

        register.setEnabled(false);

        user = new User();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        terms.setOnCheckedChangeListener(this);

    }

    private Address getGeoCoder (String location) {

        if(Geocoder.isPresent()){
            Log.d(TAG, "onCreate: inside geocoder");
            try {
                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(location, 1); // get the found Address Objects
                Log.d(TAG, "onCreate: address list: " + addresses.get(0));

                return addresses.get(0);

            } catch (IOException e) {
                // handle the exception
                Log.e(TAG, "onCreate: IOException: " + e.getMessage());
                return null;
            }
        }

        return null;
    }

    public void register(View view) {

        alert = new Alert(Register.this);
        alert.showAlert();
        error = 0;
        validateInputs();

        if (error == 0) {

            mAuth.createUserWithEmailAndPassword(user.getEmail(), get_password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            store_firebase(user);
                            store_local();

                            alert.hideAlert();
                            startActivity(new Intent(Register.this,Login.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            alert.hideAlert();
                        }

                        }
                    });
        } else {
            alert.hideAlert();

        }

    }

    private void store_firebase(FirebaseUser user) {

        DatabaseReference userRef = database.getReference("Users");

        token = new ArrayList<>();


        MyFirebaseInstanceIDService fcm = new MyFirebaseInstanceIDService();
        this.user.setToken(fcm.getToken());
        token.add(this.user.getToken());


        if(this.user.isDonor()) {
            addToTopic(user);
        }

        userRef.child(user.getUid()).setValue(this.user);


    }

    private void addToTopic(FirebaseUser user) {

        String topic;

        Topic model = new Topic();

        model.setHt_lat(ht_lat);
        model.setHt_long(ht_long);
        model.setId(user.getUid());
        model.setName(this.user.getName());
        model.setNumber(this.user.getContact_no());


        if(this.user.isSend_notifications()){
            topic = "O-";
        } else {
            topic = this.user.getBg();
        }

        DatabaseReference topicRef = database.getReference("Topics");
        topicRef.child(topic).child(user.getUid()).setValue(model);

        subscribeToTopic(topic); //subscribe to FCM topic messaging
    }

    private void subscribeToTopic(String topic) {

        JSONObject bodyObject = new JSONObject();

        JSONArray tokenArray = new JSONArray(token);


        try {
            bodyObject.put("token",tokenArray);
            bodyObject.put("bloodGroup",topic);

            String bodyString = bodyObject.toString();

            PostMethod subscribe = new PostMethod();
            String result = subscribe.post(Constants.SUBSCRIBE_TO_TOPIC,bodyString);

            Log.i("Subscription_Report", "Notification results: " + result);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void store_local() {
    }

    public void registerToLogin(View view) {

        startActivity(new Intent(this,Login.class));
        finish();
    }


    private void validateInputs() {

        if(!TextUtils.isEmpty(name.getText().toString())) {
            user.setName(name.getText().toString().trim());
        } else {
            name.setError(getString(R.string.empty_field));
            error++;
        }

        if (!TextUtils.isEmpty(nearest_town.getText().toString().trim())) {

            Address result = getGeoCoder(nearest_town.getText().toString().trim());

            try {
                ht_lat = result.getLatitude();
                ht_long = result.getLongitude();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        if(!TextUtils.isEmpty(age.getText().toString())) {
            user.setAge(age.getText().toString().trim());
        } else {
            age.setError(getString(R.string.empty_field));
            error++;
        }

        if(!TextUtils.isEmpty(contact_no.getText().toString())) {
            user.setContact_no(contact_no.getText().toString().trim());
        } else {
            contact_no.setError(getString(R.string.empty_field));
            error++;
        }

        if(!TextUtils.isEmpty(email.getText().toString())) {
            user.setEmail(email.getText().toString().trim());
        } else {
            email.setError(getString(R.string.empty_field));
            error++;
        }

        if(!TextUtils.isEmpty(password.getText().toString())) {
            get_password = password.getText().toString().trim();
        } else {
            password.setError(getString(R.string.empty_field));
        }

        if (!confirm_password.getText().toString().equals(get_password)) {
            confirm_password.setError(getString(R.string.mismatch));
            error++;
        }

        if (TextUtils.isEmpty(confirm_password.getText().toString())) {
            confirm_password.setError(getString(R.string.empty_field));
            error++;
        }

        switch (gender.getCheckedRadioButtonId()) {
            case R.id.male: {
                user.setGender("Male");
                break;
            }

            case R.id.female:{
                user.setGender("Female");
                break;
            }

            default:{
                male.setError(getString(R.string.empty_field));
                error++;
                break;
            }
        }

        if (donor.isChecked()) {
            user.setDonor(true);
        } else {
            user.setDonor(false);
        }

        if (send_notifications.isChecked()){
            user.setSend_notifications(true);
        } else {
            user.setSend_notifications(false);
        }

        if(district.getSelectedItemPosition() != 0) {
            user.setDistrict(district.getSelectedItem().toString());
        } else {
            error++;
            Toast.makeText(getApplicationContext(),getString(R.string.distric_required),Toast.LENGTH_SHORT).show();
        }

        if(bg.getSelectedItemPosition() != 0) {
            user.setBg(bg.getSelectedItem().toString());
        } else {
            error++;
            Toast.makeText(getApplicationContext(),getString(R.string.bg_required),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (terms.isChecked()) {
            register.setEnabled(true);
        } else {
            register.setEnabled(false);
        }
    }

}
