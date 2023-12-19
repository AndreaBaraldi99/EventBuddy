package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;

public interface UserCallback {
    void onSuccessFromFirebase(User user);
    void onSuccessFromOnlineDB(User user);
    void onSuccessFromLocalDB(User user);
    void onUserSearchedSuccess(List<User> users);
    void onDeleteSuccess();
    void onChangePasswordSuccess();
    void onFailureFromRemote(Exception e);
}
