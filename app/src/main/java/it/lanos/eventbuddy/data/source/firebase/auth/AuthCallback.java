package it.lanos.eventbuddy.data.source.firebase.auth;

import com.google.firebase.auth.FirebaseUser;

public interface AuthCallback {
    void onRegisterSuccess(FirebaseUser user);
    String onRegisterFailure(Exception e);
    void onLoginSuccess(FirebaseUser user);
    String onLoginFailure(Exception e);
    void onDeleteSuccess();
    String onDeleteFailure(Exception e);
    void onChangePasswordSuccess();
    String onChangePasswordFailure(Exception e);
}
