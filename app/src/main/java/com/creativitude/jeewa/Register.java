package com.creativitude.jeewa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.creativitude.jeewa.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText name;
    private EditText age;
    private EditText contact_no;
    private EditText email;
    private EditText password;
    private EditText confirm_password;


    private Spinner district;
    private Spinner bg;

    private RadioGroup gender;
    private RadioButton male;

    private User user;
    private int error;

    private FirebaseAuth mAuth;


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

        user = new User();

        mAuth = FirebaseAuth.getInstance();
    }


    public void register(View view) {
        error = 0;
        validateInputs();

        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });

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
            user.setPassword(password.getText().toString().trim());
        } else {
            password.setError(getString(R.string.empty_field));
        }

        if(!confirm_password.getText().toString().equals(user.getPassword())) {
            confirm_password.setError(getString(R.string.mismatch));
            error++;
        }

        if(!TextUtils.isEmpty(confirm_password.getText().toString())) {
            user.setConfirm_password(confirm_password.getText().toString().trim());
        } else {
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

        Toast.makeText(getApplicationContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();



    }
}
