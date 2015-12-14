package com.ghanshyamguides.apps.tweetngreet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    private String id;
    private String userName;
    private String userProfileImage;
    private String userScreenName;

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    public static User fromJson(JSONObject json) {
        User user = new User();
        try {
            user.id = json.getString("id_str");
            user.userName = json.getString("name");
            user.userProfileImage = json.getString("profile_image_url");
            user.userScreenName = json.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    protected User(Parcel in) {
        id = in.readString();
        userName = in.readString();
        userProfileImage = in.readString();
        userScreenName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(userProfileImage);
        dest.writeString(userScreenName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
