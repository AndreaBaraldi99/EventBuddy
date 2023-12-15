package it.lanos.eventbuddy.data.source.firebase.auth;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.entities.User;

public interface UserCallback {
    void onRegisterSuccess(User user);
    void onLoginSuccess(FirebaseUser user);
    void onDeleteSuccess();
    void onChangePasswordSuccess();
    void onFailureFromRemote(Exception e);
}
