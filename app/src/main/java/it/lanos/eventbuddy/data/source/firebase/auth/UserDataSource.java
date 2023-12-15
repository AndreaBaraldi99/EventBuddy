package it.lanos.eventbuddy.data.source.firebase.auth;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.services.AuthService;
import it.lanos.eventbuddy.data.source.entities.User;

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
                        authCallback.onSuccessFromOnlineDB(null);
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }

    /***
     * Log the user out
     */
    // TODO: 15/12/2023 quando l'utente slogga cancellare lo user e tutti gli eventi in locale
    @Override
    public void signOut() {
        authService.signOut();
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

    // TODO: 15/12/2023
    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword){
       /* FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onChangePasswordSuccess();
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                }); */
    }

    public FirebaseUser getCurrentUser() {
        return authService.getCurrentUser();
    }
}
