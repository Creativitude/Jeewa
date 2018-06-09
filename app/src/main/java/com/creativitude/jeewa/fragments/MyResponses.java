package com.creativitude.jeewa.fragments;

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
import com.creativitude.jeewa.activity.RequestPost;
import com.creativitude.jeewa.helpers.Alert;
import com.creativitude.jeewa.helpers.CommonOnClicks;
import com.creativitude.jeewa.models.Post;
import com.creativitude.jeewa.viewholders.AllRequestsHolder;
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
public class MyResponses extends Fragment {

    private View myResponseView;
    private Alert loader;
    private DatabaseReference insideUser;
    private DatabaseReference insidePost;
    private boolean emptyViewCheck;
    private TextView emptyView;
    private RecyclerView rvMyResponse;

    public MyResponses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myResponseView =  inflater.inflate(R.layout.fragment_my_responses, container, false);

        emptyView = myResponseView.findViewById(R.id.myResponsesEmptyView);
        rvMyResponse = myResponseView.findViewById(R.id.rv_my_responses);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        rvMyResponse.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                rvMyResponse.getContext(),
                linearLayoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        R.drawable.divider_item_decorator
                )
        );

        rvMyResponse.addItemDecoration(
                itemDecoration
        );
        return myResponseView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loader = new Alert(getActivity());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        insidePost = rootRef.child("Posts");
        insideUser = rootRef.child("Users").child(user.getUid()).child("MyResponses");

    }


    @Override
    public void onStart() {
        super.onStart();

        loader.showAlert();

        Log.d("MyResponses","inside onStart");


        FirebaseIndexRecyclerAdapter<Post,AllRequestsHolder> adapter = new FirebaseIndexRecyclerAdapter<Post, AllRequestsHolder>(
                Post.class,
                R.layout.request_card,
                AllRequestsHolder.class,
                insideUser,
                insidePost

        ) {
            @Override
            protected void populateViewHolder(AllRequestsHolder viewHolder, Post model, int position) {

                emptyViewCheck = false;
                if (emptyView.getVisibility() == View.VISIBLE){
                    emptyView.setVisibility(View.GONE);
                }

                final String post_id = getRef(position).getKey();

                Log.d("MyResponses","inside view holder");

                viewHolder.setBloodType(model.getBloodGroup());
                viewHolder.setContactPerson(model.getContactPerson());
                viewHolder.setDistrict(model.getArea());
                viewHolder.setOptionalMessage(model.getOptionalMessage());
                viewHolder.setCallNow(model.getContactNumber());
                viewHolder.setPriority(model.getPriority());


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonOnClicks.fullCardOnClick(view,post_id,getActivity(),getContext(), RequestPost.class);
                    }
                });

            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();

                Log.d("MyResponses","inside onDataChanged");


                if(emptyViewCheck) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                loader.hideAlert();
            }
        };

        rvMyResponse.setAdapter(adapter);



    }
}
