package it.lanos.eventbuddy.data.source.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
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

}
