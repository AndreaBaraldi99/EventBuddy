package it.lanos.eventbuddy.data.source;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.entities.User;

public interface UserCallback {
    void onSuccessFromFirebase(User user);
    void onSuccessFromOnlineDB(User user);
    void onSuccessFromLocalDB(User user);
    void onDeleteSuccess();
    void onChangePasswordSuccess();
    void onFailureFromRemote(Exception e);
}
