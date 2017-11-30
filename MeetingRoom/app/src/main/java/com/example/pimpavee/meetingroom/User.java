package com.example.pimpavee.meetingroom;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class User {

    public String username;
    public String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
