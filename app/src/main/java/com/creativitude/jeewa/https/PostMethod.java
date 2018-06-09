package com.creativitude.jeewa.https;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by naveen on 08/06/2018.
 */

public class PostMethod {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private String result;

    public PostMethod() {
        client = new OkHttpClient();
    }

    public String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                result =  e.getLocalizedMessage();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                result = response.body().string();

                Log.d("SUB_TEST",result);
            }
        });
        return result;
    }
}
