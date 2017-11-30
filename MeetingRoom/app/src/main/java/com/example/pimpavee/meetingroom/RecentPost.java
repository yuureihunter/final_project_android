package com.example.pimpavee.meetingroom;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentPost {

    public RecentPost(){}


    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.child("posts");

        return recentPostsQuery;
    }
}
