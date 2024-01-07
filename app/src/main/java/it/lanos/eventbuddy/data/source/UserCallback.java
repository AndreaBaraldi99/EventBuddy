package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.User;

public interface UserCallback {
    void onSuccessFromFirebase(User user);
    void onSuccessFromOnlineDB(User user);
    void onSuccessFromLocalDB(List<User> user);
    void onUserSearchedSuccess(List<User> users);
    void onDeleteSuccess();
    void onChangePasswordSuccess();
    void onResetPasswordSuccess();
    void onFailureFromRemote(Exception e);
    void onFriendUpdatedToRemote(User user);
    void onFriendFromRemoteSuccess(List<User> friendsList);

    void onUpdatedFriendFromLocal(User user);
    void onFailureFromLocal(Exception userNotFound);
}
