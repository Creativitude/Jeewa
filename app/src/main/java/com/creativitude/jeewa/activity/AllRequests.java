package com.creativitude.jeewa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.CommonOnClicks;
import com.creativitude.jeewa.helpers.Connectivity;
import com.creativitude.jeewa.helpers.Transitions;
import com.creativitude.jeewa.models.Post;
import com.creativitude.jeewa.viewholders.AllRequestsHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllRequests extends Drawer implements AdapterView.OnItemClickListener,View.OnClickListener {

    private RecyclerView rvAllRequests;
    private DatabaseReference postsRef;
    private Connectivity connectivity;
    private Alert loader;
    private boolean emptyViewCheck;
    private TextView emptyView;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_all_requests, null, false);
        drawerLayout.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.requests);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(AllRequests.this);
        }

        connectivity = new Connectivity(this);
        loader = new Alert(AllRequests.this);
        emptyViewCheck = true;
        emptyView = findViewById(R.id.allRequestsEmptyView);
        paint = new Paint();

        rvAllRequests = findViewById(R.id.rvAllRequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllRequests.this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rvAllRequests.setLayoutManager(linearLayoutManager);
        rvAllRequests.setOnClickListener(this);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                rvAllRequests.getContext(),
                linearLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        AllRequests.this,
                        R.drawable.divider_item_decorator
                )
        );

        rvAllRequests.addItemDecoration(
                itemDecoration
        );


        FloatingActionButton floatingActionButton = findViewById(R.id.addNewPost);
        floatingActionButton.setOnClickListener(this);

        postsRef = FirebaseDatabase.getInstance().getReference("Posts");

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(rvAllRequests);


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


        FirebaseRecyclerAdapter<Post, AllRequestsHolder> allRequestAdapter = new FirebaseRecyclerAdapter<
                Post, AllRequestsHolder>(
                Post.class,
                R.layout.request_card,
                AllRequestsHolder.class,
                postsRef
        ) {
            @Override
            protected void populateViewHolder(AllRequestsHolder viewHolder, Post model, int position) {

                emptyViewCheck = false;
                if (emptyView.getVisibility() == View.VISIBLE){
                    emptyView.setVisibility(View.GONE);
                }

                final String post_id = getRef(position).getKey();

                viewHolder.setBloodType(model.getBloodGroup());
                viewHolder.setContactPerson(model.getContactPerson());
                viewHolder.setDistrict(model.getArea());
                viewHolder.setOptionalMessage(model.getOptionalMessage());
                viewHolder.setCallNow(model.getContactNumber());
                viewHolder.setPriority(model.getPriority());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonOnClicks.fullCardOnClick(view,post_id,AllRequests.this,getApplicationContext());
                    }
                });

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

        rvAllRequests.setAdapter(allRequestAdapter);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {

            case R.id.addNewPost: {

                startActivity(new Intent(AllRequests.this,CreatePost.class));
                break;
            }

        }
    }


    private ItemTouchHelper.Callback createHelperCallback() {

        return new ItemTouchHelper.SimpleCallback(
                0, //drag to reposition cancel
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override //not implementing because the reposition is cancelled (dragDirs = 0)
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                onItemSwipe(direction);

            }

            @Override
            public void onChildDraw(Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                try {
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        final View itemView = viewHolder.itemView;

//                        Toast.makeText(getApplicationContext(),String.valueOf(itemView.getId()),Toast.LENGTH_SHORT).show();
                        itemView.setTranslationX(dX/3);

//                        AnimateSwipe.animateLeftSwipe(viewHolder.itemView);

                    } else {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

    }

    private void onItemSwipe(int direction) {


        if (direction == ItemTouchHelper.LEFT) {
            Snackbar.make(navigationView,"Left",Snackbar.LENGTH_SHORT).show();

        }

        else if (direction == ItemTouchHelper.RIGHT) {
            Snackbar.make(navigationView,"Right",Snackbar.LENGTH_SHORT).show();

        }

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
}
