package com.example.pimpavee.meetingroom;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class PostHolder extends RecyclerView.ViewHolder{

    TextView textViewTitle;
    TextView textViewBody;

    public PostHolder(View itemView){
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewBody = itemView.findViewById(R.id.textViewBody);

    }

    public void bindToPost(Post post) {
        textViewTitle.setText(post.title);
        textViewBody.setText(post.body);
    }
}
