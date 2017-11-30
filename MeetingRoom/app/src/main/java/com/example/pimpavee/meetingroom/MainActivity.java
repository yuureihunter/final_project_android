package com.example.pimpavee.meetingroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;

    private AccessToken accessToken;

    private TextView textViewName, textViewEmail, textViewID;
    private Button buttonAdd;

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;

    String userName, firstName, lastName, email, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewID = (TextView) findViewById(R.id.textViewID);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        initializeFacebookLogin();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });
    }

    private void initializeFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                accessToken = loginResult.getAccessToken();
                String userID = accessToken.getUserId();

                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        displayUserInfo(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserInfo(JSONObject object) {
        userName = "";
        firstName = "";
        lastName = "";
        email = "";
        id = "";

        try {
            firstName = object.getString("first_name");
            lastName = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userName = firstName + " " + lastName;
        textViewName.setText(userName);
        textViewEmail.setText("Email : " + email);
        textViewID.setText("ID : " + id);
        writeNewUser(id, userName, email);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null){
        }
    }

    private void writeNewUser(String userId, String username, String email) {
        User user = new User(username, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
