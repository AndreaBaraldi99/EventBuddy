package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.UserCallback;
import it.lanos.eventbuddy.data.source.models.UserFromRemote;

public abstract class BaseUserRemoteDataSource {
    protected UserCallback userCallback;
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
    public abstract void addUser(UserFromRemote user);
    public abstract void getUser(String uid);
    public abstract void searchUsers(String query);
    public abstract void changeUsername(User newUser);
    public abstract void getFriends(String uid);
    public abstract void addFriend(String uid, User friend);
    public abstract void removeFriend(String uid, User friend);
}
