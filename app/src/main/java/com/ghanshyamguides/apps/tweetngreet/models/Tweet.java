package com.ghanshyamguides.apps.tweetngreet.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ghanshyamguides.apps.tweetngreet.helpers.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet implements Parcelable {

    private String id;
    private String body;
    private String createdAt;
    private int retweetCount;

    public Tweet() {

    }

    public com.ghanshyamguides.apps.tweetngreet.models.User getUser() {
        return user;
    }

    public void setUser(com.ghanshyamguides.apps.tweetngreet.models.User user) {
        this.user = user;
    }

    private com.ghanshyamguides.apps.tweetngreet.models.User user;

    public String getId() {
        return id;
    }

    public void setId(int String) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return TwitterClient.getRelativeTimeAgo(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.id = json.getString("id_str");
            tweet.body = json.getString("text");
            tweet.createdAt = json.getString("created_at");
            tweet.retweetCount = json.getInt("retweet_count");
            tweet.user = com.ghanshyamguides.apps.tweetngreet.models.User.fromJson(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray json) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(json.length());
        for (int i = 0; i < json.length(); i++) {
            try {
                Tweet tweet = fromJson(json.getJSONObject(i));
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    protected Tweet(Parcel in) {
        id = in.readString();
        body = in.readString();
        createdAt = in.readString();
        retweetCount = in.readInt();
        user = (com.ghanshyamguides.apps.tweetngreet.models.User) in.readValue(com.ghanshyamguides.apps.tweetngreet.models.User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(body);
        dest.writeString(createdAt);
        dest.writeInt(retweetCount);
        dest.writeValue(user);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

}
