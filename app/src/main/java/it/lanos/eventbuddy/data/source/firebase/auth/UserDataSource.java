package it.lanos.eventbuddy.data.source.firebase.auth;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.entities.User;

public class UserDataSource extends BaseUserDataSource {
    private static final String TAG = UserDataSource.class.getSimpleName();
    private FirebaseAuth mAuth;

    public UserDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    /***
     * Create a new user
     * @param email user email
     * @param password user password
     */
    @Override
    public void register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        authCallback.onRegisterSuccess(new User(currentUser.getUid(), userName, fullName));
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
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
                        authCallback.onFailureFromRemote(task.getException());
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
                        authCallback.onFailureFromRemote(task.getException());
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
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }
}
