package it.lanos.eventbuddy.data.source.local.datasource;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.UserCallback;

public abstract class BaseUserLocalDataSource {
    protected UserCallback userCallback;
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
    public abstract void addUser(User user);
}
