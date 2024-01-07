package it.lanos.eventbuddy.data.source.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User{
    @PrimaryKey
    @NonNull
    private String userId;
    @ColumnInfo(name = "username")
    @NonNull
    private String username;
    @ColumnInfo(name = "full_name")
    @NonNull
    private String fullName;
    // If the user is a friend of the current user, this value is 1, otherwise 0
    private int isFriend = 0;
    private String profilePictureUrl;
    @Ignore
    public User(){}
    public User(@NonNull String userId, @NonNull String username, @NonNull String fullName) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
    }
    @NonNull
    public String getUserId() {
        return userId;
    }
    @NonNull
    public String getUsername() {
        return username;
    }
    @NonNull
    public String getFullName() {
        return fullName;
    }
    public int getIsFriend() {
        return isFriend;
    }
    public void setUsername(@NonNull String username) {
        this.username = username;
    }
    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }
    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof User){
            User user = (User) obj;
            return this.getUserId().equals(user.getUserId());
        }
        return false;
    }
}
