package it.lanos.eventbuddy.data.source.local.datasource;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.UserCallback;

public abstract class BaseUserLocalDataSource {
    protected UserCallback userCallback;
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
    public abstract void getFriends();
    public abstract void addFriends(List<User> user);
    public abstract void updateFriend(User user);
}