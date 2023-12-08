package it.lanos.eventbuddy.data.source.firebase.auth;

import com.google.firebase.auth.FirebaseUser;

public interface AuthCallback {
    void onLoginSuccess(FirebaseUser user);
    void onLoginFailure(Exception e);
    void onRegisterSuccess();
    void onRegisterFailure(Exception e);

}
