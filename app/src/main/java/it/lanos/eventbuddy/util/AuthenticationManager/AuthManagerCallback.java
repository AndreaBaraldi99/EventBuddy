package it.lanos.eventbuddy.util.AuthenticationManager;

public interface AuthManagerCallback {
    void onSuccess(AuthManagerResponse response);

    void onFailure(AuthManagerResponse response);
}
