package it.lanos.eventbuddy.data.source.firebase.auth;

public abstract class BaseAuthDataSource {
    protected AuthCallback authCallback;
    public void setAuthCallback(AuthCallback authCallback) {
        this.authCallback = authCallback;
    }

    public abstract void signIn(String email, String password);
    public abstract void register(String email, String password);
}
