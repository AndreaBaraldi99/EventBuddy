package it.lanos.eventbuddy.data.source.firebase.auth;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthDataSource extends BaseAuthDataSource {
    private static final String TAG = AuthDataSource.class.getSimpleName();
    private FirebaseAuth mAuth;
    public AuthDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    /***
     * Create a new user
     * @param email user email
     * @param password user password
     */
    @Override
    public void register(@NonNull String email, @NonNull String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        authCallback.onRegisterSuccess(mAuth.getCurrentUser());
                    } else {
                        authCallback.onRegisterFailure(task.getException());
                    }
                });
    }

    /***
     * Log the user in
     * @param email user email
     * @param password user password
     */
    @Override
    public void signIn(@NonNull String email, @NonNull String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onLoginSuccess(mAuth.getCurrentUser());
                    } else {
                        authCallback.onLoginFailure(task.getException());
                    }
                });
    }

    /***
     * Log the user out
     */
    @Override
    public void signOut() {
        mAuth.signOut();
    }

    /***
     * Delete the user account
     */
    public void deleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        authCallback.onDeleteSuccess();
                    } else {
                        authCallback.onDeleteFailure(task.getException());
                    }
                });
    }

    /***
     * Change the user password
     */
    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword){
        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onChangePasswordSuccess();
                    } else {
                        authCallback.onChangePasswordFailure(task.getException());
                    }
                });
    }
}
