package com.example.pimpavee.meetingroom;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public abstract class PostAdapter{

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Post, PostHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;





    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);


}
