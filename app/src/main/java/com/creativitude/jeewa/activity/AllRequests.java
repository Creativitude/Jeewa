package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.models.Post;
import com.creativitude.jeewa.viewholders.AllRequestsHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllRequests extends Drawer implements AdapterView.OnItemClickListener {

    private RecyclerView rvAllRequests;
    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_all_requests, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.requests);

        rvAllRequests = findViewById(R.id.rvAllRequests);
        rvAllRequests.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllRequests.this);
        linearLayoutManager.setStackFromEnd(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rvAllRequests.setLayoutManager(linearLayoutManager);

        postsRef = FirebaseDatabase.getInstance().getReference("Posts");



    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.requests);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, AllRequestsHolder> allRequestAdapter = new FirebaseRecyclerAdapter<Post, AllRequestsHolder>(
                Post.class,
                R.layout.request_card,
                AllRequestsHolder.class,
                postsRef
        ) {
            @Override
            protected void populateViewHolder(AllRequestsHolder viewHolder, Post model, int position) {

                viewHolder.setBloodType(model.getBloodGroup());
                viewHolder.setContactPerson(model.getContactPerson());
                viewHolder.setDistrict(model.getArea());
                viewHolder.setOptionalMessage(model.getOptionalMessage());
                viewHolder.setCallNow(model.getContactNumber());

            }
        };

        rvAllRequests.setAdapter(allRequestAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



    }

    public void onClickAddNewPost(View view) {

        startActivity(new Intent(AllRequests.this,CreatePost.class));

    }
}
