package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.UserCallback;

public abstract class BaseUserCloudDBDataSource {
    protected UserCallback userCallback;
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
    public abstract void addUser(User user);
    public abstract void getUser(String uid);
    public abstract void searchUsers(String query);
    public abstract void changeUsername(User newUser);
}
