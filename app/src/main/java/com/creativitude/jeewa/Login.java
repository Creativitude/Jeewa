package com.creativitude.jeewa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.Connectivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText email;
    private EditText password;

    private String getEmail;
    private String getPassword;

    private FirebaseAuth mAuth;
    private int error;
    private AlertDialog alertDialog;
    private Alert alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_login);

        initialize();

        Button register = findViewById(R.id.login_btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
    }


    private void initialize() {

        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickLogin(View view) {
        alert = new Alert(Login.this);
        alert.showAlert();

        error = 0;
        validateInputs();

        if (error == 0) {

            Connectivity connectivity = new Connectivity(this);

            if (connectivity.isOnline()) {
                firebaseLogin();
            } else {
                localLogin();
            }

        }

    }


    private void localLogin() {
        Toast.makeText(getApplicationContext(), getString(R.string.offline_login), Toast.LENGTH_SHORT).show();
    }

    private void firebaseLogin() {

        mAuth.signInWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            alert.hideAlert();

                            startActivity(new Intent(Login.this, Dashboard.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            alert.hideAlert();
                        }

                    }
                });
    }


    private void validateInputs() {

        if (!TextUtils.isEmpty(email.getText().toString())) {
            getEmail = email.getText().toString().trim();
        } else {
            email.setError(getString(R.string.empty_field));
            error++;
        }

        if (!TextUtils.isEmpty(password.getText().toString())) {
            getPassword = password.getText().toString().trim();
        } else {
            password.setError(getString(R.string.empty_field));
        }


    }
}
