package it.lanos.eventbuddy.data.source.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User implements Parcelable {
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
    public User(@NonNull String userId, @NonNull String username, @NonNull String fullName, int isFriend, String profilePictureUrl) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.isFriend = isFriend;
        this.profilePictureUrl = profilePictureUrl;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.username);
        dest.writeString(this.fullName);
        dest.writeInt(this.isFriend);
        dest.writeString(this.profilePictureUrl);
    }

    public void readFromParcel(Parcel source) {
        this.userId = source.readString();
        this.username = source.readString();
        this.fullName = source.readString();
        this.isFriend = source.readInt();
        this.profilePictureUrl = source.readString();
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.username = in.readString();
        this.fullName = in.readString();
        this.isFriend = in.readInt();
        this.profilePictureUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
