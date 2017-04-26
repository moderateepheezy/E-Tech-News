package com.simpumind.e_tech_news.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by simpumind on 4/17/17.
 */

public class Comment {

    public String text;
    public String username;
    public long timeSent;
    public String userImage;

    public Comment() {
    }

    public Comment(String text, String username, long timeSent, String userImage) {
        this.text = text;
        this.username = username;
        this.timeSent = timeSent;
        this.userImage = userImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("text", text);
        result.put("timeSent", timeSent);
        result.put("userImage", userImage);
        return  result;
    }
}
