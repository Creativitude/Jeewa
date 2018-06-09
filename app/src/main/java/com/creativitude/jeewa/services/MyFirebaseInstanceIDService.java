package com.creativitude.jeewa.services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by naveen on 07/06/2018.
 */

@SuppressLint("Registered")
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String token;

    public MyFirebaseInstanceIDService(){
        this.token = "";
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
         setToken(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        Log.d("TAG", token);
    }

    private void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        onTokenRefresh();
        return token;
    }
}
