package it.lanos.eventbuddy.data.source.firebase.auth;

import androidx.annotation.NonNull;

public abstract class BaseAuthDataSource {
    protected AuthCallback authCallback;
    public void setAuthCallback(AuthCallback authCallback) {
        this.authCallback = authCallback;
    }
    public abstract void register(@NonNull String email, @NonNull String password);
    public abstract void signIn(@NonNull String email, @NonNull String password);
    public abstract void signOut();
    public abstract void deleteUser();
    public abstract void changePassword(@NonNull String oldPassword, @NonNull String newPassword);
}
