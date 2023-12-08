package it.lanos.eventbuddy.data;

import androidx.annotation.NonNull;

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
    public void signIn(@NonNull String email, @NonNull String password) {
        authDataSource.signIn(email, password);
    }

    @Override
    public void register(@NonNull String email, @NonNull String password) {
        authDataSource.register(email, password);
    }

    @Override
    public void deleteUser() {
        authDataSource.deleteUser();
    }

    @Override
    public void signOut() {
        authDataSource.signOut();
    }

    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        authDataSource.changePassword(oldPassword, newPassword);
    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {

    }

    @Override
    public String onLoginFailure(Exception e) {
       return e.getMessage();
    }

    @Override
    public void onRegisterSuccess(FirebaseUser user) {

    }

    @Override
    public String onRegisterFailure(Exception e) {
        return e.getMessage();
    }

    @Override
    public void onDeleteSuccess() {

    }

    @Override
    public String onDeleteFailure(Exception e) {
        return e.getMessage();
    }

    @Override
    public void onChangePasswordSuccess() {

    }

    @Override
    public String onChangePasswordFailure(Exception e) {
        return e.getMessage();
    }
}
