package me.jathusan.android.model;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    private int mId;

    @SerializedName("user_id")
    private int mUserId;

    @SerializedName("ride_id")
    private int mRideId;

    @SerializedName("comment")
    private String mComment;

    @SerializedName("last_updated")
    private String mLastUpdated;

    @SerializedName("user_name")
    private String mUserName;

    @SerializedName("user_facebook_id")
    private String mFacebookId;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public int getRideId() {
        return mRideId;
    }

    public void setRideId(int rideId) {
        this.mRideId = rideId;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public void setFacebookId(String facebookId) {
        this.mFacebookId = facebookId;
    }
}
