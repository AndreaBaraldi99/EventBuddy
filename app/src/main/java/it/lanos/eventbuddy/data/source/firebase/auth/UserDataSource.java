package it.lanos.eventbuddy.data.source.firebase.auth;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.UserRepository;
import it.lanos.eventbuddy.data.services.AuthService;
import it.lanos.eventbuddy.data.source.models.User;

public class UserDataSource extends BaseUserDataSource {
    private static final String TAG = UserDataSource.class.getSimpleName();
    private AuthService authService;

    public UserDataSource(AuthService authService) {
        this.authService = authService;
    }

    /***
     * Create a new user
     * @param email user email
     * @param password user password
     */
    @Override
    public void register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password) {
        authService.register(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String currentUserId = authService.getCurrentUser().getUid();
                        authCallback.onSuccessFromFirebase(new User(currentUserId, userName, fullName));
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
        authService.signIn(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onSuccessFromFirebase(null);
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
        authService.signOut();
        UserRepository.onSignOutSuccess();
    }

    /***
     * Delete the user account
     */
    public void deleteUser() {
        authService.deleteUser()
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

        FirebaseUser currentUser = authService.getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(currentUser.getEmail(), oldPassword);

        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                authService.changePassword(newPassword);
                authCallback.onChangePasswordSuccess();
            } else {
                authCallback.onFailureFromRemote(task.getException());
            }
        });
    }

    /***
     * Send an email to the user to initiate a password reset
     * @param email user email
     */
    @Override
    public void resetPassword(@NonNull String email) {
        authService.resetPassword(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onResetPasswordSuccess();
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }
    @Override
    public FirebaseUser getCurrentUser() {
        return authService.getCurrentUser();
    }


}
