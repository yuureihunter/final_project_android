package com.example.pimpavee.meetingroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference database;

    private EditText editTextTitle;
    private EditText editTextBody;
    private Button buttonSubmit;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        database = FirebaseDatabase.getInstance().getReference();

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextBody = (EditText) findViewById(R.id.editTextBody);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPost();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewPostActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void submitPost() {
        final String title = editTextTitle.getText().toString();
        final String body = editTextBody.getText().toString();

        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(body)) {
            editTextBody.setError(REQUIRED);
            return;
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        final String userId = getUserId();
        database.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(NewPostActivity.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Write new post
                    writeNewPost(userId, user.username, title, body);
                }

                setEditingEnabled(true);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                setEditingEnabled(true);
            }
        });
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        String key = database.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        database.updateChildren(childUpdates);
    }

    private void setEditingEnabled(boolean b) {
        editTextTitle.setEnabled(b);
        editTextBody.setEnabled(b);
        if (b) {
            buttonSubmit.setVisibility(View.VISIBLE);
        } else {
            buttonSubmit.setVisibility(View.GONE);
        }
    }
}
