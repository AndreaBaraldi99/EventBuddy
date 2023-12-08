package it.lanos.eventbuddy.data;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.firebase.auth.AuthCallback;
import it.lanos.eventbuddy.data.source.firebase.auth.AuthDataSource;

public class AuthRepository implements IAuthRepository, AuthCallback {
    private AuthDataSource authDataSource;
    public AuthRepository(AuthDataSource authDataSource) {
        this.authDataSource = authDataSource;
        this.authDataSource.setAuthCallback(this);
    }
    @Override
    public void signIn(String email, String password) {
        authDataSource.signIn(email, password);
    }

    @Override
    public void register(String email, String password) {

    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {

    }

    @Override
    public void onLoginFailure(Exception e) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Exception e) {

    }
}
