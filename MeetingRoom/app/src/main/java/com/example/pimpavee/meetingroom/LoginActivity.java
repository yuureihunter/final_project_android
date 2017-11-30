package com.example.pimpavee.meetingroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;

    private AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        initializeFacebookLogin();
//        loginButton.setReadPermissions("email", "public_profile");

//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
//
////                String userID = loginResult.getAccessToken().getUserId();
////
////                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
////                    @Override
////                    public void onCompleted(JSONObject object, GraphResponse response) {
////                        displayUserInfo(object);
////                    }
////                });
////
////                Bundle parameters = new Bundle();
////                parameters.putString("fields", "first_name, last_name, email, id");
////                graphRequest.setParameters(parameters);
////                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//
////        firebaseAuth = FirebaseAuth.getInstance();
////        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
////            @Override
////            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
////                FirebaseUser user = firebaseAuth.getCurrentUser();
////                if (user != null){
////                    goMainScreen();
////                }
////            }
////        };
    }

    private void initializeFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                accessToken = loginResult.getAccessToken();
                goMainScreen();
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

//    private void handleFacebookAccessToken(AccessToken accessToken) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (!task.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), R.string.firebase_database_url, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

//    public void displayUserInfo(JSONObject object){
//        String firstName = "", lastName = "", email = "", id = "";
//
//
//        try {
//            firstName = object.getString("first_name");
//            lastName = object.getString("last_name");
//            email = object.getString("email");
//            id = object.getString("id");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        TextView textViewName, textViewEmail, textViewID;
//        textViewName = (TextView) findViewById(R.id.textViewName);
//        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
//        textViewID = (TextView) findViewById(R.id.textViewID);
//
//        textViewName.setText(firstName + " " + lastName);
//        textViewEmail.setText("Email : " + email);
//        textViewID.setText("ID : " + id);
//    }

    private void goMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null){
            goMainScreen();
        }

//        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
