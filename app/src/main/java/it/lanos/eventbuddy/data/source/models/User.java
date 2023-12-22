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
    public void setUsername(@NonNull String username) {
        this.username = username;
    }
    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
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
    }

    public void readFromParcel(Parcel source) {
        this.userId = source.readString();
        this.username = source.readString();
        this.fullName = source.readString();
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.username = in.readString();
        this.fullName = in.readString();
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
