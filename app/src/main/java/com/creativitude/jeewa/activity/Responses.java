package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.Connectivity;
import com.creativitude.jeewa.models.User;
import com.creativitude.jeewa.viewholders.RespondersHolder;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Responses extends Drawer {

    private Connectivity connectivity;
    private Alert loader;
    private boolean emptyViewCheck;
    private TextView emptyView;
    private RecyclerView rvResponders;
    private DatabaseReference insideUser;
    private DatabaseReference insidePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_responses, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.my_requests);

        Intent intent = getIntent();
        String post_id = intent.getStringExtra("POST_ID");

        connectivity = new Connectivity(this);
        loader = new Alert(Responses.this);
        emptyViewCheck = true;
        emptyView = findViewById(R.id.respondersEmptyView);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        insideUser = rootRef.child("Users");
        insidePost = rootRef.child("Posts").child(post_id).child("Responses");

        rvResponders = findViewById(R.id.rv_responders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        rvResponders.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                rvResponders.getContext(),
                linearLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        Responses.this,
                        R.drawable.divider_item_decorator
                )
        );

        rvResponders.addItemDecoration(
                itemDecoration
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.requests);
        connectivity.checkConnectionState(navigationView);

    }

    @Override
    protected void onStart() {
        super.onStart();

        loader.showAlert();

        FirebaseIndexRecyclerAdapter<User, RespondersHolder> adapter = new FirebaseIndexRecyclerAdapter<User, RespondersHolder>(
                User.class,
                R.layout.response_card,
                RespondersHolder.class,
                insidePost,
                insideUser

        ) {
            @Override
            protected void populateViewHolder(RespondersHolder viewHolder, User model, int position) {

                emptyViewCheck = false;
                if (emptyView.getVisibility() == View.VISIBLE) {
                    emptyView.setVisibility(View.GONE);
                }

                viewHolder.setBloodType(model.getBg());
                viewHolder.setDistrict(model.getDistrict());
                viewHolder.setCall(model.getContact_no());
                viewHolder.setName(model.getName());

            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();

                if (emptyViewCheck) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                loader.hideAlert();
            }
        };

        rvResponders.setAdapter(adapter);
    }
}
