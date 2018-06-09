package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
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
import com.creativitude.jeewa.helpers.Transitions;
import com.creativitude.jeewa.viewholders.NotificationsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notifications extends Drawer {

    private RecyclerView rvNotification;
    private DatabaseReference notificationRef;
    private Connectivity connectivity;
    private Alert loader;
    private boolean emptyViewCheck;
    private TextView emptyView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_notifications, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.notifications);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(Notifications.this);
        }

        init();
    }

    private void init() {

        connectivity = new Connectivity(this);
        loader = new Alert(Notifications.this);
        emptyViewCheck = true;
        emptyView = findViewById(R.id.notificationsEmptyView);

        rvNotification = findViewById(R.id.rv_notification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvNotification.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                rvNotification.getContext(),
                linearLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        Notifications.this,
                        R.drawable.divider_item_decorator
                )
        );

        rvNotification.addItemDecoration(
                itemDecoration
        );

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        notificationRef = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);

    }

    @Override
    protected void onStart() {
        super.onStart();

        loader.showAlert();


        FirebaseRecyclerAdapter<String, NotificationsViewHolder> adapter = new FirebaseRecyclerAdapter<
                String, NotificationsViewHolder>(
                String.class,
                R.layout.notification_card,
                NotificationsViewHolder.class,
                notificationRef
        ) {
            @Override
            protected void populateViewHolder(NotificationsViewHolder viewHolder, String model, int position) {

                emptyViewCheck = false;
                if (emptyView.getVisibility() == View.VISIBLE){
                    emptyView.setVisibility(View.GONE);
                }

                final String post_id = getRef(position).getKey();

                viewHolder.setMessage(model);

            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();

                if(emptyViewCheck) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                loader.hideAlert();
            }
        };

        rvNotification.setAdapter(adapter);


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

    @Override
    protected void onResume() {
        super.onResume();
        connectivity.checkConnectionState(navigationView);
        navigationView.setCheckedItem(R.id.notifications);
    }
}
