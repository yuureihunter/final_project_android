package com.example.pimpavee.meetingroom;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {

    public String userId;
    public String username;
    public String title;
    public String body;

    public Post(String userId, String username, String title, String body) {
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.body = body;
    }

    @Exclude
    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userid", userId);
        result.put("name", username);
        result.put("title", title);
        result.put("body", body);

        return result;
    }
}
