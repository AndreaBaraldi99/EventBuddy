package it.lanos.eventbuddy.util.AuthenticationManager;

public interface AuthManagerCallback {
    void onComplete(AuthManagerResponse response);

    void onFailure(AuthManagerResponse response);
}
