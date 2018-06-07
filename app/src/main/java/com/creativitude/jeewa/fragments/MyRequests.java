package com.creativitude.jeewa.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.activity.Responses;
import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.models.Post;
import com.creativitude.jeewa.viewholders.MyRequestsHolder;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MyRequests extends Fragment {

    private View myRequestView;
    private Alert loader;
    private DatabaseReference insideUser;
    private DatabaseReference insidePost;
    private boolean emptyViewCheck;
    private TextView emptyView;
    private RecyclerView rvMyRequests;


    public MyRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myRequestView =  inflater.inflate(R.layout.fragment_my_requests, container, false);

        emptyView = myRequestView.findViewById(R.id.myRequestsEmptyView);
        rvMyRequests = myRequestView.findViewById(R.id.rv_my_requests);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rvMyRequests.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                rvMyRequests.getContext(),
                linearLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        R.drawable.divider_item_decorator
                )
        );

        rvMyRequests.addItemDecoration(
                itemDecoration
        );

        return myRequestView;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loader = new Alert(getActivity());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        insidePost = rootRef.child("Posts");
        insideUser = rootRef.child("Users").child(user.getUid()).child("Posts");

    }

    @Override
    public void onStart() {
        super.onStart();

        loader.showAlert();

        Log.d("MyRequests","inside onStart");


        FirebaseIndexRecyclerAdapter<Post,MyRequestsHolder> adapter = new FirebaseIndexRecyclerAdapter<Post, MyRequestsHolder>(
                Post.class,
                R.layout.my_request_card,
                MyRequestsHolder.class,
                insideUser,
                insidePost

        ) {
            @Override
            protected void populateViewHolder(MyRequestsHolder viewHolder, Post model, int position) {

                emptyViewCheck = false;
                if (emptyView.getVisibility() == View.VISIBLE){
                    emptyView.setVisibility(View.GONE);
                }

                Log.d("MyRequests","inside view holder");

                final String post_id = getRef(position).getKey();


                long rp = (model.getNumber_of_responses() != null) ? model.getNumber_of_responses() : 0;

                viewHolder.setBloodType(model.getBloodGroup());
                viewHolder.setDate(model.getDate());
                viewHolder.setNumberOfResponses(String.valueOf(rp));
                viewHolder.setPriority(model.getPriority());


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fullCardOnClick(view,post_id,getActivity(),getContext(),Responses.class);
                    }
                });

            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();

                Log.d("MyRequests","inside onDataChanged");


                if(emptyViewCheck) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                loader.hideAlert();
            }
        };

        rvMyRequests.setAdapter(adapter);



    }

    public void fullCardOnClick(View view, String key, Activity activity, Context context, Class toClass) {

        Intent intent = new Intent(activity, toClass);
        intent.putExtra("POST_ID",key);
//        Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
//                view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
//
//        ActivityCompat.startActivity(AllRequests.this, intent, options);

        context.startActivity(intent);

    }
}
